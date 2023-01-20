#pragma once
constexpr auto INITIAL_CAPACITY = 1;


template<typename TElem>
class IteratorVector;

template<typename TElem>
class VectorDinamic {
private:
	//reprezentare
	TElem* elems;
	int capacitate;
	int lungime;

	//resize
	void resize();
public:
	using value_type = TElem;
	//default constructor
	VectorDinamic() noexcept;
	//copy constructor
	VectorDinamic(const VectorDinamic& ot);
	//assignment operator
	VectorDinamic& operator=(const VectorDinamic& ot);
	//destructor
	~VectorDinamic();

	//add element in vector
	void add(TElem elem);
	void push_back(TElem elem);
	//get element from position in vector
	TElem& get(int poz) noexcept;
	//set element on position in vector
	void set(TElem elem, int poz);
	//get size - length of vector
	int size() noexcept;
	void deleteelement(const TElem& el);
	//iterator methods
	friend class IteratorVector<TElem>;

	//ca sa folosim for each, avem nevoie de aceste metode
	IteratorVector<TElem> begin() noexcept;
	IteratorVector<TElem> end() noexcept;

};

template<typename TElem>
void VectorDinamic<TElem>::deleteelement(const TElem& el) {
	int i = 0;
	while (elems[i] != el) {
		i++;
	}
	while (i < lungime - 1) {
		elems[i] = elems[i + 1];
		i++;
	}
	lungime--;
}

template<typename TElem>
class IteratorVector {
private:
	const VectorDinamic<TElem>& vec;
	int poz = 0;
	
public:
	using difference_type = long;
	using value_type = TElem;
	using pointer = TElem*;
	using reference = TElem&;
	using iterator_category = std::random_access_iterator_tag;
	IteratorVector(const VectorDinamic<TElem>& v) noexcept;
	IteratorVector(const VectorDinamic<TElem>& v, int poz) noexcept;
	bool valid() const;
	TElem& element() const noexcept;
	void next() noexcept;
	TElem& operator*() noexcept;
	IteratorVector& operator++() noexcept;
	IteratorVector& operator=(const IteratorVector<TElem>& ot) noexcept;
	long operator-(const IteratorVector<TElem>& ot) noexcept;
	bool operator==(const IteratorVector& ot) noexcept;
	bool operator!=(const IteratorVector& ot) noexcept;
};

template<typename TElem>
VectorDinamic<TElem>::VectorDinamic() noexcept {
	this->elems = new TElem[INITIAL_CAPACITY];
	this->lungime = 0;
	this->capacitate = INITIAL_CAPACITY;
}

template<typename TElem>
VectorDinamic<TElem>::VectorDinamic(const VectorDinamic& ot) {
	//constructor de copiere
	//se creeaza un nou vector dinamic 
	//pe baza unuia existent

	//fiindca se creeaza un nou vector dinamic, trebuie sa alocam spatiu
	this->elems = new TElem[ot.capacitate];

	//copiem elementele si setam lungime, capacitate
	for (int i = 0; i < ot.lungime; i++) {
		this->elems[i] = ot.elems[i];
	}
	this->lungime = ot.lungime;
	this->capacitate = ot.capacitate;
}


template<typename TElem>
VectorDinamic<TElem>& VectorDinamic<TElem>::operator=(const VectorDinamic& ot) {
	
	//fiindca in obiectul curent punem altceva prin
	//assignment, eliberam memoria alocata anterior

	delete[] this->elems;

	//copiem elementele din ot si setam lungime, capacitate
	this->elems = new TElem[ot.capacitate];
	for (int i = 0; i < ot.lungime; i++) {
		elems[i] = ot.elems[i];  //utilizeaza operator assignment aferent tipului T
	}
	this->lungime = ot.lungime;
	this->capacitate = ot.capacitate;
	return *this;
}
template<typename TElem>
void VectorDinamic<TElem>::resize() {
	int const newCapacity = this->capacitate * 2;
	TElem* newElems = new TElem[newCapacity];
	for (int i = 0; i < this->lungime; i++) {
		newElems[i] = this->elems[i];
	}
	delete[] this->elems;
	this->elems = newElems;
	this->capacitate = newCapacity;

}
template<typename TElem>
VectorDinamic<TElem>::~VectorDinamic() {
	delete[] this->elems;
}
template<typename TElem>
void VectorDinamic<TElem>::add(TElem elem) {
	if (this->lungime == this->capacitate)
		resize();
	this->elems[this->lungime] = elem;
	this->lungime++;
}

template<typename TElem>
void VectorDinamic<TElem>::push_back(TElem elem) {
	if (this->lungime == this->capacitate)
		resize();
	this->elems[this->lungime] = elem;
	this->lungime++;
}

template<typename TElem>
TElem& VectorDinamic<TElem>::get(int poz) noexcept {
	//verificam daca pozitia este valida

	return this->elems[poz];
}

template<typename TElem>
void VectorDinamic<TElem>::set(TElem elem, int poz) {
	//verificam daca pozitia este valida

	this->elems[poz] = elem;
}

template<typename TElem>
int VectorDinamic<TElem>::size() noexcept{
	//verificam daca pozitia este valida

	return this->lungime;
}

template<typename TElem>
IteratorVector<TElem> VectorDinamic<TElem>::begin() noexcept {
	return IteratorVector<TElem>(*this);
}
template<typename TElem>
IteratorVector<TElem> VectorDinamic<TElem>::end() noexcept {
	return IteratorVector<TElem>(*this, this->lungime);
}



template<typename TElem>
IteratorVector<TElem>::IteratorVector(const VectorDinamic<TElem>& v) noexcept :vec{ v } { };
template<typename TElem>
IteratorVector<TElem>::IteratorVector(const VectorDinamic<TElem>& v, int poz) noexcept :vec{ v }, poz{ poz }{};

template<typename TElem>
bool IteratorVector<TElem>::valid() const {
	return this->poz < this->vec.lungime;
}
template<typename TElem>
TElem& IteratorVector<TElem>::element() const noexcept {
	return this->vec.elems[this->poz];
}
template<typename TElem>
void IteratorVector<TElem>::next() noexcept {
	this->poz++;
}

template<typename TElem>
TElem& IteratorVector<TElem>::operator*() noexcept {
	return this->element();
}

template<typename TElem>
IteratorVector<TElem>& IteratorVector<TElem>::operator++()noexcept {
	this->next();
	return *this;
}

template<typename TElem>
inline IteratorVector<TElem>& IteratorVector<TElem>::operator=(const IteratorVector<TElem>& ot) noexcept
{
	//*this = IteratorVector<T>(ot);
	//exit(1);
	new (this) IteratorVector<TElem>(ot);
	return *this;
}

template<typename TElem>
long IteratorVector<TElem>::operator-(const IteratorVector<TElem>& ot) noexcept
{
	return this->poz - ot.poz;
}

template<typename TElem>
bool IteratorVector<TElem>::operator==(const IteratorVector& ot) noexcept {
	return this->poz == ot.poz;
}
template<typename TElem>
bool IteratorVector<TElem>::operator!=(const IteratorVector& ot) noexcept {
	return !(*this == ot);
}