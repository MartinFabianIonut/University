#include "reponou.h"

RepoNou::RepoNou() noexcept
{
	this->prob = 0;
}

RepoNou::RepoNou(const float prob)
{
	this->prob = prob;
}

void RepoNou::adauga(const Locatar& p)
{
	if (((double)rand() / RAND_MAX) < ((double)prob / 100))
		throw  RepoException("\nExceptie aruncata din cauza probabilitatii!\n");
	auto it = locatari.find(p.get_ap());
	if (it != locatari.end()) {
		throw  RepoException("\nExceptie aruncata din cauza probabilitatii!\n");
	}
	locatari.insert(pair<int, Locatar>(p.get_ap(), p));
}

Locatar RepoNou::sterge_dupa_id(int ap)
{
	if (((double)rand() / RAND_MAX) < ((double)prob / 100))
		throw  RepoException("\nExceptie aruncata din cauza probabilitatii!\n");
	auto it = locatari.find(ap);
	if (it != locatari.end()) {
		Locatar l = (it)->second;
		locatari.erase(ap);
		return l;
	}throw RepoException("element inexistent!\n");}

Locatar RepoNou::modifica(const Locatar& p)
{
	if (((double)rand() / RAND_MAX) < ((double)prob / 100))
		throw  RepoException("\nExceptie aruncata din cauza probabilitatii!\n");

	auto it = locatari.find(p.get_ap());
	if (it != locatari.end()) {
		Locatar l = it->second;
		it->second = p;
		return l;
	}
	throw RepoException("element inexistent!\n");}

const Locatar& RepoNou::cauta(const Locatar& p)
{
	if (((double)rand() / RAND_MAX) < ((double)prob / 100))
		throw  RepoException("\nExceptie aruncata din cauza probabilitatii!\n");

	auto it = locatari.find(p.get_ap());
	if (it != locatari.end()) {
		Locatar& l = it->second;
		return l;
	}
	throw RepoException("element inexistent!\n");}
//
//std::unique_ptr < vector<Locatar> >& RepoNou::getAll2() noexcept
//{
//	std::unique_ptr< vector<Locatar> > v = std::make_unique < vector<Locatar> > ();
//	for (auto it = locatari.begin(); it != locatari.end(); it++) {
//		v->push_back(it->second);
//	}
//	return v;
//}

//
//vector< std::unique_ptr< Locatar>> & RepoNou::getAll() noexcept
//{
//	vector< std::unique_ptr< Locatar> > v;
//	//= std::make_unique < vector<Locatar> >();
//	for (auto it = locatari.begin(); it != locatari.end(); it++) {
//		v.push_back(std::make_unique < Locatar>(it->second));
//	}
//	return v;
//}


vector< Locatar>& RepoNou::getAll()
{	
	if (((double)rand() / RAND_MAX) < ((double)prob / 100))
		throw  RepoException("\nExceptie aruncata din cauza probabilitatii!\n");

	vector<Locatar> *v = new vector<Locatar> ();
	for (auto it = locatari.begin(); it != locatari.end(); it++) {
		v->push_back(it->second);
	}
	return *v;
}

RepoNou::~RepoNou()
{
}
