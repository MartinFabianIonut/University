function f_substituted = perform_variable_substitution(f_original, a, b, alpha, beta)
    % Transformarea liniară
    f_substituted = @(u) f_original(((b - a) * u + a * (beta - alpha) - b * (beta - alpha)) / (beta - alpha));
end
