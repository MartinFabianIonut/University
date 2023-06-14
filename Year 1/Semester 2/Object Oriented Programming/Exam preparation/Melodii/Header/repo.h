#pragma once

#include "domeniu.h"
using std::ostream;

class RepoException : public std::exception {
private:
	string mesaj;
public:
	RepoException(const string& _mesaj) :mesaj{ _mesaj } {}
	string& get_mesaj() { return mesaj; }
	//friend ostream& operator<<(ostream& g, RepoException& re) { g << re.get_mesaj(); return g; }
};

class Repo {
protected:
	vector<Melodie>melodii;
public:
	Repo() = default;
	virtual vector<Melodie>& get_all() { return melodii; }
	/*
	Functie care adauga o melodie
	pre: m - Melodie
	post: obiectul e adaugat, se vede si in fisier, daca nu mai exista deja,
			altfel arunca excepti
	*/
	virtual void adauga(Melodie& m);

	/*
	Functie care modifica o melodie
	pre: m - Melodie
	post: obiectul cu idul m.get_id() este modificat,
		se returneaza un obiect de tipul Melodie (melodia stearsa), cel modificat adica
			altfel, daca se doreste modificarea unui obiect cu el insusi, arunca exceptie
	*/
	virtual Melodie modifica(const Melodie& m);

	/*
	Functie care sterge o melodie dupa id
	pre: id - int
	post: obiectul cu idul id e sters daca exista, ceea ce se reflecta si in fisier,
		se returneaza un obiect de tipul Melodie (melodia stearsa)
			altfel, daca nu exista, arunca exceptie
	*/
	virtual Melodie sterge_dupa_id(int id);
};

class RepoFile : public Repo {

private:
	string path;
	void read();
	void write();
public:
	RepoFile(string _path) : Repo(), path{ _path }{ read(); }

	/*
	Functie care suprascrie metoda de adauga() din clasa de baza Repo
	*/
	void adauga(Melodie& t)override {
		read();
		Repo::adauga(t);
		write();
	}

	/*
	Functie care suprascrie metoda de get_all() din clasa de baza Repo
	*/
	vector<Melodie>& get_all() override {
		return Repo::get_all();
	}

	/*
	Functie care suprascrie metoda de modifica() din clasa de baza Repo
	*/
	Melodie modifica(const Melodie& m) override {
		read();
		Melodie modificata = Repo::modifica(m);
		write();
		return modificata;
	}

	/*
	Functie care suprascrie metoda de sterge_dupa_id() din clasa de baza Repo
	*/
	Melodie sterge_dupa_id(int id) override {
		read();
		Melodie stearsa = Repo::sterge_dupa_id(id);
		write();
		return stearsa;
	}
};