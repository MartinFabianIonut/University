#define _CRTDBG_MAP_ALLOC 
#include <stdlib.h> 
#include <crtdbg.h> 
#include "prezentare.h"
#include "teste.h"
#include <stdio.h>


int main() {
	run_all_tests1();
	

	
	Repo repo = NULL;
	Service srv = NULL;
	Prezentare prezi = NULL;

	CreeazaRepo(&repo);
	CreeazaService(&srv, repo);
	CreeazaPrezentare(&prezi, srv);

	prezentare(prezi);

	DistrugeRepo(&repo);
	DistrugeService(&srv);
	DistrugePrezentare(&prezi);
	_CrtDumpMemoryLeaks();
	/**/

}
