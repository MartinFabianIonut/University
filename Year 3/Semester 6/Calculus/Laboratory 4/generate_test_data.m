function [A, b] = generate_test_data(n)
    % Generare matrice de dimensiune nxn
    A = diag(1:n) + tril(randn(n), -1); % O matrice diagonală dominantă plus un pic de zgomot

    % Generare vector de termeni liberi de lungime n
    b = randn(n, 1);
end
