#include <assert.h>

#include "MDO.h"
#include "IteratorMDO.h"
#include <iostream>
#include <exception>
#include <vector>

using namespace std;

bool relatie1(TCheie cheie1, TCheie cheie2) {
    if (cheie1 <= cheie2) {
        return true;
    }
    else {
        return false;
    }
}

void test_cerinta() {
    MDO dictOrd = MDO(relatie1);
    dictOrd.adauga(1, 2);
    dictOrd.adauga(1, 3);
    dictOrd.adauga(4, 6);
    dictOrd.adauga(2, 9);
    IteratorMDO it = dictOrd.iterator();
    it.prim();
    try {
        it.revinoKPasi(0);
    }
    catch (const exception& re) {
        std::cout << re.what();
    }
    try {
        it.revinoKPasi(6);
    }
    catch (const exception& re) {
        std::cout << re.what();
    }
    it.urmator();//sunt pe 1,2 - pt ca 1,3 e primul, asa se face adaugarea
    it.urmator();//sunt pe 2,9
    it.revinoKPasi(2);
    TElem e{1,3};
    assert(it.element() == e);
}

void testAll() {
    MDO dictOrd = MDO(relatie1);
    assert(dictOrd.dim() == 0);
    assert(dictOrd.vid());
    dictOrd.adauga(1, 2);
    dictOrd.adauga(1, 3);
    assert(dictOrd.dim() == 2);
    assert(!dictOrd.vid());
    vector<TValoare> v = dictOrd.cauta(1);
    assert(v.size() == 2);
    v = dictOrd.cauta(3);
    assert(v.size() == 0);

    IteratorMDO it = dictOrd.iterator();
    it.prim();
    
    while (it.valid()) {
        TElem e = it.element();
        it.urmator();
    }
    assert(dictOrd.sterge(1, 2) == true);
    assert(dictOrd.sterge(1, 3) == true);
    assert(dictOrd.sterge(2, 1) == false);
    assert(dictOrd.vid());

    test_cerinta();
}

