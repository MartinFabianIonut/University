
#include <iostream>
#include <vector>
#include <string>
using std::vector;
using std::string;
using std::cout;


template <typename T>
class Expresie {
protected:
	T suma;
	vector<T>valori;
public:
	Expresie() = default;
	Expresie(int op) : suma{ op } { valori.push_back(op); }
	Expresie(int op, vector<int>v) : suma{ op }, valori{ v } { }
	Expresie& operator+(int a)
	{
		suma += a; 
		valori.push_back(-a);
		return *this;
	}

	Expresie& operator-(int a)
	{
		suma -= a;  
		valori.push_back(a);
		return *this;
	}

	T valoare() {
		return suma;
	}

	Expresie& undo() {
		int ultim = *(std::end(valori) - 1);
		valori.erase(valori.end()-1);
		suma += ultim;
		return *this;
	}
};

class Sesiune
{
private:
    string nume;
    vector<string>attendace;
public:
    vector<string>::iterator begin() {
        return attendace.begin();
    }
    vector<string>::iterator end() {
        return attendace.end();
    }

    Sesiune(string n) : nume{ n } {}
    string& getNume() { return nume; }

    Sesiune& operator+(string n) {
        attendace.push_back(n);
        return *this;
    }
    vector<string> select(const string& str) {
        vector<string>v;
        for (auto el : attendace) {
            if (el.find(str) >= 0)
                v.push_back(el);
        }
        return v;
    }
};
class Conferinta
{
private:
    vector<Sesiune>v;
public:
    Sesiune& track(string nume) {

        for (auto& s : v)
            if (s.getNume() == nume)
                return s;

        Sesiune S{ nume };
        v.push_back(S);

        return v.back();
    }
};


void operatii() {
	Expresie<int> exp{ 3 };//construim o expresie,pornim cu operandul 3
	//se extinde expresia in dreapta cu operator (+ sau -) si operand
	exp = exp + 7 + 3;
	exp = exp - 8;
	//tipareste valoarea expresiei (in acest caz:5 rezultat din 3+7+3-8)
	cout << exp.valoare() << "\n";
	exp.undo(); //reface ultima operatie efectuata
	//tipareste valoarea expresiei (in acest caz:13 rezultat din 3+7+3)
	cout << exp.valoare() << "\n";
	exp.undo().undo();
	cout << exp.valoare() << "\n"; //tipareste valoarea expresiei (in acest caz:3)
}

void conf() {
	Conferinta conf;
	//add 2 attendants to "Artifiial Inteligente" track
	conf.track("Artifiial Inteligente") + "Ion Ion" + "Vasile Aron";
	//add 2 attendants to "Software" track
	Sesiune s = conf.track("Software");
	s + "Anar Lior" + "Aurora Bran";
	//print all attendants from group "Artifiial Inteligente" track
	for (auto name : conf.track("Artifiial Inteligente")) {
		std::cout << name << ",";
	}
	//find and print all names from Software track that contains "ar"
	vector<string> v = conf.track("Software").select("ar");
	for (auto name : v) { std::cout << name << ","; }
}


int main() {
	operatii();
}