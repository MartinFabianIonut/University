function [A, B] = generareSistemDiagDom(n)
    A = rand(n);
    A = A + eye(n) * n;
    sol = zeros(n, 1);
    for i = 1 : n
        sol(i, 1) = i;
    end
    B = A * sol;
end