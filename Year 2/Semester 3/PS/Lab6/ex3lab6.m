function ex3lab6 (nr_simulari)
  r = rand(1, nr_simulari);
  t = exprnd(5,1,nr_simulari) .* (r<=0.4) + unifrnd(4,6,1,nr_simulari) .* (r>0.4);
  %a)
  mean(t)
  std(t)
  %b)
  mean(t>5) %sum(t > 5)/nr_simulari
  %c)
  count1 = 0;
  count2 = 0;
  for i=1:nr_simulari
    r = rand;
    if r<=0.4
      I = 1;
      T = exprnd(5);
    else
      I=2;
      T = unifrnd(4,6);
    endif
    if T > 5
      count1++;
      if I == 2
        count2++;
      endif
    endif
  endfor
  count2/count1
endfunction
