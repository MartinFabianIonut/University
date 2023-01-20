#ifndef REPO_H_
#define REPO_H_

#include "domain.h"

typedef struct {
	Medicament* medicamente;
	int n;
	int cp;
}Repo;

/*
	Constructorul repozitoriului
*/
Repo createEmpty();


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Destructorul repozitoriului
*/
void DistrugeRepo(Repo* repo);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care face redimensionarea repozitoriului (care are o capacitate initiala de doua elemente); la fiecare redimensionare, capacitatea se dubleaza
*/
void Redimensionare(Repo* repoTotal);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care da dimensiunea repozitoriului (cate medicamente am adaugat)
*/
int dimensiune(Repo* repo);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care returneaza medicamentul de pe pozitia i in repozitoriu
*/
Medicament get(Repo* repo, int i);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care da unui medicament din repozitoriu valorile unui alt medicament (functie folosita la interschimbarea de medicamente in apelul sortarii)
*/
Medicament set(Repo* repo, int i, Medicament medi);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care copiaza repozitoriul, returnand o entitate de tip Repo, cu alta adresa fata de repozitoriul original (orice modificare adusa copiei nu afecteaza originalul)
*/
Repo CopiazaRepo(Repo* repo);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care sterge un medicament din repozitoriu, decrementand numarul de elemente cu 1
		input: poz (int) - pozitia din repozitoriu de pe care se sterge, pozitie aflata si transmisa ca paramentru din service
		output: medicament sters, numar de elemente actualizat si actualizare a id-urilor medicamentelor (astfel incat mereu sa existe id-uri de la
				1 la n, unde n este numarul de medicamente)
*/
void StergeMedicamentRepo(Repo* repo, int poz);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care actualizeaza un medicament, setand un nume nou si o concentratie noua, pe care le ia din paramentrul medicamentActualizat primit ca paramentru
*/
void ActualizareRepo(Repo* repoTotal, Medicament medicamentActualizat);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care adauga un medicament nou in repozitoriu. Daca s-a atins capacitatea maxima, se face redimensionare. Daca medicamentul a mai fost adaugat, 
	atunci se incrementeaza cantitatea aceluia deja existent cu capacitatea celui nou. Altfel, se actualizeaza numarul de elemente (+1) si se adauga medicamentul,
	id-ul acestuia fiind setat automat (in format de la 1 la n, unde n este numarul de medicamente din repozitoriu)
*/
void adaugaMedicamentStoc(Repo* repoTotal, Medicament medicamentNou);

#endif