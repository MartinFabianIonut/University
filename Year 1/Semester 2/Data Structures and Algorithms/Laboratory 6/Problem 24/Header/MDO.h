#pragma once

#include <vector>

typedef int TCheie;
typedef int TValoare;
#define MAX 10
#include <utility>
typedef std::pair<TCheie, TValoare> TElem;

using namespace std;

class IteratorMDO;

typedef bool(*Relatie)(TCheie, TCheie);

//referire a clasei Nod
class Nod;

//se defineste tipul PNod ca fiind adresa unui Nod dintr-o lista inlantuita
typedef Nod* PNod;

class Nod
{
private:
	TElem e;
	PNod urm;
public:
	friend class MDO;
	friend class IteratorMDO;
	//constructor
	Nod(TElem e, PNod urm);
	TElem element();
	PNod urmator(); 
};

class MDO {
	friend class IteratorMDO;
private:
	/* aici e reprezentarea */
	//reprezentare folosind o TD - rezolvare coliziuni prin liste 
	int m; //numarul de locatii din tabela de dispersie
	PNod l[MAX]; //listele independente 

	Relatie rel;

	//functia de dispersie
	int d(TCheie c) const;
public:

	// constructorul implicit al MultiDictionarului Ordonat
	MDO(Relatie r);

	bool myrel(TCheie c1, TCheie c2);
	// adauga o pereche (cheie, valoare) in MDO
	void adauga(TCheie c, TValoare v);

	//cauta o cheie si returneaza vectorul de valori asociate
	vector<TValoare> cauta(TCheie c) const;

	//sterge o cheie si o valoare 
	//returneaza adevarat daca s-a gasit cheia si valoarea de sters
	bool sterge(TCheie c, TValoare v);

	//returneaza numarul de perechi (cheie, valoare) din MDO 
	int dim() const;

	//verifica daca MultiDictionarul Ordonat e vid 
	bool vid() const;

	// se returneaza iterator pe MDO
	// iteratorul va returna perechile in ordine in raport cu relatia de ordine
	IteratorMDO iterator() const;

	// destructorul 	
	~MDO();

	

};
