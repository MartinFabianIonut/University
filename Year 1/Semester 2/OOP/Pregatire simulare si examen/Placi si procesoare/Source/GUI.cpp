#include "GUI.h"

void GUI::init()
{
	lymain = new QHBoxLayout();
	setLayout(lymain);

	stanga = new QWidget;
	lystanga = new QVBoxLayout;

	stanga->setLayout(lystanga);

	QWidget* form = new QWidget;
	QFormLayout* lyForm = new QFormLayout;
	form->setLayout(lyForm);

	soclu = new QLabel;
	lbnume = new QLabel("Nume: ");
	lbsoclu = new QLabel("Soclu: ");
	lbpret = new QLabel("Pret: ");

	editn = new QLineEdit; edits = new QLineEdit; editp = new QLineEdit;

	lyForm->addRow(lbnume, editn);
	lyForm->addRow(lbsoclu, edits);
	lyForm->addRow(lbpret, editp);

	lystanga->addWidget(form);

	ad = new QPushButton("Adauga placa");
	fil = new QPushButton("Filtreaza dupa soclu");
	actualizare = new QPushButton("Actualizeaza");

	lystanga->addWidget(soclu); lystanga->addWidget(ad); lystanga->addWidget(fil); lystanga->addWidget(actualizare);

	dreapta = new QWidget;
	lydreapta = new QVBoxLayout;

	dreapta->setLayout(lydreapta);

	l1 = new QListWidget; l2 = new QListWidget;
	vector<Procesor> pr = srv.get_all_procesoare_service();
	vector<Placa>pl = srv.get_all_placi_service();

	for (auto& p : pr) {

		l1->addItem(QString::fromStdString(
			"Nume: " + p.get_nume() + " NrThreaduri: " + std::to_string(p.get_nr()) + " SocluProcesor: " + p.get_soclu() + " Pret: " + std::to_string(p.get_pret())));

	}
	for (auto& p : pl) {
		l2->addItem(QString::fromStdString(
			"Nume: " + p.get_nume() + " SocluProcesor: " + p.get_soclu() + " Pret: " + std::to_string(p.get_pret())));
	}

	lydreapta->addWidget(l1); lydreapta->addWidget(l2);

	lymain->addWidget(stanga); lymain->addWidget(dreapta);
}

void GUI::conecteaza()
{
	QObject::connect(ad, &QPushButton::clicked, [&] {
		try {
			QString nume, soclu, pret;
			nume = editn->text();
			soclu = edits->text();
			pret = editp->text();
			editn->clear();
			edits->clear();
			editp->clear();
			int p = pret.toInt();
			string n = nume.toStdString();
			string s = soclu.toStdString();
			srv.adaugaplacaservice(n, s, p);
			actual(srv.get_all_procesoare_service(), srv.get_all_placi_service());
			QMessageBox::information(this, "Info", QString::fromStdString("Adaugat cu succes"));
		}
		catch (RepoException& re) {
			QMessageBox::warning(this, "Warning", QString::fromStdString(re.get_mesaj()));
		}
		catch (ValidException& ve) {
			QMessageBox::warning(this, "Warning", QString::fromStdString(ve.get_mesaj()));
		}
		});

	QObject::connect(fil, &QPushButton::clicked, [&] {
			QString numesoclu;
			numesoclu = soclu->text();
			soclu->clear();
			if (numesoclu.size() == 0) {
				QMessageBox::warning(this, "Warning", QString::fromStdString("Nu e selectat soclul!"));
			}
			else {
				string soclustring = numesoclu.toStdString();
				vector<Placa> rez = srv.filtrare_dupa_soclu(soclustring);
				actual(srv.get_all_procesoare_service(), srv.filtrare_dupa_soclu(soclustring));
			}
		});

	QObject::connect(actualizare, &QPushButton::clicked, [&] {
			actual(srv.get_all_procesoare_service(), srv.get_all_placi_service());
		});

	QObject::connect(l1, &QListWidget::itemClicked, [&] {
		QString s = l1->currentItem()->text();
		string tot = s.toStdString();
		vector<string>cuvinte{};
		int poz;
		while ((poz = tot.find(" ")) != string::npos) {
			cuvinte.push_back(tot.substr(0, poz));
			tot.erase(0, poz + 1);
		}
		cuvinte.push_back(tot);
		soclu->setText(QString::fromStdString(cuvinte[5]));
		});
}

void GUI::actual(vector<Procesor> pr, vector<Placa> pl)
{
	l1->clear(); l2->clear();
	for (auto& p : pr) {
		l1->addItem(QString::fromStdString(
		"Nume: " + p.get_nume() + " NrThreaduri: " + std::to_string(p.get_nr()) + " SocluProcesor: " + p.get_soclu() + " Pret: " + std::to_string(p.get_pret())));
	}
	for (auto& p : pl) {
		l2->addItem(QString::fromStdString(
			"Nume: " + p.get_nume() + " SocluProcesor: " + p.get_soclu() + " Pret: " + std::to_string(p.get_pret())));
	}
}
