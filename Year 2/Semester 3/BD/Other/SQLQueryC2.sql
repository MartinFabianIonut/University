USE MASTER
IF EXISTS (SELECT NAME FROM sys.databases WHERE NAME = 'Clinica')
	DROP DATABASE Clinica

GO
CREATE DATABASE Clinica
GO
USE Clinica
GO

CREATE TABLE Pacienti (
	id_p INT PRIMARY KEY IDENTITY,
	nume VARCHAR(20),
	prenume VARCHAR(20),
	adresa VARCHAR(50)
);

CREATE TABLE Specializari (
	id_s INT PRIMARY KEY IDENTITY,
	denumire VARCHAR(10)
);

CREATE TABLE Diagnostice (
	id_d INT PRIMARY KEY IDENTITY,
	denumire VARCHAR(20),
	descriere VARCHAR(80)
);

CREATE TABLE Medici (
	id_m INT PRIMARY KEY IDENTITY,
	nume VARCHAR(20),
	prenume VARCHAR(20),
	id_s INT FOREIGN KEY REFERENCES Specializari(id_s) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE Lista_pacienti (
	id_m INT,
	id_p INT,
	id_d INT,
	data_consult DATE,
	ora_consult TIME,
	observatii VARCHAR(150),
	CONSTRAINT fk_Facturi FOREIGN KEY (id_m) REFERENCES Medici(id_m) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_Pacienti FOREIGN KEY (id_p) REFERENCES Pacienti(id_p) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_Diagnostice FOREIGN KEY (id_d) REFERENCES Diagnostice(id_d) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT pk_Lista_pacienti PRIMARY KEY (id_m, id_p, id_d, data_consult)
); 

GO
CREATE OR ALTER PROCEDURE AdaugaProgramare 
	@id_p INT, @id_m INT, @id_d INT, @observatii VARCHAR(150), @data DATE, @ora TIME
AS
BEGIN
	IF NOT EXISTS (SELECT * FROM Pacienti WHERE id_p = @id_p) OR NOT EXISTS (SELECT * FROM Medici WHERE id_m = @id_m)
	OR NOT EXISTS (SELECT * FROM Diagnostice WHERE id_d = @id_d)
	BEGIN
		PRINT 'Nu exista ori pacientul, ori medicul, ori diagnosticul'
		RETURN;
	END
	IF EXISTS (SELECT * FROM Lista_pacienti WHERE id_m=@id_m AND id_p=@id_p AND data_consult=@data)
		UPDATE Lista_pacienti
		SET id_d=@id_d, observatii=@observatii, ora_consult=@ora
		WHERE id_m=@id_m AND id_p=@id_p AND data_consult=@data
	ELSE
		INSERT INTO Lista_pacienti (id_m, id_p, id_d, data_consult, ora_consult, observatii)
		VALUES (@id_m, @id_p, @id_d, @data, @ora, @observatii)
END
GO

EXEC AdaugaProgramare 1,1,1,'Sa nu manance','2023-02-17','17:00:00'

GO
CREATE OR ALTER VIEW vwMediciPopulariLunaAceasta 
AS
SELECT TOP(1000) M.nume, M.prenume, COUNT(*) AS NrPacienti
FROM Medici M
INNER JOIN Lista_pacienti LP ON LP.id_m=M.id_m
WHERE MONTH(LP.data_consult) = MONTH(GETDATE())
GROUP BY M.nume, M.prenume
HAVING COUNT(*) > 1
ORDER BY M.nume, M.prenume
GO
SELECT * FROM vwMediciPopulariLunaAceasta


GO
CREATE FUNCTION ConsultatiiSuprapuse (@data DATE, @ora TIME)
RETURNS TABLE
AS
	RETURN SELECT M.nume, M.prenume 
	FROM Medici M 
	INNER JOIN Lista_pacienti LS ON LS.id_m = M.id_m 
	GROUP BY M.nume, M.prenume 
	HAVING (SELECT COUNT(*) FROM Lista_pacienti WHERE data_consult = @data AND ora_consult = @ora) > 1
GO

SELECT * FROM ConsultatiiSuprapuse('2023-01-26','15:00:00')