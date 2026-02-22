% f - f(x) functie de interat. ex: f = @(x) 1 + x.^2;
% a,b - limitele integralei
% n - numarul de patrate. Cu cat n e mai mare => cu atat precizia creste
function I = RegulaTrapezului(f,a,b,n)
  % delta x;
  dx     = (b-a)/n; 
  I = f(a)+f(b);
  for i=1:n-1
      xi = a+i*dx;
      I = I + (2*f(xi));
  end
  I = (dx/2) * I;
end
