#include "Lab1011FAB.h"
#include <QtWidgets/QApplication>
#include "repo.h"
#include "valid.h"
#include "service.h"
#include "teste.h"
#include "GUI.h"

int main(int argc, char *argv[])
{
    Teste t;
    t.ruleaza_teste();

    QApplication a(argc, argv);
    
    RepoFile repo{ "locatari.txt" };
    LocatarValidator val;
    Service ctr{ repo,val };

    GUI w{ ctr };

  /*  QTableView* tabelView = new QTableView();
    MyTableModel* model = new MyTableModel(ctr.getAll());
    tabelView->setModel(model);
    tabelView->show();*/

    w.show();
    return a.exec();
}


