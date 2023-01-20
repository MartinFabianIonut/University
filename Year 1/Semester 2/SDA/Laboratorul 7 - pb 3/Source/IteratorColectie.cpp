#include "IteratorColectie.h"
#include "Colectie.h"


IteratorColectie::IteratorColectie(const Colectie& c) : col(c) {
	/*
		Complexitate Teta(1)
	*/
	curent = c.prim;
}

TElem IteratorColectie::element()  {
	/*
		Complexitate Teta(h) - h = inaltimea pe partea stanga a radacinii curente
	*/
	while (curent != -1) {
		s.push(curent);
		curent = col.stanga[curent];
	}
	curent = s.top();
	s.pop();
	return col.e[curent].element;
}

bool IteratorColectie::valid() const {
	/*
		Complexitate Teta(1)
	*/
	return curent != -1 || !s.empty();
}

void IteratorColectie::urmator() {
	/*
		Complexitate Teta(1)
	*/
	curent = col.dreapta[curent];
}

void IteratorColectie::prim() {
	/*
		Complexitate Teta(1)
	*/
	curent = col.prim;
}
