function [dfN] = derivataDeOrdinNPlusUnu(f, n) 
    syms x; 
    % Calculează derivata funcției f în raport cu x
    for i=1:n+1
        dfN = diff(f, x);
        f = dfN;
    end
end