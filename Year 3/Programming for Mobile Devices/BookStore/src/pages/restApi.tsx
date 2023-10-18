import axios from 'axios';
import { getLogger } from '../core';
import { BookProps } from './BookProps';

const log = getLogger('restApi');

const baseUrl = 'localhost:3000';
const bookUrl = `http://${baseUrl}/book`;

interface ResponseProps<T> {
    data: T;
}

function withLogs<T>(promise: Promise<ResponseProps<T>>, fnName: string): Promise<T> {
    log(`${fnName} - started`);
    return promise
        .then(res => {
            log(`${fnName} - succeeded`);
            return Promise.resolve(res.data);
        })
        .catch(err => {
            log(`${fnName} - failed`);
            return Promise.reject(err);
        });
}

const config = {
    headers: {
        'Content-Type': 'application/json'
    }
};

export const getBooks: () => Promise<BookProps[]> = () => {
    return withLogs(axios.get(bookUrl, config), 'getBooks');
}

export const createBook: (book: BookProps) => Promise<BookProps[]> = book => {
    return withLogs(axios.post(bookUrl, book, config), 'createBook');
}

export const updateBook: (book: BookProps) => Promise<BookProps[]> = book => {
    return withLogs(axios.put(`${bookUrl}/${book.id}`, book, config), 'updateBook');
}

interface MessageData {
    event: string;
    payload: {
        book: BookProps;
    };
}

export const newWebSocket = (onMessage: (data: MessageData) => void) => {
    const ws = new WebSocket(`ws://${baseUrl}`)
    ws.onopen = () => {
        log('web socket onopen');
    };
    ws.onclose = () => {
        log('web socket onclose');
    };
    ws.onerror = error => {
        log('web socket onerror', error);
    };
    ws.onmessage = messageEvent => {
        log('web socket onmessage');
        onMessage(JSON.parse(messageEvent.data));
    };
    return () => {
        ws.close();
    }
}
