#ifndef PREZENTARE_H_

#define PREZENTARE_H_
#include "service.h"


typedef struct {
	Service srv;
}Prezentare;

/*
	Constructorul prezentarii
*/
Prezentare CreeazaPrezentare();


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Destructorul prezentarii
*/
void DistrugePrezentare(Prezentare* prezi);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Printarea mediului aplicatiei mele :)))
*/
void meniu();


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Subprogramul de filtrare a medicamentelor ori dupa cantitate, ori care incep cu o litera citita
*/
void ui_filtrare(Prezentare* prezi);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Subprogramul de sortare a medicamentelor ori dupa nume, ori dupa cantitate, cu posibilitatea alegerii ordinii (crescatoare/descrescatoare)
*/
void ui_ordonare(Prezentare* prezi);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Subprogramul de stergere a unui medicament din stoc (se vor printa toate medicamentele existente, prin apelul subprogramului ui_afiseaza, ca, apoi,
	utilizatorul sa introduca id-ul medicamentului pe care doreste sa il stearga; in cazul in care introduce un id inexistent, se va afisa acest lucru,
	altfel, daca id-ul exista, se sterge medicamentul din stoc si se face o actualizare automata a id-urilor tuturor medicamentelor, asfel incat, la orice
	moment din rularea aplicatiei, elementele de tip Medicament sa aiba id-uri de la 1 la n, unde n este numarul de medicamente)
*/
void ui_sterge(Prezentare* prezi);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Subprogramul de actualizare a unui medicament, prin introducerea id-ului (se face apel la subprogramul ui_afiseaza pentru ca utilizatorul sa cunoasca id-urile existente),
	acest medicament urmand sa fie mofidicat prin atribuirea unui nou nume si a unei noi concentratii (date care se vor valida)
*/
void ui_actualizeaza(Prezentare* prezi);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Subprogramul de afisare a tuturor medicamentelor din repozitoriul de medicamente (in cazul in care repozitoriul este vid, se va afisa un mesaj corespunzator)
*/
void ui_afiseaza(Prezentare* prezi);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Subprogramul de adaugare a unui nou medicament, prin introducerea codului, a numelui, a concentratiei si a cantitatii. In cazul in care, dupa cod, acest medicament
	nu a mai fost adaugat, se face adaugarea sa (daca este necesar, se redimensioneaza repozitoriul, alocat dinamic), iar in cazul in care a mai fost adaugat, 
	cantitatea initiala va fi incrementata cu cea nou citita (chiar daca numele si concentratia nu coincid, deoarece se verifica doar codul!)
*/
void ui_adauga(Prezentare* prezi);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Subprogramul de generare a consolei prin care utilizatorul foloseste aplicatia, avand la dispozitie functionalitati de:
		1. adaugare a unui nou medicament
		2. afisarea tuturor medicamentelor
		3. actualizare de medicamente
		4. stergere de medicamente
		5. sortare a medicamentelor (dupa nume sau cantitate)
		6. filtrare a medicamentelor (cu stoc mai mic decat un intreg dat sau incepand cu o anumita litera)
*/
int prezentare(Prezentare* prezi);


#endif