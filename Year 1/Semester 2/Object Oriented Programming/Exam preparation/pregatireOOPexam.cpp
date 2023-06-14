#define _CRTDBG_MAP_ALLOC 
#include <stdlib.h> 
#include <crtdbg.h> 
#include <iostream>
#include <vector>
#include <string>
#include <algorithm>

#include <iterator>
using std::vector;
using std::string;
using std::cout;
using std::unique_ptr;
using std::make_unique;
using std::sort;
constexpr auto INITIAL_CAPACITY = 10;


class Placinta {
protected:
	int pret;

public:
	virtual ~Placinta() = default;
	Placinta(int pret): pret{pret}{}
	virtual string descriere() = 0;
	virtual int cost() { return pret; };
	
};

class PlacintaSimpla : public Placinta
{
protected:
	string den;
public:
	PlacintaSimpla(string den, int _pret) : den{ den }, Placinta(_pret) {}
	string descriere() override
	{
		return "Placinta de " + den;
	}

	int cost()override
	{
		return pret;
	}
};

class PlacintaDecorator : public Placinta {
private:
	Placinta* placinta;
public:
	PlacintaDecorator(Placinta* p): placinta{p}, Placinta(p->cost()){}

	string descriere() override {
		return placinta->descriere();
	}

	int cost() override {
		return placinta->cost();
	}
	~PlacintaDecorator() { delete placinta; }
};


class PlacintaMere : public PlacintaDecorator {

public:
	PlacintaMere(Placinta* p) : PlacintaDecorator(p) {}

	string descriere() override
	{
		return PlacintaDecorator::descriere() + " cu mere ";
	}
	int cost() override
	{
		return PlacintaDecorator::cost() + 40;
	}
};

class PlacintaVisine : public PlacintaDecorator {
public:
	PlacintaVisine(Placinta* p) : PlacintaDecorator(p) {}

	string descriere() override
	{
		return PlacintaDecorator::descriere() + " cu visine ";
	}
	int cost() override
	{
		return PlacintaDecorator::cost() + 70;
	}
};

vector<Placinta*> ret() {
	
	Placinta* p = new PlacintaSimpla("kivi",7);
	p = new PlacintaMere(p);
	p = new PlacintaVisine(p);

	Placinta* p2 = new PlacintaSimpla("capsuni",7);
	p2 = new PlacintaMere(p2);

	Placinta* p3 = new PlacintaSimpla("kivi",7);
	vector<Placinta*> vec;
	vec.push_back(p);
	vec.push_back(p2);
	vec.push_back(p3);
	return vec;
}

/*
vector< unique_ptr<Placinta>> ret() {
	unique_ptr<Placinta>p = make_unique<PlacintaSimpla>("kivi");
	p = make_unique < PlacintaMere>(p);
	p = make_unique < PlacintaVisine>(p);

	unique_ptr<Placinta>p2 = make_unique<PlacintaSimpla>("capsuni");
	p2 = make_unique < PlacintaMere>(p2);

	unique_ptr<Placinta>p3 = make_unique<PlacintaSimpla>("kivi");
	vector< unique_ptr<Placinta>> vec;
	vec.push_back(p);
	vec.push_back(p2);
	vec.push_back(p3);
	return vec;
}
*/

template<typename tipulmeu>
class Geanta {
private:
	vector<tipulmeu> geanta;
public:
	Geanta(tipulmeu g) { geanta.push_back(g); }
	vector<tipulmeu> operator+(tipulmeu altul) {
		geanta.push_back(altul);
		return geanta;
	}

	tipulmeu operator[](int poz) {
		return geanta[poz];
	}

	Geanta& operator=(const Geanta& altul) {
		geanta = altul.geanta;
		return *this;
	}

};



//
//void calatorie() {
//	Geanta<string> ganta{ "Ion" };//creaza geanta pentru Ion
//	ganta = ganta + string{ "haine" }; //adauga obiect in ganta
//	ganta + string{ "pahar" };
//	cout << ganta[0];
//}


bool f(int a) {
	if (a <= 0)
		throw std::exception("Illegal argument");
	int d = 2;
	while (d < a && a % d>0) d++;
	return d >= a;
}
#include <assert.h>
void test() {
	assert(f(1) == true);
	assert(f(2) == true); assert(f(3) == true); assert(f(5) == true); assert(f(7) == true);
	assert(f(4) == false); assert(f(6) == false); assert(f(8) == false); assert(f(9) == false);
	try {
		bool nu = f(0);
		assert(false);
	}
	catch (std::exception& e) {
		string c = e.what();
		assert(c== "Illegal argument");
	}
}

int main() {
	/*Placinta* p = new PlacintaSimpla("kivi");
	cout << p->descriere() << p->cost() <<"\n";
	p = new PlacintaMere(p);
	cout << p->descriere() << p->cost() << "\n";
	p = new PlacintaVisine(p);
	cout << p->descriere() << p->cost() << "\n";*/


	{
		vector<Placinta*> v = ret();
		for (auto& p : v) {
			cout << p->descriere() << "\n";
		}
		cout << "\n";
		sort(v.begin(), v.end(), [](Placinta* p1, Placinta* p2) {return p1->descriere() < p2->descriere(); });
		for (auto& p : v) {
			cout << p->descriere() << ", la pretul de " << p->cost() << "\n";
		}
		for (auto p : v) {
			delete p;
		}
		v.clear();
		cout << "\nAcum iterarea:\n";
		for (int i = 2; i < 30; i++) {
			cout << f(i) << " ";
		}
		test();
	}

	_CrtDumpMemoryLeaks();
	return 0;
}
