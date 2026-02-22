
% f - funcția
% x0, x1 - puncte de pornire
% ea, er - eroarea absolută, respectiv relativă
% Nmax - numărul maxim de iterații

function [z, ni] = Secant(f, x0, x1,  er, Nmax)

    if (nargin < 5)
        Nmax = 50;
    end
    if (nargin < 4)
        er = 0;
    end

    xv = x0; fv = f(xv); xc = x1; fc = f(xc);
    
    for k = 1 : Nmax
        xn = xc - fc .* (xc - xv) / (fc - fv);
        if (abs(xn - xc) < er) % Succes
            z = xn; ni = k;
            return
        end
        xv = xc; fv = fc; xc = xn; fc = feval(f, xn); % Iterația următoare
    end

    % Eșec
    error('S-a depășit numărul maxim de iterații.')

end

