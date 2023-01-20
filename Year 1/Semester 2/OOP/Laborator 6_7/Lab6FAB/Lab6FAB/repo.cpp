#include "repo.h"
#include "repoException.h"
#include <algorithm>
using std::find;
using std::find_if;



Repo::Repo() noexcept
{
}


bool Repo::exist(const Locatar& p) {
	try {
		myfind(p.get_ap());
		return true;
	}
	catch (RepoException&) {
		return false;
	}}
/*
Cauta
arunca exceptie daca nu exista animal
*/
const Locatar& Repo::myfind(int ap) {
	for (const auto& p : locatari) {
		if (p.get_ap() == ap) {
			return p;
		}
	}
	//daca nu exista arunc o exceptie
	throw RepoException("Nu exista ");
}


void Repo::adauga(const Locatar& p)
{
	if (exist(p)) {
		throw RepoException("element existent!\n");
	}
	locatari.add(p);
}

void Repo::sterge_dupa_id(int id)
{
	Locatar l{ id,"da",2,3 };
	//auto it = find_if(this->locatari.begin(), this->locatari.end(), [&](const Locatar& p) noexcept { return p.get_ap() == id; });
	if (exist(l)) {
		this->locatari.deleteelement(l);
		return;
	}
	throw RepoException("element inexistent!\n");}

void Repo::modifica(const Locatar& p)
{
	auto it{ find(this->locatari.begin(), this->locatari.end(),p) };
	if (it != locatari.end()) {
		
		this->locatari.set( p, it - this->locatari.begin());
		return;
	}
	throw RepoException("element inexistent!\n");}

const Locatar& Repo::cauta(const Locatar& p)
{
	auto it{ find(this->locatari.begin(), this->locatari.end(), p) };
	if (it != locatari.end()) {
		return *it;
	}
	throw RepoException("element inexistent!\n");}

VectorDinamic<Locatar>& Repo::getAll() noexcept
{
	return locatari;
}

//int Repo::size() noexcept
//{
//	const size_t  s = locatari.size();
//	const int size = int(s);
//	return size;
//}
//
//Repo::~Repo() 
//{
//}
