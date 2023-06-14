#include "pregatiremm1.h"
#include <QtWidgets/QApplication>
#include "repo.h"
#include "domeniu.h"
#include "teste.h"
#include "service.h"
#include "GUI.h"

int main(int argc, char* argv[])
{
    Teste t;
    t.run_all();
    QApplication a(argc, argv);

    RepoFile repo{ "melodii.txt" };
    Service srv{ repo };
    GUI w{ srv };

    w.show();
    return a.exec();
}
