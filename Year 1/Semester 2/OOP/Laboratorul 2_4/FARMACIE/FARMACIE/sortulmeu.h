#ifndef SORTULMEU_H_
#define SORTULMEU_H_

#include "repo.h"


/*
   Tipul functie de comparare pentru 2 elemente
   returneaza 0 daca sunt egale, 1 daca m1 > m2, -1 altfel
*/
typedef int(*FunctieComparare)(Medicament m1, Medicament m2, int cresc_descresc);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care face sortare in place
	input: repo, cmpF - relatia dupa care se sorteaza, cresc_descresc (int) - care indica prin 1 - ordonare crescatoare, respectiv prin 2 - descrescatoare
	output: repo se va sorta (a se remarca faptul ca aceasta sortare se aplica, in aplicatie, pe o copie a repozitoriului original!)
*/
void sortulmeu(Repo* repo, FunctieComparare cmpF, int cresc_descresc);

#endif