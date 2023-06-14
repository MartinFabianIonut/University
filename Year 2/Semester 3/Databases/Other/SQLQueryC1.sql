USE MASTER
IF EXISTS (SELECT NAME FROM sys.databases WHERE NAME = 'Vanzari')
	DROP DATABASE Vanzari

GO
CREATE DATABASE Vanzari
GO
USE Vanzari
GO

CREATE TABLE Clienti (
	id_c INT PRIMARY KEY IDENTITY,
	denumire VARCHAR(20),
	cod_fiscal VARCHAR(10)
);

CREATE TABLE Produse (
	id_p INT PRIMARY KEY IDENTITY,
	denumire VARCHAR(20),
	unitate_masura VARCHAR(10)
);

CREATE TABLE Agenti_de_vanzare (
	id_a INT PRIMARY KEY IDENTITY,
	nume VARCHAR(20),
	prenume VARCHAR(20)
);

CREATE TABLE Facturi (
	id_f INT PRIMARY KEY IDENTITY,
	numar INT,
	data_emiterii TIME,
	id_c INT FOREIGN KEY REFERENCES Clienti(id_c) ON DELETE NO ACTION ON UPDATE NO ACTION,
	id_a INT FOREIGN KEY REFERENCES Agenti_de_vanzare(id_a) ON DELETE CASCADE ON UPDATE CASCADE
);

ALTER TABLE Facturi
ALTER COLUMN data_emiterii SMALLDATETIME

CREATE TABLE Lista_produse (
	id_f INT,
	id_p INT,
	numar_ordine INT,
	pret MONEY,
	cantitate INT,
	CONSTRAINT fk_Facturi FOREIGN KEY (id_f) REFERENCES Facturi(id_f) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_Produse FOREIGN KEY (id_p) REFERENCES Produse(id_p) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT pf_Lista_produse PRIMARY KEY (id_f, id_p)
); 

GO
CREATE OR ALTER PROCEDURE AdaugaProdusLaFactura 
	@id_f INT, @id_p INT, @numar_ordine INT, @pret MONEY, @cantitate INT
AS
BEGIN
	IF NOT EXISTS (SELECT * FROM Facturi WHERE id_f = @id_f)
	BEGIN
		PRINT 'Nu exista factura'
		RETURN;
	END
	IF NOT EXISTS (SELECT * FROM Produse WHERE id_p = @id_p)
	BEGIN
		PRINT 'Nu exista produsul'
		RETURN;
	END
	IF EXISTS (SELECT * FROM Lista_produse WHERE id_f=@id_f AND id_p=@id_p AND numar_ordine=@numar_ordine)
		INSERT INTO Lista_produse (id_f, id_p, numar_ordine, pret, cantitate)
		VALUES (@id_f,@id_p,@numar_ordine,@pret,-@cantitate)
	ELSE
		INSERT INTO Lista_produse (id_f, id_p, numar_ordine, pret, cantitate)
		VALUES (@id_f,@id_p,@numar_ordine,@pret,@cantitate)
END
GO

EXEC AdaugaProdusLaFactura 1,1,424,56.4,25

GO
CREATE OR ALTER VIEW vwFacturiCuShaorma 
AS
SELECT DenumireClient, NumarFactura, DataEmiterii, SUM(L.pret*L.cantitate) AS ValoareTotala
FROM (
	SELECT F.id_f, C.denumire AS DenumireClient, F.numar AS NumarFactura, F.data_emiterii AS DataEmiterii
	FROM Lista_produse L 
	INNER JOIN Produse P ON L.id_p=P.id_p
	INNER JOIN Facturi F ON L.id_f=F.id_f 
	INNER JOIN Clienti C ON F.id_c=C.id_c
	WHERE P.denumire IN ('Shaorma')
)AS CEVA 

	INNER JOIN Lista_produse L ON L.id_f = CEVA.id_f
	GROUP BY DenumireClient, NumarFactura, DataEmiterii
	HAVING SUM(L.pret*L.cantitate) > 300

GO
SELECT * FROM vwFacturiCuShaorma
--e necesar select peste select, pt ca altfel primesc doar pretul pentru shaorma

GO
CREATE OR ALTER VIEW vwFacturiCuShaormaSimplu 
AS
SELECT C.denumire AS DenumireClient, F.numar AS NumarFactura, F.data_emiterii AS DataEmiterii,SUM(L.pret*L.cantitate) AS ValoareTotala
FROM Lista_produse L 
INNER JOIN Produse P ON L.id_p=P.id_p
INNER JOIN Facturi F ON L.id_f=F.id_f 
INNER JOIN Clienti C ON F.id_c=C.id_c
WHERE P.denumire IN ('Shaorma')
GROUP BY C.denumire,  F.numar, F.data_emiterii
HAVING SUM(L.pret*L.cantitate)>300
GO
DROP VIEW vwFacturiCuShaormaSimplu
SELECT * FROM vwFacturiCuShaormaSimplu

GO
CREATE FUNCTION ValoareTotala ()
RETURNS @Tabelul TABLE (Luna INT, NumeAgent VARCHAR(20), PrenumeAgent VARCHAR(20), ValoareTotala MONEY)
AS 
BEGIN
	INSERT INTO @Tabelul

	SELECT MONTH(F.data_emiterii) AS Luna, A.nume, A.prenume, SUM(L.pret*L.cantitate) AS ValoareTotala
	FROM Lista_produse L 
	INNER JOIN Facturi F ON L.id_f=F.id_f 
	INNER JOIN Agenti_de_vanzare A ON F.id_a=A.id_a
	GROUP BY YEAR(F.data_emiterii),MONTH(F.data_emiterii),  A.nume, A.prenume
	ORDER BY MONTH(F.data_emiterii), A.nume

	RETURN;
END;
GO

SELECT * FROM ValoareTotala()
