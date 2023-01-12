%Exercitiul 1

function pb1(v=[0,1,2,3],p=[0.46,0.4,0.1,0.04],n=1000)

  clf;
  %pt a vedea valorile pe unde vin
  grid on;
  %pt a avea si val relative, si histograma
  hold on;

  x = rndvardisc(v,p,n);

  y = randsample(v,n, replacement = true, p);

  frecvRelativax = hist(x,v)/n;
  bar(v, frecvRelativax,'hist','FaceColor','b');

  frecvRelativay = hist(y,v)/n;
  bar(v, frecvRelativay,'FaceColor','y');

  set(findobj('type','patch'), 'facealpha', 0.7);


endfunction
