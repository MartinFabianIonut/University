m = 1000;

p = sum(hygepdf(3 : 6, 49, 6, 6));
% primul parametru - cate bile trebuie sa avem extrase din cele marcate (de pe bilet)
% al doilea parametru - numarul total de bile in urna
% al treilea parametru - numar de bile marcate
% al patrulea parametru - cate bile contine extragerea

x = geornd(p, 1, m); % cu acest apel de functie estimam ...
                     % numarul de esecuri pana la primul ...
                     % succes (cu probabilitatea p) de 1000 ori;

prob_estim = mean(x >= 10)
prob_teor = 1 - sum(geopdf(0 : 9, p))