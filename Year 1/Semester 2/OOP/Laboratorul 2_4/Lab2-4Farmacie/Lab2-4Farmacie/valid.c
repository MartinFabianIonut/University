#include "valid.h"
#include "domain.h"
#include <string.h>
#include <stdlib.h>

int valideaza(Medicament medi, int erori[4]) {
    if (GetCod(medi) < 1) {
        erori[0] = 1;
    }
    char* sir = NULL;
    GetNume(medi, &sir);
    if (strcmp(sir, "") == 0) {
        erori[1] = 1;
    }
    free(sir);
    if (GetConcentratie(medi) < 0.1) {
        erori[2] = 1;
    }
    if (GetCantitate(medi) < 0) {
        erori[3] = 1;
    }
    for (int i = 0; i <= 3; i++) {
        if (erori[i] == 1) {
            return 0;
        }
    }
    return 1;
}

