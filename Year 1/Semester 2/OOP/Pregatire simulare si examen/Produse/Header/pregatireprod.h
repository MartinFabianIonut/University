#pragma once

#include <QtWidgets/QMainWindow>
#include "ui_pregatireprod.h"

class pregatireprod : public QMainWindow
{
    Q_OBJECT

public:
    pregatireprod(QWidget *parent = nullptr);
    ~pregatireprod();

private:
    Ui::pregatireprodClass ui;
};
