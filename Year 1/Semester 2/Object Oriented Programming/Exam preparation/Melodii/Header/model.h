#pragma once
#include <qwidget.h>
#include <qtableview.h>
#include "domeniu.h"
#include <vector>
#include <QAbstractTableModel>

using std::vector;


class MyTableModel : public QAbstractTableModel {
	vector<Melodie> melodii;
public:
	MyTableModel(vector<Melodie>& _melodii) : melodii{ _melodii } {}

	int rowCount(const QModelIndex& parent = QModelIndex()) const override {
		return melodii.size();
	}

	int columnCount(const QModelIndex& parent = QModelIndex()) const override {
		return 5;
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
			Melodie  l = melodii[index.row()];
			int cati = 0;
			for (auto& m : melodii) {
				if (m.get_rank() == l.get_rank()) {
					cati++;
				}
			}

			switch (index.column()) {
			case 0: return QString::fromStdString(std::to_string(l.get_id())); break;
			case 1: return QString::fromStdString(l.get_titlu()); break;
			case 2: return QString::fromStdString(l.get_artist()); break;
			case 3: return QString::fromStdString(std::to_string(l.get_rank())); break;
			case 4: return QString::fromStdString(std::to_string(cati)); break;
			default: break;
			}
		}

		if (role == Qt::FontRole) {
			int rand = index.row();
			switch (rand % 3) {
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
		if (orientation == Qt::Horizontal) {
			if (role == Qt::DisplayRole) {
				switch (section) {
				case 0: return "ID"; break;
				case 1: return "Titlu"; break;
				case 2: return "Artist"; break;
				case 3: return "Rank"; break;
				case 4: return "Cate de rank"; break;
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

	void seteazaLocatari(vector<Melodie>& melodiinoi) {
		this->melodii = melodiinoi;
		QModelIndex topLeft = createIndex(0, 0);
		QModelIndex bottomRight = createIndex(rowCount(), columnCount());
		emit dataChanged(topLeft, bottomRight);
		emit layoutChanged();
	}
};