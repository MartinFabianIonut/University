
% f - functia
% fd - derivata
% x0 - aproximația inițială
% ea - eroarea absolută (implicit 1E-3)
% er - eroarea relativă (implicit 0)
% Nmax - numărul maxim de iterații

function [z,ni] = Newton2(f,fd,x0,ea,er,nmax)

    if (nargin < 6)
        nmax = 50;
    end
    if (nargin < 5)
        er = 0;
    end
    if (nargin < 4)
        ea = 1E-3;
    end

    xp = x0(:); % x precedent
    
    for k = 1 : nmax
        xc = xp - feval(fd, xp) \ feval(f, xp);
        if (norm(xc - xp, inf) < ea + er * norm(xc, inf)) % Succes
            z = xc; ni = k;
            return
        end
        xp = xc;
    end

    % Eșec
    error('S-a depășit numărul maxim de iterații.');

end
