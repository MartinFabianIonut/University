#pragma once

#include "domeniu.h"
#include <qtableview.h>
#include <qslider.h>
#include <vector>

using std::vector;

class TableModel : public QAbstractTableModel
{
	Q_OBJECT
private:
	vector < Produs > produse;
	QSlider& slider;
public:
	TableModel(vector < Produs > _produse, QSlider& _slider) : produse{ _produse }, slider{ _slider }{}

	int rowCount(const QModelIndex& parent = QModelIndex()) const override
	{
		return produse.size();
	}

	int columnCount(const QModelIndex& parent = QModelIndex()) const override
	{
		return 5;
	}

	QVariant headerData(int section, Qt::Orientation orientation, int role = Qt::DisplayRole) const override
	{
		if (Qt::Horizontal == orientation)
		{
			if (Qt::DisplayRole == role)
			{
				switch (section)
				{
				case 0:return "Id";break;
				case 1:return "Nume";break;
				case 2:return "Tip";break;
				case 3:return "Pret";break;
				case 4:return "Numar Vocale";break;
				default:break;
				}
			}
		}
		return QVariant();
	}

	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override
	{
		if (Qt::DisplayRole == role)
		{
			Produs aux = produse[index.row()];
			switch (index.column())
			{
			case 0:return QString::number(aux.get_id()); break;
			case 1:return QString::fromStdString(aux.get_nume()); break;
			case 2:return QString::fromStdString(aux.get_tip()); break;
			case 3:return QString::number(aux.get_pret()); break;
			case 4:
				int nrVocale = 0;
				string nume = aux.get_nume();
				for (auto& litera : nume)
				{
					if (strchr("aeiouAEIOU", litera))
						nrVocale++;
				}
				return QString::number(nrVocale); break;
			}
		}
		if (Qt::BackgroundRole == role)
		{
			Produs aux = produse[index.row()];
			if (aux.get_pret() <= slider.value())
				return QBrush{ Qt::red };
			return QBrush{ Qt::white };
		}
		if (Qt::UserRole == role)
		{
			int id = produse[index.row()].get_id();
			return QString::number(id);
		}
		return QVariant();
	}

	void update()
	{
		emit layoutChanged();
	}

	void setProducts(vector<Produs> pro)
	{
		this->produse = pro;
		auto topLeft = createIndex(0, 0);
		auto bottomR = createIndex(rowCount(), columnCount());
		emit dataChanged(topLeft, bottomR);
		emit layoutChanged();
	}
};