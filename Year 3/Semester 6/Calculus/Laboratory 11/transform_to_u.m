function g = transform_to_u(f, a, b)
    % Define the transformation function
    g = @(u) f((b - a) / 2 * u + (b + a) / 2);
end