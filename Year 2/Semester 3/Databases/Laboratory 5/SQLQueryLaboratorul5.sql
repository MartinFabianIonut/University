
--FUNCTII DE VALIDARE A DATELOR DIN TABELUL      PREMII

GO
CREATE OR ALTER FUNCTION validarePremiu (@premiu NVARCHAR(50))
RETURNS BIT
AS
BEGIN 
	IF @premiu IS NULL --sir vid
		RETURN 1;
	IF LTRIM(@premiu) = '' --contine doar spatii
		RETURN 0;
	IF LEN(@premiu) < 3 --lungime mai mica decat 3
		RETURN 0;

	DECLARE @lungime INT
	SET @lungime = LEN(@premiu);

	DECLARE @contor INT
	SET @contor = 1;
	
	WHILE @contor <= @lungime --parcurg tot sirul
	BEGIN
		IF NOT SUBSTRING(@premiu, @contor, 1) LIKE '%[A-Za-z 0-9]' --nu vreau sa am altceva in afara de litere, cifre si spatiu
			RETURN 0;
		SET @contor = @contor + 1;
	END
	RETURN 1;
END
GO


CREATE OR ALTER FUNCTION validareValoare (@valoare INT)
RETURNS BIT
AS
BEGIN 
	IF @valoare < 0 OR @valoare > 4487667
		RETURN 0;
	RETURN 1;
END
GO

--FUNCTII DE VALIDARE A DATELOR DIN TABELUL      SCRIITORI (sunt cele de la utilizator: )

--FUNCTII DE VALIDARE A DATELOR DIN TABELUL      LAUREATI



CREATE OR ALTER FUNCTION validareIdScriitorPtLaureati (@idScriitor INT)
RETURNS BIT
AS
BEGIN 
	IF NOT EXISTS (SELECT * FROM Scriitori WHERE id_scriitor=@idScriitor)
		RETURN 0;
	RETURN 1;
END
GO

CREATE OR ALTER FUNCTION validareIdPremiuPtLaureati (@idPremiu INT)
RETURNS BIT
AS
BEGIN 
	IF NOT EXISTS (SELECT * FROM Premii WHERE id_premiu=@idPremiu)
		RETURN 0;
	RETURN 1;
END
GO



CREATE OR ALTER FUNCTION validareMotiv (@motiv NVARCHAR(250))
RETURNS BIT
AS
BEGIN 
	IF @motiv IS NULL --sir vid
		RETURN 1;
	IF LTRIM(@motiv) = '' --contine doar spatii
		RETURN 0;
	IF LEN(@motiv) < 10 --lungime mai mica decat 10
		RETURN 0;
	
	DECLARE @lungime INT
	SET @lungime = LEN(@motiv);

	DECLARE @contor INT
	SET @contor = 1;
	
	WHILE @contor <= @lungime --parcurg tot sirul
	BEGIN
		IF NOT SUBSTRING(@motiv, @contor, 1) LIKE '%[a-zA-Z0-9 .,!?;\[\]\(\)\{\}\-]' ESCAPE '\'  --nu vreau sa am altceva in afara de cele trecute
			RETURN 0;
		SET @contor = @contor + 1;
	END
	RETURN 1;
END
GO

CREATE OR ALTER FUNCTION validareData (@data INT)
RETURNS BIT
AS
BEGIN 
	IF @data >= 1901
		RETURN 1
	RETURN 0
END
GO





--FUNCTII DE VALIDARE A DATELOR DIN TABELUL      UTILIZATOR

GO
CREATE OR ALTER FUNCTION validareNume (@nume NVARCHAR(50))
RETURNS BIT
AS
BEGIN 
	IF @nume IS NULL --sir vid
		RETURN 1;
	IF LTRIM(@nume) = '' --contine doar spatii
		RETURN 0;
	IF LEN(@nume) < 3 --lungime mai mica decat 3
		RETURN 0;
	IF NOT LEFT(@nume, 1) = UPPER(LEFT(@nume, 1)) Collate SQL_Latin1_General_CP1_CS_AS -- trebuie sa inceapa cu litera mare
		RETURN 0;
	RETURN 1;
END
GO

CREATE OR ALTER FUNCTION validarePrenume (@prenume NVARCHAR(50))
RETURNS BIT
AS
BEGIN 
	IF @prenume IS NULL --sir vid
		RETURN 1;
	IF LTRIM(@prenume) = '' --contine doar spatii
		RETURN 0;
	IF LEN(@prenume) < 3 --lungime mai mica decat 3
		RETURN 0;
	IF NOT LEFT(@prenume, 1) = UPPER(LEFT(@prenume, 1)) Collate SQL_Latin1_General_CP1_CS_AS -- trebuie sa inceapa cu litera mare
		RETURN 0;
	RETURN 1;
END
GO


CREATE OR ALTER FUNCTION validareAdresa (@adresa NVARCHAR(150))
RETURNS BIT
AS
BEGIN 
	IF @adresa IS NULL --sir vid
		RETURN 1;
	IF LTRIM(@adresa) = '' --contine doar spatii
		RETURN 0;
	IF LEN(@adresa) < 7 --lungime mai mica decat 7
		RETURN 0;
	IF CHARINDEX('Str',@adresa,0) <= 0 --trebuie sa existe Str
		RETURN 0;
	RETURN 1;
END
GO

CREATE OR ALTER FUNCTION validareEmail (@email NVARCHAR(50))
RETURNS BIT
AS
BEGIN 
	IF @email IS NULL --sir vid
		RETURN 1;
	IF LTRIM(@email) = '' --contine doar spatii
		RETURN 0;
	IF LEN(@email) < 3 --lungime mai mica decat 3
		RETURN 0;
	IF CHARINDEX('@',@email,0) <= 0 OR CHARINDEX('.',@email,0) <=0 --trebuie sa existe @ si .
		RETURN 0;

	DECLARE @lungime INT
	SET @lungime = LEN(@email);

	DECLARE @contor INT
	SET @contor = 1;
	
	WHILE @contor <= @lungime --parcurg tot sirul
	BEGIN
		IF NOT SUBSTRING(@email, @contor, 1) LIKE '%[a-zA-Z_0-9@.]'  --nu vreau sa am altceva in afara de litere, cifre, '_','@','.'
			RETURN 0;
		SET @contor = @contor + 1;
	END
	RETURN 1;
END
GO


CREATE OR ALTER FUNCTION validareTelefon (@telefon NVARCHAR(15))
RETURNS BIT
AS
BEGIN 
	IF @telefon IS NULL --sir vid
		RETURN 1;
	IF LTRIM(@telefon) = '' --contine doar spatii
		RETURN 0;
	IF LEN(@telefon) < 10 OR LEN(@telefon) > 15 --lungimea mai mica decat 10 ori mai mare decat 15 nu e buna
		RETURN 0;

	DECLARE @lungime INT
	SET @lungime = LEN(@telefon);

	DECLARE @contor INT
	SET @contor = 1;
	
	WHILE @contor <= @lungime --parcurg tot sirul
	BEGIN
		IF NOT SUBSTRING(@telefon, @contor, 1) LIKE '%[0-9]'  --nu vreau sa am altceva in afara de cifre
			RETURN 0;
		SET @contor = @contor + 1;
	END
	RETURN 1;
END
GO







GO
CREATE OR ALTER PROCEDURE CRUDOnUtilizatori 
	@flag BIT OUTPUT,
	@actiune VARCHAR(20), 
	@nume NVARCHAR(50), 
	@prenume NVARCHAR(50), 
	@adresa NVARCHAR(150),  
	@email NVARCHAR(50),
	@telefon NVARCHAR(15),
	@id_utilizator INT
AS 
BEGIN
	SET NOCOUNT ON;
	SET @flag = 1;

	IF dbo.validareNume(@nume) = 0 
		OR dbo.validarePrenume(@prenume) = 0 
		OR dbo.validareAdresa(@adresa) = 0 
		OR dbo.validareEmail(@email) = 0 
		OR dbo.validareTelefon(@telefon) = 0
		SET @flag = 0;

	IF @flag = 1
	BEGIN
		IF @actiune = 'insert'
		BEGIN
			IF EXISTS (SELECT * FROM Utilizatori WHERE telefon=@telefon)
			BEGIN
				PRINT 'Nu se poate face insert, caci se repeta telefonul'
				SET @flag = 0;
				RETURN 
			END
			INSERT INTO Utilizatori (nume,prenume,adresa,email,telefon) VALUES (@nume,@prenume,@adresa,@email,@telefon)
			PRINT 'Inserare in tabelul Utilizatori pe: '+@nume+' '+@prenume
		END
		ELSE IF @actiune = 'delete'
		BEGIN
			IF NOT EXISTS (SELECT * FROM Utilizatori WHERE id_utilizator = @id_utilizator) OR @id_utilizator IS NULL
			BEGIN
				PRINT 'Nu se poate sterge din tabelul Utilizatori pentru id = '+CAST(@id_utilizator AS VARCHAR)
				SET @flag = 0;
				RETURN
			END
			DELETE FROM Utilizatori WHERE id_utilizator = @id_utilizator
			PRINT 'Stergere din tabelul Utilizatori pe: '+@nume+' '+@prenume
		END
		ELSE IF @actiune = 'update'
		BEGIN
			IF NOT EXISTS (SELECT * FROM Utilizatori WHERE id_utilizator = @id_utilizator) OR @id_utilizator IS NULL
			BEGIN
				PRINT 'Nu se poate face update in tabelul Utilizatori pentru id = '+CAST(@id_utilizator AS VARCHAR)
				SET @flag = 0;
				RETURN
			END
			UPDATE Utilizatori SET nume=@nume,prenume=@prenume,adresa=@adresa,email=@email,telefon=@telefon WHERE id_utilizator=@id_utilizator
			PRINT 'Update in tabelul Utilizatori pentru id = '+CAST(@id_utilizator AS VARCHAR)
		END
		ELSE IF @actiune = 'select'
		BEGIN 
			SELECT * FROM Utilizatori WHERE id_utilizator=@id_utilizator
			PRINT 'Select facut pe tabelul Utilizatori'
		END
		ELSE
			PRINT 'Actiune inexistenta'
	END
	ELSE 
		PRINT 'Eroare la validare'
END;
GO



GO
CREATE OR ALTER PROCEDURE CRUDOnPremii 
	@actiune VARCHAR(20), 
	@premiu NVARCHAR(50), 
	@valoare INT,
	@id_premiu INT
AS 
BEGIN
	SET NOCOUNT ON;
	DECLARE @ok BIT = 1;

	IF dbo.validarePremiu(@premiu) = 0 OR dbo.validareValoare(@valoare) = 0
		SET @ok = 0;

	IF @ok = 1
	BEGIN
		IF @actiune = 'insert'
		BEGIN
			INSERT INTO Premii (premiu,valoare) VALUES (@premiu,@valoare)
			PRINT 'Inserare in tabelul Premii pe: '+@premiu
		END
		ELSE IF @actiune = 'delete'
		BEGIN
			IF NOT EXISTS (SELECT * FROM Premii WHERE id_premiu = @id_premiu) OR @id_premiu IS NULL
			BEGIN
				PRINT 'Nu se poate sterge din tabelul Premii pentru id = '+CAST(@id_premiu AS VARCHAR)
				RETURN
			END
			DELETE FROM Premii WHERE id_premiu = @id_premiu
			PRINT 'Stergere din tabelul Premii pe: '+@premiu
		END
		ELSE IF @actiune = 'update'
		BEGIN
			IF NOT EXISTS (SELECT * FROM Premii WHERE id_premiu = @id_premiu) OR @id_premiu IS NULL
			BEGIN
				PRINT 'Nu se poate face update in tabelul Premii pentru id = '+CAST(@id_premiu AS VARCHAR)
				RETURN
			END
			UPDATE Premii SET premiu = @premiu, valoare = @valoare WHERE id_premiu=@id_premiu
			PRINT 'Update in tabelul Premii pentru id = '+CAST(@id_premiu AS VARCHAR)
		END
		ELSE IF @actiune = 'select'
		BEGIN 
			SELECT * FROM Premii WHERE  id_premiu=@id_premiu
			PRINT 'Select facut pe tabelul Premii'
		END
		ELSE
			PRINT 'Actiune inexistenta'
	END
	ELSE 
		PRINT 'Eroare la validare'
END;
GO

GO
CREATE OR ALTER PROCEDURE CRUDOnScriitori 
	@actiune VARCHAR(20), 
	@nume NVARCHAR(50), 
	@prenume NVARCHAR(50), 
	@id_scriitor INT
AS 
BEGIN
	SET NOCOUNT ON;
	DECLARE @ok BIT = 1;

	IF dbo.validareNume(@nume) = 0 OR dbo.validarePrenume(@prenume)= 0
		SET @ok = 0;

	IF @ok = 1
	BEGIN
		IF @actiune = 'insert'
		BEGIN
			INSERT INTO Scriitori (nume, prenume) VALUES (@nume, @prenume)
			PRINT 'Inserare in tabelul Scriitori pe: '+@nume+' ' +@prenume
		END
		ELSE IF @actiune = 'delete'
		BEGIN
			IF NOT EXISTS (SELECT * FROM Scriitori WHERE id_scriitor = @id_scriitor) OR @id_scriitor IS NULL
			BEGIN
				PRINT 'Nu se poate sterge din tabelul Scriitori pentru id = '+CAST(@id_scriitor AS VARCHAR)
				RETURN
			END
			DELETE FROM Scriitori WHERE id_scriitor = @id_scriitor
			PRINT 'Stergere din tabelul Scriitori pe: '+@nume+' ' +@prenume
		END
		ELSE IF @actiune = 'update'
		BEGIN
			IF NOT EXISTS (SELECT * FROM Scriitori WHERE id_scriitor = @id_scriitor) OR @id_scriitor IS NULL
			BEGIN
				PRINT 'Nu se poate face update in tabelul Scriitori pentru id = '+CAST(@id_scriitor AS VARCHAR)
				RETURN
			END
			UPDATE Scriitori SET nume = @nume, prenume = @prenume WHERE id_scriitor=@id_scriitor
			PRINT 'Update in tabelul Scriitori pentru id = '+CAST(@id_scriitor AS VARCHAR)
		END
		ELSE IF @actiune = 'select'
		BEGIN 
			SELECT * FROM Scriitori WHERE  id_scriitor=@id_scriitor
			PRINT 'Select facut pe tabelul Scriitori'
		END
		ELSE
			PRINT 'Actiune inexistenta'
	END
	ELSE 
		PRINT 'Eroare la validare'
END;
GO

GO
CREATE OR ALTER PROCEDURE CRUDOnLaureati
	@actiune VARCHAR(20), 
	@idScriitor INT,
	@idPremiu INT,
	@motiv NVARCHAR(250),
	@data INT
AS 
BEGIN
	SET NOCOUNT ON;
	DECLARE @ok BIT = 1;

	IF dbo.validareIdScriitorPtLaureati(@idScriitor) = 0 OR dbo.validareIdPremiuPtLaureati(@idPremiu) = 0
		OR dbo.validareMotiv(@motiv) = 0 OR dbo.validareData(@data) = 0
		SET @ok = 0;

	IF @ok = 1
	BEGIN
		IF @actiune = 'insert'
		BEGIN
			IF EXISTS (SELECT * FROM Laureati WHERE id_premiu = @idPremiu AND id_scriitor = @idScriitor)
			BEGIN
				PRINT 'Nu se poate face insert in tabelul Laureati pentru ca deja exista cheia'
				RETURN
			END
			INSERT INTO Laureati (id_scriitor, id_premiu, motivul_juriului, data_decernarii) 
			VALUES (@idScriitor, @idPremiu, @motiv, @data)
			PRINT 'Inserare in tabelul Laureati'
		END
		ELSE IF @actiune = 'delete'
		BEGIN
			DELETE FROM Laureati WHERE id_premiu = @idPremiu AND id_scriitor = @idScriitor
			PRINT 'Stergere din tabelul Laureati'
		END
		ELSE IF @actiune = 'update'
		BEGIN
			IF NOT EXISTS (SELECT * FROM Laureati WHERE id_premiu = @idPremiu AND id_scriitor = @idScriitor) 
			BEGIN
				PRINT 'Nu se poate face update in tabelul Laureati pentru idScriitor = '+
				CAST(@idScriitor AS VARCHAR)+' si idPremiu = '+CAST(@idPremiu AS VARCHAR)
				RETURN
			END
			UPDATE Laureati SET motivul_juriului=@motiv, data_decernarii=@data 
			WHERE id_premiu = @idPremiu AND id_scriitor = @idScriitor
			PRINT 'Update in tabelul Laureati'
		END
		ELSE IF @actiune = 'select'
		BEGIN 
			SELECT * FROM Laureati WHERE  id_premiu = @idPremiu AND id_scriitor = @idScriitor
			PRINT 'Select facut pe tabelul Laureati'
		END
		ELSE
			PRINT 'Actiune inexistenta'
	END
	ELSE 
		PRINT 'Eroare la validare'
END;
GO

GO
CREATE OR ALTER PROCEDURE mainCRUD 
AS
BEGIN
	SET NOCOUNT ON;
	DELETE FROM Utilizatori  
	DELETE FROM Laureati  
	DELETE FROM Premii  
	DELETE FROM Scriitori 
	
	 
	DBCC CHECKIDENT (Utilizatori, RESEED, 0);
	DBCC CHECKIDENT (Premii, RESEED, 0);
	DBCC CHECKIDENT (Scriitori, RESEED, 0);
	
	PRINT 'OPERATII CRUD PE TABELUL UTILIZATORI'

	DECLARE @flag BIT
	EXEC CRUDOnUtilizatori @flag OUTPUT, 'insert','Mateescu','David','Strada Sperantei','mateescudavid@gmail.com','0743782912',1
	IF @flag = 0 PRINT '         Nu s-a intamplat nimic'
	EXEC CRUDOnUtilizatori @flag OUTPUT, 'insert','Sotropa','David','Strada Violet','davdsotro@gmail.com','0743787913',2
	IF @flag = 0 PRINT '         Nu s-a intamplat nimic'
	EXEC CRUDOnUtilizatori @flag OUTPUT, 'insert','Baciu','Rares','Strada Iernii','rares223baciu@gmail.com','0743786610',3
	IF @flag = 0 PRINT '         Nu s-a intamplat nimic'
	EXEC CRUDOnUtilizatori @flag OUTPUT, 'insert','Manciulescu','Dana','Strada Viorelelor','danaaa3@gmail.com','0743777711',4
	IF @flag = 0 PRINT '         Nu s-a intamplat nimic'
	EXEC CRUDOnUtilizatori @flag OUTPUT, 'insert','Manciulescu','Dana','Strada Viorelelor','danaaa3@gmailcom','0743757711',4 --nu e bun mailul
	IF @flag = 0 PRINT '         Nu s-a intamplat nimic'
	EXEC CRUDOnUtilizatori @flag OUTPUT, 'insert','Manciulescu','Dana','Strada Viorelelor','danaaa3@gmail.com','0743777711',4 -- se repeta telefonul
	IF @flag = 0 PRINT '         Nu s-a intamplat nimic'
	EXEC CRUDOnUtilizatori @flag OUTPUT, 'select','Manciulescu','Dana','Strada Viorelelor','danaaa3@gmail.com','0743777711',1
	IF @flag = 0 PRINT '         Nu s-a intamplat nimic'
	EXEC CRUDOnUtilizatori @flag OUTPUT, 'select','Manciulescu','Dana','Strada Viorelelor','danaaa3@gmail.com','0743777711',5
	IF @flag = 0 PRINT '         Nu s-a intamplat nimic'
	EXEC CRUDOnUtilizatori @flag OUTPUT, 'delete','Manciulescu','Dana','Strada Viorelelor','danaaa3@gmail.com','0743777711',2
	IF @flag = 0 PRINT '         Nu s-a intamplat nimic'
	EXEC CRUDOnUtilizatori @flag OUTPUT, 'delete','Manciulescu','Dana','Strada Viorelelor','danaaa3@gmail.com','0743777711',2
	IF @flag = 0 PRINT '         Nu s-a intamplat nimic'
	EXEC CRUDOnUtilizatori @flag OUTPUT, 'update','Manciulescu','Daniela','Strada Viorelelor Nr 42','danaaa3@gmail.com','0743777711',4
	IF @flag = 0 PRINT '         Nu s-a intamplat nimic'
	EXEC CRUDOnUtilizatori @flag OUTPUT, 'update','Baciu','Rares','Strada Plopilor 7c','rares223baciu@gmail.com','0743786610',3
	IF @flag = 0 PRINT '         Nu s-a intamplat nimic'
	EXEC CRUDOnUtilizatori @flag OUTPUT, 'update','Manciulescu','Daniela','Strada Viorelelor Nr 42','danaaa3@gmail.com','0743777711',5
	IF @flag = 0 PRINT '         Nu s-a intamplat nimic'

	PRINT ''
	PRINT 'OPERATII CRUD PE TABELUL PREMII'

	EXEC CRUDOnPremii 'insert','rNobel??',4487667,1 --nume rau
	EXEC CRUDOnPremii 'insert','Nobel',4487667,1
	EXEC CRUDOnPremii 'insert','Pulitzer',74724,2
	EXEC CRUDOnPremii 'insert','Observator Cultural',0,3
	EXEC CRUDOnPremii 'insert','Familia',10,4
	EXEC CRUDOnPremii 'insert','Timpul',1024,5
	EXEC CRUDOnPremii 'select','Nobel',4487667,1
	EXEC CRUDOnPremii 'select','Pulitzer',74724,2
	EXEC CRUDOnPremii 'delete','Familia',10,4
	EXEC CRUDOnPremii 'delete','Familia',10,4
	EXEC CRUDOnPremii 'delete','Timpul',1024,5
	EXEC CRUDOnPremii 'update','Observator Cultural 2022',5000,3
	EXEC CRUDOnPremii 'update','Pulitzer Prize',74747,2
	EXEC CRUDOnPremii 'select','Pulitzer',74747,2

	PRINT ''
	PRINT 'OPERATII CRUD PE TABELUL SCRIITORI'

	EXEC CRUDOnScriitori 'insert','Tartt','Donna',1
	EXEC CRUDOnScriitori 'insert','Garcia Marquez','Gabriel',2
	EXEC CRUDOnScriitori 'insert','Golding','William',3
	
	EXEC CRUDOnScriitori 'insert','Popescu','Simona',4
	EXEC CRUDOnScriitori 'insert','Tolstoi','Lev',5
	EXEC CRUDOnScriitori 'insert','Ivanescu','Mircea',6
	EXEC CRUDOnScriitori 'insert','Paraschivescu','Radu',7
	EXEC CRUDOnScriitori 'insert','paraschivescu','Radu',7 -- nume rau
	EXEC CRUDOnScriitori 'insert','Von Goethe','Johann Wolfgang',8
	EXEC CRUDOnScriitori 'select','Paraschivescu','Radu',5
	EXEC CRUDOnScriitori 'select','Paraschivescu','Radu',6
	EXEC CRUDOnScriitori 'delete','Paraschivescu','Radu',7
	EXEC CRUDOnScriitori 'delete','Paraschivescu','Radu',9
	EXEC CRUDOnScriitori 'delete','Von Goethe','Johann Wolfgang',8
	EXEC CRUDOnScriitori 'delete','Paraschivescu','ra?du',8 --prenume rau
	EXEC CRUDOnScriitori 'update','Cartarescu','Mircea',6
	EXEC CRUDOnScriitori 'update','Tolstoi','Lev Nikolaevici',5
	EXEC CRUDOnScriitori 'update','cartar??escu','Mircea',6 -- nume rau
	EXEC CRUDOnScriitori 'update','Cartarescu','Mircea',10 --id inexistent
	EXEC CRUDOnScriitori 'select','Cartarescu','Radu',5
	EXEC CRUDOnScriitori 'select','Tolstoi','Radu',6

	PRINT ''
	PRINT 'OPERATII CRUD PE TABELUL LAUREATI'

	EXEC CRUDOnLaureati 'insert',2,1,'Gabriel {Garcia} Marquez was one of the(()) best-known Latin American writers in history. He won a Nobel Prize for Literature, mostly for his masterpiece of magic re(?alism.',1982
	EXEC CRUDOnLaureati 'insert',3,1,'For ?{his novels which, with the perspicuity of realistic narrative art and the diversity and universality of myth, illuminate the human condition))[]]] in the world}of today.',1983
	EXEC CRUDOnLaureati 'insert',1,2,'Donna Tartt has won the Pulitzer award for fiction for her third novel The Goldfinch, which judges described as a book which stimulates the mind and touches the heart.',2014
	EXEC CRUDOnLaureati 'insert',4,3,'Simona Popescu a fost laureata la categoria Poezie, pentru volumul Cartea plantelor si animalelor.',2022
	EXEC CRUDOnLaureati 'insert',4,30,'Simona Popescu a fost laureata la categoria Poezie, pentru volumul Cartea plantelor si animalelor.',2022 --idPremiu rau
	EXEC CRUDOnLaureati 'select',4,3,'Simona Popescu a fost laureata la categoria Poezie, pentru volumul Cartea plantelor si animalelor.',2022
	EXEC CRUDOnLaureati 'delete',4,3,'Simona Popescu a fost laureata la categoria Poezie, pentru volumul Cartea plantelor si animalelor.',2022
	EXEC CRUDOnLaureati 'select',4,3,'Simona Popescu a fost laureata la categoria Poezie, pentru volumul Cartea plantelor si animalelor.',2022 --deleted
	EXEC CRUDOnLaureati 'delete',475,3,'Simona Popescu a fost laureata la categoria Poezie, pentru volumul Cartea plantelor si animalelor.',2022 --not ex
	EXEC CRUDOnLaureati 'select',2,1,'Gabriel {Garcia} Marquez was one of the(()) best-known Latin American writers in history. He won a Nobel Prize for Literature, mostly for his masterpiece of magic re(?alism.',1982
	EXEC CRUDOnLaureati 'select',3,1,'For ?{his novels which, with the perspicuity of realistic narrative art and the diversity and universality of myth, illuminate the human condition))[]]] in the world}of today.',1983
	EXEC CRUDOnLaureati 'select',4,533,'Simona Popescu a fost laureata la categoria Poezie, pentru volumul Cartea plantelor si animalelor.',2022 -- not ex
	EXEC CRUDOnLaureati 'update',2,1,'Gabriel Garcia Marquez was one of the best-known Latin American writers in history. He won a Nobel Prize for Literature, mostly for his masterpiece of magic realism.',1982
	EXEC CRUDOnLaureati 'update',3,1,'For his novels which, with the perspicuity of realistic narrative art and the diversity and universality of myth, illuminate the human condition in the world of today.',1983
	EXEC CRUDOnLaureati 'select',2,1,'Gabriel Garcia Marquez was one of the best-known Latin American writers in history. He won a Nobel Prize for Literature, mostly for his masterpiece of magic realism.',1982
	EXEC CRUDOnLaureati 'select',3,1,'For his novels which, with the perspicuity of realistic narrative art and the diversity and universality of myth, illuminate the human condition in the world of today.',1983
END;

EXEC mainCRUD

SELECT * FROM Utilizatori
SELECT * FROM Premii  
SELECT * FROM Scriitori 
SELECT * FROM Laureati  

GO
CREATE or alter view ViewUtilizatori
as 
	select nume,prenume,telefon from Utilizatori
go

CREATE OR ALTER VIEW ViewCatePremiiDeUnTipDecernate
AS 
	SELECT P.premiu, COUNT(*) AS NrPremiiDecernate 
	FROM Premii P INNER JOIN Laureati L ON L.id_premiu = P.id_premiu
	GROUP BY P.premiu
GO

CREATE OR ALTER VIEW ViewCatiScriitoriAuPremii
AS 
	SELECT NumeScriitor = S.nume+' '+S.prenume, Premiu = P.premiu, Motiv = L.motivul_juriului, Anul = L.data_decernarii
	FROM Scriitori S INNER JOIN Laureati L ON L.id_scriitor = S.id_scriitor
	INNER JOIN Premii P ON L.id_premiu = P.id_premiu
	
GO

SELECT * FROM ViewCatiScriitoriAuPremii


CREATE INDEX IX_Utilizatori_Alfabetic_Dupa_F ON Utilizatori (nume ASC) WHERE nume > 'F'
SELECT nume FROM Utilizatori WHERE nume > 'F'
SELECT nume FROM ViewUtilizatori WHERE nume > 'F'
SELECT nume FROM ViewUtilizatori WHERE nume > 'A' --?

CREATE INDEX IX_Scriitori_Nume ON Scriitori (nume ASC, prenume ASC)
SELECT * FROM Scriitori 
SELECT * FROM ViewCatiScriitoriAuPremii ORDER BY NumeScriitor

CREATE INDEX IX_Premiu_Valoare ON Premii (valoare DESC)
SELECT * FROM Premii
SELECT * FROM ViewCatePremiiDeUnTipDecernate
SELECT * FROM ViewCatiScriitoriAuPremii ORDER BY Premiu

CREATE INDEX IX_Data_Mai_Recenta_Decernare ON Laureati (data_decernarii DESC)
ALTER INDEX IX_Data_Mai_Recenta_Decernare ON Laureati DISABLE
ALTER INDEX IX_Data_Mai_Recenta_Decernare ON Laureati REBUILD
SELECT data_decernarii FROM Laureati WHERE data_decernarii = 2014
SELECT * FROM ViewCatiScriitoriAuPremii ORDER BY Anul --?

GO
SELECT i2.name, i1.user_scans, i1.user_seeks, i1.user_updates, i1.last_user_scan, i1.last_user_seek, i1.last_user_update
FROM sys.dm_db_index_usage_stats i1 INNER JOIN sys.indexes i2 ON i1.index_id = i2.index_id
WHERE  i1.object_id = i2.object_id
