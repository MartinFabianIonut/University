#include "valid.h"

void Valid::valideazaprocesor(Procesor& p)
{
	string mesaj = "";
	if (p.get_nume().size() == 0) { mesaj += "Nume vid!\n"; }
	if (p.get_nume().find(" ") != string::npos) { mesaj += "Numele contine spatiu!\n"; }
	if (p.get_nr() <= 0) { mesaj += "Numar mai mic sau egal cu 0!\n"; }
	if (p.get_soclu().size() == 0) { mesaj += "Soclu vid!\n"; }
	if (p.get_soclu().find(" ") != string::npos) { mesaj += "Numele soclului contine spatiu!\n"; }
	if (p.get_pret() <= 0) { mesaj += "Numar mai mic sau egal cu 0!\n"; }
	if (mesaj.size() > 0) {
		throw ValidException(mesaj);
	}
}

void Valid::valideazaplaca(Placa& p)
{
	string mesaj = "";
	if (p.get_nume().size() == 0) { mesaj += "Nume vid!\n"; }
	if (p.get_nume().find(" ") != string::npos) { mesaj += "Numele contine spatiu!\n"; }
	if (p.get_soclu().size() == 0) { mesaj += "Soclu vid!\n"; }
	if (p.get_soclu().find(" ") != string::npos) { mesaj += "Numele soclului contine spatiu!\n"; }
	if (p.get_pret() <= 0) { mesaj += "Numar mai mic sau egal cu 0!\n"; }
	if (mesaj.size() > 0) {
		throw ValidException(mesaj);
	}
}
