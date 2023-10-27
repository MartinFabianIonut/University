const Koa = require('koa');
const app = new Koa();
const server = require('http').createServer(app.callback());
const WebSocket = require('ws');
const wss = new WebSocket.Server({ server });
const Router = require('koa-router');
const cors = require('koa-cors');
const bodyparser = require('koa-bodyparser');

app.use(bodyparser());
app.use(cors());
app.use(async (ctx, next) => {
  const start = new Date();
  await next();
  const ms = new Date() - start;
  console.log(`${ctx.method} ${ctx.url} ${ctx.response.status} - ${ms}ms`);
});

app.use(async (ctx, next) => {
  await new Promise(resolve => setTimeout(resolve, 200));
  await next();
});

app.use(async (ctx, next) => {
  try {
    await next();
  } catch (err) {
    ctx.response.body = { issue: [{ error: err.message || 'Unexpected error' }] };
    ctx.response.status = 500;
  }
});

class Book {
  constructor({ id, title, author, publicationDate, isAvailable, price }) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.publicationDate = publicationDate;
    this.isAvailable = isAvailable;
    this.price = price;
  }
}

const books = [];
books.push(new Book({ id: '1', title: 'Anna Karenina', author: 'Tolstoi', publicationDate: new Date(Date.now()), isAvailable: true, price: 130 }));
books.push(new Book({ id: '2', title: 'War and Peace', author: 'Tolstoi', publicationDate: new Date(Date.now()), isAvailable: true, price: 141 }));
books.push(new Book({ id: '3', title: 'The Idiot', author: 'Dostoievsky', publicationDate: new Date(Date.now()), isAvailable: false, price: 88 }));
books.push(new Book({ id: '4', title: 'The Brothers Karamazov', author: 'Dostoievsky', publicationDate: new Date(Date.now()), isAvailable: true, price: 76 }));
books.push(new Book({ id: '5', title: 'Les Miserables', author: 'Hugo', publicationDate: new Date(Date.now()), isAvailable: true, price: 234 }));
books.push(new Book({ id: '6', title: 'Steppenwolf', author: 'Hesse', publicationDate: new Date(Date.now()), isAvailable: true, price: 45 }));
books.push(new Book({ id: '7', title: 'The Magic Mountain', author: 'Mann', publicationDate: new Date(Date.now()), isAvailable: false, price: 98 }));
books.push(new Book({ id: '8', title: 'The Trial', author: 'Kafka', publicationDate: new Date(Date.now()), isAvailable: true, price: 33 }));
books.push(new Book({ id: '9', title: 'The Metamorphosis', author: 'Kafka', publicationDate: new Date(Date.now()), isAvailable: false, price: 25 }));
books.push(new Book({ id: '10', title: 'The Stranger', author: 'Camus', publicationDate: new Date(Date.now()), isAvailable: true, price: 34 }));

let lastUpdated = books[books.length - 1].publicationDate;
let lastId = books[books.length - 1].id;
const pageSize = 10;

const broadcast = data =>
  wss.clients.forEach(client => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify(data));
    }
  });

const router = new Router();

router.get('/book', ctx => {
  ctx.response.body = books;
  ctx.response.status = 200;
});

router.get('/book/:id', async (ctx) => {
  const bookId = ctx.request.params.id;
  const book = books.find(book => bookId === book.id);
  if (book) {
    ctx.response.body = book;
    ctx.response.status = 200;
  } else {
    ctx.response.body = { message: `book with id ${bookId} not found` };
    ctx.response.status = 404;
  }
});

const createBook = async (ctx) => {
  const book = ctx.request.body;
  if (!book.title || !book.author) {
    ctx.response.body = { message: 'Title and Author are required' };
    ctx.response.status = 400;
    return;
  }
  book.id = `${parseInt(lastId) + 1}`;
  lastId = book.id;
  books.push(book);
  ctx.response.body = book;
  ctx.response.status = 201;
  broadcast({ event: 'created', payload: { book } });
};

router.post('/book', async (ctx) => {
  await createBook(ctx);
});

router.put('/book/:id', async (ctx) => {
  const id = ctx.params.id;
  const book = ctx.request.body;
  const bookId = book.id;
  if (bookId && id !== book.id) {
    ctx.response.body = { message: `Param id and body id should be the same` };
    ctx.response.status = 400;
    return;
  }
  if (!bookId) {
    await createBook(ctx);
    return;
  }
  const index = books.findIndex(book => book.id === id);
  if (index === -1) {
    ctx.response.body = { issue: [{ error: `book with id ${id} not found` }] };
    ctx.response.status = 400;
    return;
  }
  books[index] = book;
  lastUpdated = new Date();
  ctx.response.body = book;
  ctx.response.status = 200;
  broadcast({ event: 'updated', payload: { book } });
});

router.del('/book/:id', ctx => {
  const id = ctx.params.id;
  const index = books.findIndex(book => id === book.id);
  if (index !== -1) {
    const book = books[index];
    books.splice(index, 1);
    lastUpdated = new Date();
    broadcast({ event: 'deleted', payload: { book } });
  }
  ctx.response.status = 204;
});

app.use(router.routes());
app.use(router.allowedMethods());

server.listen(3000);
