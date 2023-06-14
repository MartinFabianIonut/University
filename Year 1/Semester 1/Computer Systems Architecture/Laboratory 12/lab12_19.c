#include <stdio.h>

void siruri(int sir[100], int dimensiune);

//Se citesc din fisierul numere.txt mai multe numere (pozitive si negative). Sa se creeze 2 siruri rezultat N si P astfel: N - doar numere negative si P - doar numere pozitive. Afisati cele 2 siruri rezultate pe ecran.

int main()
{
    char input_filename[] = "numere.txt";

    FILE *in = fopen(input_filename, "rt");

    if (in == NULL) {
        fprintf(stderr, "ERROR: Can't open file %s", input_filename);
        return -1;
    }

    int n, v[100], poz;

    poz = -1;
    while (!feof(in))
    {
        poz++;
        fscanf(in, "%d", &v[poz]);
    }

    fclose(in);

    siruri(v,poz+1); //apelul procedurii asm

    return 0;
}

void afisare(char mesaj[],int un_sir[], int dim)
{
    printf("%s ",mesaj); //daca e vorba de sirul cu numere pozitive sau negative
    int i;
    for (i = 0; i < dim; i++)
    {
        printf("%d ",un_sir[i]);
    }
    printf("\n");
}