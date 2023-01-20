#ifndef SORTULMEU_H_
#define SORTULMEU_H_

#include "repo.h"
/*
   Tipul functie de comparare pentru 2 elemente
   returneaza 0 daca sunt egale, 1 daca m1>m2, -1 altfel
*/
typedef int(*FunctieComparare)(Medicament m1, Medicament m2, int cresc_descresc);

/*
	Sorteaza in place
	cmpF - relatia dupa care se sorteaza
*/
void sortulmeu(Repo* repo, FunctieComparare cmpF, int cresc_descresc);

#endif