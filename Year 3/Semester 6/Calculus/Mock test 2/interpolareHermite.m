function [ res ] = interpolareHermite (x, f, fd, point)
    % Initialize
    [~, m] = size(x);
    z = zeros(2 * m, 1);
    q = zeros(2 * m, 2 * m);

    % Setup z and q for Hermite conditions
    for i = 1:m
        z(2*i-1) = x(i);
        z(2*i) = x(i);
        q(2*i-1, 1) = f(i);
        q(2*i, 1) = f(i);
        q(2*i, 2) = fd(i);
        if i > 1
            q(2*i-1, 2) = (q(2*i-1, 1) - q(2*i-2, 1)) / (z(2*i-1) - z(2*i-2));
        end
    end

    % Compute divided differences
    for i = 3:2*m
        for j = 3:i
            q(i, j) = (q(i, j-1) - q(i-1, j-1)) / (z(i) - z(i-j+1));
        end
    end

   % Compute polynomial p at point
    p = q(1, 1);
    s = 1;
    res = p;
    for i = 2:2*m
        s = s * (point - z(i-1)); % Correct updating of s
        p = p + s * q(i, i);
        if abs(p - res) < 1e-10  % Use res to check difference
            break;
        end
        res = p;
    end
end