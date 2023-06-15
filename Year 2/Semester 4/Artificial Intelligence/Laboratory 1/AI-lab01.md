# Lab01 - Algoritmi simpli

## :microscope: Obiective

Rezolvarea problemelor folosind metode diferite din punct de vedere al complexității (temporale și spațiale).

## :book: Aspecte teoretice

Rezolvarea problemelor. Analiza complexității procesului de rezolvare.

## :bulb: Probleme simple, dar cu dichis :blossom:

1. Să se determine ultimul (din punct de vedere alfabetic) cuvânt care poate apărea într-un text care conține mai multe cuvinte separate prin ” ” (spațiu). _De ex. ultimul (dpdv alfabetic) cuvânt din ”Ana are mere rosii si galbene” este cuvântul "si"._

2. Să se determine distanța Euclideană între două locații identificate prin perechi de numere.
   _De ex. distanța între (1,5) și (4,1) este 5.0_

3. Să se determine produsul scalar a doi vectori rari care conțin numere reale. Un vector este rar atunci când conține multe elemente nule. Vectorii pot avea oricâte dimensiuni.
   _De ex. produsul scalar a 2 vectori unisimensionali [1,0,2,0,3] și [1,2,0,3,1] este 4._

4. Să se determine cuvintele unui text care apar exact o singură dată în acel text.
   _De ex. cuvintele care apar o singură dată în ”ana are ana are mere rosii ana" sunt: 'mere' și 'rosii'._

5. Pentru un șir cu n elemente care conține valori din mulțimea {1, 2, ..., n - 1} astfel încât o singură valoare se repetă de două ori, să se identifice acea valoare care se repetă.
   _De ex. în șirul [1,2,3,4,2] valoarea 2 apare de două ori._

6. Pentru un șir cu n numere întregi care conține și duplicate, să se determine elementul majoritar (care apare de mai mult de n / 2 ori).
   _De ex. 2 este elementul majoritar în șirul [2,8,7,2,2,5,2,3,1,2,2]._

7. Să se determine al k-lea cel mai mare element al unui șir de numere cu n elemente (k < n).
   _De ex. al 2-lea cel mai mare element din șirul [7,4,6,3,9,1] este 7._

8. Să se genereze toate numerele (în reprezentare binară) cuprinse între 1 și n.
   _De ex. dacă n = 4, numerele sunt: 1, 10, 11, 100._

9. Considerându-se o matrice cu n x m elemente întregi și o listă cu perechi formate din coordonatele a 2 căsuțe din matrice ((p,q) și (r,s)), să se calculeze suma elementelor din sub-matricile identificate de fieare pereche.

   > _De ex, pt matricea_\
   > [[0, 2, 5, 4, 1], \
 [4, 8, 2, 3, 7], \
 [6, 3, 4, 6, 2], \
 [7, 3, 1, 8, 3], \
 [1, 5, 7, 9, 4]] \
   > _și lista de perechi ((1, 1) și (3, 3)), ((2, 2) și (4, 4)), suma elementelor din prima sub-matrice este 38, iar suma elementelor din a 2-a sub-matrice este 44._

10. Considerându-se o matrice cu n x m elemente binare (0 sau 1) sortate crescător pe linii, să se identifice indexul liniei care conține cele mai multe elemente de 1.

    > _De ex. în matricea_ \
    > [[0,0,0,1,1], \
[0,1,1,1,1], \
[0,0,1,1,1]] \
    > _a doua linie conține cele mai multe elemente 1._

11. Considerându-se o matrice cu n x m elemente binare (0 sau 1), să se înlocuiască cu 1 toate aparițiile elementelor egale cu 0 care sunt complet înconjurate de 1.
    > _De ex. matricea_ \
    > [[1,1,1,1,0,0,1,1,0,1],\
[1,0,0,1,1,0,1,1,1,1],\
[1,0,0,1,1,1,1,1,1,1],\
[1,1,1,1,0,0,1,1,0,1],\
[1,0,0,1,1,0,1,1,0,0],\
[1,1,0,1,1,0,0,1,0,1],\
[1,1,1,0,1,0,1,0,0,1],\
[1,1,1,0,1,1,1,1,1,1]]\
    > _devine _ \
    > [[1,1,1,1,0,0,1,1,0,1],\
[1,1,1,1,1,0,1,1,1,1],\
[1,1,1,1,1,1,1,1,1,1],\
[1,1,1,1,1,1,1,1,0,1],\
[1,1,1,1,1,1,1,1,0,0],\
[1,1,1,1,1,1,1,1,0,1],\
[1,1,1,0,1,1,1,0,0,1],\
[1,1,1,0,1,1,1,1,1,1]]\

## :memo: Cerinte

Specificaţi, implementaţi și testați subalgoritmi pentru problemele enuntate. Încercați să rezolvați fiecare cerință cât mai eficient (ca număr de pași și / sau ca resurse de memorie folosite).

## :hourglass: Termen de predare

Laborator 2

## :moneybag: Evaluarea

**Obligatoriu** 5 probleme la alegere, pentru fiecare problemă se va prezenta o soluție cât mai eficientă. Fiecare rezolvare corectă va primi maxim 20 puncte.

**\[Opțional\]**
Rezolvarea prin metode noi, mai eficiente, a problemelor alese la punctul precedent sau rezolvarea unor noi probleme. Fiecare rezolvare corectă va primi maxim 20 puncte.

Notă:

- punctajul maxim acumulat pentru acest laborator este 200 puncte.
- punctajul minim pentru ca tema predată sa fie validă este 100 puncte.
