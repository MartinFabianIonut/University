clc; clear all; history -c

clf; grid on; hold on;
p = 1 / 3; n = 5; m = 2000;

% generate m values following the binomial law with n tries and probability p;
x = binornd(n, p, 1, m);

% histogram of values from the binomial distribution;
N = hist(x, 0 : n);

% bar graph with the frequencies;
bar(0 : n, N / m, 'hist', 'FaceColor', 'b');

% theoretical binomial distribution;
bar(0 : n, binopdf(0 : n, n, p), 'FaceColor', 'y');

% legend for the figure;
legend('probabilitatile estimate', 'probabilitatile teoretice');

set(findobj('type', 'patch'), 'facealpha', 0.7); xlim([-1 n + 1]);

