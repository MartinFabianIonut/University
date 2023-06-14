USE MASTER
IF EXISTS(SELECT NAME FROM sys.databases WHERE NAME = 'LIBRARIE')
	DROP DATABASE LIBRARIE

GO
CREATE DATABASE LIBRARIE
GO
USE LIBRARIE
GO

CREATE TABLE Categorii_de_produse
( 
id_categorie INT PRIMARY KEY IDENTITY,
nume_categorie NVARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Ani_aparitie
( 
id_an INT PRIMARY KEY IDENTITY,
anul INT NOT NULL UNIQUE
);

CREATE TABLE Limbi
( 
id_limba INT PRIMARY KEY IDENTITY,
limba NVARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Tari
( 
id_tara INT PRIMARY KEY IDENTITY,
tara NVARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Edituri
( 
id_editura INT PRIMARY KEY IDENTITY,
editura NVARCHAR(50) NOT NULL UNIQUE,
sediu NVARCHAR(100) NOT NULL UNIQUE,
anul_infiintarii INT NOT NULL,
id_tara INT FOREIGN KEY REFERENCES Tari(id_tara) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Stari_ale_produselor
( 
id_stare INT PRIMARY KEY IDENTITY,
tip_stare NVARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Carti
( 
id_carte INT PRIMARY KEY IDENTITY,
titlu NVARCHAR(50) NOT NULL,
numar_de_pagini INT NOT NULL,
tip_format NVARCHAR(50) NOT NULL,
data_introducerii_in_librarie DATETIME DEFAULT GETDATE(),
pret FLOAT NOT NULL,
stoc INT NOT NULL,
id_an INT FOREIGN KEY REFERENCES Ani_aparitie(id_an) ON UPDATE CASCADE ON DELETE CASCADE,
id_editura INT FOREIGN KEY REFERENCES Edituri(id_editura) ON UPDATE CASCADE ON DELETE CASCADE,
id_categorie INT FOREIGN KEY REFERENCES Categorii_de_produse(id_categorie) ON UPDATE CASCADE ON DELETE CASCADE,
id_stare INT FOREIGN KEY REFERENCES Stari_ale_produselor(id_stare) ON UPDATE CASCADE ON DELETE CASCADE
);

ALTER TABLE Carti
ADD id_coperta INT FOREIGN KEY REFERENCES Tipuri_de_coperta(id_coperta) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE Carti
ADD id_curent INT FOREIGN KEY REFERENCES CurenteLiterare(id_curent) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE Carti
ADD id_gen INT FOREIGN KEY REFERENCES GenuriLiterare(id_gen) ON UPDATE CASCADE ON DELETE CASCADE;

CREATE TABLE GenuriLiterare
( 
id_gen INT PRIMARY KEY IDENTITY,
gen_literar NVARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE CurenteLiterare
( 
id_curent INT PRIMARY KEY IDENTITY,
curent_literar NVARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Multilingv
( 
id_limba INT,
id_carte INT,
CONSTRAINT fk_CartiMultilingv FOREIGN KEY (id_carte) REFERENCES Carti(id_carte),
CONSTRAINT fk_LimbiMultilingv FOREIGN KEY (id_limba) REFERENCES Limbi(id_limba),
CONSTRAINT pk_Multilingv PRIMARY KEY (id_carte, id_limba)
);

CREATE TABLE Scriitori
( 
id_scriitor INT PRIMARY KEY IDENTITY,
nume NVARCHAR(50) NOT NULL,
prenume NVARCHAR(50) NOT NULL
);

CREATE TABLE Publicatii
( 
id_carte INT,
id_scriitor INT,
CONSTRAINT fk_CartiPublicatii FOREIGN KEY (id_carte) REFERENCES Carti(id_carte) ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT fk_ScriitoriPublicatii FOREIGN KEY (id_scriitor) REFERENCES Scriitori(id_scriitor) ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT pk_Publicatii PRIMARY KEY (id_carte, id_scriitor)
);

CREATE TABLE Recenzii
( 
id_recenzie INT PRIMARY KEY IDENTITY,
descriere NVARCHAR(250) NOT NULL UNIQUE,
stele INT NOT NULL CHECK (stele >= 1 AND stele <=5),
data_recenziei DATETIME DEFAULT GETDATE(),
id_carte INT FOREIGN KEY REFERENCES Carti(id_carte) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Premii
( 
id_premiu INT PRIMARY KEY IDENTITY,
premiu NVARCHAR(50) NOT NULL UNIQUE,
valoare INT NOT NULL UNIQUE
);

CREATE TABLE Laureati
(
id_scriitor INT,
id_premiu INT,
motivul_juriului NVARCHAR(250) NOT NULL UNIQUE,
data_decernarii DATETIME NOT NULL,
CONSTRAINT fk_ScriitoriLaureati FOREIGN KEY (id_scriitor) REFERENCES Scriitori(id_scriitor),
CONSTRAINT fk_PremiiLaureati FOREIGN KEY (id_premiu) REFERENCES Premii(id_premiu),
CONSTRAINT pk_Laureatii PRIMARY KEY (id_scriitor, id_premiu)
);

CREATE TABLE Promotii
( 
id_promotie INT PRIMARY KEY IDENTITY,
denumire NVARCHAR(50) NOT NULL UNIQUE,
descriere_promotie NVARCHAR(150) NOT NULL UNIQUE,
inceput_valabilitate DATETIME NOT NULL,
sfarsit_valabilitate DATETIME NOT NULL,
discount INT NOT NULL
);

CREATE TABLE Utilizatori
( 
id_utilizator INT PRIMARY KEY IDENTITY,
nume NVARCHAR(50) NOT NULL,
prenume NVARCHAR(50) NOT NULL,
adresa NVARCHAR(150) NOT NULL UNIQUE,
email NVARCHAR(50) NOT NULL UNIQUE,
telefon NVARCHAR(15) NOT NULL UNIQUE
);

CREATE TABLE Cos_de_cumparaturi
( 
id_cos INT PRIMARY KEY IDENTITY,
id_utilizator INT FOREIGN KEY REFERENCES Utilizatori(id_utilizator) ON UPDATE CASCADE ON DELETE CASCADE
);

ALTER TABLE Cos_de_cumparaturi
ADD serie CHAR(3), numar CHAR(10), emitere DATETIME DEFAULT GETDATE();

CREATE TABLE Randuri_cos
(
id_cos INT,
id_carte INT,
cantitate INT,
pret MONEY,
CONSTRAINT fk_CarteInCos FOREIGN KEY (id_carte) REFERENCES Carti(id_carte),
CONSTRAINT fk_CosInCos FOREIGN KEY (id_cos) REFERENCES Cos_de_cumparaturi(id_cos),
CONSTRAINT pk_RanduriCos PRIMARY KEY (id_cos, id_carte)
);

CREATE TABLE Tipuri_de_coperta
( 
id_coperta INT PRIMARY KEY IDENTITY,
coperta NVARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE Culori
( 
id_culoare INT PRIMARY KEY IDENTITY,
culoare NVARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE CartiColorate
(
id_carte INT,
id_culoare INT,
CONSTRAINT fk_CartiCartiColorate FOREIGN KEY (id_carte) REFERENCES Carti(id_carte),
CONSTRAINT fk_CuloriCartiColorate FOREIGN KEY (id_culoare) REFERENCES Culori(id_culoare),
CONSTRAINT pk_CartiColorate PRIMARY KEY (id_carte, id_culoare)
);
