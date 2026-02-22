function C = NewtonCotes(n, a, b)
    % Generate equally spaced points
    x = linspace(a, b, n);
    
    % Initialize coefficients
    C = zeros(1, n);
    
    for i = 1:n
        % Construct Lagrange basis polynomial Li
        Li = ones(1, n);
        for j = 1:n
            if i ~= j
                Li = conv(Li, [1, -x(j)]) / (x(i) - x(j));
            end
        end
        
        % Integrate Li over [a, b]
        C(i) = integral(@(t) polyval(Li, t), a, b);
    end
end