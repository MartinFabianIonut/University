function adaptive_Gauss_Laguerre(f, err, NMAX, I1)
    n = 5;

    % Compute the initial Gauss-Laguerre integral
    [g_nodes, g_coeff] = Gauss_Laguerre(n-1);
    I2p = vquad(g_nodes, g_coeff, f);
    [g_nodes, g_coeff] = Gauss_Laguerre(n);
    I2 = vquad(g_nodes, g_coeff, f);

    % Iteratively refine the Gauss-Laguerre approximation
    while abs(I2 - I2p) > err && n < NMAX
        n = n + 1;
        I2p = I2;
        [g_nodes, g_coeff] = Gauss_Laguerre(n);
        I2 = vquad(g_nodes, g_coeff, f);
    end

    % Display results
    fprintf("%s\n", func2str(f));
    fprintf("Numărul de noduri pentru precizia de %.1e este: %d\n", err, n);
    fprintf("Aproximarea integralei cu Gauss-Laguerre: %.10f\n", I2);
    fprintf("Valoarea integrală calculată folosind funcția integral: %.10f\n", I1);
end
