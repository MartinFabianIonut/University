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

	ceva = new QLabel("Aici va aparea:");
	edit = new QLineEdit; 

	lyForm->addRow(ceva, edit);

	lystanga->addWidget(form);

	sortpret= new QPushButton("Sortare dupa pret!");
	sortbrand = new QPushButton("Sortare dupa brand!");
	sortmodel = new QPushButton("Sortare dupa model!");
	actualizare = new QPushButton("Actualizare!");

	lystanga->addWidget(sortpret); lystanga->addWidget(sortbrand); lystanga->addWidget(sortmodel); lystanga->addWidget(actualizare);

	dreapta = new QWidget;
	lydreapta = new QVBoxLayout;

	dreapta->setLayout(lydreapta);

	l1 = new QListWidget;
	vector<Telefon> te = srv.get_all();

	for (auto& t : te) {
		QListWidgetItem* it = new QListWidgetItem(QString::fromStdString("Cod: " + t.get_cod() + " Brand: " + t.get_brand() + " Model: " + t.get_model()));
		l1->addItem(it);
		
		l1->setCurrentItem(it);
		if (t.get_brand() == "Samsung") {
			l1->currentItem()->setBackground(QBrush{ Qt::red });
		}
		else {
			if (t.get_brand() == "Huawei") {
				l1->currentItem()->setBackground(QBrush{ Qt::yellow });
			}
			else {
				if (t.get_brand() == "Apple") {
					l1->currentItem()->setBackground(QBrush{ Qt::blue });
				}
				else {
					l1->currentItem()->setBackground(QBrush{ Qt::black });
				}
			}
		}
	}

	lydreapta->addWidget(l1);


	vector<Telefon>inainte = srv.get_all();
	vector<string>branduri;
	for (auto& a : inainte) {
		string b = a.get_brand();
		bool maaa = false;
		vector<string>::iterator it;
		for (it = branduri.begin(); it != branduri.end(); it++) {
			if (*it == b) {
				maaa = true;
			}
		}
		if (maaa == false) {
			branduri.push_back(b);
			string ss = b;
			QString d = QString::fromStdString(ss);
			QPushButton* but = new QPushButton(d); 
			butoane.push_back(but);
			QObject::connect(but, &QPushButton::clicked, [=]() {
				try {
					string br = but->text().toStdString();
					srv.increment(br);
					actual(srv.get_all());
				}
				catch (RepoException& re) {
					QMessageBox::warning(this, "Warning", QString::fromStdString(re.get_mesaj()));
				}
				
				});
		}
	}

	foreach(QPushButton * bu, butoane)
	{
		lydreapta->addWidget(bu);
	}


	lymain->addWidget(stanga); lymain->addWidget(dreapta);
}

void GUI::conecteaza()
{
	QObject::connect(sortpret, &QPushButton::clicked, [&] {
		vector<Telefon>sortat = srv.sortbypret();
		actual(sortat);
		});

	QObject::connect(sortbrand, &QPushButton::clicked, [&] {
		vector<Telefon>sortat = srv.sortbybrand();
		actual(sortat);
		});

	QObject::connect(sortmodel, &QPushButton::clicked, [&] {
		vector<Telefon>sortat = srv.sortbymodel();
		actual(sortat);
		});

	QObject::connect(actualizare, &QPushButton::clicked, [&] {
		vector<Telefon>da = srv.get_all();
		actual(da);
		});

	QObject::connect(l1, &QListWidget::itemClicked, [&] {
		try {
			QString s = l1->currentItem()->text();
			string tot = s.toStdString();
			vector<string>cuvinte{};
			int poz;
			while ((poz = tot.find(" ")) != string::npos) {
				cuvinte.push_back(tot.substr(0, poz));
				tot.erase(0, poz + 1);
			}
			cuvinte.push_back(tot);
			string cod = cuvinte[1];
			Telefon t = srv.cauta(cod);
			edit->setText(QString::fromStdString(t.get_cod()+" " + t.get_brand() + " " + t.get_model() + " " + std::to_string(t.get_pret())));
		}
		catch (RepoException& re) {
			QMessageBox::warning(this, "Warning", QString::fromStdString(re.get_mesaj()));
		}
		});
	

}

void GUI::actual(vector<Telefon> te)
{
	l1->clear();
	for (auto& t : te) {
		QListWidgetItem* it = new QListWidgetItem(QString::fromStdString("Cod: " + t.get_cod() + " Brand: " + t.get_brand() + " Model: " + t.get_model()));
		l1->addItem(it);

		l1->setCurrentItem(it);
		if (t.get_brand() == "Samsung") {
			l1->currentItem()->setBackground(QBrush{ Qt::red });
		}
		else {
			if (t.get_brand() == "Huawei") {
				l1->currentItem()->setBackground(QBrush{ Qt::yellow });
			}
			else {
				if (t.get_brand() == "Apple") {
					l1->currentItem()->setBackground(QBrush{ Qt::blue });
				}
				else {
					l1->currentItem()->setBackground(QBrush{ Qt::black });
				}
			}
		}
	}
}
