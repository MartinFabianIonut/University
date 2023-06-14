#include "domeniu.h"

const string& Telefon::get_cod() const noexcept
{
    return this->cod;
}
 
const string& Telefon::get_brand() const noexcept
{
    return brand;
}

const string& Telefon::get_model() const noexcept
{
    return model;
}

const int Telefon::get_pret() const noexcept
{
    return pret;
}

void Telefon::set_cod(const string& _cod)
{
    this->cod = _cod;
}

Telefon& Telefon::operator=(const Telefon& altul)
{
    cod = altul.cod;
    brand = altul.brand;
    model = altul.model;
    pret = altul.pret;
    return *this;
}

bool Telefon::operator==(const Telefon& altul) const noexcept
{
    return cod == altul.cod;
}

bool Telefon::operator!=(const Telefon& altul) const noexcept
{
    return cod != altul.cod;
}

bool cmpCod(Telefon& l1, Telefon& l2) noexcept {
    return l1.get_cod() < l2.get_cod();
}