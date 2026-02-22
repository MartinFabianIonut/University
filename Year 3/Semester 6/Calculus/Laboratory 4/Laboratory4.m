clc;

for dimensiune = 5:8
    disp(['Matrice și vector de dimensiune ', num2str(dimensiune), ':']);
    
    % Generare matrice aleatoare, vector de termeni liberi
    [A,b] = generate_matrices(dimensiune);

    [Ar,br] = generate_test_data(dimensiune);

    % Descompunere Cholesky
    R = cholesky_decomposition(Ar);
    disp('Matricea triunghiulară superioară R obținută prin descompunerea Cholesky RANDOM:');
    disp(R);

    % Rezolvare sistem folosind descompunerea Cholesky
    x_cholesky = solve_with_cholesky(Ar, br);
    disp('Soluția sistemului folosind descompunerea Cholesky RANDOM:');
    disp(x_cholesky');

     disp('-------------');

    % Descompunere Cholesky
    R = cholesky_decomposition(A);
    disp('Matricea triunghiulară superioară R obținută prin descompunerea Cholesky:');
    disp(R);
    R2 = cholesky_decomposition2(A);
    disp('Matricea triunghiulară inferioară R2 obținută prin descompunerea Cholesky:');
    disp(R2);

    % Rezolvare sistem folosind descompunerea Cholesky
    x_cholesky = solve_with_cholesky(A, b);

    % Descompunere LUP
    [L, U, P] = LUP_decomposition(A);
    disp('Matricea inferior triunghiulară L:');
    disp(L);
    disp('Matricea superior triunghiulară U:');
    disp(U);
    disp('Matricea de permutare P:');
    disp(P);

    % Rezolvare sistem folosind descompunerea LUP
    x_lup = solve_LUP(A, b);
    disp('Soluția sistemului folosind descompunerea LUP:');
    disp(x_lup');

    % Eliminare Gauss
    x_gauss = gauss_elimination_partial_pivoting([A, b]);
    disp('Soluția sistemului folosind eliminarea Gauss:');
    disp(x_gauss');

    % Cholesky
    disp('Soluția sistemului folosind descompunerea Cholesky:');
    disp(x_cholesky');
    
    disp('---------------------------------------------');
end





