#include "repo.h"
#include <fstream>
using std::ifstream;
using std::ofstream;

void Repo::adaugaprocesor(Procesor& p)
{
	auto it = find(procesoare.begin(), procesoare.end(), p);
	if (it != procesoare.end()) {
		throw RepoException("Mai exista acest procesor!");
	}
	procesoare.push_back(p);
}

void Repo::adaugaplaca(Placa& p)
{
	auto it = find(placi.begin(), placi.end(), p);
	if (it != placi.end()) {
		throw RepoException("Mai exista aceasta placa!");
	}
	placi.push_back(p);
}

void RepoFile::read()
{
	ifstream f1(path1);
	if (!f1.is_open()) {
		throw RepoException("Nu am putut deschide fisierul de la calea: " + path1);
	}
	string nume;
	int numarT;
	string soclu;
	int pret;
	Repo::procesoare.clear();
	while (!f1.eof()) {
		f1 >> nume >> numarT >> soclu >> pret;
		Procesor p{ nume,numarT,soclu,pret };
		Repo::adaugaprocesor(p);
	}
	f1.close();
	Repo::placi.clear();
	ifstream f2(path2);
	if (!f2.is_open()) {
		throw RepoException("Nu am putut deschide fisierul de la calea: " + path2);
	}
	while (!f2.eof()) {
		f2 >> nume >> soclu >> pret;
		Placa p{ nume,soclu,pret };
		Repo::adaugaplaca(p);
	}
	f2.close();
}

void RepoFile::write()
{
	ofstream f1(path1);
	if (!f1.is_open()) {
		throw RepoException("Nu am putut deschide fisierul de la calea: " + path1);
	}
	int i;
	i = 0;
	 
	for(auto&p : Repo::procesoare) {
		f1 << p.get_nume() << " " << p.get_nr() << " " << p.get_soclu() << " " << p.get_pret();
		if (i < Repo::procesoare.size() - 1) { f1 << "\n"; }
		i++;
	}
	f1.close();
	ofstream f2(path2);
	i = 0;
	
	for (auto& p : Repo::placi) {
		f2 << p.get_nume() << " " << p.get_soclu() << " " << p.get_pret();
		if (i < Repo::placi.size() - 1) { f2 << "\n"; }
		i++;
	}
	f2.close();
}
