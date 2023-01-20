#pragma once
#include <qwidget.h>
#include <qtableview.h>
#include "pok.h"
#include <vector>
#include <QAbstractTableModel>

using std::vector;


class MyTableModel : public QAbstractTableModel {
	vector<Locatar> locatari;
public:
	MyTableModel(vector<Locatar>& _locatari): locatari{_locatari}{}
	
	int rowCount(const QModelIndex& parent = QModelIndex()) const override {
		return locatari.size();
	}

	int columnCount(const QModelIndex& parent = QModelIndex()) const override {
		return 4;
	}

	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override {
		if (role == Qt::BackgroundRole) {
			switch (index.row() % 3) {
				case 0: return QBrush{ QColor{235,91,91} }; break;
				case 1: return QBrush{ QColor{250,253,162} }; break;
				case 2: return QBrush{ QColor{136,205,252} }; break;
				default: break;
			}
		}

		if (role == Qt::DisplayRole) {
			Locatar  l = locatari[index.row()];
			switch (index.column()){
				case 0: return QString::fromStdString(std::to_string(l.get_ap())); break;
				case 1: return QString::fromStdString(l.get_nume()); break;
				case 2: return QString::fromStdString(std::to_string(l.get_sup())); break;
				case 3: return QString::fromStdString(std::to_string(l.get_tip())); break;
				default: break;
			}
		}

		if (role == Qt::FontRole) {
			int rand = index.row() ;
			switch (rand%3) {
				case 0: { QFont f("Arial", 10); f.setUnderline(true); return f;  break; }
				case 1: { QFont f("Comic Sans MS", 10); f.setBold(true); return f;  break; }
				case 2: { QFont f("Palatino Linotype", 10); f.setItalic(true); return f;  break; }
				default: break;
			}
		}
		return QVariant{};
	}

	QVariant headerData(int section, Qt::Orientation orientation, int role) const override
	{
		if (orientation == Qt::Horizontal){
			if (role == Qt::DisplayRole) {
				switch (section) {
					case 0: return "Apartament"; break;
					case 1: return "Nume locatar"; break;
					case 2: return "Suprafata"; break;
					case 3: return "Tipul de comfort"; break;
					default: break;
				}
			}
		}
		
		if (orientation == Qt::Vertical) {
			if (role == Qt::DisplayRole) {
				return section + 1;
			}
		}
		
		return QVariant();
	}

	void seteazaLocatari( vector<Locatar>& locatari) {
		this->locatari = locatari;
		QModelIndex topLeft = createIndex(0, 0);
		QModelIndex bottomRight = createIndex(rowCount(), columnCount());
		emit dataChanged(topLeft, bottomRight);
		emit layoutChanged();
	}
};