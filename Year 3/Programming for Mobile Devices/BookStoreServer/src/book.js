import Router from "koa-router";
import dataStore from "nedb-promise";
import { broadcast } from "./websocket.js";

export class BookStore {
    constructor({ filename, autoload }) {
        this.store = dataStore({ filename, autoload });
    }
    async find(query) {
        return this.store.find(query);
    }
    async findOne(query) {
        return this.store.findOne(query);
    }
    async insert(book) {
        if (!book.title || !book.author) {
            throw new Error("Title and Author are required");
        }
        return this.store.insert(book);
    }
    async update(query, book) {
        return this.store.update(query, book);
    }
    async remove(query) {
        return this.store.remove(query);
    }
}

const bookStore = new BookStore({ filename: "./db/books.json", autoload: true });

export const bookRouter = new Router();

bookRouter.get('/', async (ctx) => {
    const userId = ctx.state.user._id;
    ctx.response.body = await bookStore.find({ userId });
    ctx.response.status = 200;
});

bookRouter.get('/:id', async (ctx) => {
    const userId = ctx.state.user._id;
    const book = await bookStore.findOne({ _id: ctx.params.id });
    const response = ctx.response;
    if (book) {
        if (book.userId === userId) {
            response.body = book;
            response.status = 200;
        } else {
            response.body = { message: 'Unauthorized User' };
            response.status = 403;
        }
    }
    else {
        response.body = { message: `book with id ${ctx.params.id} not found` };
        response.status = 404;
    }
});

const createBook = async (ctx, book, response) => {
    try {
        book.userId = ctx.state.user._id;
        const newBook = await bookStore.insert(book);
        response.body = newBook;
        response.status = 201;
        broadcast(book.userId, {type : 'created', payload : book});
    } catch (err) {
        response.body = { message: err.message };
        response.status = 400;
    }
};

bookRouter.post('/', async (ctx) => {
    await createBook(ctx, ctx.request.body, ctx.response);
});

bookRouter.put('/:id', async (ctx) => {
    const book = ctx.request.body;
    const bookId = book._id;
    const id = ctx.params.id;
    const response = ctx.response;
    if (bookId && bookId !== id) {
        response.body = { message: 'Unauthorized User' };
        response.status = 403;
        return;
    }
    if (!bookId) {
        await createBook(ctx, book, response);
    } else {
        const userId = ctx.state.user._id;
        book.userId = userId;
        const updatedCount = await bookStore.update({ _id: id }, book);
        if (updatedCount === 1) {
            response.body = book;
            response.status = 200;
            broadcast(book.userId, {type : 'updated', payload : book});
        } else {
            response.body = { message: `book with id ${bookId} not found` };
            response.status = 404;
        }
    }
});

bookRouter.del('/:id', async (ctx) => {
    const userId = ctx.state.user._id;
    const book = await bookStore.findOne({ _id: ctx.params.id });
    if (book && book.userId !== userId) {
        ctx.response.body = { message: 'Unauthorized User' };
        ctx.response.status = 403;
        return;
    }
    await bookStore.remove({ _id: ctx.params.id });
    ctx.response.body = { message: 'success' };
    ctx.response.status = 204;
    broadcast(book.userId, {type : 'deleted', payload : book});
});
