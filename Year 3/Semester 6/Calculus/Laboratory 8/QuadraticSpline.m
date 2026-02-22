function [a, b, c] = QuadraticSpline(x, f, der, type)
% QUADRATICSPLINE 
% Spline natural:
if (nargin < 4) || (type==2)
    der = [0, 0];
end

n = length(x);
% Sortăm nodurile, dacă e nevoie
if any(diff(x) < 0)
    [x, ind] = sort(x);
else
    ind = 1 : n;
end
y = f(ind); x = x(:); y = y(:);

dx = diff(x);
ddiv=diff(y) ./ dx;
ds = dx(1 : end - 1);

% Modify md calculation for quadratic term
md = 2 * (ddiv(1 : end - 1) + ddiv(2 : end));

% Tratare diferențiată tip
switch type
    case 0 % complet (zero second derivative)
        dp1 = 1; dpn = 1;
        md1 = der(1); mdn = der(2);
    case 1   % d1 (specified first derivatives)
% Modify for quadratic splines with specified first derivatives
        dp1 = 1; dpn = 1;
        md1 = 2 * ddiv(1) - der(1);
        mdn = 2 * ddiv(end) + der(2);
    case {2, 3}  % natural and deBoor not implemented (modify for quadratic)
        error('Natural and deBoor boundary conditions not implemented for quadratic splines');
end
% Construim sistemul rar:
dp = [dp1; ds; dpn];
dp1 = [0; 0; 0];
dm1 = [0; 0; 0];
md = [md1; md; mdn];
diag_elements = {[dm1; zeros(n - length(dm1), 1)], dp, [dp1; zeros(n - length(dp1), 1)]};

% Use the cell array with spdiags
A = spdiags(diag_elements, -1:1, n, n);
m = A \ md;
c = y(1 : end - 1);
b = m(1 : end - 1);
% Modify coefficient calculations for quadratic terms
a = (m(2 : end) + m(1 : end - 1) - 2 * ddiv) ./ (dx.^2);
end