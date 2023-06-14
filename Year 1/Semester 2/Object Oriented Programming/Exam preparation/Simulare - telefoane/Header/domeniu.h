#pragma once
#include <iostream>
#include <vector>
#include <string>
using std::string;
using std::vector;

class Telefon {
private:
	string cod, brand, model;
	int pret;
public:
	Telefon(string _cod, string _brand, string _model, int _pret): cod{_cod},brand{_brand},model{_model},pret{_pret}{}

	const string& get_cod() const noexcept;
	const string& get_brand() const noexcept;
	const string& get_model() const noexcept;
	const int get_pret() const noexcept;
	void set_cod(const string& _cod);

	Telefon& operator=(const Telefon& altul);
	bool operator==(const Telefon& altul) const noexcept;
	bool operator!=(const Telefon& altul) const noexcept;

};