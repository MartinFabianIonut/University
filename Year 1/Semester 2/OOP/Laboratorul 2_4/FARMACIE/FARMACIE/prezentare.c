#include <stdio.h>
#include <stdlib.h>

#include "domain.h"
#include "repo.h"
#include "prezentare.h"
#include "teste.h"
#include "valid.h"
#include "service.h"


Prezentare CreeazaPrezentare() {
	Prezentare prezi;
	prezi.srv = CreeazaService();
	return prezi;
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
void DistrugePrezentare(Prezentare* prezi)
{
	DistrugeService(&prezi->srv);
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
void meniu() {
	printf("\nLaboratorul 2-4 - Aplicatia Farmacia:\n"
		"\t 1. Introdu 1 pentru a adauga un medicament.\n"
		"\t 2. Introdu 2 pentru afisarea medicamentelor.\n"
		"\t 3. Introdu 3 pentru actualizare.\n"
		"\t 4. Introdu 4 pentru a sterge stocul.\n"
		"\t 5. Introdu 5 pentru a ordona medicamentele dupa un anumit criteriu.\n"
		"\t 6. Introdu 6 pentru a vizualiza lista de medicamente filtrate dupa un criteriu.\n"
		"\t 0. Introdu 0 pentru a iesi din aplicatie.\n");
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
void ui_filtrare(Prezentare* prezi) {
	printf("\nCe fel de filtrare doresti?\n"
		"\t 1. Care afiseaza medicamentele unde stocul disponibil este mai mic decat o valoare data.\n"
		"\t 2. Care afiseaza medicamentele cu numele incepand cu o litera data.\n");
	int tip = 0;
	char litera[2] = "";
	int stoc = 0;
	char* dePrintat = NULL;
tip:
	if (scanf_s("%d", &tip) != 1) {
		printf("Trebuie introdus un numar natural!\n");
		fseek(stdin, 0L, SEEK_END);
		goto tip;
	}
	if (tip != 1 && tip != 2) {
		printf("\nComanda invalida!");
	}
	else {
		if (tip == 1) {
			printf("Introdu valoarea: ");
		stoc:
			if (scanf_s("%d", &stoc) != 1) {
				printf("Trebuie introdus un numar natural!\n");
				fseek(stdin, 0L, SEEK_END);
				goto stoc;
			}
			Repo repoFiltrat = FiltreazaStoc(&prezi->srv, stoc);
			if (dimensiune(&repoFiltrat) == 0) {
				printf("Nu exista medicamente cu stoc mai mic decat cel introdus!\n");
			}
			else {
				printf("Medicamentele filtrate cu stoc mai mic decat cel introdus:\n");
				for (int i = 0; i < dimensiune(&repoFiltrat); i++) {
					Medicament m = get(&repoFiltrat, i);
					GetNume(m, &dePrintat);
					printf("Id: %d,\t Cod: %d,\t Nume: %s, \t Concentratie: %f, \t Cantitate: %d;\n", GetID(m), GetCod(m), dePrintat, GetConcentratie(m), GetCantitate(m));
					//DistrugeMedicament(&m);
					free(dePrintat);
				}
				
			}DistrugeRepo(&repoFiltrat);
		}
		if (tip == 2) {
			printf("Introdu litera: ");
		litera:
			if (scanf_s("%s", litera, 2) != 1) {
				printf("Trebuie introdusa o singura litera!\n");
				fseek(stdin, 0L, SEEK_END);
				goto litera;
			}
			Repo repoFiltrat = FiltreazaLitera(&prezi->srv, litera);
			if (dimensiune(&repoFiltrat) == 0) {
				printf("Nu exista medicamente care sa inceapa cu litera introdusa!\n");
			}
			else {
				printf("Medicamentele filtrate cu stoc mai mic decat cel introdus:\n");
				for (int i = 0; i < dimensiune(&repoFiltrat); i++) {
					Medicament m = get(&repoFiltrat, i);
					printf("Id: %d,\t Cod: %d,\t Nume: %s, \t Concentratie: %f, \t Cantitate: %d;\n",
						m.id, m.cod, m.nume, m.concentratie, m.cantitate);
				}
				
			}DistrugeRepo(&repoFiltrat);
		}
	}
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
void ui_ordonare(Prezentare* prezi) {
	printf("\nCe fel de ordonare doresti?\n"
		"\t 1. Dupa nume.\n"
		"\t 2. Dupa cantitate.\n");
	int tip = 0;
	int cresc_descresc = 0;
tip:
	if (scanf_s("%d", &tip) != 1) {
		printf("Trebuie introdus un numar natural!\n");
		fseek(stdin, 0L, SEEK_END);
		goto tip;
	}
	if (tip != 1 && tip != 2) {
		printf("\nComanda invalida!");
	}
	else {
		printf("\nIn ce ordine le doresti?\n"
			"\t 1. Crescatoare.\n"
			"\t 2. Descrescatoare.\n");
	cresc_descresc:
		if (scanf_s("%d", &cresc_descresc) != 1) {
			printf("Trebuie introdus un numar natural!\n");
			fseek(stdin, 0L, SEEK_END);
			goto cresc_descresc;
		}
		if (cresc_descresc != 1 && cresc_descresc != 2) {
			printf("\nComanda invalida!");
		}
		else {
			if (tip == 1) {
				Repo repoOrd = OrdonareNume(&prezi->srv, cresc_descresc);
				printf("Medicamentele ordonate dupa nume:\n");
				for (int i = 0; i < dimensiune(&repoOrd); i++) {
					Medicament m = get(&repoOrd, i);
					printf("Id: %d,\t Cod: %d,\t Nume: %s, \t Concentratie: %f, \t Cantitate: %d;\n",
						m.id, m.cod, m.nume, m.concentratie, m.cantitate);
				}
				DistrugeRepo(&repoOrd);
			}
			if (tip == 2) {
				Repo repoOrd = OrdonareCantitate(&prezi->srv, cresc_descresc);
				printf("Medicamentele ordonate dupa cantitate:\n");
				for (int i = 0; i < dimensiune(&repoOrd); i++) {
					Medicament m = get(&repoOrd, i);
					printf("Id: %d,\t Cod: %d,\t Nume: %s, \t Concentratie: %f, \t Cantitate: %d;\n",
						m.id, m.cod, m.nume, m.concentratie, m.cantitate);
				}
				DistrugeRepo(&repoOrd);
			}
		}
	}
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
void ui_sterge(Prezentare* prezi) {
	ui_afiseaza(prezi);
	printf("\nIntrodu id-ul medicamentului pe care doresti sa-l stergi: ");
	int id;
id:
	if (scanf_s("%d", &id) != 1) {
		printf("Trebuie introdus un numar natural!\n");
		fseek(stdin, 0L, SEEK_END);
		goto id;
	}
	Medicament medi = InitializeazaMedicamentID(id, 1, "a",1, 1);
	int exista = AflaPozitieMedicamentInRepo(&prezi->srv, medi);
	if(exista==-1) {
		printf("Nu exista medicament cu id-ul introdus!\n");
	}
	else {
		StergeMedicamentSrv(&prezi->srv,medi);
		printf("Medicament sters cu succes!\n");
		DistrugeMedicament(&medi);
	}
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
void ui_actualizeaza(Prezentare* prezi) {
	ui_afiseaza(prezi);
	printf("\nIntrodu id-ul medicamentului pe care doresti sa-l actualizezi: ");
	int id;
	char nume[30] = "";
	float concentratie = 1;
id:
	if (scanf_s("%d", &id) != 1) {
		printf("Trebuie introdus un numar natural!\n");
		fseek(stdin, 0L, SEEK_END);
		goto id;
	}
	printf("Noul nume: ");
	scanf_s("%s", nume, 30);
concentratie:
	printf("Noua concentratie: ");
	if (scanf_s("%f", &concentratie) != 1) {
		printf("Trebuie introdus un numar float!\n"); 
		fseek(stdin, 0L, SEEK_END);
		goto concentratie;
	}
	Medicament medi = InitializeazaMedicamentID(id, 1, nume, concentratie, 1);
	int exista = AflaPozitieMedicamentInRepo(&prezi->srv, medi);
	if (exista == -1) {
		printf("Nu exista medicament cu id-ul introdus!\n");
	}
	else {
		int erori[4] = { 0 };
		ActualizareSrv(&prezi->srv,id,nume,concentratie, erori);
		if (erori[0] != 0 || erori[1] != 0 || erori[2] != 0 || erori[3] != 0) {
			printf("!!!Erori la validarea datelor de intrare!!!\n");
			if (erori[0] == 1) {
				printf("\tCodul este invalid!\n");
			}
			if (erori[1] == 1) {
				printf("\tNumele este invalid!\n");
			}
			if (erori[2] == 1) {
				printf("\tConcentratia este invalida!\n");
			}
			if (erori[3] == 1) {
				printf("\tCantitatea este invalida!\n");
			}
			printf("!!!\n\n");
		}
		else {
			printf("Medicament actualizat cu succes!\n");
		}
		DistrugeMedicament(&medi);
	}
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
void ui_afiseaza(Prezentare* prezi) {
	Repo repo = Copiere(&prezi->srv);
	if (dimensiune(&repo) == 0) {
		printf("\nNu exista medicamente inregistrate!\n");
	}
	for (int i = 0; i < dimensiune(&repo); i++) {
		Medicament m = get(&repo, i);
		printf("Id: %d,\t Cod: %d,\t Nume: %s, \t Concentratie: %f, \t Cantitate: %d;\n",
			m.id, m.cod, m.nume, m.concentratie, m.cantitate);
	}
	DistrugeRepo(&repo);
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
void ui_adauga(Prezentare* prezi) {
	int cod = 1, cantitate = 1;
	char nume[30] = "";
	float concentratie = 1;
cod:
	printf("Codul: ");
	if (scanf_s("%d", &cod) == 0) {
		printf("Trebuie introdus un numar natural!\n");
		fseek(stdin, 0L, SEEK_END);
		goto cod;
	}
	else {
		printf("Numele: ");
		scanf_s("%s", nume, 30);
	concentratie:
		printf("Concentratie: ");
		if (scanf_s("%f", &concentratie) != 1) {
			printf("Trebuie introdus un numar float!\n");
			fseek(stdin, 0L, SEEK_END);
			goto concentratie;
		}
		else {
		cantitate:
			printf("Cantitate disponibila in stoc: ");
			if (scanf_s("%d", &cantitate) != 1) {
				printf("Trebuie introdus un numar natural!\n");
				fseek(stdin, 0L, SEEK_END);
				goto cantitate;
			}
		}
	}
	int erori[4] = { 0 };
	AdaugaMedicamentStoc(&prezi->srv, cod, nume, concentratie, cantitate, erori);
	if (erori[0] != 0 || erori[1] != 0 || erori[2] != 0 || erori[3] != 0) {
		printf("!!!Erori la validarea datelor de intrare!!!\n");
		if (erori[0] == 1) {
			printf("\tCodul este invalid!\n");
		}
		if (erori[1] == 1) {
			printf("\tNumele este invalid!\n");
		}
		if (erori[2] == 1) {
			printf("\tConcentratia este invalida!\n");
		}
		if (erori[3] == 1) {
			printf("\tCantitatea este invalida!\n");
		}
		printf("!!!\n\n");
	}
	else {
		printf("Medicament adaugat cu succes!\n");
	}
}


/*----------------------------------------------------------------------------------------------------------------------------------------------------------*/
int prezentare(Prezentare* prezi)
{
	int repeat = 1;
	while (repeat)
	{
		meniu();
		scanf_s("%d", &repeat);
		if (repeat == 0) {
			break;
		}
		if (repeat == 1) {
			ui_adauga(prezi);
		}
		if (repeat == 2) {
			ui_afiseaza(prezi);
		}
		if (repeat == 3) {
			ui_actualizeaza(prezi);
		}
		if (repeat == 4) {
			ui_sterge(prezi);
		}
		if (repeat == 5) {
			ui_ordonare(prezi);
		}
		if (repeat == 6) {
			ui_filtrare(prezi);
		}
	}
	return 0;
}



