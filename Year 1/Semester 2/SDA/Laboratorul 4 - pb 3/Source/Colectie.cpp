#include "Colectie.h"
#include "IteratorColectie.h"
#include <exception>
#include <iostream>

using namespace std;


Colectie::Colectie() {
	/*
		Complexitate Teta(cp)
	*/
	//lista e vida
	prim = -1;
	//se initializeaza lista spatiului liber - toate pozitiile din vecto sunt marcate ca fiind libere
	urm = new int[cp];
	e = new Pereche[cp];
	for (int i = 0; i < cp - 1; i++)
		urm[i] = i + 1;
	urm[cp - 1] = -1;
	//referinta spre prima pozitie libera din lista
	primLiber = 0;
}

void Colectie::redim() {
	/*
	Complexitate Teta(cp)
	*/
	//alocam un spatiu de capacitate dubla
	
	Pereche* eNou = new Pereche[2 * cp];
	int* urmNou = new int[2 * cp];

	//se initializeaza lista spatiului liber - toate pozitiile din vecto sunt marcate ca fiind libere
	for (int i = 0; i < cp; i++) {
		urmNou[i] = urm[i];
	}
	for (int i = cp; i < 2*cp - 1; i++) {
		urmNou[i] = i + 1;
	}
	urmNou[2 * cp - 1] = -1;
	for (int i = 0; i < cp ; i++)
		eNou[i] = e[i];
	
	primLiber = cp;

	//dublam capacitatea
	cp = 2 * cp;

	//eliberam vechea zona
	delete[] e;
	delete[] urm;
	//vectorul indica spre noua zona
	e = eNou;
	urm = urmNou;
}


int Colectie::aloca() {
	/*
		Complexitate Teta(1) 
	*/

	//se sterge primul element din lista spatiului liber
	int i = primLiber;
	primLiber = urm[primLiber];
	return i;
}

void Colectie::dealoca(int i) {
	/*
		Complexitate Teta(1)
	*/

	//se trece pozitia i in lista spatiului liber
	urm[i] = primLiber;
	primLiber = i;
}


//creeaza un nod in lista inlantuita unde se memoreaza informatia utila e
int Colectie::creeazaNod(TElem elem) {
	/*
		Total: complexitate O(n) amortizat
		- complexitate Teta(1) in caz favorabil, cand mai exista elementul si e la inceputul listei
		- in caz defavorabil si amortizat, cand nu mai exista elementul (sau mai exista si este la finalul listei reprezentate pe tablou),
		complexitatea este Teta(n)
		- in caz mediu, complexitate Teta(n) - T(n) = (1 + 2 + ... + n) / n = [n(n + 1)] / 2n = (n + 1) / 2
	*/

	int i = prim;
	bool ex = false;
	while (i != -1) {
		if (e[i].element== elem) {//verific daca mai exista si cersc frecventa cu unu
			e[i].frecventa += 1;
			ex = true;
		}
		i = urm[i];
	}//daca nu mai exista valoarea
	if (ex == false) {
		
		if (primLiber == -1) {
			redim();
		}
		i = aloca();
		if (i != -1) {//exista spatiu liber in lista
			this->e[i].element = elem;
			this->e[i].frecventa = 1;
			urm[i] = -1;
		}
	}
	return i;
}

void Colectie::adauga(TElem elem) {

	/*
		Complexitate O(n) amortizat, asemanator creeazaNod
	*/

	int i = creeazaNod(elem);
	//in cazul folosirii unui vector static, e posibil ca i sa iasa -1 in cazul in care lista e plina
	if (i != -1) {
		urm[i] = prim;
		prim = i;
	}
}


bool Colectie::sterge(TElem elem) {
	/* 
		Total: complexitate O(n) 
		- complexitate Teta(1) in caz favorabil, cand sterg de la inceputul listei sau lista e goala
		- in caz defavorabil, cand sterg la finalul listei reprezentate pe tablou, complexitatea este Teta(n)
		- in caz mediu, complexitate Teta(n) - T(n) = (1 + 2 + ... + n) / n = [n(n + 1)] / 2n = (n + 1) / 2
	*/
	int i = prim, poz = prim,precedent;
	if (i == -1) {
		return false;
	}
	while (i != -1) {
		if (e[i].element == elem) {
			if (poz == prim) {
				if (e[i].frecventa > 1) {
					e[i].frecventa--;
				}
				else {
					prim = urm[poz];
					dealoca(poz);
				}
			}
			else {
				precedent = prim;
				while (urm[precedent] != poz) {
					precedent = urm[precedent];
				}
				if (e[urm[precedent]].frecventa > 1) {
					e[urm[precedent]].frecventa--;
				}
				else {
					urm[precedent] = urm[poz];
					dealoca(poz);
				} 
			}
			return true;
		}
		i = urm[i];
		poz = i;
	}
	return false;
}


bool Colectie::cauta(TElem elem) const {
	/*
		Total: complexitate O(n)
		- complexitate Teta(1) in caz favorabil, cand gasesc la inceputul listei sau lista e goala
		- in caz defavorabil, cand gasesc la finalul listei reprezentate pe tablou, complexitatea este Teta(n)
		- in caz mediu, complexitate Teta(n) - T(n) = (1 + 2 + ... + n) / n = [n(n + 1)] / 2n = (n + 1) / 2
	*/
	int i = prim;
	if (i == -1) {
		return false;
	}
	while (i != -1) {
		if (e[i].element == elem) {
			return true;
		}
		i = urm[i];
	}
	return false;
}

int Colectie::nrAparitii(TElem elem) const {
	/*
		Total: complexitate O(n)
		- complexitate Teta(1) in caz favorabil, cand elementul a carei frecventa o caut e la inceputul listei sau lista e goala
		- in caz defavorabil, cand elementul a carei frecventa o caut e la finalul listei reprezentate pe tablou, complexitatea este Teta(n)
		- in caz mediu, complexitate Teta(n) - T(n) = (1 + 2 + ... + n) / n = [n(n + 1)] / 2n = (n + 1) / 2
	*/
	int i = prim;
	if (i == -1) {
		return 0;
	}
	while (i != -1) {
		if (e[i].element == elem) {
			return e[i].frecventa;
		}
		i = urm[i];
	}
	return 0;
}


int Colectie::dim() const {
	/*
		Total: complexitate O(n)
		- complexitate Teta(1) in caz favorabil, cand  lista e goala
		- in caz defavorabil, Teta(n) cand nu e goala
	*/
	int i = prim, dim = 0;
	if (i == -1) {
		return 0;
	}
	while (i != -1) {
		dim += e[i].frecventa;
		i = urm[i];
	}
	return dim;
}


bool Colectie::vida() const {
	/*
		Total: complexitate Teta(1)
	*/
	if (prim == -1) {
		return true;
	}
	return false;
}

TElem Colectie::celMaiPutinFrecvent() const
{
	int i = prim;
	if (i == -1) {
		return NULL_TELEM;
	}
	int frecvMin = e[0].frecventa;
	i = urm[0];
	TElem deReturnat = e[0].element;
	while (i != -1) {
		if (e[i].frecventa < frecvMin) {
			frecvMin = e[i].frecventa;
			deReturnat = e[i].element;
		}
		i = urm[i];
	}
	return deReturnat;
}

IteratorColectie Colectie::iterator() const {
	/*
		Total: complexitate Teta(1)
	*/
	return  IteratorColectie(*this);
}


Colectie::~Colectie() {
	/*
		Total: complexitate Teta(1)
	*/
	delete[]e;
	delete[]urm;
}


