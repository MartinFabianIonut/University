import React, { useCallback, useContext, useEffect, useReducer } from 'react';
import PropTypes from 'prop-types';
import { getLogger } from '../core';
import { BookProps } from './BookProps';
import { createBook, getBooks, newWebSocket, updateBook } from './restApi';
import { AuthContext } from '../auth';
import { useNetwork } from '../use/useNetwork';
import { Preferences } from '@capacitor/preferences';

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
                    // add in back
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
    useEffect(getBooksEffect, [token]);
    useEffect(webSocketEffect, [token, books]);
    const saveBook = useCallback<SaveBookFn>(saveBookCallback, [token]);
    const value = { books, fetching, fetchingError, saving, savingError, saveBook };
    const { networkStatus } = useNetwork();
    useEffect(() => {
        if (networkStatus.connected) {
            syncBooks();
        }
    }, [networkStatus.connected, books, token]);

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
            // callback to sync the books
            syncBooksCallback();
        }

        async function syncBooksCallback() {
            // get the books from the local storage Preferences
            const localBooks = await Preferences.get({ key: 'books' });
            // they have also the dirty flag, map them to the book props
            // if localbooks contain books with dirty flag, save them on the server


            if (localBooks.value) {
                // if exists dirty books, save them on the server

                let booksArray: BookProps[] = JSON.parse(localBooks.value);

                // Check if there is at least one book with dirty flag
                const hasDirtyBooks = booksArray.some((book) => book?.dirty?.valueOf() === true);

                if (hasDirtyBooks) {
                    // iterate through the books and save them on the server
                    for (const book of booksArray) {
                        try {
                            if (book?.dirty?.valueOf() === true) {
                                // remove the dirty flag
                                book.dirty = false;
                                log('here is the book to be saved:')
                                const savedBook = await saveBook(book);

                                setTimeout(() => {
                                    if (book.id && savedBook.id) {
                                        if (parseFloat(book.id) < 0) {
                                            // remove the book from the state
                                            // const updatedBooks = state.books?.filter(b => b.id !== book.id);
                                            // log('here i start the dispatch')
                                            // dispatch({ type: FETCH_BOOKS_SUCCEEDED, payload: { books: updatedBooks } });
                                            // log('here i end the dispatch')
                                            // Remove the book from local storage
                                            booksArray = booksArray.filter(b => b.id !== book.id);
                                        } else {
                                            // update the book in the local storage Preferences
                                            const index = booksArray.findIndex(b => b.id === book.id);
                                            if (index !== -1) {
                                                booksArray[index] = savedBook;
                                            } else {
                                                booksArray.push(savedBook);
                                            }
                                        }
                                    }
                                }, 100);
                            }
                        } catch (error) {
                            log('Error syncing book:', error);
                            // Handle errors as needed
                        }
                    }
                    // change network status to connected
                    networkStatus.shouldSync = 'do-not-sync';

                    // remove all the books with negative ids
                    Preferences.set({ key: 'books', value: JSON.stringify(booksArray) });
                    // also from books
                    getBooksEffect();
                    // const updatedBooks = await getBooks(token);
                    // dispatch({ type: FETCH_BOOKS_SUCCEEDED, payload: { books: updatedBooks } });

                }


                // let booksArray: BookProps[] = [];
                // booksArray = JSON.parse(localBooks.value);

                // // iterate through the books and save them on the server
                // for (const book of booksArray) {
                //     try {
                //         if (book?.dirty?.valueOf() === true) {
                //             // remove the dirty flag
                //             book.dirty = false;
                //             log('here is the book to be saved:')
                //             const savedBook = await saveBook(book);
                //             if (book.id && savedBook.id) {
                //                 if (parseFloat(book.id) < 0) {
                //                     // remove the book from the state
                //                     // const updatedBooks = state.books?.filter(b => b.id !== book.id);
                //                     // log('here i start the dispatch')
                //                     // dispatch({ type: FETCH_BOOKS_SUCCEEDED, payload: { books: updatedBooks } });
                //                     // log('here i end the dispatch')
                //                     // Remove the book from local storage
                //                     booksArray = booksArray.filter(b => b.id !== book.id);
                //                 }
                //                 else {
                //                     // update the book in the local storage Preferences
                //                     const index = booksArray.findIndex(b => b.id === book.id);
                //                     if (index !== -1) {
                //                         booksArray[index] = savedBook;
                //                     }
                //                     else {
                //                         booksArray.push(savedBook);
                //                     }
                //                 }
                //             }
                //         }
                //     } catch (error) {
                //         log('Error syncing book:', error);
                //         // Handle errors as needed
                //     }
                // }


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
                // save the books in the local storage Preferences

                // each book should also have a dirty flag to indicate if it was saved on the server or not
                const dirty = false;
                // create a new array of books with the dirty flag in the stringified version
                books = books.map((book: BookProps) => {
                    return { ...book, dirty: dirty };
                });
                // save the books in the local storage Preferences

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
            log('saveBook started');
            dispatch({ type: SAVE_BOOK_STARTED });
            const savedBook = await (book.id ? updateBook(token, book) : createBook(token, book));
            log('saveBook succeeded');
            dispatch({ type: SAVE_BOOK_SUCCEEDED, payload: { book: savedBook } });
            return savedBook;
        } catch (error) {
            log('saveBook failed - > store the book in the local storage');
            // if (!networkStatus.connected) {
            // save the book in the local storage Preferences
            // the key is the id if it exists, or otherwise generate a negative id (which will be replaced when the book is saved on the server)
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

            error = { message: "The book could not be save on the server right now, but it will be as soon as you are back online!" };
            dispatch({ type: SAVE_BOOK_FAILED, payload: { error } });
            dispatch({ type: SAVE_BOOK_SUCCEEDED, payload: { book: bookToSave } });
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
                log(`ws message, book ${type}`);
                if (type === 'created' || type === 'updated') {
                    if (books) {
                        const dirty = false;
                        book.book = { ...book.book, dirty: dirty };
                        const booksArray = [...books];
                        const index = booksArray.findIndex(b => b.id === book.book.id);
                        if (index === -1) {
                            // add in back
                            booksArray.splice(booksArray.length, 0, book.book);
                        } else {
                            booksArray[index] = book.book;
                        }
                        Preferences.set({ key: 'books', value: JSON.stringify(booksArray) });
                    }
                    dispatch({ type: SAVE_BOOK_SUCCEEDED, payload: { book } });
                }
            });
        }

        return () => {
            log('wsEffect - disconnecting');
            canceled = true;
            closeWebSocket?.(); // Check if closeWebSocket is defined before calling it
        }
    }

};
