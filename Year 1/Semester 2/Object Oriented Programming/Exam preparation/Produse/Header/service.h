#pragma once

#include "repo.h"
#include "validator.h"

class Service
{
private:
	Repo& repo;
	Validator& valid;
public:
	
	Service(Repo& _repo, Validator& _valid) : repo{ _repo }, valid{ _valid } {}

	void adauga_produs(int _id, string _nume, string _tip, double _pret);
	vector < Produs > get_all();
};
