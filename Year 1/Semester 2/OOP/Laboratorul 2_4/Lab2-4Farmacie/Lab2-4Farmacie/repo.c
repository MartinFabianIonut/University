#define _CRT_SECURE_NO_WARNINGS
#include "repo.h"
#include "domain.h"
#include <stdlib.h>
#include <string.h>

#define INIT_CAPACITY 2

struct _Repo {

	Medicament* medicamente;
	int n;
	int cp;
};

void Redimensionare(Repo repoTotal) {
	int nouaCapacitate = 2 * repoTotal->cp;
	Medicament* local =  (Medicament*)calloc(1,nouaCapacitate*sizeof(Medicament));
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


void CreeazaRepo(Repo* repo)
{
	Repo local = NULL;
	local = (Repo)malloc(sizeof(struct _Repo));
	if (NULL != local)
	{
		local->medicamente = (Medicament*)calloc(INIT_CAPACITY, sizeof(Medicament));
		if (NULL != local->medicamente)
		{
			local->n = 0;
			local->cp = INIT_CAPACITY;
			*repo = local;
		}
	}
}

Repo createEmpty() {
	Repo rez = NULL;
	rez = (Repo)malloc(sizeof(struct _Repo));
	if (rez != NULL) {
		rez->n = 0;
		rez->cp = INIT_CAPACITY;
		rez->medicamente = malloc(sizeof(Medicament) * rez->cp);
		return rez;
	}
	return NULL;
}

void DistrugeRepo(Repo* repo)
{
	Repo local = *repo;
	int i;
	for (i = 0; i < local->n; i ++)
	{
		DistrugeMedicament(&local->medicamente[i]);
	}
	free(local->medicamente);
	free(local);
	*repo = NULL;
}

int dimensiune(Repo repo) {
	return repo->n;
}

Medicament get(Repo repo, int i) {
	return repo->medicamente[i];
}

Medicament set(Repo repo, int i, Medicament medi) {
	Medicament rez = repo->medicamente[i];
	repo->medicamente[i] = medi;
	return rez;
}

Repo CopiazaRepo(Repo repo) {
	Repo rez;
	CreeazaRepo(&rez);
	for (int i = 0; i < dimensiune(repo); i++) {
		Medicament medi = get(repo,i);
		adaugaMedicamentStoc(rez, CopiazaMedicament(medi));
	}
	return rez;
}

void StergeMedicamentRepo(Repo repo, int poz) {
	Medicament medi = repo->medicamente[poz];
	for (int i = poz; i < repo->n - 1; i++) {
		repo->medicamente[i] = repo->medicamente[i+1];
	}
	repo->n--;
	DistrugeMedicament(&medi);
}

void ActualizareRepo(Repo repoTotal, Medicament medicamentActualizat) {
	int id = GetID(medicamentActualizat);
	char* sir = NULL;
	GetNume(medicamentActualizat, &sir);
	float concentratie = GetConcentratie(medicamentActualizat);
	SetNume(repoTotal->medicamente[id-1], sir);
	SetConcentratie(repoTotal->medicamente[id-1], concentratie);
	free(sir);
}

void adaugaMedicamentStoc(Repo repoTotal, Medicament medicamentNou) {
	/*
	adauga un medicament nou in lista; daca acesta exista deja ii actualizeaza cantitatea
	totalMedicamente-tipul Medicamente*
	medicamentNou-tipul Medicament
	*/

	if (repoTotal->n == repoTotal->cp)
	{
		Redimensionare(repoTotal);
	}
	char* sir = NULL;
	GetNume(medicamentNou, &sir);
	repoTotal->medicamente[repoTotal->n]=InitializeazaMedicament(GetCod(medicamentNou),
		sir, GetConcentratie(medicamentNou), GetCantitate(medicamentNou));
	free(sir);
	int n = repoTotal->n,i;
	int maiexista = 0,pozitie = 0;
	for (i = 0; i < n; i++) {
		if (GetCod(repoTotal->medicamente[repoTotal->n]) == GetCod(repoTotal->medicamente[i])) {
			maiexista = 1;
			pozitie = i;
		}
	}
	if (maiexista == 1) {
		int cantitateCurenta = 0;
		cantitateCurenta = GetCantitate(repoTotal->medicamente[pozitie]);
		SetCantitate(repoTotal->medicamente[(repoTotal->n)-1], cantitateCurenta + GetCantitate(medicamentNou));
	}
	else {
		repoTotal->n = repoTotal->n + 1;
		int dim = repoTotal->n;
		
		SetID(repoTotal->medicamente[dim-1], dim);
	}
}

void GetMedicamentDupaIndiceRepo(Repo repo, const int Index, Medicament* medi)
{
	Medicament local = NULL;
	int id = GetID(repo->medicamente[Index]);
	int cod = GetCod(repo->medicamente[Index]);
	float concentratie = GetConcentratie(repo->medicamente[Index]);
	int cantitate = GetCantitate(repo->medicamente[Index]);
	char* nume = NULL;
	GetNume(repo->medicamente[Index],&nume);
	
	local=InitializeazaMedicamentID(id,cod,nume,concentratie,cantitate);
	*medi = local;
	free(nume);
}

void GetToateMedicamenteleRepo(Repo repo, Medicament ** medi , int* n)
{
	Medicament* local = NULL;

	local = (Medicament*)calloc(repo->n, sizeof(Medicament));
	if (local != NULL) {
		for (int i = 0; i < repo->n; i++) {
			GetMedicamentDupaIndiceRepo(repo, i, &local[i]);
		}
		*n = repo->n;
		*medi = local;
	}
	
}


Medicament GetMedicamentDupaIDRepo(Repo repo, int id)
{
	Medicament* local = NULL;

	local = (Medicament*)calloc(repo->n, sizeof(Medicament));
	if (local != NULL) {
		for (int i = 0; i < repo->n; i++) {
			GetMedicamentDupaIndiceRepo(repo, i, &local[i]);
			if (GetID(local[i]) == id) {
				return local[i];
			}
			DistrugeMedicament(&local[i]);
		}
	}
	free(local);
	return NULL;
}