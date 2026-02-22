function R = cholesky_decomposition(A)
    % Descompunerea Cholesky
    % A - matrice hermitiană și pozitiv definită

    % Verificare dacă matricea A este pătratică
    [m, n] = size(A);
    if m ~= n
        error('Matricea nu este pătratică.');
    end

    % Inițializare matrice R cu matricea A
    R = A;

    % Descompunerea Cholesky
    for k = 1:m
        for j = k+1:m
            R(j, j:m) = R(j, j:m) - R(k, j:m) * conj(R(k, j)) / R(k, k); % Actualizare elemente
        end
        R(k, k:m) = R(k, k:m) / sqrt(R(k, k)); % Normalizare elemente de pe diagonală
    end
    R = triu(R);
end
