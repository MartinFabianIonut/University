import axios from 'axios';
import { authConfig, baseUrl, getLogger, withLogs } from '../core';
import { BookProps } from './BookProps';

const log = getLogger('restApi');
const bookUrl = `http://${baseUrl}/api/book`;

export const getBooks: (token: string) => Promise<BookProps[]> = token => {
    return withLogs(axios.get(bookUrl, authConfig(token)), 'getBooks');
}

export const createBook: (token: string, book: BookProps) => Promise<BookProps[]> = (token, book) => {
    return withLogs(axios.post(bookUrl, book, authConfig(token)), 'createBook');
}

export const updateBook: (token: string, book: BookProps) => Promise<BookProps[]> = (token, book) => {
    return withLogs(axios.put(`${bookUrl}/${book.id}`, book, authConfig(token)), 'updateBook');
}

interface MessageData {
    type: string;
    payload: {
        book: BookProps;
    };
}

export const newWebSocket = (token: string, onMessage: (data: MessageData) => void) => {
    const ws = new WebSocket(`ws://${baseUrl}`)
    ws.onopen = () => {
        log('web socket onopen');
        ws.send(JSON.stringify({ type: 'authorization', payload: { token } }));
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
