function x = gauss_elimination_partial_pivoting(A)
    % Eliminare gaussiana cu pivotare partiala
    % A - matricea extinsa a sistemului

    n = size(A, 1); % Dimensiunea matricei
    x = zeros(n, 1); % Inițializare soluții
    NROW = (1:n)'; % Inițializare pointeri la rânduri

    % Etapa de eliminare
    for i = 1:n-1
        % Găsirea pivotului
        [~, p] = max(abs(A(NROW(i:n), i))); 
        p = p + i - 1;
        if A(NROW(p), i) == 0
            error('Nu există o soluție unică');
        end

        % Schimbarea rândurilor dacă pivotul nu este pe diagonală
        if NROW(i) ~= NROW(p)
            NCOPY = NROW(i);
            NROW(i) = NROW(p);
            NROW(p) = NCOPY;
        end

        % Eliminare
        for j = i+1:n
            m = A(NROW(j), i) / A(NROW(i), i);
            A(NROW(j), i+1:end) = A(NROW(j), i+1:end) - m * A(NROW(i), i+1:end);
        end
    end

    % Verificare dacă sistemul este compatibil determinat
    if A(NROW(n), n) == 0
        error('Nu există o soluție unică');
    end

    % Etapa de substituție inversă
    x(n) = A(NROW(n), n+1) / A(NROW(n), n);
    for i = n-1:-1:1
        x(i) = (A(NROW(i), n+1) - A(NROW(i), i+1:n) * x(i+1:n)) / A(NROW(i), i);
    end
end
