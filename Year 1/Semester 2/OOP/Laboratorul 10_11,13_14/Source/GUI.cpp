#include "GUI.h"
#include "GUI.h"
#include <fstream>



void GUI::initGUI()
{
	lyMain = new QHBoxLayout;
	setLayout(lyMain);

	//partea stanga

	QWidget* stanga = new QWidget;
	QVBoxLayout* lyStanga = new QVBoxLayout;
	stanga->setLayout(lyStanga);

	QWidget* form = new QWidget;
	QFormLayout* lyForm = new QFormLayout;
	form->setLayout(lyForm);
	
	QLabel* informativ = new QLabel;
	informativ->setText("Pentru adaugare se vor completa toate casutele, \n"
		"la fel si pentru modifica, iar pentru stergere doar prima!");

	lyForm->addWidget(informativ);

	editApartament = new QLineEdit;
	editNume = new QLineEdit;
	editSuprafata = new QLineEdit;
	editTip = new QLineEdit;

	lyForm->addRow(lbApartament, editApartament);
	lyForm->addRow(lbNume, editNume);
	lyForm->addRow(lbSuprafata, editSuprafata);
	

	comboTip->addItem(QString::number(1));
	comboTip->addItem(QString::number(2));
	comboTip->addItem(QString::number(3));
	comboTip->addItem(QString::number(4));
	comboTip->addItem(QString::number(5));
	comboTip->addItem(QString::number(6));
	lyForm->addRow(lbTip, comboTip);
	//lyForm->addWidget(comboTip);

	QWidget* alaturat = new QWidget;
	QHBoxLayout* lyAlaturat = new QHBoxLayout;
	alaturat->setLayout(lyAlaturat);

	butonAdauga = new QPushButton("Adauga locatar!");

	lyAlaturat->addWidget(butonAdauga);
	

	butonSterge = new QPushButton("Sterge locatar!");
	lyAlaturat->addWidget(butonSterge);

	butonModifica = new QPushButton("Modifica locatar!");
	lyAlaturat->addWidget(butonModifica);

	lyStanga->addWidget(form);
	lyStanga->addWidget(alaturat);

	//filtrari

	QWidget* filtrari = new QWidget;
	QFormLayout* lyFiltrari = new QFormLayout;
	filtrari->setLayout(lyFiltrari);

	QLabel* tip = new QLabel("Dati tipul: ");
	filtrareDupaTip = new QLineEdit;
	butonFiltrareTip = new QPushButton("Filtare dupa tip!");

	lyFiltrari->addRow(tip, filtrareDupaTip);
	lyFiltrari->addWidget(butonFiltrareTip);

	QLabel* suprafata = new QLabel("Dati interval pentru suprafata: ");
	filtrareDupaSuprafataMin = new QLineEdit;
	filtrareDupaSuprafataMax = new QLineEdit;
	butonFiltrareSuprafata= new QPushButton("Filtare dupa suprafata!");

	lyFiltrari->addRow(suprafata, filtrareDupaSuprafataMin);
	lyFiltrari->addWidget(filtrareDupaSuprafataMax);
	lyFiltrari->addWidget(butonFiltrareSuprafata);

	//filtrareDupaSuprafata = new QLineEdit;
	lyStanga->addWidget(filtrari);

	//sortari

	QHBoxLayout* sortari = new QHBoxLayout;
	grupajSortari->setLayout(sortari);

	sortari->addWidget(sortareDupaNume);
	sortari->addWidget(sortareDupaSuprafata);
	sortari->addWidget(sortareDupaTipSuprafata);
	
	butonSort1 = new QPushButton("Sorteaza crescator!");
	butonSort2 = new QPushButton("Sorteaza descrescator!");
	sortari->addWidget(butonSort1);
	sortari->addWidget(butonSort2);

	lyStanga->addWidget(grupajSortari);

	actualizare = new QPushButton("Actualizati tabelul!");
	lyStanga->addWidget(actualizare);

	undo = new QPushButton("Realizati undo!");
	lyStanga->addWidget(undo);

	QStringList antet;
	antet << "Apartamentul: " << "Nume proprietar: ";
	antet << "Suprafata apartament: " << "Tip comfort: ";
	
	
	tabelView = new QTableView();
	model = new MyTableModel(ctr.getAll());
	tabelView->setModel(model);
	lyStanga->addWidget(tabelView);


	lyMain->addWidget(stanga);

	//partea dreapta
	
	dreapta = new QWidget;
	 lyDreapta = new QVBoxLayout;
	dreapta->setLayout(lyDreapta);

	tabelLocatari = new QTableWidget{ 5,4 };
	tabelLocatari->setHorizontalHeaderLabels(antet);
	tabelLocatari->horizontalHeader()->setSectionResizeMode(QHeaderView::ResizeToContents);
	tabelLocatari->setSelectionBehavior(QAbstractItemView::SelectRows);
	lyDreapta->addWidget(tabelLocatari);

	

	lista = new QListWidget;
	vector<Locatar>toti = ctr.getAll();
	for (auto& c : toti) {
		lista->addItem(QString::fromStdString("Ap: " + std::to_string(c.get_ap()) + " Nume: " + c.get_nume() + " Sup: ") + QString::number(c.get_sup()) + QString::fromStdString(" Tip: " + std::to_string(c.get_tip())));
	}
	lyDreapta->addWidget(lista);

	QLabel* notificari = new QLabel("Lista de notificari:");
	lyDreapta->addWidget(notificari);

	tabelNotificari = new QTableWidget{ 5,4 };
	tabelNotificari->setHorizontalHeaderLabels(antet);

	tabelNotificari->horizontalHeader()->setSectionResizeMode(QHeaderView::ResizeToContents);

	lyDreapta->addWidget(tabelNotificari);

	QWidget* form2 = new QWidget;
	QFormLayout* lyForm2 = new QFormLayout;
	form2->setLayout(lyForm2);


	deleteall = new QPushButton("Sterge tot!");
	ad = new QLineEdit();
	addbyid = new QPushButton("Adauga dupa id!");
	QLabel* addrandomlabel = new QLabel;
	ra = new QLineEdit();
	addrandom = new QPushButton("Adauga random!");
	ex = new QLineEdit();
	exportt = new QPushButton("Export in csv!");

	lyForm2->addWidget(deleteall);
	lyForm2->addRow(ad, addbyid);
	lyForm2->addRow(ra, addrandom);
	lyForm2->addRow(ex, exportt);

	lyDreapta->addWidget(form2);

	deschidecos1 = new QPushButton("Cos 1");
	deschidecos2 = new QPushButton("Cos 2");

	lyDreapta->addWidget(deschidecos1);
	lyDreapta->addWidget(deschidecos2);


	vector<Locatar>inainte = ctr.getAll();
	vector<int>tipuri;
	for (auto& a : inainte) {
		int ti = a.get_tip();
		bool maaa = false;
		vector<int>::iterator it;
		for (it = tipuri.begin(); it != tipuri.end(); it++) {
			if (*it == ti) {
				maaa = true;
			}
		}
		if (maaa == false) {
			tipuri.push_back(ti);
			auto s = std::to_string(ti);
			string ss = s;
			QString d = QString::fromStdString(ss);
			QPushButton* but = new QPushButton(d); butoane.push_back(but);
			QObject::connect(but, &QPushButton::clicked, [=]() {
				int tip = but->text().toInt();
				vector<Locatar>inainte = ctr.getAll(); int cate = 0;
				for (auto& c : inainte) { if (c.get_tip() == tip) { cate++; } }
				string am = "Avem " + std::to_string(cate) + " apartamente de tipul " + std::to_string(tip);
				QMessageBox::information(this, "Info", QString::fromStdString(am));
				});
		}
	}

	foreach (QPushButton* b, butoane)
	{
		lyDreapta->addWidget(b);
	}
	lyMain->addWidget(dreapta);
}

void GUI::connectSignalSlots()
{
	ctr.addObserver(this);
	QObject::connect(butonAdauga, &QPushButton::clicked, this, &GUI::adaugaGUI);
	QObject::connect(butonSterge, &QPushButton::clicked, this, &GUI::stergeGUI);
	QObject::connect(butonModifica, &QPushButton::clicked, this, &GUI::modificaGUI);
	QObject::connect(actualizare, &QPushButton::clicked, [&]() { this->actualizareLocatari(ctr.getAll());});

	QObject::connect(butonFiltrareTip, &QPushButton::clicked, [&]() {
		int tip = filtrareDupaTip->text().toInt();
		filtrareDupaTip->clear();
		actualizareLocatari(ctr.locatari_dupa_tip(tip));
		});

	QObject::connect(butonFiltrareSuprafata, &QPushButton::clicked, [&]() {
		double supi = filtrareDupaSuprafataMin->text().toDouble();
		double supf = filtrareDupaSuprafataMax->text().toDouble();
		filtrareDupaSuprafataMin->clear();
		filtrareDupaSuprafataMax->clear();
		actualizareLocatari(ctr.locatari_dupa_suprafata(supi,supf));
		});

	QObject::connect(butonSort1, &QPushButton::clicked, [&]() {
		if (sortareDupaNume->isChecked()) {
			actualizareLocatari(ctr.sortareDupaNume(1));
		}
		else {
			if (sortareDupaSuprafata->isChecked()) {
				actualizareLocatari(ctr.sortareDupaSuprafata(1));
			}
			else {
				if (sortareDupaTipSuprafata->isChecked()) {
					actualizareLocatari(ctr.sortareDupaTipSuprafata(1));
				}
			}
		}
		});

	QObject::connect(butonSort2, &QPushButton::clicked, [&]() {
		if (sortareDupaNume->isChecked()) {
			actualizareLocatari(ctr.sortareDupaNume(2));
		}
		else {
			if (sortareDupaSuprafata->isChecked()) {
				actualizareLocatari(ctr.sortareDupaSuprafata(2));
			}
			else {
				if (sortareDupaTipSuprafata->isChecked()) {
					actualizareLocatari(ctr.sortareDupaTipSuprafata(2));
				}
			}
		}
		});

	QObject::connect(undo, &QPushButton::clicked, [&]() {
		try {
			ctr.undo();
			actualizareLocatari(ctr.getAll());
			QMessageBox::information(this, "Info", QString::fromStdString("Undo realizat cu succes!"));

		}
		catch (RepoException& re) {
			QMessageBox::warning(this, "Warning", QString::fromStdString(re.get_message()));
		}
		
		});

	QObject::connect(deleteall, &QPushButton::clicked, [&]() {
		try {
			ctr.stergeListaComplet();
			actualizareNotificari(ctr.getAllNotificari());
			
		}
		catch (RepoException& re) {
			QMessageBox::warning(this, "Warning", QString::fromStdString(re.get_message()));
		}
		});

	QObject::connect(addbyid, &QPushButton::clicked, [&]() {
		int ap = ad->text().toInt();ad->clear();
		try {
			ctr.adaugaLocatarDupaApInLista(ap);
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


	QObject::connect(exportt, &QPushButton::clicked, [&]() {
		string nume = ex->text().toStdString(); ex->clear();
		if (nume != "") {
			nume = nume + ".csv";
			std::ofstream fout(nume);
			if (fout.is_open()) {
				vector<Locatar>res4 = ctr.getAllNotificari();
				for (auto it = res4.begin(); it != res4.end(); it++) {
					const double sup = (*it).get_sup();
					string Sup = std::to_string(sup);
					fout << (*it).get_ap() << ";" << (*it).get_nume() << ";" << Sup << ";" << (*it).get_tip() << "\n";
				}
			}
			QMessageBox::information(this, "Info", QString::fromStdString("Export realizat cu succes!"));
		}
		else {
			QMessageBox::warning(this, "Warning", QString::fromStdString("Nume vid, nu se poate face exportul!"));
		}
		});


	QObject::connect(tabelLocatari, &QTableWidget::clicked, [&]() {
		QList l = tabelLocatari->selectedItems();
		QString ap, nume, sup;
		int tip=0;
		int i = 0;
		for (auto& a : l) {
			if (i == 0) { ap = a->text(); }
			if (i == 1) { nume = a->text(); }
			if (i == 2) { sup = a->text(); }
			if (i == 3) { tip = a->text().toInt(); }
			i++;
		}
		editApartament->setText(ap);
		editNume->setText(nume);
		editSuprafata->setText(sup);
		comboTip->setCurrentIndex(tip-1);
		});

	QObject::connect(lista, &QListWidget::itemClicked, [&]() {
		QString s = lista->currentItem()->text();
		string m = s.toStdString();
		vector<string>cuvinte{};
		size_t poz;
		while ((poz = m.find(" ")) != string::npos) {
			cuvinte.push_back(m.substr(0, poz));
			m.erase(0, poz + 1);
		}
		cuvinte.push_back(m);
		Locatar l = ctr.cautaLocatar(stoi(cuvinte[1]));
		editApartament->setText(QString::fromStdString(cuvinte[1]));
		editNume->setText(QString::fromStdString(l.get_nume()));
		editSuprafata->setText(QString::fromStdString(std::to_string(l.get_sup())));
		comboTip->setCurrentIndex(l.get_tip() - 1);
		
		lista->currentItem()->setBackground(QBrush{ Qt::red});
		
		//QMessageBox::information(this, "Info", QString::fromStdString(cuvinte[7]));

		});

	QObject::connect(deschidecos1, &QPushButton::clicked, [this]() {
		auto fer = new CosCRUDGUI{ ctr };
		fer->show();
		});

	QObject::connect(deschidecos2, &QPushButton::clicked, [this]() {
		auto fer = new CosReadOnlyGUI{ ctr };
		fer->show();
		});

	QObject::connect(tabelView->selectionModel(), &QItemSelectionModel::selectionChanged, [this]() {
		if (tabelView->selectionModel()->selectedIndexes().isEmpty()) {
			editApartament->clear();
			editNume->clear();
			editSuprafata->clear();
			return;
		}
		QString ap, nume, sup, tips;
		int selRow = tabelView->selectionModel()->selectedIndexes().at(0).row();
		ap = tabelView->model()->data(tabelView->model()->index(selRow, 0), Qt::DisplayRole).toString();
		nume = tabelView->model()->data(tabelView->model()->index(selRow, 1), Qt::DisplayRole).toString();
		sup = tabelView->model()->data(tabelView->model()->index(selRow, 2), Qt::DisplayRole).toString();
		tips = tabelView->model()->data(tabelView->model()->index(selRow, 3), Qt::DisplayRole).toString();
		int tip = tips.toInt();
		editApartament->setText(ap);
		editNume->setText(nume);
		editSuprafata->setText(sup);
		comboTip->setCurrentIndex(tip - 1);
		});
}


void GUI::actualizareLocatari(vector<Locatar> locatari)
{
	tabelLocatari->clearContents();
	tabelLocatari->setRowCount(locatari.size());

	int index = 0;
	for (auto& l : locatari) {
		c1 = new QTableWidgetItem(QString::number(l.get_ap()));
		c2 = new QTableWidgetItem(QString::fromStdString(l.get_nume()));
		c3 = new QTableWidgetItem(QString::number(l.get_sup()));
		c4 = new QTableWidgetItem(QString::number(l.get_tip()));
		tabelLocatari->setItem(index, 0, c1);
		tabelLocatari->setItem(index, 1, c2);
		tabelLocatari->setItem(index, 2, c3);
		tabelLocatari->setItem(index, 3, c4);
		index++;
	}

	lista->clear();
	for (auto& c : locatari) {
		lista->addItem(QString::fromStdString("Ap: " + std::to_string(c.get_ap()) + " Nume: " + c.get_nume() + " Sup: " + std::to_string(c.get_sup()) + " Tip: " + std::to_string(c.get_tip())));
	}

	model->seteazaLocatari(locatari);

}

void GUI::actualizareNotificari(vector<Locatar> locatari)
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

void GUI::actNot() {
	vector<Locatar> locataridupa = ctr.getAll();
	vector<Locatar> notificariactualizate = ctr.getAllNotificari();
	int ap,orig;
	int indice = -1, poz;
	for (auto& n : notificariactualizate) {
		ap = n.get_ap();
		indice++;
		bool exista = false;
		for (auto& l : locataridupa) {
			orig = l.get_ap();
			if (ap == orig) {
				n = l;
				exista = true;
			}
		}
		if (!exista) {
			poz = indice;
			notificariactualizate.erase(notificariactualizate.begin()+poz);
		}
	}

	if (notificariactualizate.size()) {
		actualizareNotificari(notificariactualizate);
	}
}

void GUI::adaugaGUI()
{
	try {
		int ap = editApartament->text().toInt();
		string nume = editNume->text().toStdString();
		double sup = editSuprafata->text().toDouble();
		int tip = comboTip->currentText().toInt();

		editApartament->clear();
		editNume->clear();
		editSuprafata->clear();
		comboTip->clearEditText();
		editTip->clear();
		bool maie = false;
		vector<Locatar>inainte = ctr.getAll();
		for (auto& a : inainte) {
			if (a.get_tip() == tip) {
				maie = true;
			}
		}

		ctr.adaugaLocatar(ap, nume, sup, tip);
		actualizareLocatari(ctr.getAll());
		QMessageBox::information(this, "Info", QString::fromStdString("Locatar adaugat cu succes!"));

		vector<int>tipuri;
		bool maaa = false;
		for (auto& a : inainte) {
			if (a.get_tip() == tip) {
				maaa = true;
			}
		}
		if (maaa == false) {
			tipuri.push_back(tip);
			auto s = std::to_string(tip);
			string ss = s;
			QString d = QString::fromStdString(ss);
			QPushButton* but = new QPushButton(d); butoane.push_back(but);
			QObject::connect(but, &QPushButton::clicked, [=]() {
				int tipp = but->text().toInt();
				vector<Locatar>inainte = ctr.getAll(); int cate = 0;
				for (auto& c : inainte) { if (c.get_tip() == tipp) { cate++; } }
				string am = "Avem " + std::to_string(cate) + " apartamente de tipul " + std::to_string(tipp);
				QMessageBox::information(this, "Info", QString::fromStdString(am));
				});
			lyDreapta->addWidget(but);
		}
		actNot();

	}
	catch (RepoException& re) {
		QMessageBox::warning(this, "Warning", QString::fromStdString(re.get_message()));

	}
	catch (ValidateException& ve) {
		string erori = "";
		for (auto& mesaje : ve.get_message()) {
			erori = erori + mesaje + '\n';
		}
		QMessageBox::warning(this, "Warning", QString::fromStdString(erori));
	}
}

void GUI::stergeGUI()
{
	try {
		int ap = editApartament->text().toInt();

		editApartament->clear();
		editNume->clear();
		editSuprafata->clear();
		editTip->clear();

		ctr.stergeLocatar(ap);
		actualizareLocatari(ctr.getAll());
		QMessageBox::information(this, "Info", QString::fromStdString("Locatar sters cu succes!"));

		actNot();
	}
	catch (RepoException& re) {
		QMessageBox::warning(this, "Warning", QString::fromStdString(re.get_message()));

	}
	catch (ValidateException& ve) {
		string erori = "";
		for (auto& mesaje : ve.get_message()) {
			erori = erori + mesaje + '\n';
		}
		QMessageBox::warning(this, "Warning", QString::fromStdString(erori));
	}
}

void GUI::modificaGUI()
{
	try {
		int ap = editApartament->text().toInt();
		string nume = editNume->text().toStdString();
		double sup = editSuprafata->text().toDouble();
		int tip = comboTip->currentText().toInt();

		editApartament->clear();
		editNume->clear();
		editSuprafata->clear();
		editTip->clear();

		ctr.modificaLocatar(ap, nume, sup, tip);
		actualizareLocatari(ctr.getAll());
		QMessageBox::information(this, "Info", QString::fromStdString("Locatar modificat cu succes!"));

		actNot();
	}
	catch (RepoException& re) {
		QMessageBox::warning(this, "Warning", QString::fromStdString(re.get_message()));

	}
	catch (ValidateException& ve) {
		string erori = "";
		for (auto& mesaje : ve.get_message()) {
			erori = erori + mesaje + '\n';
		}
		QMessageBox::warning(this, "Warning", QString::fromStdString(erori));
	}
}

void GUI::filtrareTipGUI()
{
}

void GUI::filtrareSuprafataGUI()
{
}

void GUI::sortareNumeCrescGUI()
{
}

void GUI::sortareNumeDescrescGUI()
{
}

void GUI::sortareSuprafataCrescGUI()
{
}

void GUI::sortareSuprafataDescrescGUI()
{
}

void GUI::sortareTipSuprafataCrescGUI()
{
}

void GUI::sortareTipSuprafataDescrescGUI()
{
}
