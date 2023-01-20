#include "valid.h"
#include <assert.h>
#include <sstream>


ostream& operator<<(ostream& out, const ValidateException& ex)
{
	for (const auto& msg : ex.msgs) {
		out << msg << " ";
	}
	return out;
}

void LocatarValidator::validate(const Locatar& l)
{
	vector<string> msgs;
	if (l.get_ap() <= 0) { msgs.push_back("Apartament negativ sau egal cu 0!!!"); }
	if (l.get_nume().size() == 0) { msgs.push_back("Nume vid!!!"); }
	if (l.get_nume().find(" ") != string::npos) {
		msgs.push_back("Nume invalid, deoarece contine spatii!!!");
	}
	if (l.get_sup() <= 0.0) { msgs.push_back("Suprafata negativa sau egala cu 0!!!"); }
	if (l.get_tip() <= 0 || l.get_tip()>6) { msgs.push_back("Tip negativ sau egal cu 0, ori mai mare decat 6!!!"); }
	if (msgs.size() > 0) {
		throw ValidateException(msgs);
	}
}

