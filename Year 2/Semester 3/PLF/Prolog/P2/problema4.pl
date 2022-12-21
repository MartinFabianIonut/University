%lab 2 - p2 - problema 4
% a)Sa se interclaseze fara pastrarea dublurilor doua liste sortate.
%
% b)Se da o lista eterogena, formata din numere intregi si liste de
% numere sortate. Sa se interclaseze fara pastrarea dublurilor toate
% sublistele.
% De ex:[1, [2, 3], 4, 5, [1, 4, 6], 3, [1, 3, 7, 9, 10], 5, [1, 1, 11], 8]
% =>[1, 2, 3, 4, 6, 7, 9, 10, 11].




% Functie care returneaza true, daca primul argument de tip integer se
% gaseste in al doilea argument de tip lista, altfel returneaza false
% Modelul matematic:
% member1(E,l1...ln) = {
%                       false, l=[],
%                       true, E = l1,
%                       member1(E, l2...ln), E != l1,
%                      }
%
% member1(E: intreg, L: lista)
% Modele de flux: (i,i)
% E - nr intreg cautat in lista de nr intregi L
% L - lista de nr intregi in care se cauta elementul intreg E
member1(E,[E|_]):-!.
member1(E,[_|L]) :- member1(E,L).

% Cazuri de testare:
% member1(1,[]).
% member1(1,[2,3]).
% member1(1,[1]).
% member1(1,[3,4,5,1,1,1,1]).
% member1(1,[0,0,0,0,0,0,0,3,3,2,45]).

% -------------------------------------------------------------
% Functie care elimina dublurile dintr-o lista de nr intregi si
% returneaza o noua lista, formata doar din elementele unice (dintre
% cele care se repetau de 2 sau mai multe ori, pastreaza doar unul)
% Modelul matematic:
% eliminare_dubluri(l1...ln) = {
%                                [], l=[],
%                                eliminare_dubluri(l2...ln), l1 apartine
%                                    l2...ln,
%                                l1 + eliminare_dubluri(l2...ln), altfel
%                              }
% eliminare_dubluri(L1: lista, L2: lista)
% Modele de flux: (i,o,), (i,i)
% L1 - lista de nr intregi initiala, din care se vor elimina
% duplicatele, pastrandu-se doar cate o aparitie si se va construi L2
% L2 - lista de nr intregi construita din elementele unice si o singura
% aparitie a celor duplicate din lista L1
% Observatie: daca elementele din lista L1 sunt ordonate, la fel vor fi
% si in L2, altfel lista rezultata nu va fi ordonata
eliminare_dubluri([],[]).
eliminare_dubluri([H|T],LREZ):- member1(H,T),!,eliminare_dubluri(T,LREZ).
eliminare_dubluri([H|T],LREZ):- not(member1(H,T)),!,eliminare_dubluri(T,LREZ2),LREZ=[H|LREZ2].

% Cazuri de testare:
% eliminare_dubluri([],Rez).
% eliminare_dubluri([1,2,3,4],Rez).
% eliminare_dubluri([1,2,3,3,3,3,4,4,4,5,6],Rez).
% eliminare_dubluri([1,2,3,4,2,3,4,1,1,5,5,2],Rez).

% -------------------------------------------------------------------------
% 4. a) Modelul matematic: interclasare(l1...ln, l'1...l'n) = = { [],
% l=[] && l'=[] eliminare_dubluri(l1...ln), l'=[]
% eliminare_dubluri(l'1...l'n), l=[]
% interclasare_doua_liste_fara_dubluri(eliminare_dubluri(l1...ln),eliminare_dubluri(l'1...l'n)),l!=[]
% && l'!=[] }
%
% interclasare(L1: lista, L2: lista, L3: lista)
% Modele de flux: (i,i,o), (i,i,i)
% L1 - prima lista, sortata crescator (cu posibile dubluri)
% L2 - a doua lista, de asemenea sortata crescator (cu posibile dubluri)
% L3 - lista care va rezulta din interclasarea listelor L1 si L2,
% pastrandu-se elementele fara eventuale dubluri (a se observa ca se
% elimina si dublurile existente in listele initiale L1 si L2), motiv
% pentru care se foloseste o functie auxiliara de interclasare a doua
% liste sortate si fara dubluri
%
%
% Functia de interclasare a doua liste fara dubluri:
% Modelul matematic:
% interclasare_doua_liste_fara_dubluri(l1...ln, l'1...l'n) =
%                 = {
%                     l, l'=[],
%                     l', l=[],
%                     l1 + interclasare_doua_liste_fara_dubluri(l2...ln,l'), l1<l'1
%                     l1 + interclasare_doua_liste_fara_dubluri(l2...ln,l'2...l'n),l1=l'1
%                     l'1 + interclasare_doua_liste_fara_dubluri(l,l'2...l'n), l'1<l1
%                   }
% interclasare_doua_liste_fara_dubluri(L1: lista, L2: lista, L3: lista)
% Modele de flux: (i,i,o), (i,i,i)
% L1 - prima lista, sortata crescator, fara dubluri
% L2 - a doua lista, de asemenea sortata crescator si fara dubluri
% L3 - lista care va rezulta din interclasarea listelor L1 si L2,
% pastrandu-se elementele fara eventuale dubluri




interclasare([],[],[]):-!.
interclasare(L1,[],L3REZ):- !, eliminare_dubluri(L1,L3REZ).
interclasare([],L2,L3REZ):- !, eliminare_dubluri(L2,L3REZ).
interclasare(L1,L2,L3):-!,eliminare_dubluri(L1,L11),eliminare_dubluri(L2,L22),interclasare_doua_liste_fara_dubluri(L11,L22,L3).

interclasare_doua_liste_fara_dubluri(L3REZ,[],L3REZ):-!.
interclasare_doua_liste_fara_dubluri([],L3REZ,L3REZ):-!.

interclasare_doua_liste_fara_dubluri([H1|T1],[H2|T2],L3REZ):- H1<H2,!,
                               interclasare_doua_liste_fara_dubluri(T1,[H2|T2],L3),
                               L3REZ = [H1|L3].


interclasare_doua_liste_fara_dubluri([H1|T1],[H2|T2],L3REZ):- H1=H2,!,
                               interclasare_doua_liste_fara_dubluri(T1,T2,L3),
                               L3REZ = [H1|L3].
interclasare_doua_liste_fara_dubluri([H1|T1],[H2|T2],L3REZ):- H2<H1,!,
                               interclasare_doua_liste_fara_dubluri([H1|T1],T2,L3),
                               L3REZ = [H2|L3].

% Cazuri de testare:
% interclasare([],[],X).
% interclasare([1],[1],X).
% interclasare([2,4],[2,4,5],X).
% interclasare([2,4,5],[2,4],X).
% interclasare([1,2,4,9],[1,2,3,5,8],X).
% interclasare([1,2,4,9],[1,2,2,2,3,5,8],X).
% interclasare([1,2,4,4,9,9,9],[1,2,2,2,3,5,5,5,5,5,5,8],X).
% interclasare([0,0,0,0,0,0,0,0,5],[1,2,2,2,3,5,5,5,5,5,5,8],X).




% -----------------------------------------------------------------------

% 4. b)
% Modelul matematic:
% interclasare_subliste_principal(L1) = interclasare_subliste(L1,[])
%
% interclasare_subliste_principal(L1: lista, L2: lista)
% Modele de flux: (i,o), (i,i)
% L1 - lista eterogena data ca argument, pentru care se va face
% interclasarea sublistelor ei (cu elemente ordonate crescator), fara
% pastrarea dublurilor, si se va returna in o noua lista, L2
% L2 - lista in care se va memora si returna
% interclasarea sublistelor din L1, fara pastrarea dublurilor

% Modelul matematic:
% interclasare_subliste(l1...ln, Col) = {
%                                         Col, l=[]
%                                         interclasare_subliste(l2..ln,interclasare(l1,Col)),
      %                                         l1 este sublista
%                                         interclasare_subliste(l2...ln,Col),
      %                                         l1 nu este sublista
%                                       }
%
% interclasare_subliste(L1: lista, L2: lista, COL: lista)
% Modele de flux: (i,o,i), (i,i,i)
% L1 - lista eterogena data ca argument, pentru care se va face
% interclasarea sublistelor ei (cu elemente ordonate crescator), fara
% pastrarea dublurilor, si se va returna in o noua lista, L2
% L2 - lista in care se va memora si returna
% interclasarea sublistelor din L1, fara pastrarea dublurilor
% COL - lista colectoare, auxiliara, folosita pentru interclasarea
% sublistelor listei L1, fara pastrarea dublurilor, care, in final, va
% depune in L2 lista rezultata



interclasare_subliste_principal(L1,L2):-interclasare_subliste(L1,L2,[]).

interclasare_subliste([],L2,L2).
interclasare_subliste([H|T],L2,COL):- is_list(H),!,
    interclasare(H,COL,COLREZ),
    interclasare_subliste(T,L2,COLREZ).
interclasare_subliste([H|T],L2,COL):- not(is_list(H)),interclasare_subliste(T,L2,COL).

% Cazuri de testare:
% interclasare_subliste_principal([],X).
% interclasare_subliste_principal([1,2,3],X).
% interclasare_subliste_principal([1,2,[1],3],X).
% interclasare_subliste_principal([1,2,[1],3,[1]],X).
% interclasare_subliste_principal([1,2,[1],3,[1,1,1,1]],X).
% interclasare_subliste_principal([1,[2,3],4,5,[1,4,6],3,[1,3,7,9,10],5,[1,1,11],8],X).

