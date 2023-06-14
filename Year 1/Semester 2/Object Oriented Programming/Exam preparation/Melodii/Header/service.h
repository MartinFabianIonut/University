#pragma once
#include "repo.h"
#include <algorithm>
using std::vector;
using std::sort;


class Service {
private:
	Repo& repo;
public:
	Service(Repo& _repo) :repo{ _repo } {}

	/*
	Functie care returneaza toate elementele
	pre: nu sunt
	post: se returneaza un vector de melodii
	*/
	vector<Melodie>& get_all() {
		return repo.get_all();
	}

	/*
	Functie care cauta o melodie dupa id
	pre: id - int
	post: returneaza un obiect de tip Melodie daca exista,
			altfel arunca exceptie
	*/
	Melodie cauta(int id);

	/*
	Functie care modifica o melodie
	pre: id - int, titlu - string, rank - int
	post: obiectul cu idul id e modificat daca exista, ceea ce se reflecta si in fisier
			altfel arunca exceptie
	*/
	void modifica(int id, const string& titlu, int rank);

	/*
	Functie care sterge o melodie dupa id
	pre: id - int
	post: obiectul cu idul id e sters daca exista, ceea ce se reflecta si in fisier
			altfel arunca exceptie
	*/
	void sterge(int id)
	{
		Melodie l = repo.sterge_dupa_id(id);
	}

};