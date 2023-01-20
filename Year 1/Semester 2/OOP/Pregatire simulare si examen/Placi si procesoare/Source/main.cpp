#include "pregatire2sem12.h"
#include <QtWidgets/QApplication>
#include "repo.h"
#include "service.h"
#include "domeniu.h"
#include "teste.h"
#include "GUI.h"
#include "valid.h"


int main(int argc, char *argv[])
{
    Teste t;
   // t.run_teste();
    QApplication a(argc, argv);

    RepoFile repo{ "procesoare.txt","placi.txt" };
    Valid val;
    Service srv{ repo,val };

    GUI w{srv};
    w.show();
    return a.exec();
}
