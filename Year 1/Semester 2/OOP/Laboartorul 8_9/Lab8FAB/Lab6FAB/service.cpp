#include "service.h"
#include <algorithm>
#include <iterator>
#include <random>
#include <chrono> 
using std::copy_if;
using std::back_inserter;
using std::find;

#include <iostream>
#include <string>

using std::cout;
Service::Service(RepoAbstract& _repo, LocatarValidator& _val) noexcept :
	repo{ _repo }, val {_val}{}


void Service::adaugaLocatar(const int& ap, const string& nume, const double& sup, const int& tip)
{
	Locatar l{ ap,nume,sup,tip };
	//cout << "inainte val";
	val.validate(l);
	//cout << "dupa val";
	repo.adauga(l);
	//cout << "dupa repoad";
	undoAtions.push_back(std::make_unique<UndoAdauga>(repo, ap));
}


string randomName(int length) {
	vector<char> consonents { 'b','c','d','f','g','h','j','k','l','m','n','p','q','r','s','t','v','w','x','z' };
	vector<char> vowels { 'a','e','i','o','u','y' };
	string name = "";
	int random = rand() % 2;
	int count = 0;
	for (int i = 0; i < length; i++) {
		if (random < 2 && count < 2) {
			name = name + consonents.at(rand() % 19);
			count++;
		}
		else {
			name = name + vowels.at(rand() % 5);
			count = 0;
		}
		random = rand() % 2;
	}
	return name;
}

void Service::adaugaLocatarDoarAp(const int& cate_ap)
{
	std::mt19937 mt{ std::random_device{}() };
	const std::uniform_int_distribution<> dist(0, cate_ap + 100);
	const std::uniform_int_distribution<> distlit(41, 122);
	const std::uniform_int_distribution<> distsup(13,120);
	const std::uniform_int_distribution<> disttip(1,6);
	int ap=-1, tip=1;
	double sup=1;
	string nume;
	bool reusit;
	int k = 0; 
	while (k < cate_ap) {
		reusit = true;
		nume = "Nume";
		ap = dist(mt);
		nume = randomName(10);
		tip = disttip(mt);
		sup = distsup(mt);
		try {
			adaugaLocatar(ap, nume, sup, tip);
			k++;
		}
		catch (RepoException&) {
			continue;}
	}
}

int Service::adaugaLocatariInLista(const int& cati) {
	vector<Locatar>&toti = repo.getAll();
	int dim = 0;
	for (int i = 1; i <= toti.size(); i++) {
		dim++;
	}
	if (cati >dim) {
		return 1;
	}
	std::mt19937 mt{ std::random_device{}() };
	
	const std::uniform_int_distribution<> dist(0, dim-1);
	int k = 0;
	while (k < cati) {
		const int rdm = dist(mt);
		Locatar l = toti.at(rdm);
		auto it = find(notificare.begin(), notificare.end(), l);
		if (it == notificare.end()) {
			notificare.push_back(l);
			k++;
		}
	}
	return 0;
}

void Service::adaugaLocatarDupaApInLista(const int& ap) {
	auto it = find_if(notificare.begin(), notificare.end(), [&](const Locatar& l) noexcept { return l.get_ap() == ap; });
	if (it == notificare.end()) {
		vector<Locatar>toti = repo.getAll();
		auto itperepo = find_if(toti.begin(), toti.end(), [&](const Locatar& l) noexcept { return l.get_ap() == ap; });
		if (itperepo != toti.end()) {
			notificare.push_back(*itperepo);
		}
		else {
			throw RepoException("nu este asa locatar");
		}
	}
	else {
		throw RepoException("deja exista");
	}
}

void Service::stergeListaComplet()
{
	if (notificare.size() == 0) {
		throw RepoException("deja gol!\n");
	}
	notificare.clear();
}

vector<Locatar>& Service::getAllNotificari() noexcept
{
	return notificare;
}

void Service::stergeLocatar(int ap)
{
	Locatar l = repo.sterge_dupa_id(ap);
	undoAtions.push_back(std::make_unique<UndoSterge>(repo, l));
}


void Service::modificaLocatar(const int& ap, const string& nume, const double& sup, const int& tip)
{
	Locatar l{ ap,nume,sup,tip };
	val.validate(l);
	Locatar inainte = repo.modifica(l);
	undoAtions.push_back(std::make_unique<UndoModifica>(repo, inainte));
}

Locatar Service::cautaLocatar(const int& ap)
{
	vector<Locatar>& toti=repo.getAll();
	for (int i = 0; i < toti.size(); i++) {
		if (toti.at(i).get_ap() == ap) {
			return toti.at(i);
		}
	}
	throw RepoException("nu exista!\n");
}

void Service::undo()
{
	if (undoAtions.empty()) {
		throw RepoException("\nNu mai sunt actiuni pentru care sa se faca undo\n");
	}
	undoAtions.back()->doUndo();
	undoAtions.pop_back();
}

vector<Locatar> Service::locatari_dupa_tip(int tip)
{
	vector<Locatar>res;
	copy_if(repo.getAll().begin(), repo.getAll().end(), back_inserter(res), [&](const Locatar& p) noexcept {return p.get_tip() == tip; } );
	return res;
}

vector<Locatar> Service::locatari_dupa_suprafata(double supi, double sups)
{
	vector<Locatar>res;
	copy_if(repo.getAll().begin(), repo.getAll().end(), back_inserter(res), [&](const Locatar& p) noexcept {return p.get_sup() >= supi && p.get_sup() <= sups; });
	return res;
}



vector<Locatar> Service::generalSort(bool(*maiMicF)(const Locatar&, const Locatar&)) {
	vector<Locatar> v = repo.getAll() ;
	for (int i = 0; i < v.size(); i++) {
		for (int j = i + 1; j < v.size(); j++) {
			if (!maiMicF(v.at(i), v.at(j))) {
				Locatar aux = v.at(i);
				v.at(i) = v.at(j);
				v.at(j) = aux;
			}
		}
	}
	return v;
}

vector<Locatar> Service::sortareDupaNume(int x) {
	if (x == 1) {
		return generalSort(cmpNume);
	}
	return generalSort(cmpNume2);
}

vector<Locatar> Service::sortareDupaSuprafata(int x) {
	if (x == 1) {
		return generalSort(cmpSuprafata);
	}
	return generalSort(cmpSuprafata2);
}

vector<Locatar> Service::sortareDupaTipSuprafata(int x) {
	if (x == 1) {
		return generalSort(cmpTipSuprafata);
	}
	return generalSort(cmpTipSuprafata2);
}

Service::~Service()
{
}
