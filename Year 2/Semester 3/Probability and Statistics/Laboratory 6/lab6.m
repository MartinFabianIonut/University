function lab6(nr_simulari, m, sigma)
  x = normrnd(m,sigma, 1,nr_simulari)
  disp("ii)/n")
  %pt i)
  hist(x,10)
  %pt ii)
  figure %face inca o figura
  [no_of_aparitii, centers] = hist(x,10);
  hist(x,centers,10/(max(x)-min(x)));
  hold on;
  t = linspace(min(x),max(x),nr_simulari);%valori echidistante
  plot(t,normpdf(t,m,sigma),'-r') %graficul fct de densitate
  % fct de dens e normpdf
  mean(x)%val medie sim
  m%val medie teoretica
  std(x)
  sigma
  mean(160<x & x<170)
  normcdf(170,m,sigma)-normcdf(160,m,sigma)
endfunction
