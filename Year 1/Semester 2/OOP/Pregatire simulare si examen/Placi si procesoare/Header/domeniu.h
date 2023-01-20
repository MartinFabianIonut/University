#pragma once
#include <string>
#include <iostream>
using std::string;

class Procesor {
private:
	string nume;
	int numarT;
	string soclu;
	int pret;
public:
	Procesor() = default;
	Procesor(string _nume, int _numarT, string _soclu, int _pret) : nume{ _nume }, numarT{ _numarT }, soclu{ _soclu }, pret{_pret} {}
	string& get_nume();
	string& get_soclu();
	int get_nr();
	int get_pret();

	Procesor& operator=(Procesor& altul);
	bool operator==(const Procesor& altul) const noexcept;
	bool operator!=(const Procesor& altul) const noexcept;
};

class Placa {
private:
	string nume, soclu;
	int pret;
public:
	Placa() = default;
	Placa(string _nume, string _soclu, int _pret) : nume{ _nume }, soclu{ _soclu }, pret{ _pret } {}
	string& get_nume();
	string& get_soclu();
	int& get_pret();

	Placa& operator=(Placa& altul);
	bool operator==(const Placa& altul) const;
	bool operator!=(const Placa& altul) const;
};