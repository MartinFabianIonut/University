#define _CRTDBG_MAP_ALLOC 
#include <stdlib.h> 
#include <crtdbg.h> 
#include <iostream>
#include "teste.h"
#include "service.h"
#include "repo.h"
#include "reponou.h"
#include "prezentare.h"


int main() {
	Teste t;
	t.ruleaza_teste();
	
	{

		//RepoNou repo{50};
		RepoFile repo{"loc.txt"};
		LocatarValidator val;
		Service ctr{ repo,val };

		Prezentare ui{ ctr };
		ui.start(); 
	}
	
	_CrtDumpMemoryLeaks();
	return 0;
}