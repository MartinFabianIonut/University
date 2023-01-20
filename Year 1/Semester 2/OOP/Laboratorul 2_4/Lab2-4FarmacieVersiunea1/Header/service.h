#ifndef SERVICE_H_

#define SERVICE_H_

#include "domain.h"
#include "repo.h"

typedef struct _Service* Service; 

/*
Aloc spatiu pentru elemente de tip Service
*/
void CreeazaService(Service* srv, Repo repo);
/*
Dealoc spatiu pentru elemente de tip Service
*/
void DistrugeService(Service* srv);

/*
Adauga un medicament nou in repo
pre: srv, cod - int, nume - char[30], concentratie - float, cantitate - int, erori - int[4]
post: medicament adaugat in repo daca nu mai exista
*/
Repo Copiere(Service srv);
void ActualizareSrv(Service srv, int id, char* nume, float concentratie, int erori[4]);
void StergeMedicamentSrv(Service srv, Medicament medi);
void AdaugaMedicamentStoc(Service srv, int cod, char* nume, float concentratie, int cantitate, int erori[4]);

/*
Functie care "returneaza" toate medicamentele, de fapt ele vor fi in **toateMedicamentele
pre:srv, toateMedicamentele - **Medicament, n - int*
*/
void GetToateMedicamenteleSrv(Service srv, Medicament** toateMedicamentele, int* n);
//Repo getAllMedi(Service srv, char* typeSubstring);


Medicament GetMedicamentDupaIDSrv(Service srv, int id);
int AflaPozitieMedicamentInRepo(Service srv, Medicament medi);

int ComparaNume(Medicament m1, Medicament m2, int cresc_descresc);
int ComparaCantitate(Medicament m1, Medicament m2, int cresc_descresc);

Repo OrdonareNume(Service srv, int cresc_descresc);
Repo OrdonareCantitate(Service srv, int cresc_descresc);

Repo FiltreazaStoc(Service srv, int stoc);
Repo FiltreazaLitera(Service srv, char* litera);
#endif