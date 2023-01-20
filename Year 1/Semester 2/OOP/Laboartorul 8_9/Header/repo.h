#ifndef REPO_H
#define REPO_H

#include <vector>
#include <map>
#include "repoException.h"
#include "pok.h"
//#include "vectordinamic.h"
using std::vector;
using std::map;


class RepoAbstract {
public:
	virtual void adauga(const Locatar&) = 0;
	virtual Locatar sterge_dupa_id(int) = 0;
	virtual Locatar modifica(const Locatar&) = 0;
	virtual const Locatar& cauta(const Locatar&) = 0;
	virtual vector<Locatar>& getAll() = 0;
	virtual ~RepoAbstract() = default;
};


class Repo: public RepoAbstract {
protected:
	vector<Locatar> locatari;
public:
	Repo() noexcept;

	 void adauga(const Locatar&) override;
	 Locatar sterge_dupa_id(int) override;
	 Locatar modifica(const Locatar&) override;
	 const Locatar& cauta(const Locatar&) override;
	 vector<Locatar>& getAll() noexcept override;


	~Repo();
};


class RepoFile : public Repo {



protected:
	string pathName;
	void readAllFromFile();
	void printAllToFile();



public:
	RepoFile(string pathName) : Repo(), pathName{ pathName } {
		readAllFromFile();
	}


	void adauga(const Locatar& l) override {
		readAllFromFile();
		Repo::adauga(l);
		printAllToFile();
	}


	Locatar sterge_dupa_id(int ap) override {
		readAllFromFile();
		Locatar sters = Repo::sterge_dupa_id(ap);
		printAllToFile();
		return sters;
	}
	Locatar modifica(const Locatar& l) override {
		readAllFromFile();
		Locatar modificat = Repo::modifica(l);
		printAllToFile();
		return modificat;
	}

	vector<Locatar>& getAll() noexcept override {
		return Repo::getAll();
	}

	int length() noexcept {
		return Repo::getAll().size();
	}
};




#endif