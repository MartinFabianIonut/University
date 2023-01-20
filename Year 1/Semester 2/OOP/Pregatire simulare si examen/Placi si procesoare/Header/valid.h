#pragma once
#include <vector>
#include <string>
#include "domeniu.h"
#include <iostream>
#include <exception>
using std::ostream;

class ValidException {
private:
	string mesaj;
public:
	ValidException(const string& _mesaj) :mesaj{ _mesaj } {}
	string& get_mesaj() { return mesaj; }
	friend ostream& operator<<(ostream& g, ValidException& re) { g << re.get_mesaj(); return g; }
};

class Valid {

public:
	void valideazaprocesor(Procesor& p);
	void valideazaplaca(Placa& p);
};