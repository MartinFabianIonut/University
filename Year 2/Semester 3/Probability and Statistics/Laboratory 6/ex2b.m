g = @(x) exp( - x .^ 2), a = -2, b = 2, M = 1
%g = @(x) abs(sin(exp(x))), a = -1, b = 3, M = 1
%g = @(x)  x .^2 ./ (1 + x .^ 2) .* (x <= 0) + sqrt(2 * x - x .^ 2) .* (x > 0), a = -1, b = 2, M = 1


function ex2b (g,a,b,M,n)
  monte1 = MC1(g,a,b,M,n)
  monte2 = MC2(g,a,b,n)
  integral(g,a,b)
endfunction

ex2b(g,a,b,M,500)
