const Koa = require('koa');
const app = new Koa();
const server = require('http').createServer(app.callback());
const Router = require('koa-router');
const cors = require('koa-cors');
const bodyparser = require('koa-bodyparser');

app.use(bodyparser());
app.use(cors());

app.use(async function (ctx, next) {
  const start = new Date();
  await next();
  const ms = new Date() - start;
  console.log(`${ctx.method} ${ctx.url} - ${ms}ms`);
});

app.use(async (ctx, next) => {
  await new Promise(resolve => setTimeout(resolve, 2000));
  await next();
});

const spaces = Array.from(Array(30).keys())
  .map(id => ({ id, number: `s${id}`, status: id % 2 === 0 ? 'free' : 'taken', takenBy: id % 2 === 0 ? '' : 'u1' }));

const router = new Router();
router.get('/space', ctx => {
  ctx.response.body = spaces;
  ctx.response.status = 200;
});

router.put('/space/:id', ctx => {
  const space = ctx.request.body;
  const id = parseInt(ctx.params.id);
  const index = spaces.findIndex(space => space.id === id);
  if (id !== space.id || index === -1) {
    ctx.response.body = { text: 'Space not found' };
    ctx.response.status = 400;
  } else if (space.status === spaces[index].status) {
    ctx.response.body = { text: 'Conflict, cannot update status' };
    ctx.response.status = 409;
  } else {
    Object.assign(spaces[index], space);
    ctx.response.body = spaces[index];
    ctx.response.status = 200;
  }
});

app.use(router.routes());
app.use(router.allowedMethods());

server.listen(3000);
