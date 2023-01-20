#include "teste.h"

void Teste::run_teste()
{
    RepoFile repo{ "test1.txt","test2.txt" };
    Valid val;
    Service srv{ repo,val };
    assert(repo.get_all_procesoare().size() == 1);
    assert(repo.get_all_placi().size() == 2);

    Procesor pr{ "pila",10,"bun",150 };
    Placa pl{ "tu13","bun",40 };

    try {
        srv.adaugaplacaservice("", "", -2);
    }
    catch (ValidException& ve) {
        assert(ve.get_mesaj().find("vid") != string::npos);
        assert(ve.get_mesaj().find("mic") != string::npos);
    }

    try {
        srv.adaugaplacaservice("f123", "bun", 30);
    }
    catch (RepoException& re) {
        assert(re.get_mesaj().find("exista") != string::npos);
    }

    srv.adaugaplacaservice("tu13", "bun", 40);
    assert(repo.get_all_placi().size() == 3);

    vector<Placa> v1 = srv.filtrare_dupa_soclu("rau");
    assert(v1.size() == 0);
    vector<Placa> v2 = srv.filtrare_dupa_soclu("bun");
    assert(v2.size() == 2);
    assert(srv.get_all_placi_service().size() == 3);
}
