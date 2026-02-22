%  Problema 2
% a)

start = 10;
stop = 15;

A = zeros(1, stop-start);
B = zeros(1, stop-start);

for n = start:stop
    k = linspace(-1,1,n); % generam un vector cu n valori echidistante intre -1 si 1
    t = -1 + 2 .* (k./n); % construim termenul
    V = vander(t); % construim matricea Vandermode
    A(n-start+1) = cond(V, inf); % conditionare in functie de norma Cebisev
end

% b) 

for n = start:stop
    k = 1:n;
    o = ones(1, n); % generam un vector de 1
    t = o./k;   % construim termenul
    V = vander(t);  % construim matricea Vandermode
    B(n-start+1) = cond(V, inf); % conditionare in functie de norma Cebisev
end

figure(1)
hold on; % menține graficul activ pentru mai multe plasări
plot(start:stop, log10(A),'r*');
plot(start:stop, log10(B),'b*');
hold off; % eliberează graficul pentru a permite afișarea