#include "pregatiremp.h"
#include <QtWidgets/QApplication>
#include "GUI.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    Repo repo{"produse.txt"};
    Validator valid;
    Service serv{ repo, valid };
    GUI ui{ serv };

    ui.show();
    return a.exec();
}
