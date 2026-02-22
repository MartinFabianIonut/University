function z = evaluate_spline(x, a, b, c, d, t)
    n = length(x);
    x = x(:); t = t(:);
    k = ones(size(t));
    for j = 2 : n - 1
        k(x(j) <= t) = j;
    end

    % Evaluare interpolant:
    s = t - x(k);
    z = d(k) + s .* (c(k) + s .* (b(k) + s .* a(k)));
end