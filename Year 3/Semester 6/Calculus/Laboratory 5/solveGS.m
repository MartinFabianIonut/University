function [x] = solveGS(A, B, err)
    [n, ~] = size(A);
    D = diag(diag(A));
    L = zeros(size(A));
    
    for i = 1 : n
       for j = 1 : n
          if i > j
            L(i, j) = -1 * A(i, j);
          end
       end
    end

    U = zeros(size(A));
    for i = 1 : n
       for j = 1 : n
          if i < j
            L(i, j) = -1 * A(i, j);
          end
       end
    end

    T = (D + L)^(-1) * U;
    C = (D - L)^(-1) * B;

    xv = rand(n, 1);
    xn = T * xv + C;
    errC = norm(xn - xv, inf);

    while errC > err
       xv = xn;
       xn = T * xv + C;
       errC = norm(xn - xv, inf);
    end
    
    x = xn;
end