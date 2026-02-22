% Problema 1
% a)

A = [10, 7, 8, 7; 7, 5, 6, 5; 8, 6, 10, 9; 7, 5, 9, 10];
B = [32; 23; 33; 31];

x_initial = A \ B;

% Definirea perturbației membrului drept
B_perturbat = [32.1; 22.9; 33.1; 30.9];

% Calcularea soluției perturbate
x_perturbat = A \ B_perturbat;

% Calcularea erorilor relative
eroare_rel_in = norm(B - B_perturbat) / norm(B);
eroare_rel_out = norm(x_initial - x_perturbat) / norm(x_initial);
raport_erori = eroare_rel_out / eroare_rel_in;

% Afișarea rezultatelor
disp('a)');
disp('Soluția inițială:');
disp(x_initial');
disp('Soluția perturbată:');
disp(x_perturbat');
disp(['Eroarea relativă la intrare: ', num2str(eroare_rel_in)]);
disp(['Eroarea relativă la ieșire: ', num2str(eroare_rel_out)]);
disp(['Raportul erorilor este: ', num2str(raport_erori)]);

% b)

A_perturbat = [10, 7, 8.1, 7.2; 7.08, 5.04, 6, 5; 8, 5.98, 9.89, 9; 6.99, 4.99, 9, 9.98];

x_perturbat2 = A_perturbat \ B;

eroare_rel_in2 = norm(A - A_perturbat) / norm(A);
eroare_rel_out2 = norm(x_initial - x_perturbat2) / norm(x_initial);
raport_erori2 = eroare_rel_out2 / eroare_rel_in2;

% Afișarea rezultatelor
disp('b)');
disp('Soluția perturbată:');
disp(x_perturbat2');
disp(['Eroarea relativă la intrare: ', num2str(eroare_rel_in2)]);
disp(['Eroarea relativă la ieșire: ', num2str(eroare_rel_out2)]);
disp(['Raportul erorilor este: ', num2str(raport_erori2)]);

