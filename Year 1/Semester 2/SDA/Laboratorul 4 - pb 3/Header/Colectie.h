#pragma once

#define NULL_TELEM 0
typedef int TElem;
#define CAPACITATE 4
class IteratorColectie;

struct Pereche {
	TElem element;
	int frecventa;
};

class Colectie
{
	friend class IteratorColectie;

private:
	int cp = CAPACITATE;
	//vectorul static de elemente de tip TElem (cu spatiu fix de memorare - CAPACITATE)
	Pereche* e;
	//vectorul static pentru egaturi
	int* urm;
	//referinta catre primul element al listei
	int prim;
	//referinta catre primul element din lista spatiului liber
	int primLiber;

	//functii pentru alocarea/dealocarea unui spatiu liber
	//se returneaza pozitia unui spatiu liber in lista
	int aloca();
	//dealoca spatiul de indice i
	void dealoca(int i);
	//functie privata care creeaza un nod in lista inlantuita
	int creeazaNod(TElem e);
public:
	//constructorul implicit
	Colectie();
	void redim();

	//adauga un element in colectie
	void adauga(TElem e);

	//sterge o aparitie a unui element din colectie
	//returneaza adevarat daca s-a putut sterge
	bool sterge(TElem e);

	//verifica daca un element se afla in colectie
	bool cauta(TElem elem) const;

	//returneaza numar de aparitii ale unui element in colectie
	int nrAparitii(TElem elem) const;


	//intoarce numarul de elemente din colectie;
	int dim() const;

	//verifica daca colectia e vida;
	bool vida() const;

	TElem celMaiPutinFrecvent() const;

	//returneaza un iterator pe colectie
	IteratorColectie iterator() const;

	// destructorul colectiei
	~Colectie();

};

