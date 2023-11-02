# Laborator 2 - Optimizare Complexitate-Spatiu

## Descriere Problema

Problema de la laboratorul 1 a suferit o modificare pentru a se obține o performanță superioară la nivelul complexității-spațiu. Cerința este de a implementa un program care să asigure următoarea postcondiție:

**Postcondiție:** Matricea inițială conține imaginea filtrată.

**Constrângere:** NU se alocă o altă matrice rezultat și nici o matrice temporară! Se pot folosi doar vectori temporari pentru care complexitatea spațiu se încadrează în O(n).

**Atenție:** Pentru implementarea secvențială, va fi necesară utilizarea vectorilor auxiliari temporari.

## Obiectiv

Optimizarea complexității-spațiu în condițiile obținerii unei performanțe ridicate.

## Date de Intrare

Datele de intrare se citesc dintr-un fișier de intrare "date.txt". (Fișierul trebuie creat anterior prin adăugare de numere generate aleator.) Rezultatul se salvează într-un fișier "output.txt".

## Implementare

### a) Java

### b) C++ (cel puțin C++11)

## Testare

Masurați timpul de execuție pentru:

1. N=M=10 și n=m=3; p=2 +execuție secvențială
2. N=M=1000 și n=m=3; p=2,4,8,16 +execuție secvențială
3. N=10000 M=10000 și n=m=3; p=2,4,8,16 +execuție secvențială

Observații:

- Fiecare test trebuie repetat de 10 ori, iar evaluarea timpului de execuție se consideră media aritmetică a celor 10 rulări.
- Pentru fiecare execuție (cele 10) a fiecărui caz de testare folosiți același fișier input și verificați corectitudinea prin comparare cu fișierul output obținut la execuția secvențială.

## Analiza

Comparați performanța pentru fiecare caz! Comparați timpii obținuți cu implementarea Java versus implementarea C++. Evaluați complexitatea-spațiu.

## Deadline

Săptămâna 5
