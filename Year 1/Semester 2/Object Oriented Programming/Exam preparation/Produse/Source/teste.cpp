#include "teste.h"
#include "repo.h"
#include "domeniu.h"
#include "teste.h"
#include "service.h"
#include "validator.h"
#include <assert.h>
#include <sstream>
/*

9 Lapte lactate 3.5
1 Mambo suc 4
10 Fanta suc 5.5
2 Sprite suc 6

*/

void Teste::run_all()
{
    Repo repo{ "test.txt" };
    Validator valid;
    Service srv{ repo,valid };
    assert(repo.get_all().size() == 4);
    assert(srv.get_all().size() == 4);
    vector<Produs> v = srv.get_all();
    assert(repo.size() == 4);
    assert(v[1].get_id() == 1);
    assert(v[1].get_nume() == "Mambo");
    assert(v[1].get_tip() == "suc");
    assert(v[1].get_pret() == 4);

    try {
        srv.adauga_produs(3, "", "da", 102);
    }
    catch (ValidException& re) {
        string mesaj = re.getMessage();
        assert(mesaj.find("Pret") != string::npos);
    }

    //Produs cautata2 = srv.cauta(3);
    //assert(cautata2.get_artist() == "Voltaj");
    //Produs noua{ 4,"Miracol","Andra",2 };
    //repo.adauga(noua);
    //assert(repo.get_all().size() == 4);
    //Produs existenta{ 1,"Gena","Unu",7 };
    //try {
    //    repo.adauga(existenta);
    //}
    //catch (RepoException& re) {
    //    string mesaj = re.get_mesaj();
    //    assert(mesaj == "Mai exista!\n");
    //}
    //Melodie demodificat{ 4,"Miracol","Delia",4 };
    //repo.modifica(demodificat);
    //Melodie cautata3 = srv.cauta(4);
    //assert(cautata3.get_artist() == "Delia");
    //try {
    //    Melodie inex{ 9,"Da","Silviu",10 };
    //    repo.modifica(inex);
    //}
    //catch (RepoException& re) {
    //    string mesaj = re.get_mesaj();
    //    assert(mesaj == "Element inexistent!\n");
    //}
    //try {
    //    int idinexistent = 10;
    //    repo.sterge_dupa_id(idinexistent);
    //}
    //catch (RepoException& re) {
    //    string mesaj = re.get_mesaj();
    //    assert(mesaj == "Element inexistent!\n");
    //}
    //repo.sterge_dupa_id(4);
    //assert(repo.get_all().size() == 3);
    //repo.adauga(noua);

    //srv.modifica(4, "Lumina", 3);
    //Melodie cautata4 = srv.cauta(4);
    //assert(cautata4.get_titlu() == "Lumina");
    //assert(cautata4.get_rank() == 3);
    //try {
    //    srv.modifica(9, "Da", 10);
    //}
    //catch (RepoException& re) {
    //    string mesaj = re.get_mesaj();
    //    assert(mesaj == "Nu exista!\n");
    //}
    //try {
    //    int idinexistent2 = 10;
    //    srv.sterge(idinexistent2);
    //}
    //catch (RepoException& re) {
    //    string mesaj = re.get_mesaj();
    //    assert(mesaj == "Element inexistent!\n");
    //}
    //srv.sterge(4);
    //assert(srv.get_all().size() == 3);
}