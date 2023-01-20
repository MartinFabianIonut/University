#include "pok.h"


Locatar::Locatar(const int& _ap, const string& _nume, const double& _sup, const int& _tip):
	ap{ _ap },
	nume{ _nume },
	sup{ _sup },
	tip{ _tip }{}


Locatar& Locatar::operator=(const Locatar&altul)
{	
	this->ap = altul.ap;
	this->nume = altul.nume;
	this->sup = altul.sup;
	this->tip = altul.tip;
	return *this;
}

bool Locatar::operator==(const Locatar& l) const noexcept
{
	return this->ap == l.ap;
}

bool Locatar::operator!=(const Locatar& l) const noexcept
{
	return this->ap != l.ap;
}

const int& Locatar::get_ap() const noexcept
{
	return this->ap;
}

const double& Locatar::get_sup() const noexcept
{
	return this->sup;
}

const string& Locatar::get_nume() const noexcept
{
	return this->nume;
}

const int& Locatar::get_tip() const noexcept
{
	return this->tip;
}

void Locatar::set_nume(const string& _nume)
{
	this->nume = _nume;
}

bool cmpNume(const Locatar& l1, const Locatar& l2) noexcept {
	return l1.get_nume()< l2.get_nume();
}

bool cmpNume2(const Locatar& l1, const Locatar& l2) noexcept {
	return l1.get_nume() > l2.get_nume();
}

bool cmpSuprafata(const Locatar& l1, const Locatar& l2) noexcept {
	return l1.get_sup() < l2.get_sup();
}

bool cmpSuprafata2(const Locatar& l1, const Locatar& l2) noexcept {
	return l1.get_sup() > l2.get_sup();
}

bool cmpTipSuprafata(const Locatar& l1, const Locatar& l2) noexcept
{
	if (l1.get_tip() != l2.get_tip()) {
		return l1.get_tip() < l2.get_tip();
	}
	return l1.get_sup() < l2.get_sup();
}

bool cmpTipSuprafata2(const Locatar& l1, const Locatar& l2) noexcept
{
	if (l1.get_tip() != l2.get_tip()) {
		return l1.get_tip() > l2.get_tip();
	}
	return l1.get_sup() > l2.get_sup();
}

Locatar::~Locatar(){
}
