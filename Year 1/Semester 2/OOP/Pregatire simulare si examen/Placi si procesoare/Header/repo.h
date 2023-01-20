#pragma once
#include <vector>
#include <string>
#include "domeniu.h"
#include <exception>
using std::vector;
using std::string;
using std::ostream;

class RepoException : public std::exception {
private:
	string mesaj;
public:
	RepoException(const string& _mesaj) :mesaj{ _mesaj }{}
	string& get_mesaj() { return mesaj; }
	friend ostream& operator<<(ostream& g, RepoException& re) { g << re.get_mesaj(); return g; }
};

class Repo {
protected:
	vector<Procesor>procesoare;
	vector<Placa>placi;
public:
	Repo() = default;
	virtual void adaugaprocesor(Procesor& p);
	virtual void adaugaplaca(Placa& p);
	virtual vector<Procesor>& get_all_procesoare() { return procesoare; }
	virtual vector<Placa>& get_all_placi() { return placi; }
};

class RepoFile : public Repo {
private:
	string path1,path2;
	void read();
	void write();
public:
	RepoFile(string _path1, string _path2) : Repo(), path1{ _path1 }, path2{ _path2 } {read(); }
	void adaugaprocesor(Procesor& p) override {
		read();
		Repo::adaugaprocesor(p);
		write();
	}

	void adaugaplaca(Placa& p) override {
		read();
		Repo::adaugaplaca(p);
		write();
	}

	vector<Procesor>& get_all_procesoare() override {
		//read();
		return Repo::get_all_procesoare();
	}

	vector<Placa>& get_all_placi() override {
		//read();
		return Repo::get_all_placi();
	}
};