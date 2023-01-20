#include "IteratorColectie.h"
#include "Colectie.h"


IteratorColectie::IteratorColectie(const Colectie& c) : col(c) {
	/*
		Total: complexitate Teta(1)
	*/
	curent = c.prim;
	fr = 1;
}

void IteratorColectie::prim() {
	/*
		Total: complexitate Teta(1)
	*/
	curent = col.prim;
}


void IteratorColectie::urmator() {
	/*
		Total: complexitate Teta(1)
	*/
	int fr1 = col.e[curent].frecventa;
	if (fr < fr1) {
		fr++;
	}
	else {
		fr = 1;
		curent = col.urm[curent];
	}
}


bool IteratorColectie::valid() const {
	/*
		Total: complexitate Teta(1)
	*/
	return curent != -1;
}



TElem IteratorColectie::element() const {
	/*
		Total: complexitate Teta(1)
	*/
	return col.e[curent].element;
}
