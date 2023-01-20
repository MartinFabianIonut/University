#define _CRT_SECURE_NO_WARNINGS

#include "prezentare.h"
#include <random>
#include "pok.h"
#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <vector>

using std::cout;
using std::cin;

void Prezentare::tipareste(vector<Locatar>& locatarii) {
	cout << "\nLocatarii sunt:\n";
	for (const auto& l : locatarii) {
		cout << "\tAp: " << l.get_ap() << "\t Nume: " << l.get_nume();
		cout << "\t Suprafata:" << l.get_sup() << "\t Tip:" <<l.get_tip() << '\n';
	}
	//delete &locatarii; // doar la cel nou
	cout << "Sfarsit lista locatari.\n";
}

void Prezentare::tipareste2(vector<Locatar>& locatarii) {
	cout << "\nLocatarii sunt:\n";
	for (const auto& l : locatarii) {
		cout << "\tAp: " << l.get_ap() << "\t Nume: " << l.get_nume();
		cout << "\t Suprafata:" << l.get_sup() << "\t Tip:" << l.get_tip() << '\n';
	}
	//delete& locatarii; // doar la cel nou
	cout << "Sfarsit lista locatari.\n";
}

int Prezentare::citireAp() {
	int ap = -1;
	bool valid = false;
	while (!valid) {
		valid = true;
		cout << "Dati apartamentul:";
		cin >> ap;
		if (cin.fail()) {
			cin.clear();
			cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
			cout << "Trebuie introdus un numar intreg!\n";
			valid = false;
		}
	}
	return ap;
}

double Prezentare::citireSup() {
	double sup = -1;
	bool valid = false;
	while (!valid) {
		valid = true;
		cout << "Dati suprafata:";
		cin >> sup;
		if (cin.fail()) {
			cin.clear();
			cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
			cout << "Trebuie introdus un numar real!\n";
			valid = false;
		}
	}
	return sup;
}

int Prezentare::citireTip() {
	int tip = -1;
	bool valid = false;
	while (!valid) {
		valid = true;
		cout << "Dati tipul:";
		cin >> tip;
		if (cin.fail()) {
			cin.clear();
			cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
			cout << "Trebuie introdus un numar intreg!\n";
			valid = false;
		}
	}
	return tip;
}

void Prezentare::adaugaUI() {
	int ap=-1, tip=-1;
	double sup=-1;
	string nume;
	ap = citireAp();
	cout << "Dati nume:";
	cin >> nume;
	sup = citireSup();
	tip = citireTip();
	ctr.adaugaLocatar(ap, nume, sup, tip);
	cout << "Apartament adaugat cu succes\n";
}

void Prezentare::adaugaApUI() {
	int ap = -1;
	ap = citireAp();
	ctr.adaugaLocatarDoarAp(ap);
	cout << "Apartament(e) adaugat cu succes\n";
}

void Prezentare::stergeUI() {
	tipareste(ctr.getAll());
	cout << "Alege apartamentul pentru care vrei sa stergi locatarul: ";
	int ap = -1;
	ap = citireAp();
	ctr.stergeLocatar(ap);
	tipareste(ctr.getAll());
}

void Prezentare::modificaUI()
{
	tipareste(ctr.getAll());
	int ap = -1, tip = -1;
	double sup = -1;
	string nume;
	ap = citireAp();
	cout << "Dati noul nume:";
	cin >> nume;
	sup = citireSup();
	tip = citireTip();
	ctr.modificaLocatar(ap,nume,sup,tip);
	tipareste(ctr.getAll());
}

void Prezentare::cautaUI()
{
	cout << "Introdu numarul apartamentului pe care il cauti:";
	int ap = -1;
	ap = citireAp();
	Locatar gasit = ctr.cautaLocatar(ap);
	cout << "\tAp: " << gasit.get_ap() << "\t Nume: " << gasit.get_nume();
	cout << "\t Suprafata:" << gasit.get_sup() << "\t Tip:" << gasit.get_tip() << '\n';
}

void Prezentare::filtruTip()
{
	tipareste(ctr.getAll());
	
	int tip=-1;
	tip = citireTip();
	vector<Locatar>res = ctr.locatari_dupa_tip(tip);
	if (res.size() == 0) {
		cout << "Nu exista apartamente de tipul " << tip << " !";
	}
	else {
		cout << "Locatarii filtrati sunt:\n";
		for (const auto& l : res) {
			cout << "\tAp: " << l.get_ap() << "\t Nume: " << l.get_nume();
			cout << "\t Suprafata:" << l.get_sup() << "\t Tip:" << l.get_tip() << '\n';
		}
		cout << "Sfarsit lista locatari.\n";
	}
}

void Prezentare::filtruSuprafata()
{
	tipareste(ctr.getAll());
	double supi=-1,sups=-1;
	cout << "Suprafata sa fie >= decat: ";
	supi = citireSup();
	cout << "Suprafata sa fie <= decat: ";
	sups = citireSup();
	vector<Locatar>res = ctr.locatari_dupa_suprafata(supi, sups);
	if (res.size() == 0) {
		cout << "Nu exista apartamente care sa se incadreze!";
	}
	else {
		cout << "Locatarii filtrati sunt:\n";
		for (const auto& l : res) {
			cout << "\tAp: " << l.get_ap() << "\t Nume: " << l.get_nume();
			cout << "\t Suprafata:" << l.get_sup() << "\t Tip:" << l.get_tip() << '\n';
		}
		cout << "Sfarsit lista locatari.\n";
	}
}

void Prezentare::sortareNume()
{
	int cresc = -1;
	bool valid = false;
	while (!valid) {
		valid = true;
		cout << "1 crescator\n2 descrescator\n";
		cin >> cresc;
		if (cin.fail()) {
			cin.clear();
			cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
			cout << "Trebuie introdus un numar intreg!\n";
			valid = false;
		}
	}
	if (cresc != 1 && cresc != 2) {
		cout << "Optiune invalida!\n";
		return;
	}
	vector<Locatar>res = ctr.sortareDupaNume(cresc);
	tipareste(res);
}

void Prezentare::sortareSuprafata()
{
	int cresc = -1;
	bool valid = false;
	while (!valid) {
		valid = true;
		cout << "1 crescator\n2 descrescator\n";
		cin >> cresc;
		if (cin.fail()) {
			cin.clear();
			cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
			cout << "Trebuie introdus un numar intreg!\n";
			valid = false;
		}
	}
	if (cresc != 1 && cresc != 2) {
		cout << "Optiune invalida!\n";
		return;
	}
	vector<Locatar>res = ctr.sortareDupaSuprafata(cresc);
	tipareste(res);
}

void Prezentare::sortareTipSuprafata()
{
	int cresc = -1;
	bool valid = false;
	while (!valid) {
		valid = true;
		cout << "1 crescator\n2 descrescator\n";
		cin >> cresc;
		if (cin.fail()) {
			cin.clear();
			cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
			cout << "Trebuie introdus un numar intreg!\n";
			valid = false;
		}
	}
	if (cresc != 1 && cresc != 2) {
		cout << "Optiune invalida!\n";
		return;
	}
	vector<Locatar>res = ctr.sortareDupaTipSuprafata(cresc);
	tipareste(res);
}

void Prezentare::undoUI()
{
	ctr.undo();
	tipareste(ctr.getAll());
}

void Prezentare::notificareUI()
{
	int actiune = -1;
	bool valid = false;
	while (!valid) {
		valid = true;
		cout << "1 goleste lista\n2 adauga dupa apartament\n3 genereaza random\n4 export\n";
		cin >> actiune;
		if (cin.fail()) {
			cin.clear();
			cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
			cout << "Trebuie introdus un numar intreg!\n";
			valid = false;
		}
	}
	if (actiune != 1 && actiune != 2 && actiune != 3 && actiune != 4) {
		cout << "Optiune invalida!\n";
		return;
	}
	if (actiune == 1) {
		ctr.stergeListaComplet();
		vector<Locatar>res = ctr.getAllNotificari();
		tipareste2(res);
	}
	else {
		if (actiune == 2) {
			int ap = -1;
			//vector<Locatar>res = ctr.getAll();
			tipareste(ctr.getAll());
			valid = false;
			while (!valid) {
				valid = true;
				cout << "introdu apartamentul pe care vrei sa-l adaugi in lista de notificare\n";
				cin >> ap;
				if (cin.fail()) {
					cin.clear();
					cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
					cout << "Trebuie introdus un numar intreg!\n";
					valid = false;
				}
			}
			ctr.adaugaLocatarDupaApInLista(ap);
			vector<Locatar>res2 = ctr.getAllNotificari();
			tipareste2(res2);
		}
		else {
			if (actiune == 3) {
				int cati = -1;
				//vector<Locatar>res = ctr.getAll();
				tipareste(ctr.getAll());
				valid = false;
				while (!valid) {
					valid = true;
					cout << "introdu cate apartamente vrei sa bag random\n";
					cin >> cati;
					if (cin.fail()) {
						cin.clear();
						cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
						cout << "Trebuie introdus un numar intreg!\n";
						valid = false;
					}
				}
				if (cati < 1) {
					cout << "Nu se poate, numar prea mic!\n";
				}
				else {
					if (ctr.adaugaLocatariInLista(cati) == 1) {
						cout << "Nu se poate, nu exista asa multi locatari!\n";
					}
					else {
						vector<Locatar>res3 = ctr.getAllNotificari();
						tipareste2(res3);
					}
				}
			}
			else {
				string nume;
				cin >> nume;
				nume = nume + ".csv";
				std::ofstream fout(nume);
				if (fout.is_open()) {
					vector<Locatar>res4 = ctr.getAllNotificari();
					for (auto it = res4.begin(); it != res4.end(); it++) {
						const double sup = (*it).get_sup();
						string Sup = std::to_string(sup);
						fout << (*it).get_ap() << ";" << (*it).get_nume() << ";" << Sup  << ";" << (*it).get_tip() << "\n";
					}
				}
				
			}
		}
	}
}


void Prezentare::adaugaCateva(Service& ctr2)
{
	ctr2.adaugaLocatar(4, "Ana", 54.3, 2);
	ctr2.adaugaLocatar(8, "Vlad", 89.2, 1);
	ctr2.adaugaLocatar(11, "Corina", 31.3, 3);
	ctr2.adaugaLocatar(7, "Sebi", 55.4, 2);
}

void Prezentare::start() {
	//adaugaCateva(ctr);
	string tip;
	cout << "Introdu 1 pentru mode, orice altceva pentru simplu\n>>>";
	cin >> tip;
	if (tip == "1") {
		mode();
	}
	else {
		while (true) {
			cout << "\nMeniu:\n\n";
			cout << "1 adauga\n2 tipareste\n3 sterge\n4 modifica\n5 filtrare tip\n6 filtrare suprafata\n7 cauta apartament";
			cout << "\n8 sortare dupa nume\n9 sortare dupa suprafata\n10 sortare dupa tip si suprafata\n11 adauga doar cu nr ap";
			cout << "\n12 undo\n13 vezi lista notificare\n0 Iesire\nDati comanda : ";

			int cmd;
			cin >> cmd;
			try {
				switch (cmd) {
				case 1:
					adaugaUI();
					break;
				case 11:
					adaugaApUI();
					break;
				case 2:
					tipareste(ctr.getAll());
					break;
				case 3:
					stergeUI();
					break;
				case 4:
					modificaUI();
					break;
				case 5:
					filtruTip();
					break;
				case 6:
					filtruSuprafata();
					break;
				case 7:
					cautaUI();
					break;
				case 8:
					sortareNume();
					break;
				case 9:
					sortareSuprafata();
					break;
				case 10:
					sortareTipSuprafata();
					break;
				case 12:
					undoUI();
					break;
				case 13:
					notificareUI();
					break;
				case 0:
					return;
				default:
					cout << "Comanda invalida\n";
				}
			}
			catch (const RepoException& ex) {
				cout << ex << '\n';
			}
			catch (const ValidateException& ex) {
				cout << ex << '\n';
			}
			
		}
	}
}
void tokenize(std::string const& str, const char* delim,
	std::vector<std::string>& out)
{
	char* token = strtok(const_cast<char*>(str.c_str()), delim);
	while (token != nullptr)
	{
		out.push_back(std::string(token));
		token = strtok(nullptr, delim);
	}
}

void aff(vector<string>& comenzi) {
	cout << "\nMeniu:\n";
	for (int i = 0; i <= 10; i++)
	{
		cout << comenzi.at(i) << " ";
	}
	cout << "\nPentru a parasi aplicatia, apasati 0\n\n>>>";
	cout << std::endl;
}

void Prezentare::mode()
{
	vector<string> comenzi;
	comenzi.push_back("adauga");
	comenzi.push_back("tipareste");
	comenzi.push_back("sterge");
	comenzi.push_back("modifica");
	comenzi.push_back("ftip");
	comenzi.push_back("fsup");
	comenzi.push_back("cauta");
	comenzi.push_back("sortn");
	comenzi.push_back("sorts");
	comenzi.push_back("sortts");
	comenzi.push_back("random");
	int ll = 0;
	while (true) {
		aff(comenzi);
		//cin.get();
		/*std::string citesc;
		std::vector<std::string> words;
		std::istringstream iss(citesc);
		for (std::string s; iss >> s; )
			words.push_back(s);*/
		char input[100];
		//cin >> input;
		if (ll == 0) {
cin.get();
		}
		ll++;
		cin.getline(input, 100);


		std::string citesc (input);
		//std::getline(std::cin, citesc);
		if (citesc == "0") {
			return;
		}
		else {
			cout << citesc;
		}

		constexpr char space_char = ' ';
		vector<string> words{};

		std::stringstream sstream(citesc);
		string word;
		while (std::getline(sstream, word, space_char)) {
			word.erase(std::remove_if(word.begin(), word.end(), ispunct), word.end());
			words.push_back(word);
		}

		//tokenize(citesc, " ", words);

		int dim = 0;
		while (dim < words.size()) {
			auto it = std::find(comenzi.begin(), comenzi.end(), words.at(dim));
			if (it != comenzi.end()) {
				const int d1 = dim + 1, d2 = dim + 2, d3 = dim + 3, d4 = dim + 4;
				if (words.at(dim) == "adauga") {
					if (d4 < words.size()) {
						try {
							const int ap = stoi(words.at(d1));
							const string nume = words.at(d2);
							const double sup = stod(words.at(d3));
							const int tip = stoi(words.at(d4));
							ctr.adaugaLocatar(ap, nume, sup, tip);
						}
						catch (const RepoException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (const ValidateException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (std::invalid_argument e) {
							dim++;
							cout << "Caught Invalid Argument Exception\n";
						}
					}
				}
				if (words.at(dim) == "tipareste") {
					try {
						tipareste(ctr.getAll());
					}
					catch (const RepoException& re) {
						cout << re;
						dim++;
						continue;
					}
				}
				if (words.at(dim) == "sterge") {
					if (d1 < words.size()) {
						try {
							const int ap = stoi(words.at(d1));
							ctr.stergeLocatar(ap);
						}
						catch (const RepoException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (const ValidateException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (std::invalid_argument e) {
							dim++;
							cout << "Caught Invalid Argument Exception\n";
						}
					}
				}
				if (words.at(dim) == "modifica") {
					if (d4 < words.size()) {
						try {
							const int ap = stoi(words.at(d1));
							const string nume = words.at(d2);
							const double sup = stod(words.at(d3));
							const int tip = stoi(words.at(d4));
							ctr.modificaLocatar(ap, nume, sup, tip);
						}
						catch (const RepoException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (const ValidateException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (std::invalid_argument e) {
							dim++;
							cout << "Caught Invalid Argument Exception\n";
						}

					}
				}
				if (words.at(dim) == "ftip") {
					if (d1 < words.size()) {
						try {
							const int tip = stoi(words.at(d1));

							vector<Locatar>res = ctr.locatari_dupa_tip(tip);
							if (res.size() == 0) {
								cout << "Nu exista apartamente de tipul " << tip << " !";
							}
							else {
								cout << "Locatarii filtrati sunt:\n";
								for (const auto& l : res) {
									cout << "\tAp: " << l.get_ap() << "\t Nume: " << l.get_nume();
									cout << "\t Suprafata:" << l.get_sup() << "\t Tip:" << l.get_tip() << '\n';
								}
								cout << "Sfarsit lista locatari.\n";
							}
						}
						catch (const RepoException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (const ValidateException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (std::invalid_argument e) {
							dim++;
							cout << "Caught Invalid Argument Exception\n";
						}
					}
				}
				if (words.at(dim) == "fsup") {
					if (d2 < words.size()) {
						try {
							const double supi = stod(words.at(d1));
							const double sups = stod(words.at(d2));
							vector<Locatar>res = ctr.locatari_dupa_suprafata(supi, sups);
							if (res.size() == 0) {
								cout << "Nu exista apartamente care sa se incadreze!";
							}
							else {
								cout << "Locatarii filtrati sunt:\n";
								for (const auto& l : res) {
									cout << "\tAp: " << l.get_ap() << "\t Nume: " << l.get_nume();
									cout << "\t Suprafata:" << l.get_sup() << "\t Tip:" << l.get_tip() << '\n';
								}
								cout << "Sfarsit lista locatari.\n";
							}
						}
						catch (const RepoException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (const ValidateException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (std::invalid_argument e) {
							dim++;
							cout << "Caught Invalid Argument Exception\n";
						}
					}
				}
				if (words.at(dim) == "cauta") {
					if (d1 < words.size()) {
						try {
							const int ap = stoi(words.at(d1));
							Locatar gasit = ctr.cautaLocatar(ap);
							cout << "\tAp: " << gasit.get_ap() << "\t Nume: " << gasit.get_nume();
							cout << "\t Suprafata:" << gasit.get_sup() << "\t Tip:" << gasit.get_tip() << '\n';
						}
						catch (const RepoException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (const ValidateException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (std::invalid_argument e) {
							dim++;
							cout << "Caught Invalid Argument Exception\n";
						}
					}
					
				}
				
				if (words.at(dim) == "sortn") {
					if (d1 < words.size()) {
						try {
							const int cresc = stoi(words.at(d1));
							if (cresc == 1 || cresc == 2) {
								vector<Locatar>res = ctr.sortareDupaNume(cresc);
								tipareste(res);
							}
						}
						catch (const RepoException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (const ValidateException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (std::invalid_argument e) {
							dim++;
							cout << "Caught Invalid Argument Exception\n";
						}
					}
				}
				if (words.at(dim) == "sorts") {
					if (d1 < words.size()) {
						try {
							const int cresc = stoi(words.at(d1));
							if (cresc == 1 || cresc == 2) {
								vector<Locatar>res = ctr.sortareDupaSuprafata(cresc);
								tipareste(res);
							}
						}
						catch (const RepoException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (const ValidateException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (std::invalid_argument e) {
							dim++;
							cout << "Caught Invalid Argument Exception\n";
						}
					}
				}
				if (words.at(dim) == "sortts") {
					if (d1 < words.size()) {
						try {
							const int cresc = stoi(words.at(d1));
							if (cresc == 1 || cresc == 2) {
								vector<Locatar>res = ctr.sortareDupaTipSuprafata(cresc);
								tipareste(res);
							}
						}
						catch (const RepoException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (const ValidateException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (std::invalid_argument e) {
							dim++;
							cout << "Caught Invalid Argument Exception\n";
						}
					}
				}
				if (words.at(dim) == "random") {
					
					if (d1 < words.size()) {
						try {
							const int cate = stoi(words.at(d1));
							if (cate>0) {
								ctr.adaugaLocatarDoarAp(cate);
								cout << "Apartament(e) adaugat cu succes\n";
							}
						}
						catch (const RepoException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (const ValidateException& re) {
							cout << re;
							dim++;
							continue;
						}
						catch (std::invalid_argument e) {
							cout << "Caught Invalid Argument Exception\n";
						}
					}
				}
				/*if (words.at(dim) == "0") {
					return;
				}*/
			}
			dim++;
		}
	}

}
//
Prezentare::~Prezentare()
{
}

