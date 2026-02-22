% Adquad L10. Algoritm 1.
% f - functia de integrare
% a,b - limite de intergrae
% e - eroare
function I = Adquad(f,a,b,e)
  c = (a+b)/2;
  fa = f(a); fb = f(b); fc = f(c);
  I = quadstep(f,a,b,e,fa,fc,fb);
end

% f - functia de integrare
% a,b - limite intergare
% e - erroare
% fa,fc,fb - f(a) f(c) f(b)
function I = quadstep(f,a,b,e,fa,fc,fb)
  h = b-a;
  c = (a+b) / 2;
  fd = f((a+c)/2);
  fe = f((c+b)/2);
  
  % n = 2 -> h/2*n
  I1 = (h/6) * (fa+4*fc+fb);
  % n = 4 -> h/2*n
  I2 = (h/12) * (fa + 4*fb + 2* fc + 4*fe + fb);
  
  if abs(I1-I2) < e
    I = I2 + (I2-I1)/15;
    return;
  end
  
  Ia = quadstep(f, a, c, e, fa, fd, fc);
  Ib = quadstep(f, c, b, e, fc, fe, fb);
  I = Ia + Ib;
end