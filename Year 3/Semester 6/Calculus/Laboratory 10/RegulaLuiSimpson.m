% f - f(x) functie de integrat. ex: f = @(x) 1 + x.^2;
% a,b - limitele integralei
% n - !trebuie sa fie par! numarul de patrate. Cu cat n e mai mare => cu atat precizia creste
function I = RegulaLuiSimpson(f,a,b,n)
  % delta x;
  dx     = (b-a)/n; 
  I = f(a)+f(b);
  
  for i=1:n-1
      xi = a+i*dx;
      if mod(i,2) == 1
         I = I + (4 * f(xi));
      else
         I = I + (2 * f(xi));
      end
  end
  I =  (dx/3) * I;
end