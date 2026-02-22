function [z,ni]=NewtonModificat(f,fd,fdd,x0,eroarea,nmax)
    if nargin < 6, nmax=50; end
    xp=x0(:);   %x precedent
    for k=1:nmax
        xc=xp-f(xp)/sqrt(fd(xp)^2-f(xp)*fdd(xp)); %disp(xc);
        if abs(xc-xp)<eroarea
            z=xc; %succes
            ni=k;
            return
        end
        xp=xc;
    end
    error('S-a depasit numarul maxim de iteratii');
end