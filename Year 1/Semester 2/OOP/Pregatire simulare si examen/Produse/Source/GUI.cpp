#include "GUI.h"
#include <qformlayout.h>
#include <qmessagebox.h>
#include <qcolor.h>

void GUI::init()
{
	MainLayout = new QHBoxLayout;
	setLayout(MainLayout);

	QWidget* inputWidget = new QWidget;
	QFormLayout* inputLayout = new QFormLayout;
	inputWidget->setLayout(inputLayout);
	TextId = new QLineEdit;
	Text = new QLineEdit;
	TextType = new QLineEdit;
	TextPrice = new QLineEdit;
	QLabel* idLabel = new QLabel("Id: ");
	QLabel* Label = new QLabel(": ");
	QLabel* typeLabel = new QLabel("Type: ");
	QLabel* priceLabel = new QLabel("Price: ");
	inputLayout->addRow(idLabel, TextId);
	inputLayout->addRow(Label, Text);
	inputLayout->addRow(typeLabel, TextType);
	inputLayout->addRow(priceLabel, TextPrice);

	BtnAdd = new QPushButton("Add product");
	inputLayout->addRow(BtnAdd);

	SliderValue = new QLabel("0");
	Slider = new QSlider(Qt::Horizontal);
	Slider->setTickInterval(1);
	Slider->setMaximum(100);
	inputLayout->addRow(SliderValue, Slider);

	Model = new TableModel{ Serv.get_all(), *Slider };
	Table = new QTableView;
	Table->setModel(Model);

	MainLayout->addWidget(Table);
	MainLayout->addWidget(inputWidget);
}

void GUI::connectSignals()
{
	QObject::connect(Slider, &QSlider::valueChanged, [&]()
		{
			SliderValue->setText(QString::number(Slider->value()));
			Model->update();
		});
	QObject::connect(BtnAdd, &QPushButton::clicked, this, &GUI::add);
}

void GUI::reloadList(vector<Produs> Products)
{
	sort(Products.begin(), Products.end(), [](const Produs& p1, const Produs& p2) {return p1.get_pret() < p2.get_pret(); });
	Model->setProducts(Products);
	Evidence.clear();
	for (auto& product : Products)
	{
		Evidence[product.get_tip()]++;
	}
	notify();
}

void GUI::createWindows()
{
	NumberType* window;
	for (auto& it : Evidence)
	{
		window = new NumberType{ *this, it.first, it.second };
		window->show();
	}
}

void GUI::add()
{
	try
	{
		Serv.adauga_produs(
			TextId->text().toInt(),
			Text->text().toStdString(),
			TextType->text().toStdString(),
			TextPrice->text().toDouble()
		);
		reloadList(Serv.get_all());
	}
	catch (const ValidException& error)
	{
		QMessageBox::warning(this, "Warning", QString::fromStdString(error.getMessage()));
	}
	catch (const RepoException& error)
	{
		QMessageBox::warning(this, "Warning", QString::fromStdString(error.getMessage()));
	}
}

GUI::GUI(Service& _Serv) : Serv{ _Serv }
{
	init();
	connectSignals();
	reloadList(Serv.get_all());
	createWindows();
}

NumberType::NumberType(GUI& _Ui, string nume, int _Number) : Ui{ _Ui }
{
	MainLayout = new QHBoxLayout;
	setLayout(MainLayout);
	setWindowTitle(QString::fromStdString(nume));
	setMinimumHeight(100);
	setMinimumWidth(200);

	Number = new QLabel;
	Number->setText("Numar produse de acest tip: " + QString::number(_Number));

	MainLayout->addWidget(Number);
	Ui.addObserver(this);
}

void NumberType::update()
{
	Number->setText("Numar produse de acest tip: " + QString::number(Ui.Evidence[windowTitle().toStdString()]));
}