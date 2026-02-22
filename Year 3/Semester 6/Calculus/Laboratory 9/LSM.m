function [ res ] = LSM( x, y, functions, points )
    % metoda celor mai mici patrate
    % x, y datele de antrenament ; y = f(x)
    % functions - functiile de baza
    % points - punctele de aproxiamt

    try 
        phi = functions(x);
        phiApprox = functions(points);
    catch
        phi = functions(x,y);
        phiApprox = functions(points);
    end
%     disp(phi);
%     disp(points);
%     
%     disp('phiApprox below');
%     disp(phiApprox);

    [n , ~] = size(phi); % nr pct * nr functii

    % A = Z^T * Z ; B = Z^T * y ; unde Z^T e phi
    for i=1:n
        for j=1:n
            A(i,j)=phi(i,:)*transpose(phi(j,:));
        end
        B(i,1)=phi(i,:)*transpose(y);
    end

    %A*a=B
    a=linsolve(A,B);

    res = transpose(a)*phiApprox;
end