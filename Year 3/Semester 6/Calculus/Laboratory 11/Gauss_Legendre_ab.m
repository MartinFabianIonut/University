function vi=Gauss_Legendre_ab(f,n,a,b)
[t,w] = Gauss_Legendre(n);
vi = (b-a)/2*w*f((b-a)/2*t+(b+a)/2);