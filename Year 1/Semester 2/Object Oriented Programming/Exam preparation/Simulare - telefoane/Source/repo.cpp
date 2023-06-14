#include "repo.h"
#include <fstream>
using std::ifstream;
using std::ofstream;

void Repo::adauga(Telefon& t)
{
	auto it = find(telefoane.begin(), telefoane.end(), t);
	if (it != telefoane.end()) {
		throw RepoException("Mai exista acest procesor!");
	}
	telefoane.push_back(t);
}

void Repo::incrementeaza(string brand)
{
	auto it = find_if(telefoane.begin(), telefoane.end(), [&](Telefon& t) {return t.get_brand() == brand; });
	bool ex = false;
	while (it != telefoane.end()) {
		Telefon t = *it;
		string cod, model;
		int pret;
		cod = t.get_cod(); model = t.get_model(); pret = t.get_pret();
		pret += 10;
		Telefon mod{ cod,brand,model,pret };
		*it = mod;
		ex = true;
		it = find_if(it+1, telefoane.end(), [&](Telefon& t) {return t.get_brand() == brand; });
		//throw RepoException("Bun!");
	}
	if (!ex) {
		throw RepoException("Nu exista cu asa brand!");
	}
}

void RepoFile::read()
{
	ifstream f(path);
	if (!f.is_open()) {
		throw RepoException("Nu am putut deschide fisierul de la calea "+path+" !");
	}
	string cod, brand, model;
	int pret;
	Repo::telefoane.clear();
	while (!f.eof()) {
		f >> cod >> brand >> model >> pret;
		Telefon nou{ cod,brand,model,pret };
		Repo::adauga(nou);
	}
	f.close();
}


void RepoFile::write()
{
	ofstream f(path);
	if (!f.is_open()) {
		throw RepoException("Nu am putut deschide fisierul de la calea " + path + " !");
	}
	int i = 0;
	for (auto& el : Repo::telefoane) {
		f << el.get_cod() << " " << el.get_brand() << " " << el.get_model() << " " << el.get_pret();
		if (i < Repo::telefoane.size() - 1) {
			f << "\n";
		}
		i++;
	}
	f.close();
}

