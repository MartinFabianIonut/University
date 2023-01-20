#define _CRTDBG_MAP_ALLOC 
#include <stdlib.h> 
#include <crtdbg.h> 
#include <iostream>
#include "teste.h"
#include "service.h"
#include "repo.h"
#include "prezentare.h"


int main() {
	{
		Teste t;
		t.ruleaza_teste();

		/*Repo repo;
		LocatarValidator val;
		Service ctr{ repo,val };

		Prezentare ui{ ctr };
		ui.start();*/
		 
	}
	_CrtDumpMemoryLeaks();
	return 0;
}