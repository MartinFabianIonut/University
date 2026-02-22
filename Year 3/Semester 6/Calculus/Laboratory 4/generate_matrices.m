function [A, b] = generate_matrices(n)
    % Generare sisteme de dimensiune n
    % A - matrice de dimensiune n x n
    % b - vector coloană de dimensiune n

    max_val = 89;

    % Generare matrice aleatoare până când matricea nu mai este singulară
    while true
        A = randi([-max_val, max_val], n); % Generează matricea A cu valori întregi în intervalul [-max_val, max_val]
        if det(A) ~= 0
            break; % Ieșire din buclă dacă matricea A nu mai este singulară
        end
    end

    % Pentru ca soluția să fie [1, ..., 1]^T, suma elementelor de pe fiecare linie trebuie să fie egală cu dimensiunea matricei
    b = sum(A, 2);
end