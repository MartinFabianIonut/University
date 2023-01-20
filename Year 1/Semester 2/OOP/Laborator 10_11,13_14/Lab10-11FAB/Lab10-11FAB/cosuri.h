#pragma once
#include "service.h"
#include "observer.h"
#include <qwidget.h>
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
#include <random>
#include <chrono> 
#include <qpainter.h>

class CosCRUDGUI:public QWidget,public Observer {
private:
	Service& ctr;

	QVBoxLayout* lyMain;

	QLineEdit *ad, * ra;
	QPushButton *addbyitem, * deleteall, * addrandom;

	QTableWidget* tabelNotificari;

public:
	void initCosCRUDGUI();
	void connectSignalSlots();
	void actualizareNotificari(vector<Locatar>locatari);

	CosCRUDGUI(Service& _ctr) : ctr{ _ctr } {
		initCosCRUDGUI();
		connectSignalSlots();
		actualizareNotificari(ctr.getAllNotificari());
	}

	void update() override {
		actualizareNotificari(ctr.getAllNotificari());
	}
};


class CosReadOnlyGUI :public QWidget,public Observer {
private:
	Service& ctr;
public:
	CosReadOnlyGUI(Service& _ctr) : ctr{ _ctr } {
		ctr.addObserver(this); setFixedHeight(650); qSetFieldWidth(650);
	}

	void update() override {
		repaint();
	}

	void paintEvent(QPaintEvent* ev) override {
		QPainter p{ this };
		std::mt19937 mt{ std::random_device{}() };
		const std::uniform_int_distribution<> dist(0, 600);
		int x, y,r,g,b;
		
		for (auto& n : ctr.getAllNotificari()) {
			x = dist(mt);
			y = dist(mt);
			r = dist(mt) % 256;
			g = dist(mt) % 256;
			b = dist(mt) % 256;
			p.setBrush(QColor{ r,g,b });
			p.drawEllipse(x, y, 70, 70);
		}
	}
};