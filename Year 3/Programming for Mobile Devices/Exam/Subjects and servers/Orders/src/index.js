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
  console.log(`${ctx.method} ${ctx.url} - ${ms}ms`);
});

let index = 1;
app.use(async (ctx, next) => {
  if (index < 10) {
    index += 2;
  } else {
    index = 1;
  }
  await new Promise(resolve => setTimeout(resolve, index * 1000));
  await next();
});

const menuItems = Array.from(Array(10).keys()).map(code => ({ code, name: `p${code}` }));

wss.on('connection', async ws => {
  console.log('on connection');
  await new Promise(resolve => setTimeout(resolve, 5000));
  ws.send(JSON.stringify({ payload: menuItems }));
});

const router = new Router();

const items = [];

router.post('/item', ctx => {
  const { code, quantity } = ctx.request.body;
  console.log('code and quantity', code, quantity);
  const index = menuItems.findIndex(it => it.code === code);
  if (typeof code !== 'number' || index === -1) {
    console.log('code not found');
    ctx.response.body = { text: 'Menu item code not found' };
    ctx.response.status = 400;
  } else if (typeof quantity !== 'number' || quantity < 0) {
    console.log('quantity is not a positive number');
    ctx.response.body = {
      code: menuItems[index].code,
      text: 'Quantity must be a positive number'
    };
    ctx.response.status = 400;
  } else {
    const item = { id: items.length + 1, code, quantity };
    items.push(item);
    ctx.response.body = item;
    ctx.response.status = 200;
  }
});

app.use(router.routes());
app.use(router.allowedMethods());

server.listen(3000);
