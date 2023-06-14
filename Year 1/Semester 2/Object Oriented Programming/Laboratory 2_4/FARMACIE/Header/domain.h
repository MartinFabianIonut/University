#ifndef DOMAIN_H_

#define DOMAIN_H_

typedef struct {
	int id;
	int cod;
	char* nume;
	float concentratie;
	int cantitate;
}Medicament;


/*
	Constructorul entitatii de tip Medicament - acolarea spatiului (dinamic) pentru nume si initializarea campurilor cu valorile transmise prin parametru
	(id-ul nu se seteaza acum, dar primeste din oficiu valoarea zero)
*/
Medicament InitializeazaMedicament(int cod, char* nume, float concentratie, int cantiate);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Constructorul entitatii de tip Medicament - acolarea spatiului (dinamic) pentru nume si initializarea campurilor cu valorile transmise prin parametru
	(id-ul se seteaza acum)
*/
Medicament InitializeazaMedicamentID(int id, int cod, char* nume, float concentratie, int cantiate);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Destructorul entitatii de tip Medicament - deacolarea spatiului
*/
void DistrugeMedicament(Medicament* medi);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care copiaza o entitate de tip Medicament (valoarea returnata, de tip Medicament, nu are adresa medicamentului asupra careia se face copia)
*/
Medicament CopiazaMedicament(Medicament* medi);

int GetCod(Medicament medi);
void GetNume(Medicament medi, char** numele);
float GetConcentratie(Medicament medi);
int GetCantitate(Medicament medi);
int GetID(Medicament medi);

void SetNume(Medicament* medi, const char* numeNou);
void SetConcentratie(Medicament* medi, float concentratieNoua);
void SetCantitate(Medicament* medi, int cantitateNoua);
void SetCod(Medicament* medi, int cod);
void SetID(Medicament* medi, int dim);

#endif