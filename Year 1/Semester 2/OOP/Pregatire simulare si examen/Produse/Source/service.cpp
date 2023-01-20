#include "service.h"

void Service::adauga_produs(int _id, string _nume, string _tip, double _pret)
{
	Produs p{  _id,  _nume, _tip, _pret };
	valid.valideaza(p);
	repo.adauga(p);
}

vector<Produs> Service::get_all()
{
	return repo.get_all();
}