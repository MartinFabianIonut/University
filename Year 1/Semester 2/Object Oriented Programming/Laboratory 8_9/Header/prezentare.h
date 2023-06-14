#ifndef PREZENTARE_H
#define PREZENTARE_H

#include "service.h"
#include "pok.h"

class Prezentare {
	Service& ctr;
	/*
	Citeste datele de la tastatura si adauga Pet
	arunca exceptie daca: nu se poate salva, nu e valid
	*/
	void adaugaUI();
	void adaugaApUI();
	void stergeUI();
	void modificaUI();
	void cautaUI();
	void filtruTip();
	void filtruSuprafata();
	void sortareNume();
	void sortareSuprafata();
	void sortareTipSuprafata();
	void undoUI();
	void notificareUI();
	/*
	Tipareste o lista de animale la consola
	*/
	void tipareste(vector<Locatar>& locatarii);
	void tipareste2(vector<Locatar>& locatarii);
public:
	Prezentare(Service& ctr) noexcept :ctr{ ctr } {
	}
	//nu permitem copierea obiectelor
	Prezentare(const Prezentare& ot) = delete;

	void adaugaCateva(Service& ctr);

	int citireAp();
	double citireSup();
	int citireTip();
	void start();
	void mode();
	~Prezentare();
};

#endif