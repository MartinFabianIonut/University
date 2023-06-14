#ifndef POK_H
#define POK_H

#include <string>
#include <iostream>

using std::cout;
using std::string;

class Locatar {
private:

	int ap;
	string nume;
	double sup;
	int tip;

public:
	Locatar() = default;
	Locatar(const int&, const string&, const double&, const int&);
	/*Locatar(const Locatar& l):
		ap{ l.ap }, nume{ l.nume }, sup{ l.sup }, tip{ l.tip }{ cout << "Am creat locatar cu -> ap: " << l.ap << ", nume: " << l.nume << ", suprafata: " << l.sup << " si tip: " << l.tip << "\n"; }*/

	Locatar& operator=(const Locatar&);
	bool operator==(const Locatar&) const noexcept;
	bool operator!=(const Locatar&) const noexcept;

	const int& get_ap() const noexcept;
	const double& get_sup() const noexcept;
	const string& get_nume() const noexcept;
	const int& get_tip() const noexcept;

	//void set_nume(const string&);
	/*void set_ap(int ap);
	void set_tip(const int&);
	void set_sup(const double&);*/

	~Locatar();


};

bool cmpNume(const Locatar& l1, const Locatar& l2) noexcept;
bool cmpSuprafata(const Locatar& l1, const Locatar& l2) noexcept;
bool cmpTipSuprafata(const Locatar& l1, const Locatar& l2) noexcept;

bool cmpNume2(const Locatar& l1, const Locatar& l2) noexcept;
bool cmpSuprafata2(const Locatar& l1, const Locatar& l2) noexcept;
bool cmpTipSuprafata2(const Locatar& l1, const Locatar& l2) noexcept;

#endif