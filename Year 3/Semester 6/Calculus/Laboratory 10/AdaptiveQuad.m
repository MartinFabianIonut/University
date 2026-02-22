% f   - f(x) functia de intergat. ex: f = @(x) 1 + x.^2;
% a,b - Limite de intergare
% e   - eroareametoda de intergare
function I = AdaptiveQuad(f,a,b,e,g)
   n  = 4;
   I1 = g(f,a,b,n);
   I2 = g(f,a,b,n*2);
   if abs(I1-I2) < e 
       I=I2;
       return
   else
       I=AdaptiveQuad(f,a,(a+b)/2,e,g) + AdaptiveQuad(f,(a+b)/2,b,e,g);
   end
end