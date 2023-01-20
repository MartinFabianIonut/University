#define _CRT_SECURE_NO_WARNINGS
#include "repo.h"
#include "domain.h"
#include <stdlib.h>
#include <string.h>

#define INIT_CAPACITY 2


Repo createEmpty() {
	Repo rez;
	rez.n = 0;
	rez.cp = INIT_CAPACITY;
	rez.medicamente = malloc(sizeof(Medicament) * rez.cp);
	return rez;
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
void DistrugeRepo(Repo* repo)
{
	int i;
	for (i = 0; i < repo->n; i++)
	{
		DistrugeMedicament(&repo->medicamente[i]);
	}
	free(repo->medicamente);
	repo->medicamente = NULL;
	repo->n = 0;
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
void Redimensionare(Repo* repoTotal) {
	int nouaCapacitate = 2 * repoTotal->cp;
	Medicament* local = (Medicament*)calloc(1, nouaCapacitate * sizeof(Medicament));
	int i;
	if (local != NULL) {
		for (i = 0; i < repoTotal->n; i++) {
			local[i] = repoTotal->medicamente[i];
		}
		//dealocam memoria ocupata de vector
		free(repoTotal->medicamente);
		repoTotal->medicamente = local;
		repoTotal->cp = nouaCapacitate;
	}

}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
int dimensiune(Repo* repo) {
	return repo->n;
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
Medicament get(Repo* repo, int i) {
	return repo->medicamente[i];
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
Medicament set(Repo* repo, int i, Medicament medi) {
	Medicament rez = repo->medicamente[i];
	repo->medicamente[i] = medi;
	return rez;
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
Repo CopiazaRepo(Repo* repo) {
	Repo rez = createEmpty();
	for (int i = 0; i < dimensiune(repo); i++) {
		Medicament medi = get(repo, i);
		adaugaMedicamentStoc(&rez, CopiazaMedicament(&medi));
	}
	return rez;
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
void StergeMedicamentRepo(Repo* repo, int poz) {
	Medicament medi = repo->medicamente[poz];
	for (int i = poz; i < repo->n - 1; i++) {
		repo->medicamente[i] = repo->medicamente[i + 1];
		SetID(&repo->medicamente[i], repo->medicamente[i].id - 1);
	}
	repo->n--;
	DistrugeMedicament(&medi);
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
void ActualizareRepo(Repo* repoTotal, Medicament medicamentActualizat) {
	int id = GetID(medicamentActualizat);
	SetNume(&repoTotal->medicamente[id - 1], medicamentActualizat.nume);
	SetConcentratie(&repoTotal->medicamente[id - 1], medicamentActualizat.concentratie);
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
void adaugaMedicamentStoc(Repo* repoTotal, Medicament medicamentNou) {
	if (repoTotal->n == repoTotal->cp)
	{
		Redimensionare(repoTotal);
	}
	int n = repoTotal->n, i;
	int maiexista = 0, pozitie = 0;
	for (i = 0; i < n; i++) {
		if (GetCod(medicamentNou) == GetCod(repoTotal->medicamente[i])) {
			maiexista = 1;
			pozitie = i;
		}
	}
	if (maiexista == 1) {
		int cantitateCurenta = 0;
		cantitateCurenta = GetCantitate(repoTotal->medicamente[pozitie]);
		SetCantitate(&repoTotal->medicamente[pozitie], cantitateCurenta + GetCantitate(medicamentNou));
	}
	else {
		repoTotal->medicamente[repoTotal->n] = medicamentNou;
		repoTotal->n = repoTotal->n + 1;
		int dim = repoTotal->n;
		SetID(&repoTotal->medicamente[dim - 1], dim);
	}
}