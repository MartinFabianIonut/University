#include "IteratorMD.h"
#include "MD.h"

using namespace std;

IteratorMD::IteratorMD(const MD& _md) : md(_md) {
	/* de adaugat 
	Complexitate Teta(1)
	*/
	curent = _md.prim;
}

TElem IteratorMD::element() const {
	/* de adaugat 
	Complexitate Teta(1)
	*/
	TElem e = curent->element();
	return pair <TCheie, TValoare>(e.first,e.second);
}

bool IteratorMD::valid() const {
	/* de adaugat 
	Complexitate Teta(1)
	*/
	if (curent != nullptr){
		return true;
	}
	return false;
}

void IteratorMD::urmator() {
	/* de adaugat 
	Complexitate Teta(1)
	*/
	curent = curent->urmator();
}

void IteratorMD::prim() {
	/* de adaugat
	Complexitate Teta(1)
	*/
	curent = md.prim;
}

