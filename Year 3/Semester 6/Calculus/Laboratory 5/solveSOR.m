function [ x ] = solveSOR( A, B, err, omega )
    if (omega<=0) || (omega>=2)
        error('parametrul relaxarii ilegal')
    end
    [m,n]=size(A);
    x0=zeros(size(B));
    if (m~=n) || (n~=length(B))
        error('dimensiuni ilegale')
    end
    
    %calculul lui T si c (pregatirea iteratiilor)
    M = 1/omega*diag(diag(A))+tril(A,-1);
    N = M-A;
    T = M\N;
    c = M\B;
    alfa = norm(T,inf);
    x = x0(:);
    true = 1;
    while true==1
        x0 = x;
        x = T*x0+c;
        if norm(x-x0,inf)<(1-alfa)/alfa*err
            true = 0;
        end
    end
end

