#define _CRTDBG_MAP_ALLOC 
#include <stdlib.h> 
#include <crtdbg.h> 
#include <iostream>
#include "TestExtins.h"
#include "TestScurt.h"


//17.TAD MultiDicționar –reprezentare  sub  forma  unei  LSI 
// în  care  apar  toate  perechile  de  forma (cheie, valoare).
// O cheie poate apărea în listă de mai multe ori.

using namespace std;

int main() {

	testAll();
	testAllExtins();
	_CrtDumpMemoryLeaks();
	cout << "End";

}