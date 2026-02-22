function [f, semn_sin, semn_cos] = reducere_la_primul_cadran(x)
%     fprintf('Enter x = %f;', x);
    x = mod(x, 2*pi);

    precision = 10; % numărul maxim de zecimale pentru verificare

    if round(x, precision) >= round(0, precision) && round(x, precision) <= round(pi/2, precision) || round(x,precision) >= round(2*pi, precision)
        % primul cadran +, +
        semn_sin = 1;
        semn_cos = 1;
        f = x; 
%         fprintf('x = %f => c1', x);
    elseif round(x, precision) > round(pi/2, precision) && round(x, precision) <= round(pi, precision)
        % al doilea cadran +, -
        semn_sin = 1;
        semn_cos = -1;
        f = pi - x;
%         fprintf('x = %f => c2', x);
    elseif round(x, precision) > round(pi, precision) && round(x, precision) < round(3*pi/2, precision)
         % al treilea cadran -, -
        semn_sin = -1;
        semn_cos = -1;
        f = x - pi;
%         fprintf('x = %f => c3', x);
    else
        % al patrulea cadran -, +
        semn_sin = -1;
        semn_cos = 1;
        f = 2*pi - x;
%         fprintf('x = %f => c4', x);
    end
end
