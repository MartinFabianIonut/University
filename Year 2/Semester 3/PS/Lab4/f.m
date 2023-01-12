function pos = f(p=0.5, k=1000)
  x=binornd(1,p,1,k);
  x=2*x-1;
  pos = zeros(1,k+1);
  for i=1:k
    pos(i+1) = pos(i)+x(i);
  endfor
endfunction
