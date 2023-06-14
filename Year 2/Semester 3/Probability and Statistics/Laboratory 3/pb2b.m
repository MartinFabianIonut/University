v = binornd(5, 1 / 3, 1, 5000);
h = hist(v, 0 : 5);

prob_sim = sum(v == 2) / 5000;
fprintf("Probabilitatea din simulari: %7.6f\n", prob_sim);
prob_teo = binopdf(2, 5, 1 / 3);
fprintf("Probabilitatea teoretica: %7.6f\n", prob_teo);