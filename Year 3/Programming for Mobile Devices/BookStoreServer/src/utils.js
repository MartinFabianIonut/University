import jwt from 'jsonwebtoken';

export const jwtConfig = { secret: 'my-secret' };

export const exceptionHandler = async (ctx, next) => {
  try {
    return await next();
  } catch (err) {
    console.log(err);
    const originalError = err.originalError || err;
    if (originalError instanceof jwt.TokenExpiredError) {
      ctx.body = { error: 'It has been a while since we saw you! Please log in again.' };
      ctx.status = 401;
    } else if (originalError instanceof jwt.JsonWebTokenError) {
      ctx.body = { error: 'Invalid credentials. Please log in again.' };
      ctx.status = 401;
    } else {
      ctx.body = { error: err.message || 'Unexpected error.' };
      ctx.status = err.status || 500;
    }
  }
};

export const timingLogger = async (ctx, next) => {
  const start = Date.now();
  await next();
  console.log(`${ctx.method} ${ctx.url} => ${ctx.response.status}, ${Date.now() - start}ms, Time: ${new Date()}`);
};
