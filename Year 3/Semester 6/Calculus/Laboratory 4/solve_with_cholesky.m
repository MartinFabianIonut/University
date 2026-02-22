function x = solve_with_cholesky(A, b)
    % Rezolvarea unui sistem folosind descompunerea Cholesky
    R = cholesky_decomposition(A);
    % Rezolvarea sistemului R^T*x = b prin substituție inversă
    y = forward_substitution(R.',b);
    x = backward_substitution(R,y);
end
