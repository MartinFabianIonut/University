function rest = calculeazaRestulLagrange(x, x_cheb, f_derivative_order_m_plus_1)
    % x: punctul de evaluare
    % x_cheb: nodurile de interpolare
    % f_derivative_order_m_plus_1: o funcție care calculează derivata de ordin m+1 la un punct dat

    % Numărul de noduri
    m = length(x_cheb);
    
    % Calculăm produsul (x - x0)(x - x1)...(x - xm)
    prod_term = prod(x - x_cheb);
    
    % Evaluăm derivata de ordin m+1 la un punct intermediar în interval
    derivative_value = f_derivative_order_m_plus_1(x);
    
    % Calculăm restul folosind formula Lagrange
    rest = prod_term / factorial(m + 1) * derivative_value;
end
