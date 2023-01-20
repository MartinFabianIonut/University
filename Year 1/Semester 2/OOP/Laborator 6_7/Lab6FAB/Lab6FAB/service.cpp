#include "service.h"
#include <algorithm>
#include <iterator>
using std::copy_if;
using std::back_inserter;
#include <iostream>
#include <string>

using std::cout;
Service::Service(Repo& _repo, LocatarValidator& _val) noexcept :
	repo{ _repo }, val {_val}{}


void Service::adaugaLocatar(const int& ap, const string& nume, const double& sup, const int& tip)
{
	Locatar l{ ap,nume,sup,tip };
	//cout << "inainte val";
	val.validate(l);
	//cout << "dupa val";
	repo.adauga(l);
	//cout << "dupa repoad";
}

void Service::stergeLocatar(int ap)
{
	repo.sterge_dupa_id(ap);
}

void Service::modificaLocatar(const int& ap, const string& nume, const double& sup, const int& tip)
{
	Locatar l{ ap,nume,sup,tip };
	val.validate(l);
	repo.modifica(l);
}

Locatar Service::cautaLocatar(const int& ap)
{
	VectorDinamic<Locatar>& toti=repo.getAll();
	for (int i = 0; i < toti.size(); i++) {
		if (toti.get(i).get_ap() == ap) {
			return toti.get(i);
		}
	}
	throw RepoException("nu exista!\n");
}

//const Locatar& Service::cauta_dupa_id(int ap)
//{
//	
//	auto it = find(this->locatari.begin(), this->locatari.end(), p);
//	if (it != locatari.end()) {
//		return *it;
//	}
//	throw RepoException("element inexistent!\n");
//}


VectorDinamic<Locatar> Service::locatari_dupa_tip(int tip)
{
	VectorDinamic<Locatar>res;
	copy_if(repo.getAll().begin(), repo.getAll().end(), back_inserter(res), [&](const Locatar& p) noexcept {return p.get_tip() == tip; } );
	return res;
}

VectorDinamic<Locatar> Service::locatari_dupa_suprafata(double supi, double sups)
{
	VectorDinamic<Locatar>res;
	copy_if(repo.getAll().begin(), repo.getAll().end(), back_inserter(res), [&](const Locatar& p) noexcept {return p.get_sup() >= supi && p.get_sup() <= sups; });
	return res;
}



VectorDinamic<Locatar> Service::generalSort(bool(*maiMicF)(const Locatar&, const Locatar&)) {
	VectorDinamic<Locatar> v = repo.getAll() ;
	for (int i = 0; i < v.size(); i++) {
		for (int j = i + 1; j < v.size(); j++) {
			if (!maiMicF(v.get(i), v.get(j))) {
				Locatar aux = v.get(i);
				v.get(i) = v.get(j);
				v.get(j) = aux;
			}
		}
	}
	return v;
}

VectorDinamic<Locatar> Service::sortareDupaNume(int x) {
	if (x == 1) {
		return generalSort(cmpNume);
	}
	return generalSort(cmpNume2);
}

VectorDinamic<Locatar> Service::sortareDupaSuprafata(int x) {
	if (x == 1) {
		return generalSort(cmpSuprafata);
	}
	return generalSort(cmpSuprafata2);
}

VectorDinamic<Locatar> Service::sortareDupaTipSuprafata(int x) {
	if (x == 1) {
		return generalSort(cmpTipSuprafata);
	}
	return generalSort(cmpTipSuprafata2);
}

//Service::~Service()
//{
//}
