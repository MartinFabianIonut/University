#include "IteratorMDO.h"
#include "MDO.h"
#include <exception>

PNod IteratorMDO::Clone(PNod list) {
	/*Complexitate Teta(x) - x dim listei dispersate la valoarea PNod-ului trimis ca parametru*/
	if (list == NULL) return NULL;
	PNod result = new Nod(list->e,NULL);
	result->e = list->e;
	result->urm = Clone(list->urm);
	return result;
}

void IteratorMDO::interclaseazaListe() {
	//gaseste prima lista nevida incepand cu locatia poz din tabela
	/*Complexitate Teta(n*m) - conform studiului valorii medii de dispersie uniforma*/
	while (poz < dict.m && dict.l[poz] == nullptr) {
		poz++;
	}
	int poz2 = poz+1;
	if (poz < dict.m) {
		primul = Clone(dict.l[poz]);

		while (poz2 < dict.m) {
			if (dict.l[poz2] != nullptr) {
				PNod a = primul;
				PNod b = Clone(dict.l[poz2]);
				primul = SortedMerge(a, b);
				//delete a; delete b;
			}
			poz2++;
		}
	}
	else {
		primul = NULL;
	}
}

IteratorMDO::IteratorMDO(const MDO& d) : dict(d) {
	/*Complexitate Teta(n*m) */
	k = 0;
	interclaseazaListe();
	curent = primul;
	if (curent != nullptr) { 
		ptiterare.push_back(curent->e); 
		while (curent->urm != nullptr) {
			ptiterare.push_back(curent->urm->e);
			curent = curent->urm;
		}
	}
}

void IteratorMDO::prim() {
	/*Complexitate Teta(1)*/
	k = 0;
	curent = primul;

}

void IteratorMDO::revinoKPasi(int cat)
{
	/*Complexitate Teta(1)*/
	if (cat > k) {
		throw exception("prea multi pasi");
	}
	if (cat <= 0) {
		throw exception("zero");
	}
	k -= cat;
}

/*
subalgoritm revinoKPasi(int cat)
	pre: it iterator, k - pozitia curenta
	post: k' - se schimba pozitia curenta daca se poate reveni cu cat pasi,
		altfel se arunca exceptie - exceptie daca sunt prea multi pasi, sau <=0

	daca cat > k atunci
		@exceptie prea mult
	sfdaca
	daca cat <= 0 atunci
		@exceptie <=0
	sfdaca
	k <- k - cat
sfsubalgoritm
*/

void IteratorMDO::urmator() {
	/*Complexitate Teta(1)*/
	k++;

}

bool IteratorMDO::valid() const {
	/*Complexitate Teta(1)*/
	return (k >= 0 && k < ptiterare.size());
}

TElem IteratorMDO::element() const {
	/*Complexitate Teta(1)*/

	return ptiterare[k];
}

PNod IteratorMDO::SortedMerge(PNod a, PNod b)
{
	/*Complexitate O(x+y) -  x si y dim a si b */
	/* a dummy first node to hang the result on */
	Nod cap2(a->e, NULL);

	/* tail points to the last result node */
	
	PNod cap = &cap2;

	/* so tail->next is the place to
	add new nodes to the result. */

	while (1){
		if (a == NULL){
			/* if either list runs out, use the
			other list */
			cap->urm = b;
			break;
		}
		else if (b == NULL){
			cap->urm = a;
			break;
		}
		if (dict.rel(a->e.first, b->e.first))
			MoveNode(&(cap->urm), &a);
		else
			MoveNode(&(cap->urm), &b);
		cap = cap->urm;
	}
	return(cap2.urm);
}

void  IteratorMDO::MoveNode(PNod* destRef, PNod* sourceRef){
	/*Complexitate Teta(1)*/
	/* the front source node */
	PNod newNode = *sourceRef;
	/* Advance the source pointer */
	*sourceRef = newNode->urm;
	/* Link the old dest off the new node */
	newNode->urm = *destRef;
	/* Move dest to point to the new node */
	*destRef = newNode;
}


IteratorMDO::~IteratorMDO() {
	/*Complexitate Teta(n)*/
	//se impune definirea acestui destructor pentru eliberarea spatiului alocat copierii elementelor din MDO
	//se elibereaza memoria pentru lista copiata pentru iterare
	while (primul != nullptr) {
		PNod p = primul;
		primul = primul->urm;
		delete p;
	}
}