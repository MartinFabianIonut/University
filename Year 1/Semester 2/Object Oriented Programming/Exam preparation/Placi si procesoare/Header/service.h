#pragma once
#include "repo.h"
#include "valid.h"
#include <iterator>
#include <algorithm>
using std::copy_if;
using std::sort;

class Service {
private:
	Repo& repo;
	Valid& val;
public:
	Service(Repo& re, Valid&val):repo{re},val{val}{}
	void adaugaprocesorservice(string nume, int numarT, string soclu, int pret);
	void adaugaplacaservice(string nume, string soclu, int pret);
	vector<Procesor>& get_all_procesoare_service() { return repo.get_all_procesoare(); }
	vector<Placa>& get_all_placi_service() { return repo.get_all_placi(); }

	vector<Placa> filtrare_dupa_soclu(string soclu) {
		vector<Placa> v;
		copy_if(repo.get_all_placi().begin(), repo.get_all_placi().end(), back_inserter(v), [&]( Placa& p) {return p.get_soclu() == soclu; });
		return v;
	}

};