%  Problema 3
% Definirea parametrilor
n = 20; % Gradul ecuației polinomiale
r = 1:n; % Rădăcinile ecuației produsului rădăcinilor (x-1)(x-2)...(x-n) = 0
a = 2; % Coeficientul pentru ecuația specificată a^k = 2^-k

% Construirea polinomului pentru ecuația produsului rădăcinilor
poli1 = poly(r);

% Calculul rădăcinilor pentru ecuația produsului rădăcinilor
r1 = roots(poli1);

% Construirea polinomului pentru ecuația specificată
poli2 = a.^-(1:n);

% Calculul rădăcinilor pentru ecuația specificată
r2 = roots(poli2);

% Definirea dispersiei pentru perturbare
dispersie = 1e-10;

% Setarea seed-ului pentru reproducibilitate
rng(42);

% Graficul pentru ecuația produsului rădăcinilor
figure;
plot(r1, zeros(size(r1)), 'k.', 'MarkerSize', 21);
hold on;

% Perturbarea coeficienților și recalcularea rădăcinilor
for i = 1:20
    % Distribuție normală pentru perturbare
    perturbare_norm = dispersie * randn(1, length(poli1));
    poli1_perturbat_norm = poli1 + perturbare_norm;
    r1_perturbat_norm = roots(poli1_perturbat_norm);
    plot(r1_perturbat_norm, 'b.');
    
    % Distribuție uniformă pentru perturbare
    perturbare_unif = dispersie * (rand(1, length(poli1)) - 0.5);
    poli1_perturbat_unif = poli1 + perturbare_unif;
    r1_perturbat_unif = roots(poli1_perturbat_unif);
    plot(r1_perturbat_unif, 'r.');
end

title('Rădăcinile ecuației produsului rădăcinilor POLINOMULUI 1');
legend('Rădăcinile inițiale', 'Perturbare normală', 'Perturbare uniformă');
xlabel('Partea reală');
ylabel('Partea imaginară');
hold off;

% Graficul pentru ecuația specificată
figure;
plot(r2, zeros(size(r2)), 'k.', 'MarkerSize', 21);
hold on;

% Perturbarea coeficienților și recalcularea rădăcinilor
for i = 1:20
    % Distribuție normală pentru perturbare
    perturbare_norm = dispersie * randn(1, length(poli2));
    poli2_perturbat_norm = poli2 + perturbare_norm;
    r2_perturbat_norm = roots(poli2_perturbat_norm);
    plot(r2_perturbat_norm, 'bo');
    
    % Distribuție uniformă pentru perturbare
    perturbare_unif = dispersie * (rand(1, length(poli2)) - 0.5);
    poli2_perturbat_unif = poli2 + perturbare_unif;
    r2_perturbat_unif = roots(poli2_perturbat_unif);
    plot(r2_perturbat_unif, 'r.');
end

title('Rădăcinile ecuației POLINOMULUI 2');
legend('Rădăcinile inițiale', 'Perturbare normală', 'Perturbare uniformă');
xlabel('Partea reală');
ylabel('Partea imaginară');
hold off;

% Calculul derivatei polinomului pentru ecuația produsului rădăcinilor
derivata_poli1 = polyder(poli1);

% Calculul derivatei polinomului pentru ecuația specificată
derivata_poli2 = polyder(poli2);

% Initializarea matricilor pentru stocarea numerelor de conditionare
numar_condit_norm1 = zeros(length(r1), 1);
numar_condit_unif1 = zeros(length(r1), 1);
numar_condit_norm2 = zeros(length(r2), 1);
numar_condit_unif2 = zeros(length(r2), 1);

% Calculul numerelor de conditionare pentru perturbarea normala pentru ecuația produsului rădăcinilor
for i = 1:length(r1)
    numar_condit_norm1(i) = abs(r1(i)) / abs(polyval(derivata_poli1, r1(i)));
end

% Calculul numerelor de conditionare pentru perturbarea uniforma pentru ecuația produsului rădăcinilor
for i = 1:length(r1)
    numar_condit_unif1(i) = abs(r1(i)) / abs(polyval(derivata_poli1, r1(i)));
end

% Calculul numerelor de conditionare pentru perturbarea normala pentru ecuația specificată
for i = 1:length(r2)
    numar_condit_norm2(i) = abs(r2(i)) / abs(polyval(derivata_poli2, r2(i)));
end

% Calculul numerelor de conditionare pentru perturbarea uniforma pentru ecuația specificată
for i = 1:length(r2)
    numar_condit_unif2(i) = abs(r2(i)) / abs(polyval(derivata_poli2, r2(i)));
end

% Afisarea tabelului pentru perturbarea normala pentru ecuația produsului rădăcinilor
fprintf('Perturbare normală pentru ecuația produsului rădăcinilor:\n');
fprintf('Radacina\tNumar de conditionare\n');
for i = 1:length(r1)
    fprintf('%f\t%f\n', r1(i), numar_condit_norm1(i));
end

% Afisarea tabelului pentru perturbarea uniforma pentru ecuația produsului rădăcinilor
fprintf('\nPerturbare uniformă pentru ecuația produsului rădăcinilor:\n');
fprintf('Radacina\tNumar de conditionare\n');
for i = 1:length(r1)
    fprintf('%f\t%f\n', r1(i), numar_condit_unif1(i));
end

% Afisarea tabelului pentru perturbarea normala pentru ecuația specificată
fprintf('\nPerturbare normală pentru ecuația specificată:\n');
fprintf('Radacina\tNumar de conditionare\n');
for i = 1:length(r2)
    fprintf('%f\t%f\n', r2(i), numar_condit_norm2(i));
end

% Afisarea tabelului pentru perturbarea uniforma pentru ecuația specificată
fprintf('\nPerturbare uniformă pentru ecuația specificată:\n');
fprintf('Radacina\tNumar de conditionare\n');
for i = 1:length(r2)
    fprintf('%f\t%f\n', r2(i), numar_condit_unif2(i));
end