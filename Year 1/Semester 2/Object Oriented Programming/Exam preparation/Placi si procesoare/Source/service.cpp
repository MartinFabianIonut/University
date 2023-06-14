#include "service.h"

void Service::adaugaprocesorservice(string nume, int numarT, string soclu, int pret)
{
	Procesor p{ nume,numarT,soclu,pret };
	val.valideazaprocesor(p);
	repo.adaugaprocesor(p);
}

void Service::adaugaplacaservice(string nume, string soclu, int pret)
{
	Placa p{ nume,soclu,pret };
	val.valideazaplaca(p);
	repo.adaugaplaca(p);
}
