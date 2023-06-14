#include "Colectie.h"
#include "IteratorColectie.h"
#include <iostream>

using namespace std;

bool rela(TElem c1, TElem c2) {
	/*
		Complexitate Teta(1)
	*/
	if (c1 <= c2) {
		return true;
	}
	else {
		return false;
	}
}

Colectie::Colectie() {
	/*
		Complexitate Teta(cp)
	*/
	rel = rela;
	cate = 0;
	prim = -1;
	stanga = new int[cp];
	dreapta = new int[cp];
	e = new StangaDreapta[cp];
	for (int i = 0; i < cp - 1; i++)
		stanga[i] = i + 1;
	stanga[cp - 1] = -1;
	primLiber = 0;

}

void Colectie::doo(TElem elem, int primul, int i)
{
	/*
		Complexitate Teta(h) -h = adancimea ramurii
	*/
	if (rel(e[primul].element, elem) && dreapta[primul] == -1) {
		dreapta[primul] = i; return;
	}
	if (!rel(e[primul].element, elem) && stanga[primul] == -1) {
		stanga[primul] = i; return;
	}
	if (rel(e[primul].element, elem) && dreapta[primul] != -1) {
		doo(elem, dreapta[primul], i);
	}
	if (!rel(e[primul].element, elem) && stanga[primul] != -1) {
		doo(elem, stanga[primul], i);
	}
}

void Colectie::adauga(TElem e)
{
	/*
		Complexitate Teta(h)
	*/
	int i = prim, j = prim;
	i = aloca();
	if (i != -1) {
		this->e[i].element = e;
		stanga[i] = -1;
		dreapta[i] = -1;
	}
	if (prim == -1) {
		prim = 0;
		j = prim;
	}
	if (i != j) {
		doo(e, j, i);
	}
	cate++;
}

bool Colectie::stergee(int i, TElem elem) {
	/*
		Complexitate O(h) -h = adancimea arborelui dat
		Fav: Teta(1) - se gaseste in radacina
		Defav: Teta(h) - nu exista sau e pe ultimul nivel
		Mediu: Teta(h) - nivel intermediar
		toate *cp
	*/
	TElem minim;
	if (i == -1) {
		return false;
	}
	if (rel(elem, e[i].element) && e[i].element != elem) {
		return stergee(stanga[i], elem);
	}
	else {
		if (rel(e[i].element, elem) && e[i].element != elem) {
			return stergee(dreapta[i], elem); 
		}
		else {
			if (e[i].element == elem) {
				if (stanga[i] != -1 && dreapta[i] != -1) {
					minim = mini(dreapta[i]);
					e[i].element = minim;
					return stergee(dreapta[i], e[i].element);
				}
				else {
					minim = i;
					dealoca(minim);
				}
				return true;
			}
			else {
				return false;
			}
		}
		return true;
	}
	return false;
}

bool Colectie::sterge(TElem e)
{
	/*
		Complexitate O(h) -h = adancimea arborelui dat
		Fav: Teta(1) - se gaseste in radacina
		Defav: Teta(h) - nu exista sau e pe ultimul nivel
		Mediu: Teta(h) - nivel intermediar
		toate *cp
	*/
	if (cate == 0) {
		return false;
	}
	if (stergee(prim, e)) {
		cate--;
		return true;
	}
	return false;
}


bool Colectie::cautaa(int i, TElem elem)const {
	/*
		Complexitate O(h) -h = adancimea arborelui dat
		Fav: Teta(1) - se gaseste in radacina
		Defav: Teta(h) - nu exista sau e pe ultimul nivel
		Mediu: Teta(h) - nivel intermediar
	*/
	if (e[i].element == elem) {
		return true;
	}
	if (rel(e[i].element, elem) && dreapta[i] != -1) {
		return cautaa(dreapta[i], elem);
	}
	if (!rel(e[i].element, elem) && stanga[i] != -1) {
		return cautaa(stanga[i], elem);
	}
	return false;
}

bool Colectie::cauta(TElem elem) const {
	/*
		Complexitate O(h) -h = adancimea arborelui dat
		Fav: Teta(1) - arbore vid
		Defav: Teta(h) - nu exista sau e pe ultimul nivel
		Mediu: Teta(h) - nivel intermediar
	*/
	if (cate == 0) {
		return false;
	}
	return cautaa(prim, elem);
}

int Colectie::apar(int i, TElem elem)const {
	/*
		Complexitate Teta(n)
	*/
	int nr = 0;
	if (i == -1) {
		return 0;
	}
	if (e[i].element == elem) {
		nr++;
	}
	nr += apar(stanga[i],elem);
	nr += apar(dreapta[i],elem);
	return nr;
}



int Colectie::nrAparitii(TElem elem) const {
	/*
		Complexitate Teta(n)
	*/
	if (cate == 0) {
		return 0;
	}
	int nr = 0;
	nr += apar(prim,elem);
	return nr;
}

TElem Colectie::mini(int i){
	/*
		Complexitate O(h)-h = adancimea subarborelui dat
		Fav : Teta(1) - pe nivelul urmator
		Defav: Teta(h) - ultimul nivel'
		Mediu: Teta(h) - nivel intermediar
	*/
	if (stanga[i] == -1 ) {
		return e[i].element;
	}
	return mini(stanga[i]);
}

int Colectie::dimm(int i) const {
	/*
		Complexitate Teta(k)-k = nr noduri ale unui subarbore dat
	*/
	if (i == -1) {
		return 0;
	}
	int d = 1;
	if (stanga[i] != -1) {
		d+=dimm(stanga[i]);
	}
	if (dreapta[i] != -1) {
		d += dimm(dreapta[i]);
	}
	return d;
}

int Colectie::dim() const {
	/*
		Total: complexitate Teta(n), n = nr noduri arbore
	*/
	return cate;
	int i = prim, dim = 1;
	if (stanga[i] != -1) {
		dim += dimm(stanga[i]);
	}
	if (dreapta[i] != -1) {
		dim += dimm(dreapta[i]);
	}
	return dim;
}


bool Colectie::vida() const {
	/*
		Complexitate Teta(1)
	*/
	if (cate == 0) {
		return true;
	}
	return false;
}


IteratorColectie Colectie::iterator() const {
	/*
		Complexitate Teta(1)
	*/
	return  IteratorColectie(*this);
}


Colectie::~Colectie() {
	/*
		Complexitate Teta(1)
	*/
	delete[]e;
	delete[]stanga;
	delete[]dreapta;
}


int Colectie::aloca() {
	/*
		Complexitate Teta(1)
	*/
	//se sterge primul element din lista spatiului liber
	int i = primLiber;
	primLiber = stanga[primLiber];
	return i;
}

void Colectie::dealoca(int i) {
	/*
		Complexitate Teta(cp)
	*/

	//se trece pozitia i in lista spatiului liber
	for (int k = 0; k < cp; k++) {
		if (stanga[k] == i) {
			int m = max(stanga[i], dreapta[i]);
			stanga[k] = m;
		}
	}
	for (int k = 0; k < cp; k++) {
		if (dreapta[k] == i) {
			int m = max(stanga[i], dreapta[i]);
			dreapta[k] = m;
		}
	}
	if (i == prim) {
		prim = dreapta[i];primLiber = i; stanga[primLiber] = cate;
	}
	else {
		int ant = primLiber;
		primLiber = i;
		stanga[i] = ant;
	}
}

int Colectie::stergeRecursiv(int i) {
	if (i == -1) {
		return 0;
	}
	int nr,k;
	TElem el = e[i].element,e2;
	nr = nrAparitii(el);
	if (nr > 1) {
		for (k = 1; k <= nr; k++) {
			sterge(el);
		}
	}
	else {
		nr = 0;
	}
	e2 = e[i].element;
	if (e2 == el) {
		nr += stergeRecursiv(stanga[i]);
		nr += stergeRecursiv(dreapta[i]);
	}
	else {
		nr += stergeRecursiv(i);
	}
	return nr ;
}

int Colectie::stergeToateElementeleRepetitive()
{
	int nr=0;
	if (prim == -1) {
		return 0;
	}
	nr += stergeRecursiv(prim);
	return nr;
}