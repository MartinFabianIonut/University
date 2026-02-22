function [b] = generare_si_transpunere_matrice(n)
    if mod(n,2) == 1
        disp('n trebuie sa fie par');
        return;
    end
    if n < 4
        disp('n trebuie să fie cel putin 4, pentru a putea face n - 4 >= 0');
        return;
    end

    diferenta = (n - 4)/2;
    
    b = zeros(1, n); % un rand de valori de zero
    b([1,n]) = 2.5; % primul si ultimul sunt 2.5

    b(2:diferenta+1) = 1.5; % urmatoarele 'diferenta' elemente sunt 1.5
    b(diferenta+2:diferenta+3) = 1.0; % urmatoarele 2 elemente sunt 1
    b(diferenta+4:n-1) = 1.5; % urmatoarele 'diferenta' elemente sunt 1.5
    
    b = b'; % returnam transpusa matricei
end
