#include "MD.h"
#include "IteratorMD.h"
#include <exception>
#include <iostream>

using namespace std;

Nod::Nod(TElem e, PNod urm) {
	/*Complexitate Teta(1)*/
	this->e = e;
	this->urm = urm;
}

TElem Nod::element() {
	/*Complexitate Teta(1)*/
	return e;
}

PNod Nod::urmator() {
	/*Complexitate Teta(1)*/
	return urm;
}

MD::MD() {
	/* de adaugat
	Complexitate Teta(1)
	*/
	prim = nullptr;
	ultim = nullptr;
	n = 0;
}


void MD::adauga(TCheie c, TValoare v) {
	/* de adaugat 
	Complexitate Teta(1) 
	*/
	TElem e = pair <TCheie, TValoare>(c,v);
	PNod q = new Nod(e, nullptr);
	if (ultim != nullptr) {
		ultim->urm = q;
	}
	else {
		prim = q;
	}
	ultim = q;
	n++;
}


bool MD::sterge(TCheie c, TValoare v) {
	/* de adaugat 
	Complexitate O(n)
	caz favorabil - Teta(1) - pe prima pozitie in lista
	caz defavorabil - Teta(n) - la final
	caz mediu Teta(n) - T(n)=(1+2+...+n)/n=[n(n+1)]/2n=(n+1)/2
	*/
	PNod q1 = nullptr, q = prim;
	while (q != nullptr) {
		if (q->e.first == c && q->e.second == v) {
			break;
		}
		q1 = q;
		q = q->urm;
	}
	if (q != nullptr) {
		if (q == prim) {
			prim = prim->urm;
			free(q);
			if (prim == nullptr) {
				ultim = nullptr;
			}
		}
		else {
			q1->urm = q->urm;
			if (q == ultim) {
				ultim = q1;
			}
			free(q);
		}
		n--;
		return true;
	}
	return false;
}


vector<TValoare> MD::cauta(TCheie c) const {
	/* de adaugat 
	Complexitate Teta(n) - se parcurge toata lista in toate cazurile
	*/
	PNod parcurg = prim;
	TElem elem;
	vector<TValoare> vec;
	while (parcurg != nullptr) {
		elem = parcurg->element();
		if (elem.first == c) {
			vec.push_back(elem.second);
		}
		parcurg = parcurg->urm;
		
	}
	return vec;
}

TValoare MD:: ceaMaiFrecventaValoare() const {
	PNod parcurg = prim;
	if (parcurg == nullptr) {
		free(parcurg);
		return 0;
	}
	else {
		vector<pair<TValoare,int>> frecventa;
		TValoare curent,ceamai;
		bool mai_exista;
		int i,maxi = 1;
		frecventa.push_back(make_pair(parcurg->element().second,1));
		parcurg = parcurg->urm;
		while (parcurg != nullptr) {
			mai_exista = false;
			i = 0;
			curent = parcurg->element().second;
			while(i < frecventa.size() ){
				if (frecventa[i].first == curent) {
					mai_exista = true;
				}
				i++;
			}
			i--;
			if (mai_exista == true) {
				frecventa[i].second++;
			}
			else {
				frecventa.push_back(make_pair(parcurg->element().second, 1));
			}
			parcurg = parcurg->urm;
		}
		ceamai = frecventa[0].first;
		for (i = 0; i < frecventa.size(); i++)
		{
			if (frecventa[i].second > maxi) {
				maxi = frecventa[i].second;
				ceamai = frecventa[i].first;
			}
		}
		free(parcurg);
		return ceamai;
	}
	
}


int MD::dim() const {
	/* de adaugat 
	Complexitate Teta(1)
	*/
	return n;
}


bool MD::vid() const {
	/* de adaugat 
	Complexitate Teta(1)
	*/
	if (this->prim == nullptr) {
		return true;
	}
	return false;
}

IteratorMD MD::iterator() const {
	return IteratorMD(*this);
}


MD::~MD() {
	/* de adaugat 
	Complexitate Teta(1)
	*/
	while (prim != nullptr) {
		PNod p = prim;
		prim = prim->urm;
		delete p;
	}
}

