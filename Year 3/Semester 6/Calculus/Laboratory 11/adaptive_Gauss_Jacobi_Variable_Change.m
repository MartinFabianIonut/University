function adaptive_Gauss_Jacobi_Variable_Change(f, foriginala, a, b, alpha, beta, err, NMAX)
    n = 5;

    % Compute the integral using MATLAB's built-in function
    I1 = integral(foriginala, a, b);

    % Compute the initial Gauss-Legendre integral
    [g_nodes,g_coeff]=Gauss_Jacobi(n-1, alpha, beta);
    I2p = vquad(g_nodes, g_coeff, f);
    [g_nodes,g_coeff]=Gauss_Jacobi(n, alpha, beta);
    I2 = vquad(g_nodes, g_coeff, f);

    % Iteratively refine the Gauss-Legendre approximation
    while abs(I2 - I1) > err && n < NMAX
        n = n + 1;
        I2p = I2;
        [g_nodes, g_coeff] = Gauss_Jacobi(n, alpha, beta);
        I2 = vquad(g_nodes, g_coeff, f);
    end

    % Display results
    fprintf("Functia originala%s\n", func2str(foriginala));
    fprintf("Functia schimbata%s\n", func2str(f));
    fprintf("Aproximarea numarului de noduri pentru eroarea %.1e este: %d\n", err, n);
    fprintf("Aproximarea integralei date folosind Gauss-Jacobi: %.10f\n", I2);
    fprintf("Aproximarea integralei date folosind integral: %.10f\n", I1);
end