% exercitiul3

function ex3(n=500)
  clf;

  t = linspace(0,2*pi,360);

  polar(t, 4* ones(1,360), 'w');
  %pentru a pastra tipul graficului
  hold on;

  [x,y] = boxmuller(n);
  %distributia normala standard 2 linii, n col
  z = normrnd(0,1, 2,n);

  plot(x,y,'r*');
  plot(z(1,:), z(2,:), 'c*');
  polar(t,0.5* ones(1,360), 'b');


  %bm - de la boxmuller
  bulls_eye_bm = mean(x .^2 + y .^2 < 0.25)
  %n - de la distributia normala
  bulls_eye_n = mean(z(1,:) .^2 + z(2,:) .^2 < 0.25)


  1 - exp(-1/8)

  %probabilitatea teoretica

  % P(sqrt(x ^ 2 +  y ^ 2) < 0.5) = P(sqrt(- 2* log(u1)) < 0.5) =
  % = P(- 2* log(u1) < 0.25) = P (log (u1) > -1/8) =
  % = P(u1 > exp(-1/8)) = 1 - P(u1 < exp(-1/8)), 0 < u1 < 1

  % probabilitatea teoretica = 1-exp(-1/8)

  % P(u < 0.5) = 0.5, 0<u<1
endfunction
