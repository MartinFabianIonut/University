% Mock test 3 - Fabian-Ionuț Martin, 234/2
f = @(t) sqrt(3*t-t.^2-2) .* sin(t);

n = 4;
NMAX = 200;
err = 1e-10;

a=1;
b=2;
I1 = integral(f,a,b);

y=variable_change(f,a,b);
y = simplify(y)
y = matlabFunction(y);
% Observam ca avem in functie si ponderea, pe care o vom elimina
y = y ./ sqrt(1-x.^2)
y = matlabFunction(y);

[g_nodes,g_coeff]=Gauss_Cheb2(n-1)
I2p = vquad(g_nodes, g_coeff, y)
[g_nodes,g_coeff]=Gauss_Cheb2(n);
I2 = vquad(g_nodes, g_coeff, y);

while abs(I2 - I1) > err && n < NMAX
    n = n + 1;
    I2p = I2;
    [g_nodes, g_coeff] = Gauss_Cheb2(n);
    I2 = vquad(g_nodes, g_coeff, y);
end

% Display results
fprintf("Functia de aproximat %s\n", func2str(f));
fprintf("Aproximarea numarului de noduri pentru eroarea %.1e este: %d\n", err, n);
fprintf("Aproximarea integralei date folosind Gauss-Cebîșev #2: %.15f\n", I2);
fprintf("Aproximarea integralei date folosind integral: %.15f\n", I1);

% Functiile folosite:
function I = vquad(g_nodes, g_coeff, f)
    I = g_coeff * f(g_nodes);
end

function [g_nodes,g_coeff]=Gaussquad(alpha,beta)
    n=length(alpha); rb=sqrt(beta(2:n));
    J=diag(alpha)+diag(rb,-1)+diag(rb,1);
    [v,d]=eig(J);
    g_nodes=diag(d); 
    g_coeff=beta(1)*v(1,:).^2;
end

function [g_nodes,g_coeff]=Gauss_Cheb2(n)
    beta=[pi/2,1/4*ones(1,n-1)];
    alpha=zeros(n,1);
    [g_nodes,g_coeff]=Gaussquad(alpha,beta);
end

function y=variable_change(f,a,b)
    % Performs variable change if a=!-1 y b=!1
    syms x;
    x=((b-a)./2).*x+(b+a)./2;
    dx=(b-a)./2;
    y=feval(f,x)*dx;
end
