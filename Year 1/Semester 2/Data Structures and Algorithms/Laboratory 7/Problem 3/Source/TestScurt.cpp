#include "TestScurt.h"
#include "Colectie.h"
#include "IteratorColectie.h"
#include <assert.h>

void test2() {
	Colectie c;
	c.adauga(2); c.adauga(3); c.adauga(4);
	c.sterge(2);
	c.adauga(2); c.adauga(2); c.adauga(2);
	c.adauga(3); c.adauga(3); c.adauga(3);
	c.sterge(4);
	c.adauga(4); c.adauga(4); c.adauga(4);
	assert(c.sterge(2) == true);
	assert(c.sterge(2) == true);
	assert(c.sterge(2) == true);
	assert(c.sterge(2) == false);
}

void testNou() {
	Colectie c;
	assert(c.dim() == 0);
	assert(c.stergeToateElementeleRepetitive() == 0);
	c.adauga(2); c.adauga(3); c.adauga(4); c.adauga(5); c.adauga(0);
	c.adauga(2); c.adauga(2); c.adauga(2);
	c.adauga(3); c.adauga(3); c.adauga(3);
	assert(c.dim() == 11);
	assert(c.stergeToateElementeleRepetitive() == 8);
	assert(c.dim() == 3);
}

void testAll() {
	Colectie c;
	c.adauga(5);
	c.adauga(6);
	c.adauga(0);
	c.adauga(5);
	c.adauga(10);
	c.adauga(8);

	assert(c.dim() == 6);
	assert(c.nrAparitii(5) == 2);


	assert(c.sterge(5) == true);
	assert(c.dim() == 5);

	assert(c.cauta(6) == true);
	assert(c.vida() == false);

	IteratorColectie ic = c.iterator();
	assert(ic.valid() == true);
	while (ic.valid()) {
		ic.element();
		ic.urmator();
	}
	assert(ic.valid() == false);
	ic.prim();
	assert(ic.valid() == true);
	test2();
	testNou();
}
