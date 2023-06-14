 clc; history -c
m = 1000;

% ARUNCAREA A PATRU ZARURI

disp('i)')
% possible sums when throwing four dice;
sume_posibile = 4 : 24;

% generating m throws of 4 dice;
zaruri = randi(6, 4, m);

% sum the throws by columns;
sume_sim = sum(zaruri);

% histogram for the throws;
frecv_abs = hist(sume_sim, sume_posibile);

% matrix: [histogram line; posibilities line]
sume_frecv_abs = [sume_posibile; frecv_abs]'

disp('ii)')
clf; hold on; grid on;

% set the values on the Ox axis;
xticks(sume_posibile); xlim([3, 25]);

% set the values on the Oy axis;
yticks(0 : 0.01 : 0.14); ylim([0 0.14]);

% bar graph with possible sums on Ox and frequency on Oy;
bar(sume_posibile, frecv_abs / m, 'hist', 'FaceColor', 'b');

% maximum frequence
frecv_abs_max = max(frecv_abs)

% select the most frequent sum;
sume_cele_mai_frecvente = sume_posibile(frecv_abs == frecv_abs_max)

disp('iii)')

% generating the theoretical sums
sume_teor = [];
 for i1 = 1 : 6
   for i2 = 1 : 6
     for i3 = 1 : 6
       for i4 = 1 : 6
         sume_teor = [sume_teor i1 + i2 + i3 + i4];
       endfor
     endfor
   endfor
 endfor

% histogram of the theoretical sums;
frecv_abs = hist(sume_teor, sume_posibile);

% matrix = [histogram line; posibilities line]
sume_frecv_abs = [sume_posibile; frecv_abs]'

% bar graph with possible values on Ox and frequency on Oy;
bar(sume_posibile, frecv_abs / length(sume_teor), 'FaceColor','y');

% maximum frequency
frecv_abs_max = max(frecv_abs)

% selecting the sum with the maximum frequency
sume_cele_mai_frecvente = sume_posibile(frecv_abs == frecv_abs_max)

% legend for the bar graph;
legend('frecvente relative', 'probabilitati');

% I don't know what this does;
set(findobj('type', 'patch'), 'facealpha', 0.7);

disp('iv)')

% probability of sum being at least 10 when knowing it is at most 20
(sume_sim >= 10) && (sume_sim <= 20)
(sume_sim >= 10) & (sume_sim <= 20)
prob_cond_sim = sum((sume_sim >= 10) & (sume_sim <= 20)) / sum(sume_sim <= 20)

% probability of sum being at least 10 when knowing it is at most 20
prob_cond_teor = sum((sume_teor >= 10) & (sume_teor <= 20)) / sum(sume_teor <= 20)
