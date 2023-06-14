#include "service.h"
#include "domain.h"
#include "repo.h"
#include "valid.h"
#include "sortulmeu.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


Service CreeazaService() {
	Service srv;
	srv.repo = createEmpty();
	return srv;
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
void DistrugeService(Service* srv)
{
	DistrugeRepo(&srv->repo);
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
Repo Copiere(Service* srv) {
	return CopiazaRepo(&srv->repo);
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
void AdaugaMedicamentStoc(Service* srv, int cod, char* nume, float concentratie, int cantitate, int erori[4]) {
	Medicament medi = InitializeazaMedicament(cod, nume, concentratie, cantitate);
	if (valideaza(medi, erori) == 1) {
		int dim_init = dimensiune(&srv->repo);
		adaugaMedicamentStoc(&srv->repo, medi);
		if (dimensiune(&srv->repo) == dim_init) {
			free(medi.nume); // in cazul in care am avut acelasi cod, am actualizat cantitatea, deci
			//nu am modificat dimensiunea lui repo, asa ca trebuie sa eliberez spatiul alocat numelui
		}
	}
	else
	{
		DistrugeMedicament(&medi);// daca nu este valid medicamentul, eliberez spatiul alocat la inceputul functiei
	}
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
void ActualizareSrv(Service* srv, int id, char* nume, float concentratie, int erori[4]) {
	Medicament medicamentActualizat = InitializeazaMedicamentID(id, 1, nume, concentratie, 1);
	int poz = AflaPozitieMedicamentInRepo(srv, medicamentActualizat);
	if (poz != -1) {
		if (valideaza(medicamentActualizat, erori) == 1) {
			ActualizareRepo(&srv->repo, medicamentActualizat);
		}
	}
	DistrugeMedicament(&medicamentActualizat);
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
void StergeMedicamentSrv(Service* srv, Medicament medi) {
	int pozitie_stergere = AflaPozitieMedicamentInRepo(srv, medi);
	if (pozitie_stergere != -1) {
		StergeMedicamentRepo(&srv->repo, pozitie_stergere);
	}
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
int AflaPozitieMedicamentInRepo(Service* srv, Medicament medi) {
	int i, id = GetID(medi), idmCautat;
	for (i = 0; i < dimensiune(&srv->repo); i++) {
		Medicament mCautat = get(&srv->repo, i);
		idmCautat = GetID(mCautat);
		if (idmCautat == id) {
			return i;
		}
	}
	return -1;
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
int ComparaNume(Medicament m1, Medicament m2, int cresc_descresc) {
	char* sir1 = NULL;
	GetNume(m1, &sir1);
	char* sir2 = NULL;
	GetNume(m2, &sir2);
	int val;
	if (cresc_descresc == 1) {
		val = strcmp(sir1, sir2);
		free(sir1); free(sir2);
		return val;
	}
	val = strcmp(sir2, sir1);
	free(sir1); free(sir2);
	return val;
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
int ComparaCantitate(Medicament m1, Medicament m2, int cresc_descresc) {
	int cant1 = GetCantitate(m1), cant2 = GetCantitate(m2);
	if (cresc_descresc == 1) {
		if (cant1 > cant2) {
			return 1;
		}
		return 0;
	}
	if (cant1 < cant2) {
		return 1;
	}
	return 0;

}
 

/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
Repo OrdonareCantitate(Service* srv, int cresc_descresc) {
	Repo repoOrdonat = CopiazaRepo(&srv->repo);
	sortulmeu(&repoOrdonat, ComparaCantitate, cresc_descresc);
	return repoOrdonat;
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
Repo OrdonareNume(Service* srv, int cresc_descresc) {

	Repo repoOrdonat = CopiazaRepo(&srv->repo);
	sortulmeu(&repoOrdonat, ComparaNume, cresc_descresc);
	return repoOrdonat;
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
Repo FiltreazaStoc(Service* srv, int stoc) {
	Repo rez = createEmpty();
	for (int i = 0; i < dimensiune(&srv->repo); i++) {
		Medicament m = get(&srv->repo, i);
		if (GetCantitate(m) < stoc) {
			adaugaMedicamentStoc(&rez, CopiazaMedicament(&m));
		}
	}
	return rez;
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
Repo FiltreazaLitera(Service* srv, char* litera) {
	Repo rez = createEmpty();
	for (int i = 0; i < dimensiune(&srv->repo); i++) {
		Medicament m = get(&srv->repo, i);
		char* dePrintat = NULL;
		GetNume(m, &dePrintat);
		if (strstr(dePrintat, litera) == (dePrintat))
		{
			adaugaMedicamentStoc(&rez, CopiazaMedicament(&m));
		}
		free(dePrintat);
	}
	return rez;
}
