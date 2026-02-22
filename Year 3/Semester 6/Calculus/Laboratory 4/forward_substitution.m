function x = forward_substitution(A, b)
    % Solves the equation Ax = b for x, where A is a lower triangular matrix.
    
    n = length(b);
    x = zeros(n, 1);
    for i = 1:n
        x(i) = (b(i) - A(i, 1:i-1) * x(1:i-1)) / A(i, i);
    end
end
