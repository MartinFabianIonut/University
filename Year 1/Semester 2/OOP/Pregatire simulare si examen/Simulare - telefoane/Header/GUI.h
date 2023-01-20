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
#include <vector>

using std::vector;

class GUI : public QWidget {

private:
	Service& srv;

	QHBoxLayout* lymain;

	QWidget* stanga, * dreapta;
	QVBoxLayout* lystanga, * lydreapta;

	QLabel* ceva;
	QLineEdit* edit;

	QPushButton* sortpret, * sortbrand, * sortmodel, * actualizare;

	QListWidget* l1;

	vector<QPushButton*>butoane;


	void init();
	void conecteaza();
	void actual(vector<Telefon> t);
public:
	GUI(Service& _srv) :srv{ _srv } {
		init(); conecteaza(); actual(srv.get_all());
	}
};