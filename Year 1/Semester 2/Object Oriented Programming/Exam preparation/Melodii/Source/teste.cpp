#include "teste.h"
#include "repo.h"
#include "domeniu.h"
#include "teste.h"
#include "service.h"
#include <assert.h>
#include <sstream>
/*

1 Dalida Projekt 1
2 Nu Adda 4
3 Vreau Voltaj 7

*/

void Teste::run_all()
{
    RepoFile repo{ "test.txt" };
    Service srv{ repo };
    assert(repo.get_all().size() == 3);
    assert(srv.get_all().size() == 3);
    vector<Melodie> v = srv.get_all();
    assert(v[1].get_id() == 2);
    assert(v[1].get_titlu() == "Nu");
    assert(v[1].get_artist() == "Adda");
    assert(v[1].get_rank() == 4);

    try {
        Melodie cautata = srv.cauta(5);
    }
    catch (RepoException& re) {
        string mesaj = re.get_mesaj();
        assert(mesaj== "Nu exista!\n");
    }

    Melodie cautata2 = srv.cauta(3);
    assert(cautata2.get_artist() == "Voltaj");
    Melodie noua{ 4,"Miracol","Andra",2 };
    repo.adauga(noua);
    assert(repo.get_all().size() == 4);
    Melodie existenta{ 1,"Gena","Unu",7 };
    try {
        repo.adauga(existenta);
    }
    catch (RepoException& re) {
        string mesaj = re.get_mesaj();
        assert(mesaj == "Mai exista!\n");
    }
    Melodie demodificat{ 4,"Miracol","Delia",4 };
    repo.modifica(demodificat);
    Melodie cautata3 = srv.cauta(4);
    assert(cautata3.get_artist() == "Delia");
    try {
        Melodie inex{ 9,"Da","Silviu",10 };
        repo.modifica(inex);
    }
    catch (RepoException& re) {
        string mesaj = re.get_mesaj();
        assert(mesaj == "Element inexistent!\n");
    }
    try {
        int idinexistent = 10;
        repo.sterge_dupa_id(idinexistent);
    }
    catch (RepoException& re) {
        string mesaj = re.get_mesaj();
        assert(mesaj == "Element inexistent!\n");
    }
    repo.sterge_dupa_id(4);
    assert(repo.get_all().size() == 3);
    repo.adauga(noua);

    srv.modifica(4,"Lumina",3);
    Melodie cautata4 = srv.cauta(4);
    assert(cautata4.get_titlu() == "Lumina");
    assert(cautata4.get_rank() == 3);
    try {
        srv.modifica(9, "Da", 10);
    }
    catch (RepoException& re) {
        string mesaj = re.get_mesaj();
        assert(mesaj == "Nu exista!\n");
    }
    try {
        int idinexistent2 = 10;
        srv.sterge(idinexistent2);
    }
    catch (RepoException& re) {
        string mesaj = re.get_mesaj();
        assert(mesaj == "Element inexistent!\n");
    }
    srv.sterge(4);
    assert(srv.get_all().size() == 3);
}