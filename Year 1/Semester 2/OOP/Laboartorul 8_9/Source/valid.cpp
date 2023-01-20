#include "valid.h"
#include <assert.h>
#include <sstream>


ostream& operator<<(ostream& out, const ValidateException& ex)
{
	for (const auto& msg : ex.msgs) {
		out << msg<<" ";
	}
	return out;
}

void LocatarValidator::validate(const Locatar& l)
{
	vector<string> msgs;
	if (l.get_ap() < 0) msgs.push_back("Ap negativ!!!");
	if (l.get_nume().size() == 0) msgs.push_back("Nume vid!!!");
	if (l.get_sup() < 0.0) msgs.push_back("Sup negativa!!!");
	if (l.get_tip() < 0) msgs.push_back("Tip negativ!!!");
	if (msgs.size() > 0) {
		throw ValidateException(msgs);
	}
}

