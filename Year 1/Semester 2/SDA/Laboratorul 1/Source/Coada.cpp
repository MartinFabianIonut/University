
#include "Coada.h"
#include <exception>
#include <iostream>

using namespace std;

void Coada::redim() {

	/*
	Complexitate Teta(n)
	*/

	//alocam un spatiu de capacitate dubla
	TElem* eNou = new TElem[2 * cp];

	//copiem vechile valori in noua zona

	if (spate < fata) {
		for (int i = fata; i < cp; i++)
			eNou[i] = e[i];
		for (int i = 0; i < fata; i++)
			eNou[i] = e[i];
	}
	else {
		for (int i = 0; i < spate; i++)
			eNou[i] = e[i];
	}

	//dublam capacitatea
	cp = 2 * cp;

	//eliberam vechea zona
	delete[] e;

	//vectorul indica spre noua zona
	e = eNou;
}

Coada::Coada() {

	//setam capacitatea

	this->cp = 5;

	//alocam spatiu de memorare pentru vector
	e = new TElem[cp];

	//setam numarul de elemente
	this->n = 0;
	this->fata = 0;
	this->spate = 0;
}

void Coada::adauga(TElem elem) {
	/*
	Complexitate Teta(1)
	*/
	
	if (this->n == this->cp - 1) 
		redim();
	this->e[this->spate] = elem;
	if (this->spate == this->cp - 1)
		this->spate = 0;
	else
		this->spate++; 
	this->n++;
}

//arunca exceptie daca coada e vida
TElem Coada::element() const {
	/* 
	Complexitate Teta(1)
	*/
	if (this->vida() == true)
		throw exception("Ceva");
	return this->e[this->fata];
}

TElem Coada::sterge() {
	/* 
	Complexitate Teta(1)
	*/
	TElem elem;
	if (this->vida() == true)
		throw exception("Ceva 2");
	elem = this->e[this->fata];
	if (this->fata == this->cp - 1)
		this->fata = 0;
	else
		this->fata = this->fata + 1;
	this->n--;
	return elem;
}

bool Coada::vida() const {
	/*
	Complexitate Teta(1)
	*/
	if (this->n == 0)
		return true;
	return false;
}

Coada::~Coada() {
	/*
	Complexitate Teta(1)
	*/
	delete[]e;
}

