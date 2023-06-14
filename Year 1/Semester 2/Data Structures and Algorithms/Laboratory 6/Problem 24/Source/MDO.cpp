#include "IteratorMDO.h"
#include "MDO.h"
#include <iostream>
#include <vector>
#include <math.h>
#include <stdlib.h>

#include <exception>
using namespace std;

//functioa care da hashCode-ul unuei element
int hashCode(TCheie c) {
	/*Complexitate Teta(1)*/
	//ptr moment numarul e intreg
	return abs(c);
}

Nod::Nod(TElem e, PNod urm) {
	/*Complexitate Teta(1)*/
	this->e = e;
	this->urm = urm;
}

int MDO::d(TCheie c) const {
	/*Complexitate Teta(1)*/
	//dispersia prin diviziune
	return hashCode(c) % m;
}

TElem Nod::element() {
	/*Complexitate Teta(1)*/
	return e;
}

PNod Nod::urmator() {
	/*Complexitate Teta(1)*/
	return urm;
}

MDO::MDO(Relatie r) {
	/* de adaugat *//* 
	Complexitate Teta(m)
	*/
	m = MAX; //initializam m cu o valoare predefinita
		//daca va fi cazul, se poate redimensiona tabela si sa se redisperseze elementele
		//se initializeaza listele inlantuite ca fiind vide
	for (int i = 0; i < m; i++) {
		l[i] = nullptr;
	}
	rel = r;
}
bool MDO::myrel(TCheie c1, TCheie c2) {
	/* 
	Complexitate Teta(1)
	*/
	if(rel(c1,c2)) {
		return true;
	}
	else {
		return false;
	}
}

void MDO::adauga(TCheie c, TValoare v) {
	/* de adaugat
	Complexitate O(n') - n' nr de elemente dispersate la cheia de adaugat
	caz favorabil - Teta(1) - lista e goala sau are un element
	caz defavorabil - Teta(n') - la inceput
	caz mediu Teta(n) - T(n')=(1+2+...+n)/n=[n(n+1)]/2n=(n+1)/2 - undeva la mijloc
	*/
	//locatia de dispersie a cheii
	TElem e = pair <TCheie, TValoare>(c, v);
	int i = d(c);
	//se creeaza un nod
	PNod p = new Nod(e, nullptr);
	//se adauga in capul listei inlantuite de la locatia i
	
	PNod parcurg = l[i];
	bool ok = false;
	while (parcurg != nullptr && ok == false) {
		if (parcurg->urm == nullptr) {
			if (!myrel(c, parcurg->e.first)) { //daca nu e in ordinea dorita, adaug la final - se punea scrie daca e in ordine schimband pozitiile parametrilor
				ok = true;
				parcurg->urm = p;
				break;
			}
		}
		else {
			if (!myrel(c, parcurg->e.first) && myrel(c, parcurg->urm->e.first)) { //daca ma aflu undeva la mijloc, intre doua chei
				ok = true;
				p->urm = parcurg->urm;
				parcurg->urm = p;				
				break;
			}
		}
		parcurg = parcurg->urm;
	}
	if (!ok) { // adaug la inceput, cheia fiind minima
		p->urm = l[i];
		l[i] = p;
	}
}

vector<TValoare> MDO::cauta(TCheie c) const {
	/* de adaugat
	Complexitate O(n') - n' nr de elemente dispersate la cheia cautata
	caz favorabil - Teta(1) - pe prima pozitie in lista
	caz defavorabil - Teta(n') - la final sau nu exista
	caz mediu Teta(n) - T(n)=(1+2+...+n)/n=[n(n+1)]/2n=(n+1)/2
	*/
	vector<TValoare> vec;
	PNod parcurg = l[d(c)];
	TElem elem;
	
	while (parcurg != nullptr) {
		elem = parcurg->element();
		if (elem.first == c) {
			vec.push_back(elem.second);
		}
		parcurg = parcurg->urm;

	}
	return vec;
}

bool MDO::sterge(TCheie c, TValoare v) {
	/* de adaugat
	Complexitate O(n)
	caz favorabil - Teta(1) - pe prima pozitie in lista
	caz defavorabil - Teta(n) - la final
	caz mediu Teta(n) - T(n)=(1+2+...+n)/n=[n(n+1)]/2n=(n+1)/2
	*/
	PNod q1 = nullptr, q = l[d(c)], prim = l[d(c)];
	while (q != nullptr) {
		if (q->e.first == c && q->e.second == v) {
			break;
		}
		q1 = q; // q1 e precedentul lui q
		q = q->urm;
	}
	if (q != nullptr) {
		if (q == prim) {
			l[d(c)] = l[d(c)]->urm; // scap de primul
			delete q;
		}
		else {
			q1->urm = q->urm; // fac legatura intre predecesor si succesor
			delete q;
		}
		return true;
	}
	return false;
}

int MDO::dim() const {
	/* de adaugat
	Complexitate Teta(n) - n nr de elemente din MDO
	*/
	int d = 0;
	for (int i = 0; i < m; i++) {
		PNod parcurg = l[i];
		while (parcurg != nullptr) { // parcurg toate elementele de pe fiecare pozitie de dispersie
			d++;
			parcurg = parcurg->urm;
		}
	}
	return d;
}

bool MDO::vid() const {
	/* de adaugat
	Complexitate O(m)
	*/
	for (int i = 0; i < m; i++) {
		PNod parcurg = l[i];
		while (parcurg != nullptr) {
			return false;
		}
	}
	return true;
}

IteratorMDO MDO::iterator() const {
	return IteratorMDO(*this);
}

MDO::~MDO() {
	/* de adaugat
	Complexitate Teta(n) - n nr de elemente din MDO
	*/
	//se elibereaza memoria alocata listelor
	for (int i = 0; i < m; i++) {
		//se elibereaza memoria pentru lista i
		while (l[i] != nullptr) {
			PNod p = l[i];
			l[i] = l[i]->urm;
			delete p;
		}
	}
}
