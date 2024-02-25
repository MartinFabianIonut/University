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

app.use(async function (ctx, next) {
  const start = new Date();
  await next();
  const ms = new Date() - start;
  console.log(`${ctx.method} ${ctx.url} ${ctx.response.status} - ${ms}ms`);
});

function Question(i) {
  this.id = i;
  this.text = `${i} + ${i} = ?`;
  this.options = [...Array(4).keys()].map(j => 2 * i - j);
  this.indexCorrectOption = 0;
}

const questions = [...Array(3).keys()].map(i => new Question(i));

app.use(async (ctx, next) => {
  await new Promise(resolve => {
    setTimeout(resolve, 1000);
  });
  await next();
});

const router = new Router();

router.post('/auth', ctx => {
  const id = ctx.request.body.id;
  if (!id) {
    ctx.response.status = 400;
  } else {
    ctx.response.body = { token: id, questionIds: questions.map(q => q.id) };
    ctx.response.status = 201;
  }
});

router.get('/question/:id', ctx => {
  const id = parseInt(ctx.params.id);
  if (id < 0 || id >= questions.length) {
    ctx.response.status = 404;
  } else {
    ctx.response.status = 200;
    ctx.response.body = questions[id];
  }
});

const broadcast = (data) =>
  wss.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify({ payload: data }));
    }
  });

let nextIndex = questions.length;

setInterval(() => {
  const question = new Question(nextIndex++);
  console.log(`broadcast ${question.text}`);
  broadcast(question);
}, 10000);

app.use(router.routes());
app.use(router.allowedMethods());

server.listen(3000);
