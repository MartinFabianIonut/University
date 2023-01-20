#pragma once

#include "MDO.h"
#include <vector>



class IteratorMDO {
	friend class MDO;
private:

	//constructorul primeste o referinta catre Container
	//iteratorul va referi primul element din container
	IteratorMDO(const MDO& dictionar);

	PNod Clone(PNod list);
    PNod SortedMerge(PNod a, PNod b);
	void  MoveNode(PNod* destRef, PNod* sourceRef);

	//contine o referinta catre containerul pe care il itereaza
	const MDO& dict;
	/* aici e reprezentarea  specifica a iteratorului */

	void interclaseazaListe();

	//locatia curenta din tabela
	int poz;
	//retine pozitia curenta in interiorul listei de la locatia curenta - adresa Nodului curent din lista asociata
	PNod curent;
	PNod primul;

	vector <TElem> ptiterare;

	int k;


public:

	void revinoKPasi(int cat);
	//reseteaza pozitia iteratorului la inceputul containerului
	void prim();

	//muta iteratorul in container
	// arunca exceptie daca iteratorul nu e valid
	void urmator();

	//verifica daca iteratorul e valid (indica un element al containerului)
	bool valid() const;

	//returneaza valoarea elementului din container referit de iterator
	//arunca exceptie daca iteratorul nu e valid
	TElem element() const;

	// destructorul 	
	~IteratorMDO();
};

