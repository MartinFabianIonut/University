%g = @(x) exp( - x .^ 2), a = -2, b = 2, M = 1
%g = @(x) abs(sin(exp(x))), a = -1, b = 3, M = 1
%g = @(x)  x .^2 ./ (1 + x .^ 2) .* (x <= 0) + sqrt(2 * x - x .^ 2) .* (x > 0), a = -1, b = 2, M = 1

%pentru fiecare apel, scoatem din comentariu unul dintre g-urile de mai sus
function exercitiul2 (g,a,b,M,n)
  clf; hold on;
  x = unifrnd(a,b,1,n);
  y = unifrnd(0,M,1,n);
  plot(x(g(x)<=y), y(g(x)<=y),'*r');
  plot(x(g(x)>y), y(g(x)>y),'*b');

endfunction

exercitiul2(g,a,b,M,1500)
