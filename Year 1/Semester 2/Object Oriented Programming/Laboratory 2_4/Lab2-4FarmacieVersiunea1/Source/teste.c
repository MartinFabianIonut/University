#include "domain.h"
#include "repo.h"
#include "service.h"
#include "valid.h"
#include <assert.h>
#include <string.h>
#include <stdlib.h>

void test_validare_corecta() {
    Medicament medi;
    int cod = 14;
    char* nume = "Paduden";
    char*sir = NULL;
    float concentratie = 200;
    int cantitate = 3;
    medi = InitializeazaMedicament(cod, nume, concentratie, cantitate);
    assert(GetCod(medi) == cod);
    GetNume(medi, &sir);
    assert(strcmp(sir, "Paduden")==0);
    free(sir);
    assert(GetConcentratie(medi) == concentratie);
    assert(GetCantitate(medi) == cantitate);
    int erori[4] = { 0 };
    assert(valideaza(medi, erori) == 1);
    DistrugeMedicament(&medi);
}

void test_validare_invalid() {
    Medicament medi;
    int cod = -14;
    char* nume = "";
    float concentratie = -1;
    int cantitate = -3;
    medi=InitializeazaMedicament(cod,nume,concentratie,cantitate);
    int erori[4] = { 0 };
    assert(valideaza(medi, erori) == 0);
    assert(erori[0] == 1 && erori[1] == 1 && erori[2] == 1 && erori[3] == 1);
    DistrugeMedicament(&medi);
    char* nume_valid = "Diclofenac";
    int erori2[4] = { 0 };
    medi = InitializeazaMedicament(cod, nume_valid, concentratie, cantitate);
    assert(valideaza(medi, erori2) == 0);
    assert(erori2[0] == 1 && erori2[1] == 0 && erori2[2] == 1 && erori2[3] == 1);
    DistrugeMedicament(&medi);
}

void test_creeaza_repo() {
    Repo repo;
    Medicament medi;
    medi=InitializeazaMedicament(12,"Algocalmin",200,100);

    CreeazaRepo(&repo);
    adaugaMedicamentStoc(repo, medi);
    assert(dimensiune(repo) == 1);
    DistrugeMedicament(&medi);
    assert(dimensiune(repo) == 1);
    DistrugeRepo(&repo);
    //distrug, pentru ca la get de toate
    //sa se recreeze instanta medi si sa pot accesa din nou elems
    //GetMedicamentDupaIndiceRepo(repo, 0, &medi);
    /*int n;
    Repo repoTotal = CopiazaRepo(repo);
    assert(dimensiune(repoTotal)==1);
    repoTotal->
    assert(strcmp(GetNume(medi2[0]), "Algocalmin") == 0);
    assert(GetConcentratie(medi2[0]) == 200);
    assert(GetCantitate(medi2[0]) == 1000);
    
    DistrugeMedicament(&medi2);*/
}

//void test_adauga_medicament_repo() {
//    Repo repo;
//    Medicament medi;
//    InitializeazaMedicament(&medi);
//    SetCod(medi, 12);
//    SetNume(medi, "Algocalmin");
//    SetConcentratie(medi, 200);
//    SetCantitate(medi, 1000);
//    CreeazaRepo(&repo);
//    adaugaMedicamentStoc(repo, medi);
//    DistrugeMedicament(&medi);
//    int n;
//    Medicament* medi2 = NULL;
//    GetToateMedicamenteleRepo(repo, &medi2, &n);
//
//    InitializeazaMedicament(&medi);
//    SetCod(medi, 13);
//    SetNume(medi, "Algocalmin");
//    SetConcentratie(medi, 200);
//    SetCantitate(medi, 1000);
//    adaugaMedicamentStoc(repo, medi);
//    DistrugeMedicament(&medi);
//    GetToateMedicamenteleRepo(repo, &medi2, &n);
//
//    InitializeazaMedicament(&medi);
//    SetCod(medi, 14);
//    SetNume(medi, "Algocalmin");
//    SetConcentratie(medi, 200);
//    SetCantitate(medi, 1000);
//    adaugaMedicamentStoc(repo, medi);
//    DistrugeMedicament(&medi);
//    GetToateMedicamenteleRepo(repo, &medi2, &n);
//
//    assert(n == 3);//vreau sa verific daca reuseste sa faca redimensionarea
//    
//    InitializeazaMedicament(&medi);
//    SetCod(medi, 13);//adaug medicament cu acelasi id - nu va adauga
//    SetNume(medi, "Algocalmin");
//    SetConcentratie(medi, 200);
//    SetCantitate(medi, 1000);
//    adaugaMedicamentStoc(repo, medi);
//    DistrugeMedicament(&medi);
//    GetToateMedicamenteleRepo(repo, &medi2, &n);
//
//    assert(n == 3);
//}
//
//void test_creeaza_srv() {
//    Service srv;
//    Repo repo;
//    Medicament *toateMedicamentele = NULL;
//    int n = 0;
//    CreeazaRepo(&repo);
//    CreeazaService(&srv,repo);
//    int erori[4] = { 0 };
//    AdaugaMedicamentStoc(srv, 12, "Tamtum", 23, 144, erori);
//    GetToateMedicamenteleSrv(srv, &toateMedicamentele, &n);
//    assert(n == 1);
//    assert(GetCod(toateMedicamentele[0]) == 12);
//    free(toateMedicamentele);
//    DistrugeRepo(&repo);
//    DistrugeService(&srv);
//}

void run_all_tests1() {
    test_validare_corecta();
    test_validare_invalid();
    test_creeaza_repo();
    /*test_adauga_medicament_repo();
    test_creeaza_srv();*/
}

