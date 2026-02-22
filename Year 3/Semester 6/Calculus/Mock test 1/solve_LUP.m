function x = solve_LUP(A, b)
    % Rezolvare sistem folosind descompunerea LUP
    % L, U - matricile rezultate din descompunerea LUP
    % P - matricea de permutare
    % b - vectorul termenilor liberi

    % Aplicăm permutarea pe vectorul termenilor liberi
    [L, U, P] = LUP_decomposition(A);
    y = forward_substitution(L,P * b);
    x = backward_substitution(U,y);
end
