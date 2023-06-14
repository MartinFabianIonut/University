% Predicatul genereazã, pe rând, elementele Mculori, Mculori-1,...,1.
% (Mculori > 0, daca se da o valoare <=0, se va returna doar acea
% valoare).
% Model matematic: candidat(M) = {
%                                 M,
%                                 candidat(M-1), M>1
%                                }
% candidat(Mculori:Intreg, Candidat:Intreg)
% Modele de flux (i,o) - nedeterminist, (i,i)
% Mculori - nr intreg care desemneaza capatul superior
% al intervalului din care se vor lua elemente candidat si se vor pune
% in Candidat
% Candidat - nr intreg apartinand intervalului [1, Mculori]

candidat(Mculori,Mculori).
candidat(Mculori, Candidat) :-
                       Mculori>1,
                       Mculori1 is Mculori-1,
                       candidat(Mculori1,Candidat).

% Cazuri de testare: candidat(3,C). -3;2;1;false
% findall(Candidat, candidat(5,Candidat), Lista).

% -----------------------------------------------------------------------
% Functia/predicatul principal, care va returna o lista cu toate
% posibilitatile de a colora n tari cu m culori, astfel incat
% doua tari alaturate sa nu fie colorate la fel.
% Model matematic:
% cum_pot_colora_n_tari_cu_m_culori(N,M) = reuniunea tuturor listelor
%                                          generate de colorare(N,M)
%
% cum_pot_colora_n_tari_cu_m_culori(N: Intreg, M: Intreg, LRez: Lista)
% Modele de flux: (i,i,o) - nedeterminist, (i,i,i)
% N - numarul de tari, unde vecinatatea este stabilita de alaturarea in
% lista
% M - numarul de culori cu care se pot colora cele N tari, astfel incat
% doua tari alaturate sa nu aiba aceeasi culoare
% LRez - lista care contine toate listele ce desemneaza posibilitatile
% de colorare a celor N tari cu M culori


cum_pot_colora_n_tari_cu_m_culori(N,M,LRez):-findall(Rez, colorare(N,M,Rez),LRez).

% Cazuri de testare:
% cum_pot_colora_n_tari_cu_m_culori(2,2,Rez).
% cum_pot_colora_n_tari_cu_m_culori(2,4,Rez).
% cum_pot_colora_n_tari_cu_m_culori(4,2,Rez).
% cum_pot_colora_n_tari_cu_m_culori(9,2,Rez).
% cum_pot_colora_n_tari_cu_m_culori(3,2,Rez).
% cum_pot_colora_n_tari_cu_m_culori(3,3,Rez).


% -----------------------------------------------------------------------
% Functia/predicatul de colorare a Ntari cu Mculori, astfel incat tarile
% vecine sa fie colorate diferit ; se apeleaza la functia auxiliara
% colorare_aux si la aflarea unui candidat
% Model matematic:
% colorare(Ntari, Mculori) = colorare_aux(Ntari,Mculori,1,candidat(Mculori))
%
% colorare(Ntari: Intreg, Mculori: Intreg, Rez: Lista)
% Modele de flux: (i,i,o) - nedeterminist, (i,i,i)
% Ntari -numarul de tari, unde vecinatatea este stabilita de alaturarea
% in lista
% Mtari - numarul de culori cu care se pot colora cele N tari, astfel
% incat doua tari alaturate sa nu aiba aceeasi culoare
% Rez - lista care contine, pe rand, posibilitatile de colorare a celor
% Ntari cu Mculori


colorare(Ntari, Mculori, Rez):- candidat(Mculori, Candidat),
                        colorare_aux(Ntari, Mculori, Rez, 1, [Candidat]).

% Cazuri de testare:
% findall(Rez, colorare(3,3,Rez),Solutii).
% findall(Rez, colorare(4,3,Rez),Solutii).

% -----------------------------------------------------------------------
% Functie auxiliara folosita pentru construirea solutiei problemei de
% fata, stiind prima valoare din lista solutie si generand alte valori
% candidat care sa satisfaca acea conditie de colorare diferita a
% tarilor vecine, adica alaturarea intre doua culori (identificate prin
% numere) sa fie distincta
% Model matematic:
% colorare_aux(Ntari,Mculori,Lungime,Col) = {
%                                            Col, Ntari = Lungime
%                                            colorare_aux(Ntari,Mculori,L
%                                            ungime+1,candidat(Mculori)+C
%                                            ol), cadidat(Mculori) !=
%                                            capul listei Col
%                                           }
%
% colorare_aux(Ntari: Intreg, Mculori: Intreg, Rez: Lista, Lungime:
% Intreg, Col: Lista)
% Modele de flux: (i,i,o,i,i) - nedeterminist, (i,i,i,i,i)
% Ntari -numarul de tari, unde vecinatatea este stabilita de alaturarea
% in lista
% Mtari - numarul de culori cu care se pot colora cele N tari, astfel
% incat doua tari alaturate sa nu aiba aceeasi culoare
% Rez - lista care va contine, pe rand, posibilitatile de colorare a
% celor Ntari cu Mculori care se termina cu prima valoare din Col (data
% ca parametru)
% Lungime - numar care desemneaza cate valori am introdus
% in lista colectoare Col Col - lista in care construiesc solutiile
% problemelei, cate o solutie fiind descoperita in momentul in care
% Lungime este egal cu Ntari


colorare_aux(Ntari, _, Col, Ntari, Col):- !.
colorare_aux(Ntari, Mculori, Rez, Lungime, [H|T]):-
                                            candidat(Mculori, Candidat),
                                            Candidat\==H,
                                            LungimeNoua is Lungime+1,
                                            colorare_aux(Ntari, Mculori, Rez, LungimeNoua,[Candidat|[H|T]]).

% Cazuri de testare:
% findall(Rez, colorare_aux(3,3,Rez,1,[1]),Solutii).
% findall(Rez, colorare_aux(4,3,Rez,1,[3]),Solutii).
