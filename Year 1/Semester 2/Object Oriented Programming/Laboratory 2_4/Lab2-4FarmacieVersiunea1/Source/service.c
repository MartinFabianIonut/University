#include "service.h"
#include "domain.h"
#include "repo.h"
#include "valid.h"
#include "sortulmeu.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


struct _Service{
	Repo repo;
};

void CreeazaService(Service* srv, Repo repo)
{
	Service local = NULL;
	local = (Service)malloc(sizeof(struct _Service));
	if (NULL != local)
	{
		local->repo = repo;
		*srv = local;
	}
}

void DistrugeService(Service* srv)
{
	Service local = *srv;
	free(local);
	*srv = NULL;
}

Repo Copiere(Service srv) {
	Repo repoC = CopiazaRepo(srv->repo);
	return repoC;
}

void StergeMedicamentSrv(Service srv, Medicament medi) {
	int pozitie_stergere = AflaPozitieMedicamentInRepo(srv, medi);
	if (pozitie_stergere != -1) {
		StergeMedicamentRepo(srv->repo, pozitie_stergere);
	}
}

void ActualizareSrv(Service srv, int id, char* nume, float concentratie, int erori[4]) {
	Medicament medicamentActualizat = NULL;
	medicamentActualizat = InitializeazaMedicamentID(id,1, nume, concentratie, 1);
	if (valideaza(medicamentActualizat, erori) == 1) {
		ActualizareRepo(srv->repo, medicamentActualizat);
	}
}


void AdaugaMedicamentStoc(Service srv, int cod, char* nume, float concentratie, int cantitate, int erori[4]) {
	Medicament medi = NULL;
	medi=InitializeazaMedicament(cod,nume,concentratie,cantitate);
	if (valideaza(medi, erori) == 1) {
		adaugaMedicamentStoc(srv->repo, medi);
	}
	DistrugeMedicament(&medi);
}

void GetToateMedicamenteleSrv(Service srv, Medicament** toateMedicamentele, int* n)
{
	Medicament* medicamente = NULL;
	int nr = 0;
	GetToateMedicamenteleRepo(srv->repo, &medicamente, &nr);
	*toateMedicamentele = medicamente;
	*n = nr;
}

Medicament GetMedicamentDupaIDSrv(Service srv, int id)
{
	Medicament medicament = NULL;
	medicament = GetMedicamentDupaIDRepo(srv->repo,  id);
	return medicament;
}

int AflaPozitieMedicamentInRepo(Service srv, Medicament medi) {
	int i, id = GetID(medi),idmCautat;
	for (i = 0; i < dimensiune(srv->repo); i++) {
		Medicament mCautat = get(srv->repo, i);
		idmCautat = GetID(mCautat);
		if (idmCautat == id) {
			return i;
		}
	}
	return -1;
}


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
	free(sir1);free(sir2);
	return val;
}

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
/*
	sortare dupa nume
*/
Repo OrdonareNume(Service srv, int cresc_descresc) {

	Repo repoOrdonat = CopiazaRepo(srv->repo);
	sortulmeu(&repoOrdonat, ComparaNume, cresc_descresc);
	return repoOrdonat;
}

/*
	sortare dupa cantitate
*/
Repo OrdonareCantitate(Service srv, int cresc_descresc) {
	Repo repoOrdonat = CopiazaRepo(srv->repo);
	sortulmeu(&repoOrdonat, ComparaCantitate, cresc_descresc);
	return repoOrdonat;
}


Repo FiltreazaStoc(Service srv, int stoc) {
	if (stoc == 0) {
		return CopiazaRepo(srv->repo);
	}
	Repo rez = createEmpty();
	for (int i = 0; i < dimensiune(srv->repo); i++) {
		Medicament m = get(srv->repo, i);
		if (GetCantitate(m) < stoc) {
			adaugaMedicamentStoc(rez, CopiazaMedicament(m));
		}
	}
	return rez;
}

Repo FiltreazaLitera(Service srv, char* litera) {
	if (litera == NULL || strlen(litera) == 0) {
		return CopiazaRepo(srv->repo);
	}
	Repo rez = createEmpty();
	for (int i = 0; i < dimensiune(srv->repo); i++) {
		Medicament m = get(srv->repo, i);
		char* dePrintat = NULL;
		GetNume(m, &dePrintat);
		if (strstr(dePrintat, litera) == (dePrintat))
		{
			adaugaMedicamentStoc(rez, CopiazaMedicament(m));
		}
	}
	return rez;
}



//
//Repo getAllMedi(Service srv, char* typeSubstring) {
//	if (typeSubstring == NULL || strlen(typeSubstring) == 0) {
//		return CopiazaRepo(srv->repo);
//	}
//	Repo rez;
//	CreeazaRepo(&rez);
//	for (int i = 0; i < dimensiune(srv->repo); i++) {
//		Medicament medi = get(srv->repo, i);
//		if (strstr(GetNume(medi), typeSubstring) != NULL) {
//			adaugaMedicamentStoc(&rez, CopiazaMedicament(&medi));
//		}
//	}
//	return rez;
//}