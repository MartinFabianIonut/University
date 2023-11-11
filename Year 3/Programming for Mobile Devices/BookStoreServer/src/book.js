import Router from "koa-router";
import { broadcast } from "./websocket.js";
import { bookStore } from "./BookStore.js";

export const bookRouter = new Router();

bookRouter.get('/', async (ctx) => {
    const userId = ctx.state.user._id;
    const books = await bookStore.find({ userId });
    books.forEach(book => book.id = book.id.toString());
    ctx.response.body = books;
    ctx.response.status = 200;
});

bookRouter.get('/:id', async (ctx) => {
    const userId = ctx.state.user._id;
    const book = await bookStore.findOne({ id: ctx.params.id });
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
        const bookToLog = { ...book, photo: null };
        console.log(bookToLog);
        const newBook = await bookStore.insert(book);
        const newBookToLog = { ...newBook, photo: null };
        console.log(newBookToLog);
        response.body = newBook;
        response.status = 201;
        broadcast(book.userId, { type: 'created', payload: book });
    } catch (err) {
        console.log(err);
        response.body = { message: err.message };
        response.status = 400;
    }
};

bookRouter.post('/', async (ctx) => {
    await createBook(ctx, ctx.request.body, ctx.response);
});

bookRouter.put('/:id', async (ctx) => {
    const book = ctx.request.body;
    const id = ctx.params.id;
    const bookId = book.id;
    const response = ctx.response;
    if (bookId && bookId !== id) {
        response.body = { message: 'Unauthorized User' };
        response.status = 403;
        return;
    }
    if (!bookId || bookId < 0) {
        await createBook(ctx, book, response);
    } else {
        const userId = ctx.state.user._id;
        book.userId = userId;
        const updated = await bookStore.update({ id: parseInt(id) }, book);
        if (updated === 1) {
            response.body = book;
            response.status = 200;
            broadcast(book.userId, { type: 'updated', payload: book });
        } else {
            response.body = { message: `book with id ${bookId} not found` };
            response.status = 404;
        }
    }
});

bookRouter.del('/:id', async (ctx) => {
    const userId = ctx.state.user._id;
    const book = await bookStore.findOne({ id: ctx.params.id });
    if (book && book.userId !== userId) {
        ctx.response.body = { message: 'Unauthorized User' };
        ctx.response.status = 403;
        return;
    }
    await bookStore.remove({ id: ctx.params.id });
    ctx.response.body = { message: 'success' };
    ctx.response.status = 204;
    broadcast(book.userId, { type: 'deleted', payload: book });
});
