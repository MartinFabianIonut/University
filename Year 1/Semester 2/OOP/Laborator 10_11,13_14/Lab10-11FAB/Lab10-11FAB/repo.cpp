#include "repo.h"
#include "repoException.h"
#include <algorithm>
#include <fstream>
using std::find;
using std::find_if;
using std::ifstream;
using std::ofstream;


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
	}
}
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
	auto it{ find(this->locatari.begin(), this->locatari.end(),p) };
	if (it != locatari.end()) {
		throw RepoException("element existent!\n");
	}
	locatari.push_back(p);
}

Locatar Repo::sterge_dupa_id(int id)
{
	auto it = find_if(this->locatari.begin(), this->locatari.end(), [&](const Locatar& p) noexcept { return p.get_ap() == id; });
	if (it != this->locatari.end()) {
		Locatar l = *it;
		this->locatari.erase(it);
		return l;
	}
	throw RepoException("element inexistent!\n");
}

Locatar Repo::modifica(const Locatar& p)
{
	auto it{ find(this->locatari.begin(), this->locatari.end(),p) };
	if (it != locatari.end()) {
		Locatar l = *it;
		*it = p;
		return l;
	}
	throw RepoException("element inexistent!\n");
}

const Locatar& Repo::cauta(const Locatar& p)
{
	auto it{ find(this->locatari.begin(), this->locatari.end(), p) };
	if (it != locatari.end()) {
		return *it;
	}
	throw RepoException("element inexistent!\n");
}

vector<Locatar>& Repo::getAll() noexcept
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

void RepoFile::readAllFromFile() {
	ifstream fin(pathName);
	if (!fin.is_open()) {
		throw RepoException("Fisierul nu a putut fi deschis: " + pathName);
	}
	Repo::locatari.clear();
	while (!fin.eof()) {
		int ap;
		string nume;
		double sup;
		int tip;
		fin >> ap >> nume >> sup >> tip;
		Locatar nou{ ap, nume, sup, tip };
		Repo::adauga(nou);
	}
	fin.close();
}



void RepoFile::printAllToFile() {
	ofstream fout(pathName);
	if (!fout.is_open())
	{
		throw RepoException("Fisierul nu a putut fi deschis: " + pathName);
	}
	int index = 0;
	for (auto& l : locatari) {
		fout << l.get_ap() << " " << l.get_nume() << " " << l.get_sup() << " " << l.get_tip();
		if (index < locatari.size() - 1) fout << "\n";
		index++;
	}
	fout.close();
}


Repo::~Repo()
{

}
