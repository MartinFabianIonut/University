#pragma once
#include "repo.h"
#include <algorithm>
using std::vector;
using std::sort;


class Service {
private:
	Repo& repo;
public:
	Service(Repo&_repo):repo{_repo}{}
	/*functoe care incrementeaza pretul la toate cu acel brand*/
	void increment(string brand) {
		repo.incrementeaza(brand);
	}
	/*functie care da toate elementele*/
	vector<Telefon>& get_all() {
		return repo.get_all();
	}

	Telefon cauta(string& cod);

	vector<Telefon> sortbypret() {
		vector<Telefon> te = repo.get_all();

		sort(te.begin(), te.end(), [](const Telefon& t1, const Telefon& t2) {return t1.get_pret() < t2.get_pret(); });
		return te;

	}

	vector<Telefon> sortbybrand() {
		vector<Telefon> te = repo.get_all();

		sort(te.begin(), te.end(), [](const Telefon& t1, const Telefon& t2) {return t1.get_brand() < t2.get_brand(); });
		return te;
	}

	vector<Telefon> sortbymodel() {
		vector<Telefon> te = repo.get_all();
		sort(te.begin(), te.end(), [](const Telefon& t1, const Telefon& t2) {return t1.get_model() < t2.get_model(); });
		return te;
	}

};