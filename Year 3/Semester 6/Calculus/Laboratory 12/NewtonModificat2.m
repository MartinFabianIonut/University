function [z,ni]=NewtonModificat2(f,fd,fdd,x0,eroarea,nmax)
    if nargin < 6, nmax=50; end
    xp=x0(:);   %x precedent
    for k=1:nmax
        xc=xp-2*f(xp)/(fd(xp)* (1+ sqrt(1- (2*f(xp)*fdd(xp)) / (fd(xp)^2) )  ) ); %disp(xc);
        if abs(xc-xp)<eroarea
            z=xc; %succes
            ni=k;
            return
        end
        xp=xc;
    end
    error('S-a depasit numarul maxim de iteratii');
end