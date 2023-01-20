#ifndef VALID_H_

#define VALID_H_

#include "domain.h"
/*
	Functie care valideaza datele de intrare
		pre: medi - Medicament, erori - int[4]
		post: se modifica sau nu continutul vectorului erori, iar pe baza valorilor acestuia se va afisa in prezentare eroarea/erorile eventuale
			  Returneaza 1 daca nu exista erori de validare, 0 altfel
*/
int valideaza(Medicament medi, int erori[4]);


#endif