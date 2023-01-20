#define _CRTDBG_MAP_ALLOC 
#include <stdlib.h> 
#include <crtdbg.h> 
#include <iostream>
#include "TestExtins.h"
#include "TestScurt.h"
using namespace std;


int main() {
	testAll();
	testAllExtins();
	_CrtDumpMemoryLeaks();
	cout << "End";
}