function L = cholesky_decomposition2(A)
    [n, ~] = size(A);
    L = zeros(n); % Inițializare matricea L

    % Pasul 1: Calculul primei valori din matricea L
    L(1, 1) = sqrt(A(1, 1));

    % Pasul 2: Calculul primului element de pe coloana 1 a matricei L
    L(2:n, 1) = A(2:n, 1) / L(1, 1);

    % Pasul 3: Iterarea pentru celelalte elemente
    for i = 2:n-1
        % Pasul 4: Calculul elementului de pe diagonala principală
        L(i, i) = sqrt(A(i, i) - sum(L(i, 1:i-1).^2));

        % Pasul 5: Calculul elementelor de pe coloana i+1 în jos a matricei L
        for j = i+1:n
            L(j, i) = (A(j, i) - sum(L(j, 1:i-1).*L(i, 1:i-1))) / L(i, i);
        end
    end

    % Pasul 6: Calculul ultimului element de pe diagonala principală
    L(n, n) = sqrt(A(n, n) - sum(L(n, 1:n-1).^2));
end
