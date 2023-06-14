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




class GUI : public QWidget {
private:
	Service& srv;

	QHBoxLayout* lymain;

	QWidget* stanga, * dreapta;
	QVBoxLayout* lystanga, *lydreapta;

	QLabel* lbnume, * lbsoclu, * lbpret, *soclu;
	QLineEdit* editn, * edits, * editp;

	QPushButton* ad, * fil, *actualizare;

	QListWidget* l1, * l2;


	void init();
	void conecteaza();
	void actual(vector<Procesor> pr, vector<Placa> pl);
public:
	GUI(Service& _srv) :srv{ _srv } { 
		init(); conecteaza(); actual(srv.get_all_procesoare_service(), srv.get_all_placi_service()); 
	}
};