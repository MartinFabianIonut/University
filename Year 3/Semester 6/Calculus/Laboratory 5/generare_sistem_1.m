function [A,B] = generare_sistem_1(n)
% genereaza primul sistem
% n - dimensiunea sistemului
% A - matricea sistemului
% B - matricea termenilor liberi

A = spdiags([-ones(n,1),5*ones(n,1),-ones(n,1)],-1:1,n,n)
full(A);
B = A*ones(n,1)