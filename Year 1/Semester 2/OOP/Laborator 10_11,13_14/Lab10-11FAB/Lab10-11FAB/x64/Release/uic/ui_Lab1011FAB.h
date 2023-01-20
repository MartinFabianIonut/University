/********************************************************************************
** Form generated from reading UI file 'Lab1011FAB.ui'
**
** Created by: Qt User Interface Compiler version 6.3.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_LAB1011FAB_H
#define UI_LAB1011FAB_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_Lab1011FABClass
{
public:
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QWidget *centralWidget;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *Lab1011FABClass)
    {
        if (Lab1011FABClass->objectName().isEmpty())
            Lab1011FABClass->setObjectName(QString::fromUtf8("Lab1011FABClass"));
        Lab1011FABClass->resize(600, 400);
        menuBar = new QMenuBar(Lab1011FABClass);
        menuBar->setObjectName(QString::fromUtf8("menuBar"));
        Lab1011FABClass->setMenuBar(menuBar);
        mainToolBar = new QToolBar(Lab1011FABClass);
        mainToolBar->setObjectName(QString::fromUtf8("mainToolBar"));
        Lab1011FABClass->addToolBar(mainToolBar);
        centralWidget = new QWidget(Lab1011FABClass);
        centralWidget->setObjectName(QString::fromUtf8("centralWidget"));
        Lab1011FABClass->setCentralWidget(centralWidget);
        statusBar = new QStatusBar(Lab1011FABClass);
        statusBar->setObjectName(QString::fromUtf8("statusBar"));
        Lab1011FABClass->setStatusBar(statusBar);

        retranslateUi(Lab1011FABClass);

        QMetaObject::connectSlotsByName(Lab1011FABClass);
    } // setupUi

    void retranslateUi(QMainWindow *Lab1011FABClass)
    {
        Lab1011FABClass->setWindowTitle(QCoreApplication::translate("Lab1011FABClass", "Lab1011FAB", nullptr));
    } // retranslateUi

};

namespace Ui {
    class Lab1011FABClass: public Ui_Lab1011FABClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_LAB1011FAB_H
