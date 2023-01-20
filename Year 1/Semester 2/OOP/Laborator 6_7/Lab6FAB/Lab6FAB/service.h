#ifndef SERVICE_H
#define SERVICE_H
#include "pok.h"
#include "repo.h"
#include "valid.h"
#include "vectordinamic.h"
#include <vector>
using std::vector; 


class Service {
private:
	Repo& repo;
	LocatarValidator& val;
	VectorDinamic<Locatar> generalSort(bool(*maiMicF)(const Locatar&, const Locatar&));
public:
	Service(Repo&, LocatarValidator&) noexcept;

	void adaugaLocatar(const int& ap, const string& nume, const double& sup, const int& tip);
	void stergeLocatar(int ap);
	void modificaLocatar(const int& ap, const string& nume, const double& sup, const int& tip);
	Locatar cautaLocatar(const int& ap);
	VectorDinamic<Locatar>& getAll() noexcept {
		return repo.getAll();
	}
	 
	VectorDinamic <Locatar>locatari_dupa_tip(int tip);
	VectorDinamic<Locatar> locatari_dupa_suprafata(double supi, double sups);
	
	
	VectorDinamic<Locatar> sortareDupaNume(int x);
	VectorDinamic<Locatar> sortareDupaSuprafata(int x);
	VectorDinamic<Locatar> sortareDupaTipSuprafata(int x);
	//~Service();

};

#endif