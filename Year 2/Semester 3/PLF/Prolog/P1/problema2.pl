%a. Sa se scrie un predicat care determina cel mai mic multiplu comun
% al elementelor unei liste formate din numere intregi.
%


%cmmdc(A:integer, B:integer, C: integer)// returneaza in C cel mai mare
% divizor comun al nr A si B
% A - primul nr, B - al doilea nr, C - cmmdc intre A si B
% (i,i,o)
% cmmdc(A,B,C) = { 0, if A=B=0,
%                  N, if (A=N && B=0) || (A=0 && B=N) || (A=B=N)
%                  cmmdc(A-B,B,C), if A>B && B>0
%                  cmmdc(A,B-A,C), if B>A && A>0
%                  }

cmmdc(0,0,0).
cmmdc(0,N,N).
cmmdc(N,0,N).
cmmdc(E,E,E).

cmmdc(A,B,C):- B>0,
    A>B,
    A1 is A-B,
    cmmdc(A1,B,C).
cmmdc(A,B,C):- A>0,
    A<B,
    B1 is B-A,
    cmmdc(A,B1,C).


% cmmmc(A:integer, B:integer, C: integer) // returneaza in C cel mai mic
% multiplu comun al nr A si B
% A - primul nr, B - al doilea nr, C - cmmmc intre A si B
% (i,i,o)
% % cmmmc(A,B,C) = { 0, if A=B=0 || (A=0 && B!=0) || (A!=0 && B=0)
%                   N, if (A=B=N) A*B/cmmdc(A,B,C), if A!=B && A!=0 &&
%                   B!=0 }


cmmmc(0,0,0).
cmmmc(0,N,0):- N\=0.
cmmmc(N,0,0):- N\=0.
cmmmc(N,N,N):- N\=0.

cmmmc(A,B,C):- A\=B, A\=0, B\=0,
    Produs is A*B,
    cmmdc(A,B,C1),
    C is Produs/C1.

%general(C: integer, L: list) // C este cmmmc al elementelor din L
%C - nr in care se va returna cmmmc pentru toate el din L
%L - lista cu numere
%(o,i)
% general(l1...ln) = { -1, if vida(L)
%                        l1, if L = l1
%                        general ([cmmmc(l1,l2) | l3...ln])
%                        , altfel
%                        }

general(_C,[]).
general(C,[C]).
general(C,[H1,H2|T]):-
    cmmmc(H1,H2,C1),
    general(C,[C1|T]).

/*Cazuri de testare:
 * general(C,[]). % TRUE
 * general(C,[5]). % C=5
 * general(C,[-4]). % C=-4
 * general(C,[1,2,3,4]). % C=12
 * general(C,[7,7,7]). % C=7
 * general(C,[7,2,3]). % C=42
 * general(C,[7,2,-3]). % FALSE, pt ca cmmdc se face doar intre nr
 * pozitive

    */




%b. Sa se scrie un
% predicat care adauga dupa 1-ul, al 2-lea, al 4-lea, al8-lea ...element
% al unei liste o valoare V data.

% adaug_final(L1: list, E: integer, L2: list) // se adauga E la finalul
% listei L1 si se retine totul in noua lista L2
% L1 - lista input
% E - nr pe care vreau sa il adaug la finalul lui L1
% L2 - lista formata din L1 + E
% (i,i,o)
%  adaug_final(l11...l1n,E) = { [E], if vida(L1)
%                               adaug_final(l12...l1n, E), altfel
%                                       }


adaug_final([], E, [E]).
adaug_final([H | T], E, [H | Rez]) :- adaug_final(T, E, Rez).


% adaug(L1: list, V: integer, L2: list) // se adauga dupa pozitiile
% initiale din L1 care sunt puteri ale lui 2 valoarea V si se creeaza L2
% L1 - lista initiala
% V - nr pe care vreau sa-l introduc conform cerintei
% L2 - lista nou creata
% (i,i,o)
%  adaug(L1,V,L2) = adaug_aux(L1,V,1,0,L2,[])

adaug(L1,V,L2):-Poz = 1,
                Con = 0,
                print('salut'),
                adaug_aux(L1,V,Poz,Con,L2,[]).


% adaug_aux(L1: list, V: integer, Pozitie: integer, Contor: integer, L2:
% list, Colector: list)
% L1 - lista initiala
% V - nr pe care vreau sa-l introduc conform cerintei
% Pozitie - nr care reprezinta puterile lui 2
% Contor - nr care reprezinta pozitia unde trebuie introdus V
% L2 - lista nou creata
% Colector - lista auxiliara pentru crearea lui L2
%
% (i,i,i,i,o,i)
%  adaug_aux(L1,V,Pozitie,Contor,Colector) =
%                     = { Colector + V, if vida(L1) && Pozitie=Contor
%                         Colector, if vida(L1),
%                         adaug_aux(l2..ln,V,Pozitie*2,Contor+1,Colector+V+H), if Pozitie=Contor
%                         adaug_aux(l2..ln,V,Pozitie,Contor+1,Colector+H), if Pozitie!=Contor
%                         }

adaug_aux([],V,Poz,Con,L2,Colector):-Poz=Con,!,
                                   adaug_final(Colector,V,L2),
                                   print(L2).

adaug_aux([],_,_,_,L2,L2).


adaug_aux([H|T],V,Poz,Con,L2,Colector):-Poz=Con,
                                      Poz2 is Poz*2,
                                      Con2 is Con+1,
                                      adaug_final(Colector,V,L2NOU),
                                      adaug_final(L2NOU,H,L2GATA),
                                      print('unu'),
                                      print(L2GATA),
                                      adaug_aux(T,V,Poz2,Con2,L2,L2GATA).


adaug_aux([H|T],V,Poz,Con,L2,Colector):-not(Poz=Con),
                                  Con2 is Con+1,
                                  adaug_final(Colector,H,L2GATA),
                                  print(Con),
                                  print('trei'),
                                  print(L2GATA),
                                  adaug_aux(T,V,Poz,Con2,L2,L2GATA).

/*Cazuri de testare:
 * adaug([],4,L2). %[]
 * adaug([1],4,L2). %[1,4]
 * adaug([1,2,3],0,L2). %[1,0,2,0,3]
 * adaug([1,2,3,4],0,L2). %[1,0,2,0,3,4,0]
 * adaug([1,2,3,4,5],0,L2). %[1,0,2,0,3,4,0,5]
 * adaug([1,2,3,4,5,6,7,8],0,L2). %[1,0,2,0,3,4,0,5,6,7,8,0]
 * adaug([1,2,3,4,5,6,7,8,9],0,L2). %[1,0,2,0,3,4,0,5,6,7,8,0,9]
 * adaug([1,2,3,4,5,6,7,8,9,10,11,12,13,14,15],0,L2).
 * %[1,0,2,0,3,4,0,5,6,7,8,0,9,10,11,12,13,14,15]
 * adaug([1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16],0,L2).
 * %[1,0,2,0,3,4,0,5,6,7,8,0,9,10,11,12,13,14,15,16,0]


                                  */
