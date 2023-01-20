#ifndef DOMAIN_H_

#define DOMAIN_H_

typedef struct _Medicament* Medicament; 

/*
Aloc spatiu pentru elemente de tip Medicament
*/
Medicament InitializeazaMedicament(int cod, char* nume, float concentratie, int cantiate);
Medicament InitializeazaMedicamentID(int id, int cod, char* nume, float concentratie, int cantiate);
/*
Dealoc spatiu pentru elemente de tip Medicament
*/
void DistrugeMedicament(Medicament* medi);

Medicament CopiazaMedicament(Medicament medi);

int GetCod(Medicament medi);
void GetNume(Medicament medi, char** numele);
float GetConcentratie(Medicament medi);
int GetCantitate(Medicament medi);
int GetID(Medicament medi);

void SetNume(Medicament medi, const char* numeNou);
void SetConcentratie(Medicament medi, float concentratieNoua);
void SetCantitate(Medicament medi, int cantitateNoua);

void SetID(Medicament medi, int dim);

#endif