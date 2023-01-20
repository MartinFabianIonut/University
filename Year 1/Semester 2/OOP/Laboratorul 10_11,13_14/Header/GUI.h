#pragma once
#include <vector>
#include <string>
#include <QtWidgets/QApplication>
#include <QLabel>
#include <QPushButton>
#include <QHBoxLayout>
#include <QFormLayout>
#include <QLineEdit>
#include <QTableWidget>
#include <QMessageBox>
#include <QHeaderView>
#include <QGroupBox>
#include <QRadioButton>
#include <QList>
#include <QListWidget>
#include <QComboBox>
#include <qtableview.h>

#include "observer.h"
#include "Service.h"
#include "cosuri.h"
#include "model.h"

using std::vector;
using std::string;
//
//class dyButtons : public QWidget {
//private:
//	std::vector<QPushButton*> butoane;
//public:
//	void ceva();
//	void show();
//	void update();
//};




class GUI : public QWidget,public Observer {
private:
	Service& ctr;

	QLabel* lbApartament = new QLabel{ "Apartamentul: " };
	QLabel* lbNume = new QLabel{ "Nume proprietar: " };
	QLabel* lbSuprafata = new QLabel{ "Suprafata apartament: " };
	QLabel* lbTip = new QLabel{ "Tip comfort: " };

	QComboBox* comboTip = new QComboBox;

	QLineEdit* editApartament, * editNume, * editSuprafata, * editTip;
	
	QPushButton* butonAdauga;
	QPushButton* butonSterge;
	QPushButton* butonModifica;

	QLineEdit* filtrareDupaTip;
	QLineEdit* filtrareDupaSuprafataMin;
	QLineEdit* filtrareDupaSuprafataMax;
	QPushButton* butonFiltrareTip, * butonFiltrareSuprafata;

	QGroupBox* grupajSortari = new QGroupBox(tr("Sortare dupa:"));

	QRadioButton* sortareDupaNume = new QRadioButton(QString::fromStdString("nume"));
	QRadioButton* sortareDupaSuprafata = new QRadioButton(QString::fromStdString("suprafata"));
	QRadioButton* sortareDupaTipSuprafata = new QRadioButton(QString::fromStdString("tip + suprafata"));
	QPushButton* butonSort1, * butonSort2;

	QPushButton* actualizare;

	QPushButton* undo;

	QLineEdit* ad, * ra, *ex;
	QPushButton* deleteall, *addbyid, *addrandom, *exportt,*deschidecos1, *deschidecos2;

	QTableWidget* tabelLocatari;
	QTableWidget* tabelNotificari;

	QTableWidgetItem* c1, * c2, * c3, * c4;

	QListWidget* lista;

	MyTableModel* model;
	QTableView* tabelView;
	


	std::vector<QPushButton*> butoane;


	QHBoxLayout* lyMain;

	QWidget* dreapta;
	QVBoxLayout* lyDreapta;

	void connectSignalSlots();
	void actualizareLocatari(vector<Locatar>locatari);
	void actualizareNotificari(vector<Locatar>locatari);
	void actNot();
	void initGUI();

public:
	GUI(Service& _ctr) : ctr{ _ctr } {
		initGUI();
		connectSignalSlots();
		actualizareLocatari(ctr.getAll());
	}

	void update() override {
		actualizareNotificari(ctr.getAllNotificari());
	}

	void adaugaGUI();
	void stergeGUI();
	void modificaGUI();
	void filtrareTipGUI();
	void filtrareSuprafataGUI();
	void sortareNumeCrescGUI();
	void sortareNumeDescrescGUI();
	void sortareSuprafataCrescGUI();
	void sortareSuprafataDescrescGUI();
	void sortareTipSuprafataCrescGUI();
	void sortareTipSuprafataDescrescGUI();
};