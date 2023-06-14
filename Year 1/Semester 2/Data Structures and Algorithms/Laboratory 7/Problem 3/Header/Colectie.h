#pragma once

typedef int TElem;
#define NULL_TELEM 0
#define CAPACITATE 60100
typedef bool(*Relatie)(TElem, TElem);
#include <vector>
using std::vector;

//in implementarea operatiilor se va folosi functia (relatia) rel (de ex, pentru <=)
// va fi declarata in .h si implementata in .cpp ca functie externa colectiei
bool rela(TElem, TElem);

class IteratorColectie;


struct StangaDreapta {
	TElem element;
};


class Colectie {

	friend class IteratorColectie;

private:
	/* aici e reprezentarea */
	int cp = CAPACITATE;

	Relatie rel;
	int cate;

	StangaDreapta* e;
	//vectorul static pentru egaturi
	int* stanga, *dreapta;
	//referinta catre primul element al listei
	int prim;
	//referinta catre primul element din lista spatiului liber
	int primLiber;

	//functii pentru alocarea/dealocarea unui spatiu liber
	//se returneaza pozitia unui spatiu liber in lista
	int aloca();
	//dealoca spatiul de indice i
	void dealoca(int i);
	


public:
	//constructorul implicit
	//void redim();
	Colectie();

	//adauga un element in colectie
	void adauga(TElem e);

	//sterge o aparitie a unui element din colectie
	//returneaza adevarat daca s-a putut sterge
	bool sterge(TElem e);
	bool stergee(int i,TElem elem);
	//verifica daca un element se afla in colectie
	bool cauta(TElem elem) const;
	bool cautaa(int i, TElem elem)const;
	//returneaza numar de aparitii ale unui element in colectie
	int nrAparitii(TElem elem) const;

	int stergeRecursiv(int i);
	int stergeToateElementeleRepetitive();

	//intoarce numarul de elemente din colectie;
	int dim() const;
	int dimm(int i) const;

	void doo(TElem elem, int prim, int i);
	int apar(int i, TElem elem)const;

	TElem mini(int i);

	//verifica daca colectia e vida;
	bool vida() const;

	//returneaza un iterator pe colectie
	IteratorColectie iterator() const;

	// destructorul colectiei
	~Colectie();


};

