#pragma once

#include "domeniu.h"
using std::ostream;

class RepoException : public std::exception {
private:
	string mesaj;
public:
	RepoException(const string& _mesaj) :mesaj{ _mesaj } {}
	string& get_mesaj() { return mesaj; }
	friend ostream& operator<<(ostream& g, RepoException& re) { g << re.get_mesaj(); return g; }
};

class Repo {
protected:
	vector<Telefon>telefoane;
public:
	Repo() = default;
	virtual vector<Telefon>& get_all() { return telefoane; }
	/*funcite care adauga*/
	virtual void adauga(Telefon& t);
	/*funcite care incrementeaza*/
	virtual void incrementeaza(string brand);
};

class RepoFile : public Repo {

private:
	string path;
	void read();
	void write();
public:
	RepoFile(string _path) : Repo(), path{ _path }{read(); }
	void adauga(Telefon& t)override {
		read();
		Repo::adauga(t);
		write();
	}
	/*funcite care incrementeaza*/
	void incrementeaza(string brand)override {
		read();
		Repo::incrementeaza(brand);
		write();
	}
	vector<Telefon>& get_all() override {
		//read();
		return Repo::get_all();
	}
};