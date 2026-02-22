function adaptive_Gauss_Legendre(f, a, b, err, NMAX)
    n = 5;

    % Compute the integral using MATLAB's built-in function
    I1 = integral(f, a, b);

    % Compute the initial Gauss-Legendre integral
    [g_nodes,g_coeff]=Gauss_Legendre(n-1);
    I2p = vquad(g_nodes, g_coeff, f);
    [g_nodes,g_coeff]=Gauss_Legendre(n);
    I2 = vquad(g_nodes, g_coeff, f);

    % Iteratively refine the Gauss-Legendre approximation
    while abs(I2 - I2p) > err && n < NMAX
        n = n + 1;
        I2p = I2;
        [g_nodes, g_coeff] = Gauss_Legendre(n);
        I2 = vquad(g_nodes, g_coeff, f);
    end

    % Display results
    fprintf("%s\n", func2str(f));
    fprintf("Aproximarea numarului de noduri pentru eroarea %.1e este: %d\n", err, n);
    fprintf("Aproximarea integralei date folosind Gauss-Legendre: %.10f\n", I2);
    fprintf("Aproximarea integralei date folosind integral: %.10f\n", I1);
end