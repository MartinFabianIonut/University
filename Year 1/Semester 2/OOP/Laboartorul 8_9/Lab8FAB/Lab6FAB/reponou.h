#ifndef REPON_H
#define REPON_H

#include <vector>
#include <map>
#include "repoException.h"
#include "repo.h"
#include "pok.h"
//#include "vectordinamic.h"
using std::vector;
using std::map;
using std::pair;


class RepoNou : public RepoAbstract {
private:
	map<int, Locatar> locatari;
	float prob;
public:
	RepoNou() noexcept;
	RepoNou(const float prob);
	void adauga(const Locatar&) override;
	Locatar sterge_dupa_id(int) override;
	Locatar modifica(const Locatar&) override;
	const Locatar& cauta(const Locatar&) override;
	vector< Locatar>& getAll() override;
	//std::unique_ptr <vector<Locatar> >& getAll2() noexcept;
	~RepoNou();
};

#endif