import React, { useCallback, useContext, useEffect, useReducer } from 'react';
import PropTypes from 'prop-types';
import { getLogger } from '../core';
import { BookProps } from './BookProps';
import { createBook, getBooks, newWebSocket, updateBook } from './restApi';
import { AuthContext } from '../auth';

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
                    books.splice(0, 0, book);
                } else {
                    books[index] = book;
                }
                return { ...state, books, saving: false };
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
    useEffect(webSocketEffect, [token]);
    const saveBook = useCallback<SaveBookFn>(saveBookCallback, [token]);
    const value = { books, fetching, fetchingError, saving, savingError, saveBook };
    log('returns');
    return (
        <BookContext.Provider value={value}>
            {children}
        </BookContext.Provider>
    );

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
                const books = await getBooks(token);
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
        } catch (error) {
            log('saveBook failed');
            dispatch({ type: SAVE_BOOK_FAILED, payload: { error } });
        }
    }

    function webSocketEffect() {
        let canceled = false;
        log('wsEffect - connecting');
        let closeWebSocket: () => void;
        if (token?.trim()) {
            closeWebSocket = newWebSocket(token, message => {
                if (canceled) {
                    return;
                }
                const { type, payload: book } = message;
                log(`ws message, book ${type}`);
                if (type === 'created' || type === 'updated') {
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
