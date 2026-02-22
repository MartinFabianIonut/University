function [A] = generare_matrice_cu_proprietati(n)
    if mod(n,2) == 1
        disp('n trebuie sa fie par');
        return;
    end
    if n < 4
        disp('n trebuie să fie cel putin 4, pentru conformitate cu b');
        return;
    end

    A = zeros(n);

    for i=1:n
        A(i,i) = 3; % diag principala
        if i < n
            A(i,i+1) = -1; % superdiagonala
        end
        if i > 1
            A(i,i-1) = -1; % sybdiagonala
        end
        if A(i,n-i+1) == 0
            A(i,n-i+1) = 1/2; % diag secundara
        end
    end
end
