function y=variable_change(f,a,b)
    % Performs variable change if a=!-1 y b=!1
    syms x;
    x=((b-a)./2).*x+(b+a)./2;
    dx=(b-a)./2;
    y=feval(f,x)*dx;