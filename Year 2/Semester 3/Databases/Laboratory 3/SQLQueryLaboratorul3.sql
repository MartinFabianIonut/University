CREATE TABLE VersiuneaCurenta(
id_versiune TINYINT PRIMARY KEY IDENTITY,
versiune TINYINT NOT NULL DEFAULT 0
)

--P1------------------------------------------------------------------
GO
CREATE OR ALTER PROCEDURE P1_ModificareaTipuluiUneiColoane
AS 
BEGIN
	ALTER TABLE Carti
	ALTER COLUMN pret MONEY
	UPDATE VersiuneaCurenta
	SET versiune = 1
	WHERE id_versiune = 1;
END;
GO


--P2------------------------------------------------------------------
GO
CREATE OR ALTER PROCEDURE P2_AdaugConstrangereDefault
AS 
BEGIN
	IF (EXISTS (SELECT * FROM sys.default_constraints WHERE name='constrangerea_martiniana'))
		RAISERROR ('Constrangerea este deja setata default de Fabian!',10,1);
	ELSE
		BEGIN
			ALTER TABLE Carti
			ADD CONSTRAINT constrangerea_martiniana DEFAULT 1 FOR id_categorie
		END;
	UPDATE VersiuneaCurenta
	SET versiune = 2
	WHERE id_versiune = 1;
END;
GO


--P3------------------------------------------------------------------
GO
CREATE OR ALTER PROCEDURE P3_CreareTabelCapitole
AS 
BEGIN
	IF (NOT EXISTS (SELECT * FROM sys.tables WHERE name='Capitole'))
		BEGIN
			CREATE TABLE Capitole(
				id_capitol INT PRIMARY KEY IDENTITY,
				nume_capitol NVARCHAR(30) NOT NULL,
				pagina_inceput_capitol SMALLINT CONSTRAINT pagina_existenta CHECK (pagina_inceput_capitol > 0),
				--id_carte INT FOREIGN KEY REFERENCES Carti(id_carte) ON UPDATE CASCADE ON DELETE CASCADE
			)
			UPDATE VersiuneaCurenta
			SET versiune = 3
			WHERE id_versiune = 1;
		END;
	ELSE
		RAISERROR ('Tabelul Capitole a fost deja creat de Fabian!',10,1);
END;
GO


--P4------------------------------------------------------------------
--Procedura parametrizata care returneaza 0 daca poate adauga o coloana intr-un tabel, altfel (exista deja) returneaza 1
GO
CREATE OR ALTER PROCEDURE P4_AdaugareaUneiNoiColoaneParametrizata @nume_tabel NVARCHAR(20), @nume_coloana NVARCHAR(20), @tip_coloana NVARCHAR(12)
AS 
BEGIN
	IF (NOT EXISTS (SELECT COLUMN_NAME 
					FROM INFORMATION_SCHEMA.COLUMNS 
					WHERE TABLE_NAME = @nume_tabel AND column_name = @nume_coloana) )
		BEGIN
			DECLARE @executia_adaugarii NVARCHAR(200)
			SET @executia_adaugarii = 'ALTER TABLE ' + @nume_tabel + ' ADD ' + @nume_coloana + ' ' + @tip_coloana
			EXEC (@executia_adaugarii)
			UPDATE VersiuneaCurenta
			SET versiune = 4
			WHERE id_versiune = 1;
			RETURN 0;
		END;
	ELSE
		RETURN 1;
END;
GO


--P5------------------------------------------------------------------
GO
CREATE OR ALTER PROCEDURE P5_CreareaUneiConstrangeriForeignKey
AS 
BEGIN
	DECLARE @adaug_id_carte_in_Capitole INT
	EXEC @adaug_id_carte_in_Capitole=P4_AdaugareaUneiNoiColoaneParametrizata 'Capitole','id_carte','INT'
	IF ( (@adaug_id_carte_in_Capitole = 0) OR (NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_NAME='fk_ForeignKeyinCapitole')))
		BEGIN
			ALTER TABLE Capitole
			ADD CONSTRAINT fk_ForeignKeyinCapitole FOREIGN KEY (id_carte) REFERENCES Carti(id_carte) ON UPDATE CASCADE ON DELETE CASCADE
			UPDATE VersiuneaCurenta
			SET versiune = 5
			WHERE id_versiune = 1;
		END;
	ELSE
		RAISERROR ('Constrangerea fk_ForeignKeyinCapitole a fost deja adaugata de Fabian!',10,1);	
END;
GO


--P6------------------------------------------------------------------
GO
CREATE OR ALTER PROCEDURE P6_RefacereaTipuluiUneiColoane
AS 
BEGIN
	ALTER TABLE Carti
	ALTER COLUMN pret INT
	UPDATE VersiuneaCurenta
	SET versiune = 0
	WHERE id_versiune = 1;
END;
GO


--P7------------------------------------------------------------------
GO
CREATE OR ALTER PROCEDURE P7_StergConstrangereDefault
AS 
BEGIN
	IF (EXISTS (SELECT * FROM sys.default_constraints WHERE name='constrangerea_martiniana'))
		BEGIN
			ALTER TABLE Carti
			DROP CONSTRAINT constrangerea_martiniana
			UPDATE VersiuneaCurenta
			SET versiune = 1
			WHERE id_versiune = 1;
		END;
	ELSE
		RAISERROR ('Constrangerea constrangerea_martiniana a fost deja stearsa de Fabian!',10,1);
END;
GO


--P8------------------------------------------------------------------
GO
CREATE OR ALTER PROCEDURE P8_StergereTabelCapitole
AS 
BEGIN
	IF (EXISTS (SELECT * FROM sys.tables WHERE name='Capitole'))
		BEGIN
			DROP TABLE Capitole
			UPDATE VersiuneaCurenta
			SET versiune = 2
			WHERE id_versiune = 1;
		END;
	ELSE
		RAISERROR ('Tabelul Capitole a fost deja sters de Fabian!',10,1);
END;
GO


--P9------------------------------------------------------------------
GO
CREATE OR ALTER PROCEDURE P9_StergereaUneiColoaneParametrizata @nume_tabel NVARCHAR(20), @nume_coloana NVARCHAR(20)
AS 
BEGIN
	IF (EXISTS (SELECT COLUMN_NAME 
					FROM INFORMATION_SCHEMA.COLUMNS 
					WHERE TABLE_NAME = @nume_tabel AND column_name = @nume_coloana) )
		BEGIN
			DECLARE @executia_adaugarii NVARCHAR(200)
			SET @executia_adaugarii = 'ALTER TABLE ' + @nume_tabel + ' DROP COLUMN ' + @nume_coloana
			EXEC (@executia_adaugarii)
			UPDATE VersiuneaCurenta
			SET versiune = 3
			WHERE id_versiune = 1;
		END;
	ELSE
		RAISERROR ('Coloana id_carte a fost deja stearsa de Fabian!',10,1);
END;
GO


--P10-----------------------------------------------------------------
GO
CREATE OR ALTER PROCEDURE P10_StergereaUneiConstrangeriForeignKey
AS 
BEGIN
	IF (EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS WHERE CONSTRAINT_NAME='fk_ForeignKeyinCapitole'))
		BEGIN
			ALTER TABLE Capitole
			DROP CONSTRAINT fk_ForeignKeyinCapitole
			UPDATE VersiuneaCurenta
			SET versiune = 4
			WHERE id_versiune = 1;
		END;
	ELSE
		RAISERROR ('Constrangerea fk_ForeignKeyinCapitole a fost deja stearsa de Fabian!',10,1);	
END;
GO


--P11-----------------------------------------------------------------
GO
CREATE OR ALTER PROCEDURE P11_SetareProiectLaOVersiuneDorita @versiune INT
AS 
BEGIN
	DECLARE @versiunea_curenta TINYINT
	SET @versiunea_curenta = (SELECT versiune FROM VersiuneaCurenta WHERE id_versiune=1)
	PRINT @versiunea_curenta
	IF (@versiunea_curenta = @versiune)
		PRINT 'Nu s-a produs nicio modificarea, pentru ca proiectul se afla deja in versiunea ' + CAST(@versiune AS VARCHAR(10));
	ELSE
		IF (@versiune > @versiunea_curenta)
			WHILE (@versiunea_curenta != @versiune)
				BEGIN
					IF (@versiunea_curenta = 0)
						EXEC P1_ModificareaTipuluiUneiColoane --pune money la pret din Carti
					IF (@versiunea_curenta = 1)
						EXEC P2_AdaugConstrangereDefault --adauga constrangere_martiniana in Carti pentru id_categorie
					IF (@versiunea_curenta = 2)
						EXEC P3_CreareTabelCapitole --creaza tabelul Capitole
					IF (@versiunea_curenta = 3)
						EXEC P4_AdaugareaUneiNoiColoaneParametrizata 'Capitole','id_carte','INT' -- adauga coloana id_carte in Capitole
					IF (@versiunea_curenta = 4)
						EXEC P5_CreareaUneiConstrangeriForeignKey --creeaza constrangerea fk_ForeignKeyinCapitole in Capitole pentru id_carte
					SET @versiunea_curenta = (SELECT versiune FROM VersiuneaCurenta WHERE id_versiune=1)
				END;
		ELSE
			WHILE (@versiunea_curenta != @versiune)
				BEGIN
					IF (@versiunea_curenta = 5)
						EXEC P10_StergereaUneiConstrangeriForeignKey --sterge constrangerea fk_ForeignKeyinCapitole din Capitole pentru id_carte
					IF (@versiunea_curenta = 4)
						EXEC P9_StergereaUneiColoaneParametrizata 'Capitole','id_carte' -- sterge coloana id_carte din Capitole
					IF (@versiunea_curenta = 3)
						EXEC P8_StergereTabelCapitole --sterge tabelul Capitole
					IF (@versiunea_curenta = 2)
						EXEC P7_StergConstrangereDefault --sterge constrangere_martiniana din Carti pentru id_categorie
					IF (@versiunea_curenta = 1)
						EXEC P6_RefacereaTipuluiUneiColoane --pune int la pret din Carti
					SET @versiunea_curenta = (SELECT versiune FROM VersiuneaCurenta WHERE id_versiune=1)
				END;	
	PRINT 'Proiectul se afla acum in versiunea ' + CAST(@versiunea_curenta AS VARCHAR(10));
END;
GO

EXEC P11_SetareProiectLaOVersiuneDorita 4