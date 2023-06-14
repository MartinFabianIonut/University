GO
CREATE OR ALTER VIEW viewCarti 
AS
SELECT titlu FROM Carti;
GO

SELECT * FROM viewCarti

GO
CREATE OR ALTER VIEW viewRanduri_cos
AS
SELECT C.titlu, R.cantitate, [pret/bucata]=R.pret,[pret platit]=R.cantitate*R.pret
FROM Randuri_cos R 
INNER JOIN Carti C ON R.id_carte = C.id_carte
INNER JOIN Cos_de_cumparaturi CC ON R.id_cos = CC.id_cos;
GO
SELECT * FROM viewRanduri_cos;

GO
CREATE OR ALTER VIEW viewCateAchizitii 
AS 
SELECT Numele = U.nume+' '+U.prenume, COUNT(*) AS CateAchizitii
FROM Utilizatori U
INNER JOIN Cos_de_cumparaturi CC ON U.id_utilizator = CC.id_utilizator
GROUP BY U.nume+' '+U.prenume;
GO
SELECT * FROM viewCateAchizitii;

GO
CREATE OR ALTER VIEW viewCateCartiCumparate 
AS
SELECT Numele = U.nume+' '+U.prenume, SUM(R.cantitate) AS CateCartiCumparate 
FROM Utilizatori U
INNER JOIN Cos_de_cumparaturi CC ON U.id_utilizator = CC.id_utilizator
INNER JOIN Randuri_cos R ON R.id_cos = CC.id_cos
GROUP BY U.nume+' '+U.prenume;
GO
SELECT * FROM viewCateCartiCumparate;

GO
CREATE OR ALTER VIEW viewCartiCumparateDeX
AS
SELECT Numele = U.nume+' '+U.prenume, C.titlu
FROM Utilizatori U
INNER JOIN Cos_de_cumparaturi CC ON U.id_utilizator = CC.id_utilizator
INNER JOIN Randuri_cos R ON R.id_cos = CC.id_cos
INNER JOIN Carti C ON R.id_carte = C.id_carte
GROUP BY U.nume+' '+U.prenume, C.titlu;
GO
SELECT * FROM viewCartiCumparateDeX;

GO
CREATE OR ALTER VIEW viewCartiCuEdituri
AS
SELECT Numele = U.nume+' '+U.prenume, E.editura,C.titlu
FROM Utilizatori U
INNER JOIN Cos_de_cumparaturi CC ON U.id_utilizator = CC.id_utilizator
INNER JOIN Randuri_cos R ON R.id_cos = CC.id_cos
INNER JOIN Carti C ON R.id_carte = C.id_carte
INNER JOIN Edituri E On E.id_editura = C.id_editura
GROUP BY U.nume+' '+U.prenume, E.editura,C.titlu;
GO
SELECT * FROM viewCartiCuEdituri;


--Crearea procedurilor stocate necesare
GO
CREATE OR ALTER PROCEDURE InsertDataToTable
@tableName NVARCHAR(50), @itemsNumber INT
AS
BEGIN
	DECLARE @index INT, @productionID INT, @producerID INT, @userID INT
	DECLARE @profileID INT, @ratingValue INT, @maxValue INT
	SET @index = 0;

	SET NOCOUNT ON;
	IF @tableName = 'Utilizatori'
	BEGIN
		WHILE @index < @itemsNumber
		BEGIN
			INSERT INTO Utilizatori (nume,prenume,email,adresa,telefon) 
			VALUES ('Nume'+cast(@index as varchar), 'Prenume'+cast(@index as varchar),'prenume.nume@gmail.com'+cast(@index as varchar),
			'Str Aia, Nr'+cast(@index as varchar)+'Cod 00'+cast(@index as varchar), 
			'0745'+cast(@index%1000 as varchar)+cast(@index%6 as varchar)+cast(@index%3 as varchar))
			SET @index = @index + 1
		END
	END
	ELSE IF @tableName = 'Cos_de_cumparaturi'
	BEGIN
		SET @userId = (SELECT TOP (1) id_utilizator FROM Utilizatori)
		WHILE @index < @itemsNumber
		BEGIN
			INSERT INTO Cos_de_cumparaturi (id_utilizator, serie,numar,emitere) 
			VALUES (@userID, 'CJ', 
			'nr '+cast(@index as varchar),
			GETDATE())
			SET @index = @index + 1
			SET @userID = @userID + 1
			IF @userID%540 = 0
				SET @userID = (SELECT TOP (1) id_utilizator FROM Utilizatori)
			else if @userID%102 = 0
				set @userID = @userID + 3
		END
	END
	ELSE IF @tableName = 'Randuri_cos'
	BEGIN
		DECLARE @id_cos INT, @id_carte INT
		SET @id_cos = (SELECT TOP (1) id_cos FROM Cos_de_cumparaturi)
		
		SET @id_carte = (SELECT TOP (1) id_carte FROM Carti)
		WHILE @index < @itemsNumber
		BEGIN
			
			INSERT INTO Randuri_cos (id_cos, id_carte, cantitate, pret) VALUES (@id_cos, @id_carte, @id_cos%5+1, 5+@id_carte%33)
			SET @id_cos = @id_cos + 1
			SET @id_carte = @id_carte + 1
			SET @index = @index + 1
			
		END
	END
	ELSE IF @tableName = 'Carti'
	BEGIN
		DECLARE @id_editura INT
		SET @id_editura = (SELECT TOP(1) id_editura FROM Edituri ORDER BY id_editura ASC)
		WHILE @index < @itemsNumber
		BEGIN
			INSERT INTO Carti (titlu, numar_de_pagini, tip_format,stoc, id_editura) 
			VALUES ('Titlu '+cast(@index as varchar), 145+@index%675, '200 x 130 mm', @index%333, @id_editura)
			SET @index = @index + 1
			SET @id_editura = @id_editura+1
		END
	END
	ELSE IF @tableName = 'Edituri'
	BEGIN
		WHILE @index < @itemsNumber
		BEGIN
			IF @index%5=0
				INSERT INTO Edituri (editura,sediu,anul_infiintarii) VALUES ('Humanitas'+cast(@index as varchar),'Strada Nr'+cast(@index as varchar), 1970+@index%40)
			IF @index%5=1
				INSERT INTO Edituri (editura,sediu,anul_infiintarii) VALUES ('Litera'+cast(@index as varchar),'Strada Nr'+cast(@index as varchar), 1970+@index%40)
			IF @index%5=2
				INSERT INTO Edituri (editura,sediu,anul_infiintarii) VALUES ('Polirom'+cast(@index as varchar),'Strada Nr'+cast(@index as varchar), 1970+@index%40)
			IF @index%5=3
				INSERT INTO Edituri (editura,sediu,anul_infiintarii) VALUES ('Art'+cast(@index as varchar),'Strada Nr'+cast(@index as varchar), 1970+@index%40)
			IF @index%5=4
				INSERT INTO Edituri (editura,sediu,anul_infiintarii) VALUES ('RAO'+cast(@index as varchar),'Strada Nr'+cast(@index as varchar), 1970+@index%40)
			SET @index = @index + 1
		END
	END
END


GO
CREATE OR ALTER PROCEDURE TestDatabase
AS
BEGIN
	SET NOCOUNT ON
	DECLARE @IDTest INT, @NoOfRows INT, @IDTable INT, @testRunId INT
	DECLARE @TableName NVARCHAR(50), @TestName NVARCHAR(50)
	DECLARE @testStartTime DATETIME, @testFinishTime DATETIME
	DECLARE @auxStartTime DATETIME, @auxFinishTime DATETIME

	DECLARE CursorTest CURSOR FORWARD_ONLY 
	FOR SELECT * FROM Tests
	OPEN CursorTest
	FETCH NEXT FROM CursorTest INTO @IDTest, @TestName

	DELETE FROM TestRuns;
	DELETE FROM TestRunTables;
	DELETE FROM TestRunViews;
	DBCC CHECKIDENT (TestRuns, RESEED, 0);
	WHILE @@FETCH_STATUS = 0 BEGIN
		PRINT 'TEST NR -> ' + cast(@IDTest AS VARCHAR)

		DECLARE CursorDelete CURSOR SCROLL
		FOR SELECT T.Name, TT.NoOfRows, TT.TableID
			FROM Tables T INNER JOIN TestTables TT ON T.TableID = TT.TableID
			WHERE TestID = @IDTest
			ORDER BY Position
		OPEN CursorDelete

		FETCH NEXT FROM CursorDelete INTO @TableName, @NoOfRows, @IDTable
		WHILE @@FETCH_STATUS = 0 BEGIN
			EXEC ('DELETE FROM ' + @TableName)
			FETCH NEXT FROM CursorDelete INTO @TableName, @NoOfRows, @IDTable
		END

		SET @testStartTime = SYSDATETIME()
		INSERT INTO TestRuns (Description, StartAt) VALUES (@TestName, @testStartTime)
		SET @testRunId = @@IDENTITY;


		FETCH PRIOR FROM CursorDelete INTO @TableName, @NoOfRows, @IDTable
		WHILE @@FETCH_STATUS = 0 BEGIN
			SET @auxStartTime = SYSDATETIME()
			EXEC InsertDataToTable @tableName = @TableName, @itemsNumber=@NoOfRows
			SET @auxFinishTime = SYSDATETIME()

			INSERT INTO TestRunTables (TestRunID, TableID, StartAt, EndAt)
			VALUES (@testRunId, @IDTable, @auxStartTime, @auxFinishTime)

			FETCH PRIOR FROM CursorDelete INTO @TableName, @NoOfRows, @IDTable
		END
		CLOSE CursorDelete
		DEALLOCATE CursorDelete

		DECLARE @IDView INT, @ViewName NVARCHAR(50);
		DECLARE CursorView CURSOR FORWARD_ONLY
		FOR SELECT TV.ViewID, V.Name
			FROM TestViews TV INNER JOIN Views V ON TV.ViewID = V.ViewID
			WHERE TV.TestID = @IDTest;

		OPEN CursorView
		FETCH NEXT FROM CursorView INTO @IDView, @ViewName;

		WHILE @@FETCH_STATUS = 0 BEGIN
			SET @auxStartTime = SYSDATETIME()
			EXEC ('SELECT * FROM ' + @ViewName)
			SET @auxFinishTime = SYSDATETIME()

			INSERT INTO TestRunViews(TestRunID, ViewID, StartAt, EndAt)
			VALUES (@testRunId, @IDView, @auxStartTime, @auxFinishTime)

			FETCH NEXT FROM CursorView INTO @IDView, @ViewName;
		END

		CLOSE CursorView
		DEALLOCATE CursorView

		SET @testFinishTime = SYSDATETIME()
		UPDATE TestRuns SET EndAt=@testFinishTime where TestRunID=@TestRunId;

		FETCH NEXT FROM CursorTest INTO @IDTest, @TestName
	END

	CLOSE CursorTest
	DEALLOCATE CursorTest

END
GO

EXEC TestDatabase;

SELECT * FROM TestRuns
SELECT * FROM TestRunTables
SELECT * FROM TestRunViews