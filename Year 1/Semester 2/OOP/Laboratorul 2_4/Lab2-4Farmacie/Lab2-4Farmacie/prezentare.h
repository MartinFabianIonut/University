#ifndef PREZENTARE_H_

#define PREZENTARE_H_
#include "service.h"


typedef struct _Prezentare* Prezentare;

/*
Aloc spatiu pentru elemente de tip Prezentare
*/
void CreeazaPrezentare(Prezentare* prezi, Service srv);
/*
Deloc spatiu pentru elemente de tip Prezentare
*/
void DistrugePrezentare(Prezentare* prezi);

void meniu();

void ui_afiseaza(Prezentare prezi);
void ui_adauga(Prezentare prezi);

/*
functia principala
*/
int prezentare(Prezentare prezi);


#endif