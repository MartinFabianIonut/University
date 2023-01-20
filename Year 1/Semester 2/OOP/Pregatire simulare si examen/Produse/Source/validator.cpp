#include "validator.h"


void Validator::valideaza(Produs& p)
{
	string errors;

	if ("" == p.get_nume() || p.get_nume().find(" ") != string::npos)
		errors = errors + "Nume produs invalid.\n";
	if (p.get_pret() < 1 || p.get_pret() > 100)
		errors = errors + "Pret produs invalid.\n";
	if (errors != "")
		throw ValidException(errors);
}
