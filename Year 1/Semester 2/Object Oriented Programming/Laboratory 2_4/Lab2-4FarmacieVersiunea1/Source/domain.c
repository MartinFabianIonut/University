#define _CRT_SECURE_NO_WARNINGS
#include <string.h>
#include "domain.h"
#include <stdlib.h>

struct _Medicament {
	int id;
	int cod;
	char* nume;
	float concentratie;
	int cantitate;
};

Medicament InitializeazaMedicament(int cod, char *nume, float concentratie, int cantiate)
{
	Medicament local = NULL;
	local = (Medicament)malloc(sizeof(struct _Medicament));
	if (local != NULL) {
		local->id = 0;
		local->cod = cod;
		local->nume = (char*)malloc(strlen(nume) + 1);
		if (NULL != local->nume) {
			strcpy(local->nume, nume);
		}
		local->concentratie = concentratie;
		local->cantitate = cantiate;
		return local;
	}
	return NULL;
}

Medicament InitializeazaMedicamentID(int id, int cod, char* nume, float concentratie, int cantiate)
{
	Medicament local = NULL;
	local = (Medicament)malloc(sizeof(struct _Medicament));
	if (local != NULL) {
		local->id = id;
		local->cod = cod;
		local->nume = (char*)malloc(strlen(nume) + 1);
		if (NULL != local->nume) {
			strcpy(local->nume, nume);
		}
		local->concentratie = concentratie;
		local->cantitate = cantiate;
		return local;
	}
	return NULL;
}

void DistrugeMedicament(Medicament* medi) {
	Medicament local = *medi;
	free(local->nume);
	free(local);
	*medi = NULL;
}

Medicament CopiazaMedicament(Medicament medi) {
	return InitializeazaMedicamentID(medi->id,medi->cod, medi->nume,medi->concentratie,medi->cantitate);
}

int GetCod(Medicament medi) {
	return medi->cod;
}

void GetNume(Medicament medi, char** numele) {
	char* numeleString = NULL;
	numeleString = (char*)malloc(strlen(medi->nume)+1);
	if (NULL != numeleString)
	{
		strcpy(numeleString, medi->nume);
		*numele = numeleString;
	}
}

float GetConcentratie(Medicament medi) {
	return medi->concentratie;
}

int GetCantitate(Medicament medi) {
	return medi->cantitate;
}

int GetID(Medicament medi) {
	return medi->id;
}

void SetNume(Medicament medi, const char* numeNou) {
	medi->nume = (char*)malloc(strlen(numeNou) + 1);
	if (NULL != medi->nume) {
		strcpy(medi->nume, numeNou);
	}
}


void SetConcentratie(Medicament medi, float concentratieNoua) {
	medi->concentratie = concentratieNoua;
}

void SetCantitate(Medicament medi, int cantitateNoua) {
	medi->cantitate = cantitateNoua;
}

void SetID(Medicament medi, int dim) {
	medi->id = dim;
}
