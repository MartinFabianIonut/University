#include "teste.h"
#include "pok.h"
#include <iostream>
#include <assert.h>
#include "repo.h"
#include "repoException.h"
#include "service.h"
#include "valid.h"

#include <sstream>

using std::cin; 
using std::cout;

void Teste::ruleaza_teste() {
	
	this->ruleaza_teste_domeniu();
	this->ruleaza_teste_repo();
	this->ruleaza_teste_adaugare();
	this->valid();
}

void Teste::ruleaza_teste_repo()
{
	Repo repo;
	assert(repo.getAll().size() == 0);
	Locatar l(23, "Pikachu", 900.1,2);
	repo.adauga(l);
	assert(repo.getAll().size() == 1);
	try {
		repo.adauga(l);
		//assert(false);
	}
	catch (const RepoException& re) {
		assert(re.get_message() == "element existent!\n");
	}
	Locatar cautat(23, "", 0, 0);
	Locatar gasit = repo.cauta(cautat);
	assert(gasit == l);
	Locatar cautat1(24, "", 9, 9);
	try {
		repo.cauta(cautat1);
		//assert(false);
	}
	catch (const RepoException& re) {
		assert(re.get_message() == "element inexistent!\n");
	}
}

void Teste::ruleaza_teste_adaugare()
{
	Repo repo;
	LocatarValidator val;
	Service ctr{ repo,val };
	ctr.adaugaLocatar(1,"a", 7, 6);
	ctr.adaugaLocatar(10001, "ad", 8, 3);
	ctr.adaugaLocatar(10002, "ac", 1, 6);
	ctr.adaugaLocatar(10003, "aa", 5, 1);
	ctr.adaugaLocatar(10004, "ae", 7, 6);
	assert(ctr.getAll().size() == 5);

	//adaug ceva invalid
	try {
		ctr.adaugaLocatar(-2,"", 33, -1);
	}
	catch (ValidateException&) {
		assert(true);
	}
	//incerc sa adaug ceva ce existadeja
	try {
		ctr.adaugaLocatar(9, "a", 44, -1);
	}
	catch (ValidateException&) {
		assert(true);
	}
	try {
		ctr.modificaLocatar(2, "b", 44, 5);
	}
	catch (RepoException&) {
		assert(true);
	}
	try {
		ctr.cautaLocatar(53);
	}
	catch (RepoException&) {
		assert(true);
	}
	try {
		ctr.stergeLocatar(3);
	}
	catch (const RepoException& re) {
		std::stringstream sout;
		sout << re;
		auto const mesaj = sout.str();
		assert(mesaj.find("element inexistent!") >= 0);
	}
	ctr.modificaLocatar(1, "b", 44, 5);
	VectorDinamic<Locatar> v = ctr.getAll();
	assert(v.get(0).get_ap() == 1);
	assert(v.get(0).get_nume()=="b");
	assert(v.get(0).get_sup() == 44);
	assert(v.get(0).get_tip() == 5);
	v = ctr.locatari_dupa_tip(6);
	assert(v.size() == 2);
	v = ctr.locatari_dupa_suprafata(6,10);
	assert(v.size() == 2);
	ctr.stergeLocatar(1);
	assert(ctr.getAll().size() == 4);
	//test cauta
	Locatar gasit = ctr.cautaLocatar(10003);
	assert(gasit.get_ap() == 10003);
	assert(gasit.get_nume() == "aa");
	assert(gasit.get_sup() == 5);
	assert(gasit.get_tip() == 1);

	VectorDinamic<Locatar> sortatDupaNume = ctr.sortareDupaNume(1);
	
	assert(sortatDupaNume.get(0).get_nume() == "aa");
	assert(sortatDupaNume.get(1).get_nume() == "ac");
	assert(sortatDupaNume.get(2).get_nume() == "ad");
	assert(sortatDupaNume.get(3).get_nume() == "ae");

	VectorDinamic<Locatar> sortatDupaNume2 = ctr.sortareDupaNume(2);

	assert(sortatDupaNume2.get(0).get_nume() == "ae");
	assert(sortatDupaNume2.get(1).get_nume() == "ad");
	assert(sortatDupaNume2.get(2).get_nume() == "ac");
	assert(sortatDupaNume2.get(3).get_nume() == "aa");

	VectorDinamic<Locatar> sortareDupaSuprafata = ctr.sortareDupaSuprafata(1);
	assert(sortareDupaSuprafata.get(0).get_sup() == 1);
	assert(sortareDupaSuprafata.get(1).get_sup() == 5);
	assert(sortareDupaSuprafata.get(2).get_sup() == 7);
	assert(sortareDupaSuprafata.get(3).get_sup() == 8);

	VectorDinamic<Locatar> sortareDupaSuprafata2 = ctr.sortareDupaSuprafata(2);
	assert(sortareDupaSuprafata2.get(0).get_sup() == 8);
	assert(sortareDupaSuprafata2.get(1).get_sup() == 7);
	assert(sortareDupaSuprafata2.get(2).get_sup() == 5);
	assert(sortareDupaSuprafata2.get(3).get_sup() == 1);

	VectorDinamic<Locatar> sortareDupaTipSuprafata = ctr.sortareDupaTipSuprafata(1);
	assert(sortareDupaTipSuprafata.get(0).get_sup() == 5);
	assert(sortareDupaTipSuprafata.get(1).get_sup() == 8);
	assert(sortareDupaTipSuprafata.get(2).get_sup() == 1);
	assert(sortareDupaTipSuprafata.get(3).get_sup() == 7);

	VectorDinamic<Locatar> sortareDupaTipSuprafata2 = ctr.sortareDupaTipSuprafata(2);
	assert(sortareDupaTipSuprafata2.get(0).get_sup() == 7);
	assert(sortareDupaTipSuprafata2.get(1).get_sup() == 1);
	assert(sortareDupaTipSuprafata2.get(2).get_sup() == 8);
	assert(sortareDupaTipSuprafata2.get(3).get_sup() == 5);

	ctr.stergeLocatar(10003);
	assert(ctr.getAll().size() == 3);


}

void Teste::valid()
{
	LocatarValidator v;
	Locatar l{ -4,"",-6 ,-1 };
	try {
		v.validate(l);
	}
	catch (const ValidateException& ex) {
		std::stringstream sout;
		sout << ex;
		auto const mesaj = sout.str();
		assert(mesaj.find("negativ") >= 0);
		assert(mesaj.find("vid") >= 0);
	}
}

void Teste::ruleaza_teste_domeniu() {
	constexpr int id = 23;
	string nume = "Pikachu";
	constexpr int nivel = 2;
	constexpr double putere = 900.1;
	Locatar p1(id, nume, putere, nivel);
	Locatar p2(p1);
	assert(p2.get_nume() == p1.get_nume());
	p2.set_nume("Raichu");
	assert(p2.get_nume() == "Raichu");
	assert(p1.get_nume() == "Pikachu");
	Locatar p3 = p2;
}