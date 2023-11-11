import React, { useCallback, useContext, useEffect, useReducer, useRef } from 'react';
import PropTypes from 'prop-types';
import { getLogger } from '../core';
import { BookProps } from '../core/BookProps';
import { createBook, getBooks, newWebSocket, updateBook } from '../api/bookApi';
import { AuthContext } from '.';
import { useNetwork } from '../hooks/useNetwork';
import { Preferences } from '@capacitor/preferences';
import { MyPhoto, usePhotos } from '../hooks/usePhotos';

const log = getLogger('BookProvider');

type SaveBookFn = (book: BookProps) => Promise<any>;

export interface BooksState {
    books?: BookProps[],
    fetching: boolean,
    fetchingError?: Error | null,
    saving: boolean,
    savingError?: Error | null,
    saveBook?: SaveBookFn,
}

interface ActionProps {
    type: string,
    payload?: any,
}

const initialState: BooksState = {
    fetching: false,
    saving: false,
};

const FETCH_BOOKS_STARTED = 'FETCH_BOOKS_STARTED';
const FETCH_BOOKS_SUCCEEDED = 'FETCH_BOOKS_SUCCEEDED';
const FETCH_BOOKS_FAILED = 'FETCH_BOOKS_FAILED';
const SAVE_BOOK_STARTED = 'SAVE_BOOK_STARTED';
const SAVE_BOOK_SUCCEEDED = 'SAVE_BOOK_SUCCEEDED';
const SAVE_BOOK_FAILED = 'SAVE_BOOK_FAILED';

const reducer: (state: BooksState, action: ActionProps) => BooksState =
    (state, { type, payload }) => {
        switch (type) {
            case FETCH_BOOKS_STARTED:
                return { ...state, fetching: true, fetchingError: null };
            case FETCH_BOOKS_SUCCEEDED:
                log('payload.books in FETCH_BOOKS_SUCCEEDED:', payload.books);
                return { ...state, books: payload.books, fetching: false };
            case FETCH_BOOKS_FAILED:
                return { ...state, fetchingError: payload.error, fetching: false };
            case SAVE_BOOK_STARTED:
                return { ...state, savingError: null, saving: true };
            case SAVE_BOOK_SUCCEEDED:
                const books = [...(state.books || [])];
                const book = payload.book;
                const index = books.findIndex(b => b.id === book.id);
                if (index === -1) {
                    books.splice(books.length, 0, book);
                } else {
                    books[index] = book;
                }
                log('books in SAVE_BOOK_SUCCEEDED:', books);
                return { ...state, books, savingEroor: null, saving: false };
            case SAVE_BOOK_FAILED:
                return { ...state, savingError: payload.error, saving: false };
            default:
                return state;
        }
    };

export const BookContext = React.createContext<BooksState>(initialState);

interface BookProviderProps {
    children: PropTypes.ReactNodeLike,
}

export const BookProvider: React.FC<BookProviderProps> = ({ children }) => {
    const { token } = useContext(AuthContext);
    const [state, dispatch] = useReducer(reducer, initialState);
    const { books, fetching, fetchingError, saving, savingError } = state;
    const { networkStatus } = useNetwork();
    const { savePhotoLocally, deletePhoto } = usePhotos();
    useEffect(getBooksEffect, [token]);
    useEffect(webSocketEffect, [token, books]);
    const saveBook = useCallback<SaveBookFn>(saveBookCallback, [token, networkStatus]);
    const value = { books, fetching, fetchingError, saving, savingError, saveBook };

    useEffect(() => {
        if (networkStatus.connected) {
            syncBooks();
        }
    }, [networkStatus.connected]);

    log('returns');
    return (
        <BookContext.Provider value={value}>
            {children}
        </BookContext.Provider>
    );

    function syncBooks() {
        log('syncBooks - networkStatus.connected', networkStatus.connected);
        log('syncBooks - networkStatus.shouldSync', networkStatus.shouldSync);
        if (networkStatus.connected && networkStatus.shouldSync === 'disconnected') {
            syncBooksCallback();
        }

        async function syncBooksCallback() {
            const localBooks = await Preferences.get({ key: 'books' });
            log('localBooks:', localBooks);

            if (localBooks.value) {
                let booksArray: BookProps[] = JSON.parse(localBooks.value);
                const hasDirtyBooks = booksArray.some((book) => book?.dirty?.valueOf() === true);
                if (hasDirtyBooks) {
                    for (let book of booksArray) {
                        try {
                            if (book?.dirty?.valueOf() === true) {
                                book = { ...book, dirty: false };
                                const savedBook = await saveBook(book);
                                if (book.id && savedBook.id) {
                                    if (parseFloat(book.id) < 0) {
                                        booksArray = booksArray.filter(b => b.id !== book.id);
                                        booksArray.push(savedBook);
                                    } else {
                                        const index = booksArray.findIndex(b => b.id === book.id);
                                        if (index !== -1) {
                                            booksArray[index] = savedBook;
                                        }
                                    }
                                }
                            }
                        } catch (error) {
                            log('Error syncing book:', error);
                        }
                    }
                    Preferences.set({ key: 'books', value: JSON.stringify(booksArray) });
                    dispatch({ type: FETCH_BOOKS_SUCCEEDED, payload: { books: booksArray } });
                }
            }
        }
    }


    function getBooksEffect() {
        let canceled = false;
        if (token) {
            fetchBooks();
        }
        return () => {
            canceled = true;
        }


        async function fetchBooks() {
            try {
                log('Info> fetchBooks started!');
                dispatch({ type: FETCH_BOOKS_STARTED });
                let books = await getBooks(token);

                books.forEach(book => {
                    if (book.photo !== null) {
                        savePhotoLocally(book.id!, book.photo!);
                    }
                });

                const dirty = false;
                books = books.map((book: BookProps) => {
                    return { ...book, dirty: dirty };
                });
                await Preferences.set({ key: 'books', value: JSON.stringify(books) });

                // wait a bit
                await new Promise(resolve => setTimeout(resolve, 60));
                log('fetchBooks succeeded');
                if (!canceled) {
                    dispatch({ type: FETCH_BOOKS_SUCCEEDED, payload: { books } });
                }
            } catch (error) {
                log('fetchBooks failed');
                dispatch({ type: FETCH_BOOKS_FAILED, payload: { error } });
            }
        }
    }

    async function saveBookCallback(book: BookProps) {
        try {
            if (networkStatus.connected) {
                log('saveBook started');
                dispatch({ type: SAVE_BOOK_STARTED });
                const savedBook = await (book.id ? updateBook(token, book) : createBook(token, book));
                if (book.id && book.photo !== null && book.photo !== undefined && book.photo !== '') {
                    savePhotoLocally(book.id!, book.photo!);
                }
                log('saveBook succeeded');
                dispatch({ type: SAVE_BOOK_SUCCEEDED, payload: { book: savedBook } });
                return savedBook;
            }
            else {
                log('saveBook failed - > store the book in the local storage');
                const bookId = book.id ? book.id : (-(Math.random() * 1000000)).toString();
                const bookToSave = { ...book, id: bookId, dirty: true };
                const books = await Preferences.get({ key: 'books' });

                let booksArray: BookProps[] = [];
                if (books.value) {
                    booksArray = JSON.parse(books.value);
                }
                const index = booksArray.findIndex(b => b.id === bookToSave.id);
                if (index !== -1) {
                    booksArray[index] = bookToSave;
                } else {
                    booksArray.push(bookToSave);
                }
                await Preferences.set({ key: 'books', value: JSON.stringify(booksArray) });

                const error = { message: "The book could not be save on the server right now, but it will be as soon as you are back online!" };
                dispatch({ type: SAVE_BOOK_FAILED, payload: { error } });
                dispatch({ type: SAVE_BOOK_SUCCEEDED, payload: { book: bookToSave } });
            }
        } catch (error) {
            log('saveBook failed');
            dispatch({ type: SAVE_BOOK_FAILED, payload: { error } });
        }
    }

    function webSocketEffect() {
        let canceled = false;
        log('wsEffect - connecting');
        let closeWebSocket: (() => void) | undefined;

        if (token?.trim()) {
            closeWebSocket = newWebSocket(token, async message => {
                if (canceled) {
                    return;
                }
                const { type, payload: book } = message;
                log(`ws message, book type ${type} and book`, book);
                if (type === 'created' || type === 'updated') {
                    if (books) {
                        const booksArray = [...books];
                        const index = booksArray.findIndex(b => b.id === book.id);
                        if (index === -1) {
                            booksArray.splice(booksArray.length, 0, book);
                        } else {
                            booksArray[index] = book;
                        }
                        Preferences.set({ key: 'books', value: JSON.stringify(booksArray) });
                        if (book.photo === null) {
                            deletePhoto(`${book.id}.jpeg`);
                        }
                    }
                    dispatch({ type: SAVE_BOOK_SUCCEEDED, payload: { book } });
                }
            });
        }

        return () => {
            log('wsEffect - disconnecting');
            canceled = true;
            closeWebSocket?.();
        }
    }

};
