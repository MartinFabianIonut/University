#include <iostream>
#include <vector>
#include <string>
using std::vector;
using std::string;
using std::cout;

template <typename T>
class Geanta {
protected:
	vector<T>valori;
public:
	Geanta() = default;
	Geanta(T op) { valori.push_back(op); }

	Geanta operator+(T a)
	{
		Geanta old = *this; // copy old value
		old.valori.push_back(a);
		return old;    // return old value
	}

	Geanta operator=(const Geanta& g)
	{
		valori = g.valori;
		return *this;    // return old value
	}

	typename vector<T>::iterator i;
	typename vector<T>::iterator begin() {
		i = valori.begin();
		return i;
	}
	typename vector<T>::iterator end() {
		i = valori.end();
		return i;
	}
};

void calatorie() {
	Geanta<string> ganta{ "Ion" };//creaza geanta pentru Ion
	ganta = ganta + string{ "haine" }; //adauga obiect in ganta
	ganta + string{ "pahar" };
	for (auto o : ganta) {//itereaza obiectele din geanta
		cout << o << "\n";
	}
}



int main() {
	calatorie();
}