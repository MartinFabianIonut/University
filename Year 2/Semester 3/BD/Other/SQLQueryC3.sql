USE MASTER
IF EXISTS (SELECT NAME FROM sys.databases WHERE NAME = 'Masini')
	DROP DATABASE Masini

GO
CREATE DATABASE Masini
GO
USE Masini
GO

CREATE TABLE Clienti (
	id_c INT PRIMARY KEY IDENTITY,
	nume VARCHAR(20),
	prenume VARCHAR(20)
);

CREATE TABLE Marci (
	id_m INT PRIMARY KEY IDENTITY,
	denumire VARCHAR(20)
);

CREATE TABLE Angajati (
	id_a INT PRIMARY KEY IDENTITY,
	nume VARCHAR(20),
	prenume VARCHAR(20)
);

CREATE TABLE Autovehicule (
	id_auto INT PRIMARY KEY IDENTITY,
	numar_inmatriculare VARCHAR(10),
	tip_motorizare VARCHAR(10),
	id_m INT FOREIGN KEY REFERENCES Marci(id_m) ON DELETE NO ACTION ON UPDATE NO ACTION
);
DROP TABLE Inchirieri
CREATE TABLE Inchirieri (
	id_a INT,
	id_c INT,
	id_auto INT,
	inchiriere DATETIME,
	returnare DATETIME,
	CONSTRAINT fk_Angajati FOREIGN KEY (id_a) REFERENCES Angajati(id_a) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_Clienti FOREIGN KEY (id_c) REFERENCES Clienti(id_c) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_Autovehicule FOREIGN KEY (id_auto) REFERENCES Autovehicule(id_auto) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT pk_Inchirieri PRIMARY KEY (id_a,id_c,id_auto,inchiriere)
); 

INSERT INTO Angajati VALUES
('Marinescu','Silvia'),
('Traian', 'Valer'),
('Tomoioaga','Andrei')

INSERT INTO Clienti VALUES
('Dobrescu','Anton'),
('Pop', 'Maria'),
('Vasile','Elisabeta')

INSERT INTO Marci VALUES
('BMW'),
('Porche'),
('Audi')

INSERT INTO Autovehicule VALUES
('CJ-22-RAT','benzina',1),
('CJ-23-TUR', 'motorina',2),
('CJ-89-YUS','benzina',1),
('CJ-63-XIT', 'motorina',3),
('CJ-89-MUT','benzina',3)

GO
CREATE OR ALTER PROCEDURE ActiunePeRegistrulInchirieri
	@id_a INT, 
	@id_c INT,
	@id_auto INT,
	@inchiriere DATETIME,
	@returnare DATETIME,
	@operatie BIT
AS
BEGIN
	IF @operatie = 1
		INSERT INTO Inchirieri VALUES (@id_a,@id_c,@id_auto,@inchiriere,@returnare)
	ELSE
		UPDATE Inchirieri
		SET inchiriere=@inchiriere, returnare=@returnare
		WHERE id_a=@id_a AND id_c=@id_c AND id_auto=@id_auto
END;

EXEC ActiunePeRegistrulInchirieri 1,1,1,'2023-02-17 17:00:00', '2023-02-19 13:00:00',1
EXEC ActiunePeRegistrulInchirieri 4,1,1,'2023-01-13 17:00:00', '2023-01-15 13:00:00',1
EXEC ActiunePeRegistrulInchirieri 1,2,3,'2023-01-17 14:00:00', '2023-01-19 13:00:00',1
EXEC ActiunePeRegistrulInchirieri 1,2,1,'2023-02-21 17:00:00', '2023-02-24 13:00:00',1
EXEC ActiunePeRegistrulInchirieri 1,1,2,'2023-01-23 17:00:00', '2023-02-01 13:00:00',1
EXEC ActiunePeRegistrulInchirieri 4,3,3,'2023-01-23 19:00:00', '2023-01-23 23:00:00',1
EXEC ActiunePeRegistrulInchirieri 1,1,2,'2023-02-17 17:00:00', '2023-02-19 13:00:00',1
EXEC ActiunePeRegistrulInchirieri 1,2,1,'2023-02-27 17:00:00', '2023-02-28 13:00:00',1

SELECT * FROM Inchirieri

GO
CREATE OR ALTER VIEW vwAngajatiCuInchirieriLunaAstaPentruMarca 
AS
SELECT TOP (SELECT COUNT(*) FROM Angajati) nume+' '+prenume AS NumeAngajat, COUNT(*) AS NrInchirieriDeBMW
FROM Angajati A 
INNER JOIN Inchirieri I ON A.id_a = I.id_a
INNER JOIN Autovehicule AU ON I.id_auto=AU.id_auto
INNER JOIN Marci M ON M.id_m=AU.id_m
WHERE M.denumire = 'Audi' AND MONTH(GETDATE())=MONTH(I.inchiriere)
GROUP BY nume+' '+prenume
ORDER BY nume+' '+prenume
GO

SELECT * FROM vwAngajatiCuInchirieriLunaAstaPentruMarca

GO
CREATE OR ALTER FUNCTION MasiniLibereLaData (@data DATETIME)
RETURNS @Tabelul TABLE (NumarAutovehicul VARCHAR(20),Marca VARCHAR(20),TipMotorizare VARCHAR(20))
AS
BEGIN
	INSERT INTO @Tabelul 
	SELECT A.numar_inmatriculare as NRINM, M.denumire AS DEN, A.tip_motorizare as TIP FROM 
	Autovehicule A
	INNER JOIN Marci M ON A.id_m=M.id_m
	WHERE A.id_auto NOT IN (SELECT A.id_auto FROM Marci M
							INNER JOIN Autovehicule A ON M.id_m=A.id_m
							INNER JOIN Inchirieri I ON I.id_auto=A.id_auto
							WHERE I.inchiriere<=@data AND I.returnare>=@data)
	RETURN;
END;
GO

SELECT * FROM MasiniLibereLaData('2023-02-17 17:00:00')



	
