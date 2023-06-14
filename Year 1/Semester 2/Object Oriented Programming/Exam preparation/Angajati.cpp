#define _CRTDBG_MAP_ALLOC 
#include <stdlib.h> 
#include <crtdbg.h> 
#include <iostream>
#include <vector>
#include <string>
#include <algorithm>

using std::vector;
using std::string;
using std::cout;

class Participant {
public:
	virtual ~Participant() = default;
	virtual void tipareste() = 0;
	virtual bool eVoluntar() { return true; };
};

class Personal : public Participant {
private:
	string nume;
public:
	Personal(string _nume): nume{_nume}{}
	void tipareste() override {
		cout << nume;
	}
};

class Administrator :public Personal {
public:
	Administrator(string nume): Personal{nume}{}
	void tipareste() override {
		Personal::tipareste();
		cout << " Administrator\n";
	}
};

class Director :public Personal {
public:
	Director(string nume) : Personal{ nume } {}
	void tipareste() override {
		Personal::tipareste();
		cout << " Director\n";
	}
};

class Angajat : public Participant {
private:
	Participant* p;
public:
	Angajat(Participant* _p): p{_p}{}
	void tipareste() override {
		p->tipareste();
		cout << " Angajat\n";
	}
	bool eVoluntar() override {
		return false;
	}
	~Angajat() {
		delete p;
	}
};

class ONG {
private:
	vector<Participant*> v;
public:
	void add(Participant* p) {
		v.push_back(p);
	}

	vector<Participant*> getAll(bool asauv) {
		if (asauv) {//voluntar
			vector<Participant*> voluntari;
			for (const auto& p : v) {
				if (p->eVoluntar()) {
					voluntari.push_back(p);
				}
			}
			return voluntari;
		}
		else {
			vector<Participant*> angajati;
			for (const auto& p : v) {
				if (!p->eVoluntar()) {
					angajati.push_back(p);
				}
			}
			return angajati;
		}
	}

	~ONG() {
		for (auto& o : v) {
			delete o;
		}
	}
};

ONG* f() {
	Participant* admin = new Administrator("Sefu mic, voluntar");
	Participant* admin2 = new Administrator("Vasile, angajat");
	admin2 = new Angajat(admin2);
	Participant* dir = new Director("Sefu mare, voluntar");
	Participant* dir2 = new Director("Sefu mare, angajat");
	dir2 = new Angajat(dir2);
	ONG* ong = new ONG();
	ong->add(admin);
	ong->add(admin2);
	ong->add(dir);
	ong->add(dir2);
	return ong;
}

template <typename T>
class Cos {
private:
	vector<T> cos;
	
public:
	Cos() = default;
	Cos(T ceva) { cos.push_back(ceva); }

	Cos& operator+(T nou) {
		cos.push_back(nou);
		return* this;
	}

	Cos& undo() {
		if (!cos.empty()) {
			cos.pop_back();
			
		}
		return *this;
	}

	void tipareste(std::ostream& os) {
		for (const auto& c : cos) {
			os << c << " ";
		}
	}
};

void cumparaturi() {
	Cos<string> cos;//creaza un cos de cumparaturi
	cos = cos + "Mere"; //adauga Mere in cos
	cos.undo();//elimina Mere din cos
	cos + "Mere"; //adauga Mere in cos
	cos = cos + "Paine" + "Lapte";//adauga Paine si Lapte in cos
	cos.undo().undo();//elimina ultimele doua produse adaugate
	cos.tipareste(cout);//tipareste elementele din cos (Mere)
}

int main() {
	

	{/*
		ONG* ong = f();
		cout << "Voluntari:";
		for (auto& v : ong->getAll(true)) {
			v->tipareste();
		}
		cout << "\n";
		cout << "Angajati:";
		for (auto& v : ong->getAll(false)) {
			v->tipareste();
		}
		delete ong;*/

		cumparaturi();
	}

	_CrtDumpMemoryLeaks();
	return 0;
}
