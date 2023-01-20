#ifndef SERVICE_H
#define SERVICE_H
#include "pok.h"
#include "repo.h"
#include "repoException.h"
#include "valid.h"
#include "undo.h"
//#include "vectordinamic.h"
#include <vector>
#include "observer.h"
using std::vector;

using std::unique_ptr;

class Service:public Observable {
private:
	RepoAbstract& repo;
	LocatarValidator& val;

	vector<unique_ptr<ActiuneUndo>>undoAtions;

	vector<Locatar>notificare;

	vector<Locatar> generalSort(bool(*maiMicF)(const Locatar&, const Locatar&));
public:
	Service(RepoAbstract&, LocatarValidator&) noexcept;

	void adaugaLocatar(const int& ap, const string& nume, const double& sup, const int& tip);
	void adaugaLocatarDoarAp(const int& ap);
	int adaugaLocatariInLista(const int& cate);
	void adaugaLocatarDupaApInLista(const int& ap);
	void stergeListaComplet();
	vector<Locatar>& getAllNotificari() noexcept;
	void stergeLocatar(int ap);
	void modificaLocatar(const int& ap, const string& nume, const double& sup, const int& tip);
	Locatar cautaLocatar(const int& ap);
	vector<Locatar>& getAll() noexcept {
		return repo.getAll();
	}

	void undo();

	vector <Locatar>locatari_dupa_tip(int tip);
	vector<Locatar> locatari_dupa_suprafata(double supi, double sups);


	vector<Locatar> sortareDupaNume(int x);
	vector<Locatar> sortareDupaSuprafata(int x);
	vector<Locatar> sortareDupaTipSuprafata(int x);
	~Service();

};

#endif