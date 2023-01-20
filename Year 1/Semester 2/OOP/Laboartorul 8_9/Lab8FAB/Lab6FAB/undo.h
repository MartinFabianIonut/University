#pragma once

#include "pok.h"
#include "repo.h"
#include "reponou.h"

class ActiuneUndo {
public:
	virtual void doUndo() = 0;
	//destructorul e virtual pentru a ne asigura ca daca dau delete pe un 
	 // pointer se apeleaza destructorul din clasa care trebuie
	virtual ~ActiuneUndo() = default;
};

class UndoAdauga : public ActiuneUndo {
	int apLocatarAdaugat;
	RepoAbstract& repo;
public:
	UndoAdauga(RepoAbstract& repo, const int& ap) :repo{ repo }, apLocatarAdaugat{ ap } {}
	void doUndo() override {
		repo.sterge_dupa_id(apLocatarAdaugat);
	}
};

class UndoSterge : public ActiuneUndo {
	Locatar locatarSters;
	RepoAbstract& repo;
public:
	UndoSterge(RepoAbstract& repo, const Locatar& l) :repo{ repo }, locatarSters{ l } {}
	void doUndo() override {
		repo.adauga(locatarSters);
	}
};

class UndoModifica : public ActiuneUndo {
	Locatar locatarModificat;
	RepoAbstract& repo;
public:
	UndoModifica(RepoAbstract& repo, const Locatar& l) :repo{ repo }, locatarModificat{ l } {}
	void doUndo() override {
		repo.modifica(locatarModificat);
	}
};