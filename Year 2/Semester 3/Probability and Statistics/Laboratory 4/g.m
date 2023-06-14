function lastFrq = g (m=1000)
  p = 0.5;
  k=10;
  last = zeros(1,m);
  for i=1:m
    last(i) = f(p,k)(end);
  endfor
  lastFrq = hist(last,-k:k);
  [-k:k; lastFrq]'
  bar(hist(last,-k:k))
endfunction
