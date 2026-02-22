function rest = calculeazaRestulHermite(x, x_cheb, f_derivative_order_n_plus_1)
    % x: punctul de evaluare
    % x_cheb: nodurile de interpolare
    % f_derivative_order_n_plus_1: o funcție care calculează derivata de ordin n+1 la un punct dat

    % Calculăm u(x) 
    % Presupunem că fiecare nod este folosit de două ori (de exemplu, pentru valoare și derivată)
    u_x = 1;
    for k = 1:length(x_cheb)
        u_x = u_x * (x - x_cheb(k))^2;  % Rădăcina fiecărui nod este folosită de două ori
    end

    % Numărul total de condiții este dublul numărului de noduri
    n = 2 * length(x_cheb);
    
    % Evaluăm derivata de ordin n+1 la un punct intermediar în interval
    derivative_value = f_derivative_order_n_plus_1(x);
    
    % Calculăm restul folosind formula Hermite
    rest = u_x / factorial(n + 1) * derivative_value;
end
