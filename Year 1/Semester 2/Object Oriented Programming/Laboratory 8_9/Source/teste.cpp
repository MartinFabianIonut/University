#include "teste.h"
#include "pok.h"
#include <iostream>
#include <fstream>
#include <assert.h>
#include "repo.h"
#include "reponou.h"
#include "repoException.h"
#include "service.h"
#include "valid.h"

#include <sstream>

using std::cin; 
using std::cout;
using std::ofstream;
using std::ifstream;

void Teste::ruleaza_teste() {
	
	this->ruleaza_teste_domeniu();
	this->ruleaza_teste_repo();
	this->ruleaza_teste_adaugare();
	this->valid();
	this->ruleaza_teste_undo();
	this->test_adauga_repo_file();
	this->test_modifica_repo_file();
	this->test_delete_repo_file();
	this->ruleaza_teste_repo2();
	this->ruleaza_teste_adaugare2();
	this->ruleaza_teste_undo2();
	this->probabilitate100();
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
	vector<Locatar> v = ctr.getAll();
	assert(v.at(0).get_ap() == 1);
	assert(v.at(0).get_nume()=="b");
	assert(v.at(0).get_sup() == 44);
	assert(v.at(0).get_tip() == 5);
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

	vector<Locatar> sortatDupaNume = ctr.sortareDupaNume(1);
	
	assert(sortatDupaNume.at(0).get_nume() == "aa");
	assert(sortatDupaNume.at(1).get_nume() == "ac");
	assert(sortatDupaNume.at(2).get_nume() == "ad");
	assert(sortatDupaNume.at(3).get_nume() == "ae");

	vector<Locatar> sortatDupaNume2 = ctr.sortareDupaNume(2);

	assert(sortatDupaNume2.at(0).get_nume() == "ae");
	assert(sortatDupaNume2.at(1).get_nume() == "ad");
	assert(sortatDupaNume2.at(2).get_nume() == "ac");
	assert(sortatDupaNume2.at(3).get_nume() == "aa");

	vector<Locatar> sortareDupaSuprafata = ctr.sortareDupaSuprafata(1);
	assert(sortareDupaSuprafata.at(0).get_sup() == 1);
	assert(sortareDupaSuprafata.at(1).get_sup() == 5);
	assert(sortareDupaSuprafata.at(2).get_sup() == 7);
	assert(sortareDupaSuprafata.at(3).get_sup() == 8);

	vector<Locatar> sortareDupaSuprafata2 = ctr.sortareDupaSuprafata(2);
	assert(sortareDupaSuprafata2.at(0).get_sup() == 8);
	assert(sortareDupaSuprafata2.at(1).get_sup() == 7);
	assert(sortareDupaSuprafata2.at(2).get_sup() == 5);
	assert(sortareDupaSuprafata2.at(3).get_sup() == 1);

	vector<Locatar> sortareDupaTipSuprafata = ctr.sortareDupaTipSuprafata(1);
	assert(sortareDupaTipSuprafata.at(0).get_sup() == 5);
	assert(sortareDupaTipSuprafata.at(1).get_sup() == 8);
	assert(sortareDupaTipSuprafata.at(2).get_sup() == 1);
	assert(sortareDupaTipSuprafata.at(3).get_sup() == 7);

	vector<Locatar> sortareDupaTipSuprafata2 = ctr.sortareDupaTipSuprafata(2);
	assert(sortareDupaTipSuprafata2.at(0).get_sup() == 7);
	assert(sortareDupaTipSuprafata2.at(1).get_sup() == 1);
	assert(sortareDupaTipSuprafata2.at(2).get_sup() == 8);
	assert(sortareDupaTipSuprafata2.at(3).get_sup() == 5);

	ctr.stergeLocatar(10003);
	assert(ctr.getAll().size() == 3);


	ctr.adaugaLocatar(10008, "mam", 12, 2);
	ctr.adaugaLocatar(10009, "beb", 4, 3);
	assert(ctr.getAll().at(0) != ctr.getAll().at(1));
	assert(ctr.getAll().size() == 5);
	ctr.undo();
	assert(ctr.getAll().size() == 4);
	ctr.stergeLocatar(10008);
	assert(ctr.getAll().size() == 3);
	ctr.undo();
	assert(ctr.getAll().size() == 4);
	ctr.modificaLocatar(10008, "tat", 34, 1);
	assert(ctr.getAll().at(3).get_nume() == "tat");
	ctr.undo();
	assert(ctr.getAll().at(3).get_nume() == "mam");

	ctr.adaugaLocatarDoarAp(5);
	assert(ctr.getAll().size() == 9);
	assert(ctr.adaugaLocatariInLista(20) == 1);
	ctr.adaugaLocatarDupaApInLista(10008);
	assert(ctr.adaugaLocatariInLista(1) == 0);
	assert(ctr.getAllNotificari().size() == 2);
	try {
		ctr.adaugaLocatarDupaApInLista(345);
	}
	catch (const RepoException& re) {
		assert(re.get_message() == "nu este asa locatar");
	}
	ctr.adaugaLocatariInLista(7);
	try {
		ctr.adaugaLocatarDupaApInLista(10008);
	}
	catch (const RepoException& re) {
		assert(re.get_message() == "deja exista");
	}
	ctr.stergeListaComplet();
	assert(ctr.getAllNotificari().size() == 0);
	try {
		ctr.stergeListaComplet();
	}
	catch (const RepoException& re) {
		assert(re.get_message() == "deja gol!\n");
	}
	ctr.adaugaLocatarDoarAp(31);
	assert(ctr.getAll().size() == 40);
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

void Teste::ruleaza_teste_undo()
{
	Repo repo;
	LocatarValidator val;
	Service ctr{ repo,val };
	ctr.adaugaLocatar(1, "a", 7, 6);
	ctr.undo();
	try {
		ctr.undo();
	}
	catch (const RepoException& u) {

		std::stringstream sout;
		sout << u;
		auto const mesaj = sout.str();
		assert(mesaj.find("\nNu mai sunt actiuni pentru care sa se faca undo\n") >= 0);
		assert(u.get_message() == "\nNu mai sunt actiuni pentru care sa se faca undo\n");
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

void Teste::test_adauga_repo_file() {
	try {
		RepoFile repoinexistent{ "nuuu.txt" };}
	catch (const RepoException& re) {
		assert(re.get_message() == "Fisierul nu a putut fi deschis: nuuu.txt");
	}
	RepoFile repoFile{ "testFile.txt" };
	assert(repoFile.getAll().size() == 1);
	Locatar l{ 2,"b",3,4 };
	repoFile.adauga(l);
	assert(repoFile.getAll().size() == 2);
	string path = "testFile.txt";
	ofstream fout(path);
	fout << "1 " << "a " << "3 " << 4;
	fout.close();
}


void Teste::test_modifica_repo_file() {
	Locatar l1{ 100,"b",3,4 };
	Locatar l2{ 1,"b",3,4 };

	RepoFile repoFile{ "testFile.txt" };

	repoFile.modifica(l2);
	vector<Locatar>list = repoFile.getAll();
	assert(list.size() == 1);
	assert(list.at(0).get_nume() == "b");
	try {
		repoFile.modifica(l1);
	}
	catch (const RepoException& re) {
		assert(re.get_message() == "element inexistent!\n");
	}
	ofstream fout("testFile.txt");
	fout << "1 " << "a " << "3 " << 4;
	fout.close();

}
void Teste::test_delete_repo_file() {
	Locatar l1{ 3,"c",4,5 };
	Locatar l2{ 2,"b",3,4 };

	RepoFile repoFile{ "testFile.txt" };

	repoFile.adauga(l1);
	repoFile.adauga(l2);

	try {
		repoFile.sterge_dupa_id(5);
	}
	catch (const RepoException& re) {
		assert(re.get_message() == "element inexistent!\n");
	}
	repoFile.sterge_dupa_id(3);
	vector<Locatar>list = repoFile.getAll();
	assert(list.size() == 2);
	assert(list.at(0).get_nume() == "a");
	assert(list.at(1).get_nume() == "b");

	ofstream fout("testFile.txt");
	fout << "1 " << "a " << "3 " << 4;
	fout.close();
}





/// teste pentru repo nou





void Teste::ruleaza_teste_repo2()
{
	RepoNou repo{0};
	vector<Locatar>&locatarii = repo.getAll();
	assert(locatarii.size() == 0);
	Locatar l(23, "Pikachu", 900.1, 2);
	delete& locatarii;
	repo.adauga(l);
	vector<Locatar>& locatarii2 = repo.getAll();
	assert(locatarii2.size() == 1);
	try {
		repo.adauga(l);
		//assert(false);
	}
	catch (const RepoException& re) {
		assert(re.get_message() == "\nExceptie aruncata din cauza probabilitatii!\n");
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
	delete& locatarii2;
}

void Teste::ruleaza_teste_adaugare2()
{
	RepoNou repo;
	LocatarValidator val;
	Service ctr{ repo,val };
	ctr.adaugaLocatar(1, "a", 7, 6);
	ctr.adaugaLocatar(10001, "ad", 8, 3);
	ctr.adaugaLocatar(10002, "ac", 1, 6);
	ctr.adaugaLocatar(10003, "aa", 5, 1);
	ctr.adaugaLocatar(10004, "ae", 7, 6);
	vector<Locatar>& locatarii = repo.getAll();
	assert(locatarii.size() == 5);
	delete& locatarii;
	//adaug ceva invalid
	try {
		ctr.adaugaLocatar(-2, "", 33, -1);
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
		ctr.stergeLocatar(3);
	}
	catch (const RepoException& re) {
		std::stringstream sout;
		sout << re;
		auto const mesaj = sout.str();
		assert(mesaj.find("element inexistent!") >= 0);
	}
	ctr.modificaLocatar(1, "b", 44, 5);
	vector<Locatar>& v = ctr.getAll();
	assert(v.at(0).get_ap() == 1);
	assert(v.at(0).get_nume() == "b");
	assert(v.at(0).get_sup() == 44);
	assert(v.at(0).get_tip() == 5);
	delete& v;
}

void Teste::probabilitate100() {
	RepoNou repo{ 100 };
	LocatarValidator val;
	Service ctr{ repo,val };
	Locatar l{ 1,"a",2,3 };
	try {
		ctr.adaugaLocatar(9, "a", 44, 11);
	}
	catch (RepoException&) {
		assert(true);
	}
	try {
		ctr.modificaLocatar(2, "b", 44, 5);
	}
	catch (RepoException&) {
		assert(true);
	}
	try {
		ctr.stergeLocatar(3);
	}
	catch (RepoException&) {
		assert(true);
	}
	try {
		ctr.cautaLocatar(1);
	}
	catch (RepoException&) {
		assert(true);
	}
}

void Teste::ruleaza_teste_undo2()
{
	RepoNou repo{0};
	LocatarValidator val;
	Service ctr{ repo,val };
	ctr.adaugaLocatar(1, "a", 7, 6);
	ctr.undo();
	try {
		ctr.undo();
	}
	catch (const RepoException& u) {

		std::stringstream sout;
		sout << u;
		auto const mesaj = sout.str();
		assert(mesaj.find("\nNu mai sunt actiuni pentru care sa se faca undo\n") >= 0);
		assert(u.get_message() == "\nNu mai sunt actiuni pentru care sa se faca undo\n");
	}
}
