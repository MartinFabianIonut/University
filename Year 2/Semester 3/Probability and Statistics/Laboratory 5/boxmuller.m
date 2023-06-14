%generare de perechi de valori

function [x,y] = boxmuller(n)
  u = rand(2,n);

  r = sqrt( - 2* log(u(1,:)));
  v = 2*pi * u(2,:);

  x = r .* cos(v); %vector1 .* vector2 = altvector de aceleasi dim cu vector1 si vector2
  y = r .* sin(v);



endfunction
