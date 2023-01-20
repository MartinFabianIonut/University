#include "repo.h"
#include <fstream>
#include <sstream>
#include <algorithm>
using std::ifstream;
using std::ofstream;


void Repo::read()
{
	ifstream f(path);
	if (!f.is_open()) {
		throw RepoException("Nu am putut deschide fisierul de la calea " + path + " !");
	}
	int id;
	string nume, tip;
	double pret;
	Repo::produse.clear();
	while (!f.eof()) {
		f >> id >> nume >> tip >> pret;
		Produs nou{ id , nume , tip , pret };
		Repo::adauga(nou);
	}
	f.close();
}


void Repo::write()
{
	ofstream f(path);
	if (!f.is_open()) {
		throw RepoException("Nu am putut deschide fisierul de la calea " + path + " !");
	}
	int i = 0;
	for (auto& el : Repo::produse) {
		f << el.get_id() << " " << el.get_nume() << " " << el.get_tip() << " " << el.get_pret();
		if (i < Repo::produse.size() - 1) {
			f << "\n";
		}
		i++;
	}
	f.close();
}



void Repo::adauga(Produs& p)
{
	read();
	auto it = find_if(this->produse.begin(), this->produse.end(), [&](const Produs& pro) noexcept { return pro.get_id() == p.get_id(); });
	if (it != produse.end()) {
		throw RepoException("Mai exista!\n");
	}
	produse.push_back(p);
	write();
}

int Repo::size()
{
	return (int)produse.size();
}

vector<Produs> Repo::get_all()
{
	return produse;
}