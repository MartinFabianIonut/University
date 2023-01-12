USE MASTER
IF EXISTS (SELECT NAME FROM sys.databases WHERE NAME = 'Politie')
	DROP DATABASE Politie

GO
CREATE DATABASE Politie
GO
USE Politie
GO

CREATE TABLE SectiiPolitie (
	id_sp INT PRIMARY KEY IDENTITY,
	denumire VARCHAR(10),
	adresa VARCHAR(10)
);

CREATE TABLE Grade (
	id_g INT PRIMARY KEY IDENTITY,
	denumire VARCHAR(20),
	descriere VARCHAR(80)
);

CREATE TABLE Functii (
	id_f INT PRIMARY KEY IDENTITY,
	denumire VARCHAR(20),
	descriere VARCHAR(80)
);

CREATE TABLE Politisti (
	id_p INT PRIMARY KEY IDENTITY,
	nume VARCHAR(20),
	prenume VARCHAR(20),
	id_sp INT FOREIGN KEY REFERENCES SectiiPolitie(id_sp) ON DELETE NO ACTION ON UPDATE NO ACTION,
	id_g INT FOREIGN KEY REFERENCES Grade(id_g) ON DELETE NO ACTION ON UPDATE NO ACTION,
	id_f INT FOREIGN KEY REFERENCES Functii(id_f) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE Sectoare (
	id_s INT PRIMARY KEY IDENTITY,
	denumire VARCHAR(20),
	descriere VARCHAR(80)
);

CREATE TABLE Patrulari (
	id_patrulare INT PRIMARY KEY IDENTITY,
	id_s INT,
	id_p INT,
	data_intrare DATE,
	CONSTRAINT fk_Sectoare FOREIGN KEY (id_s) REFERENCES Sectoare(id_s) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_Politisti FOREIGN KEY (id_p) REFERENCES Politisti(id_p) ON DELETE CASCADE ON UPDATE CASCADE,
); 

INSERT INTO Grade VALUES ('Grad 1', 'desc 1'),('Grad 2', 'desc 2'),('Grad 3', 'desc 3'),('Grad 4', 'desc 4')
INSERT INTO Functii VALUES ('Functia 1', 'desc 11'),('Functia 2', 'desc 22'),('Functia 3', 'desc 33'),('Functia 4', 'desc 44')
INSERT INTO SectiiPolitie VALUES ('Sectia 1', 'adresa 1'),('Sectia 2', 'adresa 2'),('Sectia 3', 'adresa 3')

INSERT INTO Politisti(nume, prenume,id_sp, id_g,id_f) VALUES 
('Ion','Maria',1,1,1),
('Traian','Maria',1,2,2),
('Ion','Eusebiu',2,3,1),
('Caramitru','Tudor',2,1,3),
('Georgescu','Vasile',3,4,2),
('Vasile','Olga',3,3,4),
('Pop','Ionel',3,3,1)
INSERT INTO Politisti(nume, prenume,id_sp, id_g,id_f) VALUES 
('Anastasa','Valer',1,3,4),
('Grigore','Maria',1,3,1)

INSERT INTO Sectoare VALUES ('Sector 1', 'desc 1'),('Sector 2', 'desc 2'),('Sector 3', 'desc 3'),('Sector 4', 'desc 4')

INSERT INTO Patrulari (id_s, id_p, data_intrare) VALUES
(1,1,'2022-01-14'),(1,2,'2022-01-24'),(2,3,'2022-02-14'),(2,4,'2022-03-17'),
(4,5,'2022-01-01'),(4,6,'2022-05-15'),(4,7,'2022-12-23')
INSERT INTO Patrulari (id_s, id_p, data_intrare) VALUES (1,8,'2022-01-25'),(1,9,'2022-05-15')
INSERT INTO Patrulari (id_s, id_p, data_intrare) VALUES (1,1,'2022-01-14'),(1,4,'2022-01-14'),(1,4,'2022-01-14')
INSERT INTO Patrulari (id_s, id_p, data_intrare) VALUES (1,1,'2022-01-14')
INSERT INTO Patrulari (id_s, id_p, data_intrare) VALUES (3,6,'2022-01-14')

SELECT * FROM Politisti
SELECT * FROM Patrulari

GO
CREATE OR ALTER VIEW vwPolitistiSectia1Ian22 
AS
SELECT TOP (100) PERCENT P.nume, P.prenume 
FROM Politisti P
INNER JOIN SectiiPolitie SP ON P.id_sp=SP.id_sp
INNER JOIN Patrulari PA ON P.id_p = PA.id_p
WHERE SP.denumire = 'Sectia 1' AND MONTH(PA.data_intrare)=1 AND YEAR(PA.data_intrare)=2022
GROUP BY P.nume, P.prenume, SP.denumire
ORDER BY SP.denumire,P.nume

GO
SELECT * FROM vwPolitistiSectia1Ian22

GO
CREATE OR ALTER FUNCTION ProgramariSuprapuse2 (@data DATE)
RETURNS TABLE 
AS
	RETURN
		SELECT TOP(100) SP.denumire AS SPD, P.nume, P.prenume, G.denumire AS GD, F.denumire AS FD, COUNT(*) as nr
		FROM SectiiPolitie SP 
		INNER JOIN Politisti P ON P.id_sp = SP.id_sp
		INNER JOIN Grade G ON G.id_g=P.id_g
		INNER JOIN Functii F ON P.id_f=F.id_f
		INNER JOIN Patrulari PA ON PA.id_p=P.id_p
		WHERE data_intrare=@data
		GROUP BY P.id_p,SP.denumire, P.nume, P.prenume, G.denumire, F.denumire
		HAVING COUNT(*) > 1
	    ORDER BY COUNT(*) ASC;
GO
SELECT * FROM ProgramariSuprapuse2('2022-01-14')