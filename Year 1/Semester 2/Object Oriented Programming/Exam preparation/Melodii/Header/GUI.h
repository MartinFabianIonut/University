#pragma once
#include "service.h"
#include <qpushbutton.h>
#include <qlistwidget.h>
#include <qlineedit.h>
#include <qlabel.h>
#include <QHBoxLayout>
#include <QVBoxLayout>
#include <QWidget>
#include <QFormLayout>
#include <QMessageBox>
#include <QTableWidget>
#include <QHeaderView>
#include <QPainter>
#include <vector>
#include "model.h"
#include <QSlider>

using std::vector;

class GUI : public QWidget {

private:
	Service& srv;
	int idcurent;
	QHBoxLayout* lymain;

	QWidget* stanga, * dreapta;
	QVBoxLayout* lystanga, * lydreapta;

	QLabel* ceva, *ceva2;
	QLineEdit* edit, *rank;

	QPushButton* actualizare, *modifica, *sterge;
	QSlider* slider;

	QTableWidget* tabelMelodii;
	QTableWidgetItem* c1, * c2, * c3, * c4;


	MyTableModel* model;
	QTableView* tabelView;

	void init();
	void conecteaza();
	void actual(vector<Melodie> t);
public:
	
	GUI(Service& _srv) :srv{ _srv } {
		init(); conecteaza(); actual(srv.get_all());
	}

	/*
	Functie care suprascrie metoda de paintEvent() din clasa de baza QWidget, 
	avand ca scop desenarea a 10 dreptunghiuri de inaltime proportionala cu numarul
	de melodii avand rank-ul egal cu indexul dreptunghiului (redesenarea se face la
	fiecare modificare prin apelarea functiei repaint() in functia de actualizare a
	tabelului cu melodii.
	*/
	void paintEvent(QPaintEvent* ev) override {
		QPainter p{ this };
		int x=0, y = this->height() - 50;
		int ranks[11]={0};
		for (auto& n : srv.get_all()) {
			ranks[n.get_rank()]++;
		}
		int i;
		for (i = 1; i < 11; i++) {
			x += 20;
			p.setBrush(Qt::red);
			p.drawRect(x, y, 7, 4 * ranks[i]);
		}
	}
};