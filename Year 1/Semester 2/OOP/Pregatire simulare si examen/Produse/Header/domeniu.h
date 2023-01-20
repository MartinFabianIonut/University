#pragma once
#include <string>
#include <ostream>

using std::string;

class Produs
{
private:
	int id;
	string nume, tip;
	double pret;
public:
	Produs(int _id, string _nume, string _tip, double _pret):id{_id},nume{_nume},tip{_tip},pret{_pret}{}

	const int get_id()  const noexcept;
	const string get_nume()  const noexcept;
	const string get_tip() const noexcept;
	const double get_pret()  const noexcept;

	Produs& operator=(const Produs& altul);

	bool operator==(const Produs& altul)  const noexcept;
	bool operator!=(const Produs& altul)  const noexcept;
};