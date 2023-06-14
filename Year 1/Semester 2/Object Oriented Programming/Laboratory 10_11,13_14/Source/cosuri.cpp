#include "cosuri.h"

void CosCRUDGUI::initCosCRUDGUI()
{
	lyMain = new QVBoxLayout;
	setLayout(lyMain);

	QStringList antet;
	antet << "Apartamentul: " << "Nume proprietar: ";
	antet << "Suprafata apartament: " << "Tip comfort: ";

	tabelNotificari = new QTableWidget{ 5,4 };
	tabelNotificari->setHorizontalHeaderLabels(antet);

	tabelNotificari->horizontalHeader()->setSectionResizeMode(QHeaderView::ResizeToContents);

	lyMain->addWidget(tabelNotificari);

	ad = new QLineEdit();
	deleteall = new QPushButton("Sterge tot!");
	addbyitem = new QPushButton("Adauga!");
	ra = new QLineEdit();
	addrandom = new QPushButton("Adauga random!");


	lyMain->addWidget(deleteall);
	lyMain->addWidget(ad);
	lyMain->addWidget(addbyitem);
	lyMain->addWidget(ra);
	lyMain->addWidget(addrandom);
}

void CosCRUDGUI::connectSignalSlots()
{
	ctr.addObserver(this);
	QObject::connect(deleteall, &QPushButton::clicked, [&]() {
		try {
			ctr.stergeListaComplet();
			actualizareNotificari(ctr.getAllNotificari());
		}
		catch (RepoException& re) {
			QMessageBox::warning(this, "Warning", QString::fromStdString(re.get_message()));
		}
		});

	QObject::connect(addrandom, &QPushButton::clicked, [&]() {
		int cati = ra->text().toInt(); ra->clear();
		if (cati < 1) {
			QMessageBox::warning(this, "Warning", QString::fromStdString("Nu se poate, numar prea mic!"));
		}
		else {
			if (ctr.adaugaLocatariInLista(cati) == 1) {
				QMessageBox::warning(this, "Warning", QString::fromStdString("Nu se poate, nu exista asa multi locatari!"));
			}
			else {
				actualizareNotificari(ctr.getAllNotificari());
			}
		}
		});

	QObject::connect(addbyitem, &QPushButton::clicked, [&]() {
		int ap = ad->text().toInt(); ad->clear();
		try {
			ctr.adaugaLocatarDupaApInLista(ap);
			actualizareNotificari(ctr.getAllNotificari());
		}
		catch (RepoException& re) {
			QMessageBox::warning(this, "Warning", QString::fromStdString(re.get_message()));
		}
		});
}

void CosCRUDGUI::actualizareNotificari(vector<Locatar> locatari)
{
	tabelNotificari->clearContents();
	tabelNotificari->setRowCount(locatari.size());

	int index = 0;
	for (auto& l : locatari) {
		tabelNotificari->setItem(index, 0, new QTableWidgetItem(QString::number(l.get_ap())));
		tabelNotificari->setItem(index, 1, new QTableWidgetItem(QString::fromStdString(l.get_nume())));
		tabelNotificari->setItem(index, 2, new QTableWidgetItem(QString::number(l.get_sup())));
		tabelNotificari->setItem(index, 3, new QTableWidgetItem(QString::number(l.get_tip())));
		index++;
	}
}
