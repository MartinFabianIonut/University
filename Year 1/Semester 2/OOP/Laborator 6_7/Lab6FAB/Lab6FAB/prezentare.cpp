#include "prezentare.h"

#include "pok.h"
#include <iostream>
#include <string>

using std::cout;
using std::cin;

void Prezentare::tipareste(VectorDinamic<Locatar>& locatarii) {
	cout << "\nLocatarii sunt:\n";
	for (const auto& l : locatarii) {
		cout << "\tAp: " << l.get_ap() << "\t Nume: " << l.get_nume();
		cout << "\t Suprafata:" << l.get_sup() << "\t Tip:" <<l.get_tip() << '\n';
	}
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
	VectorDinamic<Locatar>res = ctr.locatari_dupa_tip(tip);
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
	VectorDinamic<Locatar>res = ctr.locatari_dupa_suprafata(supi, sups);
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
	VectorDinamic<Locatar>res = ctr.sortareDupaNume(cresc);
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
	VectorDinamic<Locatar>res = ctr.sortareDupaSuprafata(cresc);
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
	VectorDinamic<Locatar>res = ctr.sortareDupaTipSuprafata(cresc);
	tipareste(res);
}


void Prezentare::adaugaCateva(Service& ctr2)
{
	ctr2.adaugaLocatar(4, "Ana", 54.3, 2);
	ctr2.adaugaLocatar(8, "Vlad", 89.2, 1);
	ctr2.adaugaLocatar(11, "Corina", 31.3, 3);
	ctr2.adaugaLocatar(7, "Sebi", 55.4, 2);
}

void Prezentare::start() {
	adaugaCateva(ctr);
	while (true) {
		cout << "\nMeniu:\n";
		cout << "1 adauga\n2 tipareste\n3 sterge\n4 modifica\n5 filtrare tip\n6 filtrare suprafata\n7 cauta apartament";
		cout<<"\n8 sortare dupa nume\n9 sortare dupa suprafata\n10 sortare dupa tip si suprafata\n0 Iesire\nDati comanda : ";
		int cmd;
		cin >> cmd;
		try {
			switch (cmd) {
			case 1:
				adaugaUI();
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
//
//Prezentare::~Prezentare()
//{
//}

