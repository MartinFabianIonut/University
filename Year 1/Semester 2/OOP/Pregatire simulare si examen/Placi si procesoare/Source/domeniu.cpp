#include "domeniu.h"

string& Procesor::get_nume()
{
	return nume;
}

string& Procesor::get_soclu()
{
	return soclu;
}

int Procesor::get_nr()
{
	return numarT;
}

int Procesor::get_pret()
{
	return pret;
}

Procesor& Procesor::operator=(Procesor& altul)
{
	this->nume = altul.get_nume();
	this->numarT = altul.get_nr();
	this->soclu = altul.get_soclu();
	this->pret = altul.get_pret();
	return *this;
}

bool Procesor::operator==(const Procesor& altul) const noexcept
{
	return nume == altul.nume;
}

bool Procesor::operator!=(const Procesor& altul) const noexcept
{
	return nume != altul.nume;
}

string& Placa::get_nume()
{
	return nume;
}

string& Placa::get_soclu()
{
	return soclu;
}

int& Placa::get_pret()
{
	return pret;
}

Placa& Placa::operator=(Placa& altul)
{
	nume = altul.get_nume();
	soclu = altul.get_soclu();
	pret = altul.get_pret();
	return *this;
}

bool Placa::operator==(const Placa& altul) const
{
	return nume == altul.nume;
}

bool Placa::operator!=(const Placa& altul) const
{
	return nume == altul.nume;
}
