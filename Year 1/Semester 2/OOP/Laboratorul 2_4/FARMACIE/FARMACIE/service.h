#ifndef SERVICE_H_

#define SERVICE_H_

#include "domain.h"
#include "repo.h"

typedef struct {
	Repo repo;
}Service;


/*
	Constructorul service-ului
*/
Service CreeazaService();


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Destructorul service - ului 
*/
void DistrugeService(Service* srv);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care copiaza elementele repozitoriului, fara a indica adresa repozitoriului original
	(modificarile ulterioare nu il afecteaza pe cel original)
*/
Repo Copiere(Service* srv);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care adauga un nou medicament in stoc
		input: srv, cod (int), nume (char*), concentratie (float), cantitate (int) si erori - pentru validarea numelui si a concentratiei
		output: daca datele de intrare sunt valide, se incearca adaugarea medicamentului pe stoc
					in cazul in care exista deja (dupa cod), medicamentul este actualizat, adica este incrementata concentratia initiala cu cea nou introdusa
					si, de asemenea, este eliberat spatiul alocat numelui
				altfel, daca nu sunt valide
				-> se elibereaza spatiul rezervat entitatii de tip medicament, creata special pentru adaugare
*/
void AdaugaMedicamentStoc(Service* srv, int cod, char* nume, float concentratie, int cantitate, int erori[4]);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care actualizeaza numele si concentratia unui medicament, daca este pe stoc
		input: srv, nume (char*), concentratie (float) si erori - pentru validarea numelui si a concentratiei
		output: daca exista, medicamentul este actualizat cu numele si concentratia transmise prin parametri
				-> se elibereaza spatiul rezervat entitatii de tip medicament, creata special pentru actualizare, fie
				ca medicamentul se afla in stoc sau nu, fie ca este sunt valide datele de intrare sau nu
*/
void ActualizareSrv(Service* srv, int id, char* nume, float concentratie, int erori[4]);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care sterge un medicament din stoc, daca este pe stoc
		input: srv, medi
		output: daca exista, medicamentul este sters, altfel nu se intampla nimic
*/
void StergeMedicamentSrv(Service* srv, Medicament medi);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care afla daca un medicament exista sau nu in stoc si returneaza id-ul sau (echivalent cu pozitia in repozitoriu, cu numaratoarea incepand de la 1)
		input: srv, medi - Medicament
		output: id-ul medicamentului in repozitoriu, daca exista,
				altfel -1
*/
int AflaPozitieMedicamentInRepo(Service* srv, Medicament medi);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care compara doua entitati de tipul medicament dupa nume
		input: m1, m2 - de tip Medicament, cresc_descresc (int) - care indica prin 1 - ordonare crescatoare, respectiv prin 2 - descrescatoare
		output: val (int) - care indica prin o valoare negativa - numele primului medicament e "mai mic" decat al celui de al doilea (ex: ana<maria),
											 zero - numele celor doua medicamente sunt identice
											 o valoare pozitiva - numele primului medicament e "mai mare" decat al celui de al doilea (ex: maria>ana)
*/
int ComparaNume(Medicament m1, Medicament m2, int cresc_descresc);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care compara doua entitati de tipul medicament dupa cantitate
		input: m1, m2 - de tip Medicament, cresc_descresc (int) - care indica prin 1 - ordonare crescatoare, respectiv prin 2 - descrescatoare
		output: 1 - daca cantitatile celor doua medicamente nu sunt in conformitate cu criteriul de comparare (ex: sortare crescatoare intre 2 si 1,
																											   2 > 1 => trebuie schimbata ordinea)
				0 - daca cantitatile celor doua medicamente sunt deja in ordinea dorita
*/
int ComparaCantitate(Medicament m1, Medicament m2, int cresc_descresc);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care ordoneaza elementele de tip Medicament dintr-o copie a repozitoriului original de medicamente dupa criteriul cantitatii
		input: srv, cresc_descresc (int) - care indica prin 1 - ordonare crescatoare, respectiv prin 2 - descrescatoare
		output: repoOrdonat - copia repozitoriului original de medicamente, sortata cupa cantitate si in ordinea dorita (crescatoare/descrescatoare)
*/
Repo OrdonareNume(Service* srv, int cresc_descresc);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care ordoneaza elementele de tip Medicament dintr-o copie a repozitoriului original de medicamente dupa criteriul numelui
		input: srv, cresc_descresc (int) - care indica prin 1 - ordonare crescatoare, respectiv prin 2 - descrescatoare
		output: repoOrdonat - copia repozitoriului original de medicamente, sortata cupa cantitate si in ordinea dorita (crescatoare/descrescatoare)
*/
Repo OrdonareCantitate(Service* srv, int cresc_descresc);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care filtreaza elementele de tip Medicament din repozitoriul de medicamente daca acestea au o cantitate mai mica decat cea data ca parametru
		input: srv, stoc (int) - valoarea cantitatii sub care se doreste a fi facuta filtrarea
		output: rez - entitate de tipul Repo, in care se gasesc doar medicamentele cu cantitate mai mica decat cea data ca paramentru, iar daca asemenea medicamente
				nu s-au gasit, pur si simplu acest repozitoriu va fi gol
*/
Repo FiltreazaStoc(Service* srv, int stoc);


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
/*
	Functie care filtreaza elementele de tip Medicament din repozitoriul de medicamente daca numele acestora incep cu o litera cea data ca parametru
		input: srv, litera (char*) - litera cu care trebuie sa inceapa numele medicamentelor filtrate
		output: rez - entitate de tipul Repo, in care se gasesc doar medicamentele cu numele incepand cu litera data ca paramentru, iar daca asemenea medicamente
				nu s-au gasit, pur si simplu acest repozitoriu va fi gol
*/
Repo FiltreazaLitera(Service* srv, char* litera);

#endif