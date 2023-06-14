pkg load statistics;
ham = fileread('keywords_ham.txt');
hwords = strsplit(ham, ' ');

spam = fileread('keywords_spam.txt');
swords = strsplit(spam, ' ');


u_hwords = unique(hwords);
u_swords = unique(swords);

length_u_hwords = length(u_hwords);
length_u_swords = length(u_swords);

u_hwords = u_hwords(2:length_u_hwords)
u_swords = u_swords(2:length_u_swords)

hFrq = [];
Luh = length(u_hwords);
Lh = length(hwords);

for i=1:Luh
  counter = 0;
  for j=1:Lh
    counter += strcmp(u_hwords(i), hwords(j));
  endfor
  hFrq = [hFrq, counter/Lh];
endfor

hFrq'

email1 = fileread('email1.txt');
email1_words = strsplit(email1,' ');
u_email1_words = unique(email1_words);

length_u_email1_words = length(u_email1_words);

u_email1_words = u_email1_words(2:length_u_email1_words)

Lue = length(u_email1_words);

for i=1:Luh
  counter = 0;
  for j=1:Lue
    counter += strcmp(u_hwords(i), u_email1_words(j));
  endfor
  if counter == 0
    hFrq(i) = 1 - hFrq(i);
  endif
endfor

hFrq'

P_C_ham = length(hwords)/(length(hwords)+length(swords))
P_C_spam = 1 - P_C_ham

P_ham = P_C_ham*prod(hFrq);
P_ham

Lus = length(u_swords)
Ls = length(swords)
hFrqS = [];
for i=1:Lus
  counter = 0;
  for j=1:Ls
    counter += strcmp(u_swords(i), swords(j));
  endfor
  hFrqS = [hFrqS, counter/Ls];
endfor


for i=1:Lus
  counter = 0;
  for j=1:Lue
    counter += strcmp(u_swords(i), u_email1_words(j));
  endfor
  if counter == 0
    hFrqS(i) = 1 - hFrqS(i);
  endif
endfor
hFrqS'

P_spam = P_C_spam*prod(hFrqS);
P_spam

if(P_ham>P_spam)
  printf("Emailul 1 este ham\n")
else
  printf("Emailul 1 este spam\n")
endif

