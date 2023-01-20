#ifndef REPO_H_
#define REPO_H_

#include "domain.h"

typedef struct _Repo* Repo;

Repo createEmpty();
void CreeazaRepo(Repo* repo);
void DistrugeRepo(Repo* repo);

int dimensiune(Repo repo);
Medicament get(Repo repo, int i);
Medicament set(Repo repo, int i, Medicament medi);
Repo CopiazaRepo(Repo repo);

void InitializeazaMedicamente(Repo*);
void StergeMedicamentRepo(Repo repo, int poz);
void ActualizareRepo(Repo repoTotal, Medicament medicamentActualizat);

void adaugaMedicamentStoc(Repo repoTotal, Medicament medicamentNou);

void GetMedicamentDupaIndiceRepo(Repo repo, const int Index, Medicament* medi);
void GetToateMedicamenteleRepo(Repo repo, Medicament** medi, int* n);

Medicament GetMedicamentDupaIDRepo(Repo repo, int id);

#endif