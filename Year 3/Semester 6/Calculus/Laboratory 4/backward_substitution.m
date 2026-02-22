function x = backward_substitution(A, b)
    % Solves the equation Ax = b for x, where A is an upper triangular matrix.
    
    n = length(b);
    x = zeros(n, 1);
    for i = n:-1:1
        x(i) = (b(i) - A(i, i+1:n) * x(i+1:n)) / A(i, i);
    end
end
