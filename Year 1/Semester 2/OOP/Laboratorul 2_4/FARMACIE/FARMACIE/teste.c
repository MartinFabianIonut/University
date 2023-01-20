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
    float concentratie = 200;
    int cantitate = 3;
    medi = InitializeazaMedicament(cod, nume, concentratie, cantitate);
    assert(GetID(medi) == 0);
    assert(GetCod(medi) == cod);
    assert(strcmp(medi.nume, "Paduden") == 0);
    char* loc = NULL;
    GetNume(medi, &loc);
    assert(strcmp(loc, "Paduden") == 0);
    free(loc);
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
    medi = InitializeazaMedicament(cod, nume, concentratie, cantitate);
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
    Repo repo = createEmpty();
    Medicament medi;
    medi = InitializeazaMedicament(12, "Algocalmin", 200, 100);
    adaugaMedicamentStoc(&repo, medi);
    assert(dimensiune(&repo) == 1);
    adaugaMedicamentStoc(&repo, medi);
    assert(dimensiune(&repo) == 1);
    Medicament m = get(&repo, 0);
    assert(m.cantitate == 200);
    Repo repoTotal = CopiazaRepo(&repo);
    assert(dimensiune(&repoTotal) == 1);
    DistrugeRepo(&repo);
    DistrugeRepo(&repoTotal);
}

void test_adauga_medicament_repo() {
    Repo repo = createEmpty();
    Medicament medi = InitializeazaMedicament(111, "Paracetamol", 12, 10000);
    SetCod(&medi, 12);
    SetNume(&medi, "Algocalmin");
    SetConcentratie(&medi, 200);
    SetCantitate(&medi, 1000);
    adaugaMedicamentStoc(&repo, medi);
    assert(GetCod(medi) == 12);
    assert(strcmp(medi.nume, "Algocalmin") == 0);
    assert(GetConcentratie(medi) == 200);
    assert(GetCantitate(medi) == 1000);
    medi = InitializeazaMedicament(112, "Paracetamol", 12, 10000);
    adaugaMedicamentStoc(&repo, medi);
    medi = InitializeazaMedicament(113, "Paracetamol", 12, 10000);
    adaugaMedicamentStoc(&repo, medi);
    //testez redimensionarea
    assert(dimensiune(&repo) == 3);
    StergeMedicamentRepo(&repo, 0);
    Medicament m = get(&repo, 0);
    assert(m.id == 1);
    assert(repo.medicamente[1].id==2);
    assert(dimensiune(&repo) == 2);
    Medicament mediNou = InitializeazaMedicamentID(2, 113, "Larofen", 25, 10000);
    ActualizareRepo(&repo, mediNou);
    m = get(&repo, 1);
    assert(m.id == 2);
    assert(strcmp(m.nume, "Larofen") == 0);
    assert(repo.medicamente[1].concentratie == 25);
    DistrugeMedicament(&mediNou);
    DistrugeRepo(&repo);
}

void test_creeaza_srv() {
    Service srv = CreeazaService();
    int erori[4] = { 0 };
    AdaugaMedicamentStoc(&srv, 12, "Tamtum", 23, 144, erori);
    assert(dimensiune(&srv.repo) == 1);
    AdaugaMedicamentStoc(&srv, 12, "Tamtum", 23, 144, erori);
    assert(dimensiune(&srv.repo) == 1);
    AdaugaMedicamentStoc(&srv, -12, "Tamtum", 23, 144, erori);
    int erori2[4] = { 0 };
    assert(dimensiune(&srv.repo) == 1);
    assert(srv.repo.medicamente[0].id == 1);
    Medicament m = InitializeazaMedicamentID(1, 12, "Tamtum", 23, 144);
    assert(AflaPozitieMedicamentInRepo(&srv, m) == 0);
    DistrugeMedicament(&m);
    m = InitializeazaMedicamentID(100, 12, "Tamtum", 23, 144);
    assert(AflaPozitieMedicamentInRepo(&srv, m) == -1);
    DistrugeMedicament(&m);
    ActualizareSrv(&srv, 1, "Strepsils", 45, erori2);
    assert(srv.repo.medicamente[0].concentratie == 45);
    DistrugeService(&srv);
}

void test_sortari_srv() {
    Service srv = CreeazaService();
    int erori[4] = { 0 };
    AdaugaMedicamentStoc(&srv, 111, "Paracetamol", 12, 1000,erori);
    AdaugaMedicamentStoc(&srv, 171, "Ibuprofen", 92, 700, erori);
    AdaugaMedicamentStoc(&srv, 211, "Silimarina", 66, 800, erori);
    AdaugaMedicamentStoc(&srv, 921, "Diclofenac", 92, 450, erori);
    Repo repoSortat = OrdonareNume(&srv, 1);
    assert(strcmp(get(&repoSortat, 0).nume, "Diclofenac") == 0);
    assert(strcmp(get(&repoSortat, 1).nume, "Ibuprofen") == 0);
    assert(strcmp(get(&repoSortat, 2).nume, "Paracetamol") == 0);
    assert(strcmp(get(&repoSortat, 3).nume, "Silimarina") == 0);
    DistrugeRepo(&repoSortat);
    repoSortat = OrdonareNume(&srv, 2);
    assert(strcmp(get(&repoSortat, 0).nume, "Silimarina") == 0);
    assert(strcmp(get(&repoSortat, 1).nume, "Paracetamol") == 0);
    assert(strcmp(get(&repoSortat, 2).nume, "Ibuprofen") == 0);
    assert(strcmp(get(&repoSortat, 3).nume, "Diclofenac") == 0);
    DistrugeRepo(&repoSortat);
    repoSortat = OrdonareCantitate(&srv, 1);
    assert(get(&repoSortat, 0).cantitate == 450);
    assert(get(&repoSortat, 1).cantitate == 700);
    assert(get(&repoSortat, 2).cantitate == 800);
    assert(get(&repoSortat, 3).cantitate == 1000);
    DistrugeRepo(&repoSortat);
    repoSortat = OrdonareCantitate(&srv, 2);
    assert(get(&repoSortat, 0).cantitate == 1000);
    assert(get(&repoSortat, 1).cantitate == 800);
    assert(get(&repoSortat, 2).cantitate == 700);
    assert(get(&repoSortat, 3).cantitate == 450);
    DistrugeRepo(&repoSortat);

    Repo repoFiltrat = FiltreazaStoc(&srv, 100);
    assert(dimensiune(&repoFiltrat)==0);
    DistrugeRepo(&repoFiltrat);
    repoFiltrat = FiltreazaStoc(&srv, 1001);
    assert(dimensiune(&repoFiltrat) == 4);
    DistrugeRepo(&repoFiltrat);
    repoFiltrat = FiltreazaLitera(&srv, "P");
    assert(dimensiune(&repoFiltrat) == 1);
    DistrugeRepo(&repoFiltrat);
    repoFiltrat = FiltreazaLitera(&srv, "X");
    assert(dimensiune(&repoFiltrat) == 0);
    DistrugeRepo(&repoFiltrat);

    DistrugeService(&srv);
}

void test_copiere_srv() {
    Service srv = CreeazaService();
    int erori[4] = { 0 };
    AdaugaMedicamentStoc(&srv, 111, "Paracetamol", 12, 1000, erori);
    Repo repoCopiat = Copiere(&srv);
    assert(repoCopiat.medicamente[0].cod == 111);
    DistrugeRepo(&repoCopiat);
    DistrugeService(&srv);
}

void test_stergere_srv() {
    Service srv = CreeazaService();
    Medicament medi = InitializeazaMedicamentID(1, 111, "Paracetamol", 12, 1000);
    int erori[4] = { 0 };
    AdaugaMedicamentStoc(&srv, 111, "Paracetamol", 12, 1000, erori);
    assert(srv.repo.n == 1);
    StergeMedicamentSrv(&srv, medi);
    assert(srv.repo.n == 0);
    DistrugeMedicament(&medi);
    DistrugeService(&srv);
}

void run_all_tests1() {
    test_validare_corecta();
    test_validare_invalid();
    test_creeaza_repo();
    test_adauga_medicament_repo();
    test_creeaza_srv();
    test_sortari_srv();
    test_copiere_srv();
    test_stergere_srv();
}



