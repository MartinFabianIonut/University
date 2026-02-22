function nodes = noduriCebisevDeSpetaI(m, a, b)
    % m: numărul de noduri
    % a, b: capetele intervalului [a, b]
    j = 0:m;
    nodes = cos((2*j + 1) * pi / (2*m + 2));
    nodes = 0.5 * (b - a) * nodes + 0.5 * (b + a); % scalare la [a, b]
end