
#define _CRTDBG_MAP_ALLOC 
#include <stdlib.h> 
#include <crtdbg.h> 
#include <iostream>

#include "TestScurt.h"
#include "TestExtins.h"


/*
TAD MultiDictionar Ordonat –memorarea  tuturor  perechilor  de  forma(cheie, valoare), reprezentare sub forma unei TD cu rezolvare coliziuni prin liste independente.
*/


int main() {
    {
       testAll();
       testAllExtins();
    }
    std::cout << "Finished Tests!" << std::endl;
    _CrtDumpMemoryLeaks();
}
