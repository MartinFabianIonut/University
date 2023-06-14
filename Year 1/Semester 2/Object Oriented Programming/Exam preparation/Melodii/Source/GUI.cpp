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

	ceva = new QLabel("Titlu:");
	edit = new QLineEdit;

	lyForm->addRow(ceva, edit);

	ceva2 = new QLabel("Rank:");
	rank = new QLineEdit;

	lyForm->addRow(ceva2, rank);

	lystanga->addWidget(form);

	slider = new QSlider(Qt::Horizontal);
	slider->setTickInterval(1);
	slider->setMinimum(1);
	slider->setMaximum(10);
	lystanga->addWidget(slider);

	actualizare = new QPushButton("Actualizare!");
	lystanga->addWidget(actualizare);

	modifica = new QPushButton("Modifica!");
	lystanga->addWidget(modifica);

	sterge = new QPushButton("Sterge!");
	lystanga->addWidget(sterge);
	stanga->setMinimumWidth(500);

	tabelView = new QTableView();
	model = new MyTableModel(srv.get_all());
	tabelView->setModel(model);
	tabelView->setMaximumHeight(300);
	lystanga->addWidget(tabelView);
	lystanga->addSpacing(50);


	dreapta = new QWidget;
	lydreapta = new QVBoxLayout;

	dreapta->setLayout(lydreapta);

	QStringList antet;
	antet << "ID: " << "Titlu: ";
	antet << "Artist: " << "Rank: ";

	tabelMelodii = new QTableWidget{ 5,4 };
	tabelMelodii->setHorizontalHeaderLabels(antet);
	tabelMelodii->horizontalHeader()->setSectionResizeMode(QHeaderView::ResizeToContents);
	tabelMelodii->setSelectionBehavior(QAbstractItemView::SelectRows);
	tabelMelodii->setMinimumWidth(400);
	lydreapta->addWidget(tabelMelodii);


	lymain->addWidget(stanga); lymain->addWidget(dreapta);
	this->setMinimumHeight(600);
	this->setMinimumWidth(1000);
}

void GUI::conecteaza()
{

	QObject::connect(actualizare, &QPushButton::clicked, [&] {
		vector<Melodie>da = srv.get_all();
		actual(da);
		});

	QObject::connect(modifica, &QPushButton::clicked, [&] {
		try {
			int id = idcurent, rank = slider->value();
			string titlu = edit->text().toStdString();
			srv.modifica(id, titlu, rank);
			actual(srv.get_all());
		}
		catch (RepoException& re) {
			QMessageBox::warning(this, "Warning", QString::fromStdString(re.get_mesaj()));
		}
		});

	QObject::connect(sterge, &QPushButton::clicked, [&] {
		try {
			int id = idcurent;
			Melodie curenta = srv.cauta(id);
			bool ultimul = true;
			vector<Melodie> v = srv.get_all();
			for (auto& m : v) {
				if (m.get_artist() == curenta.get_artist()) {
					if (m.get_rank() > curenta.get_rank()) {
						ultimul = false;
					}
				}
			}
			if (!ultimul) {
				srv.sterge(id);
				actual(srv.get_all());
			}
			else {
				QMessageBox::information(this, "Info", QString::fromStdString("E ultima, nu o pot sterge!"));
			}
		}
		catch (RepoException& re) {
			QMessageBox::warning(this, "Warning", QString::fromStdString(re.get_mesaj()));
		}
		});

	QObject::connect(slider, &QSlider::valueChanged, [&] {
		int i =  slider->value();
		rank->setText(QString::number(i));
		});

	QObject::connect(tabelView->selectionModel(), &QItemSelectionModel::selectionChanged, [this]() {
		if (tabelView->selectionModel()->selectedIndexes().isEmpty()) {
			edit->clear();
			return;
		}
		QString id,titlu,rank;
		int selRow = tabelView->selectionModel()->selectedIndexes().at(0).row();
		id = tabelView->model()->data(tabelView->model()->index(selRow, 0), Qt::DisplayRole).toString();
		titlu = tabelView->model()->data(tabelView->model()->index(selRow, 1), Qt::DisplayRole).toString();
		rank = tabelView->model()->data(tabelView->model()->index(selRow, 3), Qt::DisplayRole).toString();
		edit->setText(titlu);
		slider->setValue(rank.toInt());
		idcurent = id.toInt();
		});

}

void GUI::actual(vector<Melodie> te)
{
	vector<Melodie> v = te;
	sort(v.begin(), v.end(), [](const Melodie& l1, const Melodie& l2) {return l1.get_rank() < l2.get_rank(); });

	tabelMelodii->clearContents();
	tabelMelodii->setRowCount(v.size());
	tabelMelodii->setMinimumWidth(400);
	int index = 0;
	for (auto& l : v) {
		c1 = new QTableWidgetItem(QString::number(l.get_id()));
		c2 = new QTableWidgetItem(QString::fromStdString(l.get_titlu()));
		c3 = new QTableWidgetItem(QString::fromStdString(l.get_artist()));
		c4 = new QTableWidgetItem(QString::number(l.get_rank()));
		tabelMelodii->setItem(index, 0, c1);
		tabelMelodii->setItem(index, 1, c2);
		tabelMelodii->setItem(index, 2, c3);
		tabelMelodii->setItem(index, 3, c4);
		index++;
	}

	model->seteazaLocatari(v);

	repaint();
}
