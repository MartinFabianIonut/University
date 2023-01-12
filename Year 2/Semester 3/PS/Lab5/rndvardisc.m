% Generare de valori pentru o variabila discreta

%v - valori, p - probabilitati
% v = [v1,v2,v3]

% u = rand; 0 <= u <= 1

% cumsum(v) = [v1, v1+v2, v1+v2+v3] ==>
% intervalele [0,v1],[v1,v1+v2],[v1+v2,v1+v2+v3]

% v1+v2 <= u <=v1+v2+v3 ==> generam v3
function x = rndvardisc(v, p, n)
  q = cumsum(p);
  x = zeros(1,n);

  for i=1:n

    u = rand;
    j = 1;

    while u > q(j)
      j++;
    endwhile

    x(i) = v(j);

  endfor
endfunction
