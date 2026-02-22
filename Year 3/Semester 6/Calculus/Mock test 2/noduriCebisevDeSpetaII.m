function nodes = noduriCebisevDeSpetaII(m, a, b)
    % m: numărul de noduri
    % a, b: capetele intervalului [a, b]
    j = 0:m;
    nodes = cos(j * pi / m);
    nodes = sort(0.5 * (b - a) * nodes + 0.5 * (b + a)); % scalare la [a, b]
end
