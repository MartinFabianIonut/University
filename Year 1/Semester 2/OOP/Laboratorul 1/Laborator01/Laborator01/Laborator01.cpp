#include <stdio.h>
#include <exception>

/*
Functie care genereaza toate numerele prime pana la un n dat, n - integer
    int: n - int, lista - vector de int
    pre: n>0
    post: este contruita lista cu numerele prime
*/
void generare(int n, int lista[100]) {
    int i = 1, cate = 1;
    int curent=3,nr;
    lista[0] = 2;
    while (i <= n) {
        for (nr = 2; nr <= curent - 1; nr++) {
            if (curent % nr == 0)
                break;
        }
        if (nr == curent) {
            lista[cate++] = nr;
            i++;
        }
        curent++;
    }
}

/*
Functie care genereaza sirul corespunzator numarului primit ca parametru, anume daca e prim, 
se afiseaza el si toate numerele pana la 1, altfel se afiseaza numarul si divizorii sai proprii, 
divizorul d repetandu-se de d ori
    pre: n>0
    post: o lista afisata
*/
void sub5(int n)
{
    int i,j,divizori[100],k;
    bool prim;
    if (n == 1) {
        printf("%d", 1);
    }        
    else {
        prim = true;
        divizori[1] = n;
        k = 1;
        for (i = 2; i < n; i++) {
            if (n % i == 0) {
                prim = false;
                for (j = 1; j <= i; j++) {
                    divizori[++k] = i;
                }
            }
        }
        if (prim == true) {
            for (i = n-1; i > 0; i--) {
                divizori[++k] = i;
            }
        }
        for (i = 1; i <= k; i++) {
            printf("%d ", divizori[i]);
        }
    }
}

int main()
{
    int n,repetare;
    int lista[100] = { 0 };
    bool ok = false;
    do
    {
        printf("Introdu cate numere prime doresti sa generez: ");
        scanf_s("%d", &n);
        if (n < 1)
            printf("Trebuie introdus un numar intreg >= 1!\n");
        else
        {
            printf("Cele %d numere prime sunt: ", n);
            generare(n, lista);
            for (int k = 0; k <= n - 1; k++)
                printf("%d ", lista[k]);
        }
        printf("\nDoresti repetarea actiunii? 1 - da, 2 - nu.");
        scanf_s("%d", &repetare);
        if (repetare == 1)
            ok = true;
        else
            ok = false;
    } while (ok == true);
    int nn;
    do
    {
        printf("\nIntrodu numarul: ");
        scanf_s("%d", &nn);
        if (n < 1)
            printf("Trebuie introdus un numar intreg >= 1!\n");
        else
        {
            printf("Lista dorita: ");
            sub5(nn);
            printf("\nDoresti repetarea actiunii? 1 - da, 2 - nu.");
            scanf_s("%d", &repetare);
            if (repetare == 1)
                ok = true;
            else
                ok = false;
        }
    } while (ok == true);
    return 0;
}
