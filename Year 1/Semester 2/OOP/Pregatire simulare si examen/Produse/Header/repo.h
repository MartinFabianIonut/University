#pragma once

#include "domeniu.h"
#include <vector>

using std::vector;
using std::string;

class Repo
{
private:
	vector < Produs > produse;
	string path;

	void read();
	void write();
public:
	Repo(string path): path{path}{ read(); }

	void adauga(Produs& p);
	int size();
	vector < Produs > get_all();
};

class GeneralExceptions
{
private:
	string mesaj;
public:
	GeneralExceptions(const string& mesaj) : mesaj{ mesaj } {}
	const string& getMessage() const { return mesaj; }
};

class ValidException : public GeneralExceptions
{
public:
	ValidException(string mesaj) : GeneralExceptions(mesaj) {}
};

class RepoException : public GeneralExceptions
{
public:
	RepoException(string mesaj) : GeneralExceptions(mesaj) {}
};

