#define _CRTDBG_MAP_ALLOC 
#include <stdlib.h> 
#include <crtdbg.h> 
#include "prezentare.h"
#include "teste.h"
#include <stdio.h>

/*
	Creator: Martin Fabian-Ionut
*/

int main() {
	run_all_tests1();
	
	Prezentare prezi = CreeazaPrezentare();

	prezentare(&prezi);
	
	DistrugePrezentare(&prezi);
	
	_CrtDumpMemoryLeaks();
}
