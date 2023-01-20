#ifndef REPO_H
#define REPO_H

#include <vector>
#include "repoException.h"
#include "pok.h"
#include "vectordinamic.h"
//using std::vector;

class Repo {
private:
	VectorDinamic<Locatar> locatari;
public:
	Repo() noexcept;
	void adauga(const Locatar&);
	void sterge_dupa_id(int);
	void modifica(const Locatar&);
	const Locatar& cauta(const Locatar&);
	 VectorDinamic<Locatar>& getAll() noexcept;
	//int size() noexcept;

	bool exist(const Locatar& p);
	const Locatar& myfind(int ap);

	//~Repo();
};

#endif