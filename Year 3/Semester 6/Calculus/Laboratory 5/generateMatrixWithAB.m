function A = generateMatrixWithAB(n, a, b)
    % Initialize a nxn matrix of zeros
    A = zeros(n, n);
    
    % Set the main diagonal to value a
    for i = 1:n
        A(i, i) = a;
    end
    
    % Set the first diagonal above the main diagonal to value b
    for i = 1:n-1
        A(i, i+1) = b;
    end
    
    % Set the first diagonal below the main diagonal to value b
    for i = 2:n
        A(i, i-1) = b;
    end

%     for i = 4:n
%         A(i, i-3) = b;
%     end
end
