#include "domeniu.h"

const int Produs::get_id() const noexcept
{
	return id;
}

const string Produs::get_nume() const noexcept
{
	return nume;
}

const string Produs::get_tip() const noexcept
{
	return tip;
}

const double Produs::get_pret() const noexcept
{
	return pret;
}

Produs& Produs::operator=(const Produs& altul)
{
	id = altul.id;
	nume = altul.nume;
	tip = altul.tip;
	pret = altul.pret;
	return *this;
}

bool Produs::operator==(const Produs& altul) const noexcept
{
	return id == altul.id;
}

bool Produs::operator!=(const Produs& altul) const noexcept
{
	return id != altul.id;
}
