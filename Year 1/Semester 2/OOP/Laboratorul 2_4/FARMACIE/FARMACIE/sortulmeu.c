#include "sortulmeu.h"


void sortulmeu(Repo* repo, FunctieComparare cmpF, int cresc_descresc) {
	int i, j;
	for (i = 0; i < dimensiune(repo); i++) {
		for (j = i + 1; j < dimensiune(repo); j++) {
			Medicament m1 = get(repo, i);
			Medicament m2 = get(repo, j);
			if (cmpF(m1, m2, cresc_descresc) > 0) {
				//interschimbam
				set(repo, i, m2);
				set(repo, j, m1);
			}
		}
	}
}
