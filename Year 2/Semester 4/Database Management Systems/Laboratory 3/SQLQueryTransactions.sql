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

CREATE OR ALTER FUNCTION validarePremii(
	@premiu NVARCHAR(50),
	@valoare INT
)
RETURNS NVARCHAR(200)
AS
BEGIN
	DECLARE @error NVARCHAR(200)
	SET @error=''
	IF (dbo.validarePremiu(@premiu) = 0)
		SET @error = @error + 'Premiu invalid.'
	IF (dbo.validareValoare(@valoare) = 0)
		SET @error = @error + 'Valoare invalida.'
	RETURN @error
END
GO


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
	IF @data >= 1901 AND @data <= YEAR(GETDATE())
		RETURN 1
	RETURN 0
END
GO

CREATE OR ALTER FUNCTION validareLaureati(
	@idScriitor INT,
	@idPremiu INT,
	@motiv NVARCHAR(250),
	@data INT
)
RETURNS NVARCHAR(200)
AS
BEGIN
	DECLARE @error NVARCHAR(200)
	SET @error=''
	IF (dbo.validareIdScriitorPtLaureati(@idScriitor) = 0)
		SET @error = @error + 'Id scriitor invalid.'
	IF (dbo.validareIdPremiuPtLaureati(@idPremiu) = 0)
		SET @error = @error + 'Id premiu invalid.'
	IF (dbo.validareMotiv(@motiv) = 0)
		SET @error = @error + 'Motiv invalid.'
	IF (dbo.validareData(@data) = 0)
		SET @error = @error + 'Data invalida.'
	RETURN @error
END
GO


--FUNCTII DE VALIDARE A DATELOR DIN TABELUL      SCRIITORI

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


CREATE OR ALTER FUNCTION validareScriitori(
	@nume NVARCHAR(50), 
	@prenume NVARCHAR(50)
)
RETURNS NVARCHAR(200)
AS
BEGIN
	DECLARE @error NVARCHAR(200)
	SET @error=''
	IF (dbo.validareNume(@nume) = 0)
		SET @error = @error + 'Nume invalid.'
	IF (dbo.validarePrenume(@prenume) = 0)
		SET @error = @error + 'Prenume invalid.'
	RETURN @error
END
GO


----------------------------------- TABEL DE URMARIRE ----------------------------------------------------
CREATE TABLE Urmarire
(
	nume_tabel NVARCHAR(20),
	actiune NVARCHAR(20),
	amprenta_temporala TIME
);
GO


---------------------------------- ROLL-BACK PE INTREAGA PROCEDURA ----------------------------------------

CREATE OR ALTER PROCEDURE insertScriitoriPremiiLaureati(
	@nume NVARCHAR(50), 
	@prenume NVARCHAR(50),
	@premiu NVARCHAR(50),
	@valoare INT,
	@motiv NVARCHAR(250),
	@data INT
)
AS
BEGIN
	BEGIN TRAN
	BEGIN TRY
		DECLARE @error NVARCHAR(200)
		--Scriitori
		SET @error = dbo.validareScriitori(@nume, @prenume);
		IF (@error !='')
			BEGIN
				PRINT @error
				RAISERROR(@error,14,1)
			END
		INSERT INTO Scriitori(nume, prenume) VALUES (@nume, @prenume);
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Scriitori', 'Insert', CURRENT_TIMESTAMP)

		--Premii
		SET @error = dbo.validarePremii(@premiu, @valoare)
		IF (@error !='')
			BEGIN
				PRINT @error
				RAISERROR(@error,14,1)
			END
		INSERT INTO Premii(premiu, valoare) VALUES (@premiu, @valoare);
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Premii', 'Insert', CURRENT_TIMESTAMP)

		--Laureati
		DECLARE @idScriitor INT
		DECLARE @idPremiu INT
		SET @idScriitor = (SELECT MAX(id_scriitor) FROM Scriitori)
		SET @idPremiu = (SELECT MAX(id_premiu) FROM Premii)
		SET @error = dbo.validareLaureati(@idScriitor, @idPremiu, @motiv, @data)
		IF (@error !='')
			BEGIN
				PRINT @error
				RAISERROR(@error,14,1)
			END
		INSERT INTO Laureati(id_scriitor, id_premiu, motivul_juriului, data_decernarii) 
			VALUES (@idScriitor, @idPremiu, @motiv, @data);
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Laureati', 'Insert', CURRENT_TIMESTAMP)
		
		COMMIT TRAN
		SELECT 'Commited!'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		SELECT 'Rollback!'
	END CATCH
END
GO

SELECT * FROM Scriitori
SELECT * FROM Premii
SELECT * FROM Laureati
SELECT * FROM Urmarire

--COMMITED WITH SUCCESS
--@nume NVARCHAR(50),@prenume NVARCHAR(50),@premiu NVARCHAR(50),@valoare INT,@motiv NVARCHAR(250),@data INT
EXECUTE dbo.insertScriitoriPremiiLaureati
'Mann',
'Thomas',
'Nobel3',
448667,
'...In principal pentru marele roman Casa Buddenbrook, o opera clasica a literaturii contemporane',
1929

SELECT * FROM Scriitori
SELECT * FROM Premii
SELECT * FROM Laureati
SELECT * FROM Urmarire

--ROLLBACK SCENARIO
EXECUTE dbo.insertScriitoriPremiiLaureati
'Mann',
'thomas',
'Nobel',
448667,
'...In principal pentru marele roman Casa Buddenbrook, o opera clasica a literaturii contemporane',
1929

SELECT * FROM Scriitori
SELECT * FROM Premii
SELECT * FROM Laureati
SELECT * FROM Urmarire


---------------------------------- ROLL-BACK PE O PARTE DIN PROCEDURA ----------------------------------------
GO
CREATE OR ALTER PROCEDURE insertScriitoriPremiiLaureatiPartial(
	@nume NVARCHAR(50), 
	@prenume NVARCHAR(50),
	@premiu NVARCHAR(50),
	@valoare INT,
	@motiv NVARCHAR(250),
	@data INT
)
AS
BEGIN
	DECLARE @error NVARCHAR(200)
	DECLARE @rollback INT
	SET @rollback = 0
	BEGIN TRAN
	BEGIN TRY
		SET @error = dbo.validareScriitori(@nume, @prenume);
		IF (@error !='')
			BEGIN
				PRINT @error
				RAISERROR(@error,14,1)
			END
		INSERT INTO Scriitori(nume, prenume) VALUES (@nume, @prenume);
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Scriitori', 'Insert', CURRENT_TIMESTAMP)
		COMMIT TRAN
		SELECT 'Commited for Scriitori'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		SET @rollback = 1
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Scriitori', 'Rollback', CURRENT_TIMESTAMP)
		SELECT 'Rollback for Scriitori'
	END CATCH

	BEGIN TRAN
	BEGIN TRY
		SET @error = dbo.validarePremii(@premiu, @valoare)
		IF (@error !='')
			BEGIN
				PRINT @error
				RAISERROR(@error,14,1)
			END
		INSERT INTO Premii(premiu, valoare) VALUES (@premiu, @valoare);
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Premii', 'Insert', CURRENT_TIMESTAMP)
		COMMIT TRAN
		SELECT 'Commited for Premii'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		SET @rollback = 1
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Premii', 'Rollback', CURRENT_TIMESTAMP)
		SELECT 'Rollback for Premii'
	END CATCH

	BEGIN TRAN
	BEGIN TRY
		IF (@rollback = 1)
			BEGIN
				PRINT 'Nu se poate face o asociere daca una dintre entitati nu s-a comis!'
				RAISERROR(@error,14,1)
			END
		DECLARE @idScriitor INT
		DECLARE @idPremiu INT
		SET @idScriitor = (SELECT MAX(id_scriitor) FROM Scriitori)
		SET @idPremiu = (SELECT MAX(id_premiu) FROM Premii)
		SET @error = dbo.validareLaureati(@idScriitor, @idPremiu, @motiv, @data)
		IF (@error !='')
			BEGIN
				PRINT @error
				RAISERROR(@error,14,1)
			END
		INSERT INTO Laureati(id_scriitor, id_premiu, motivul_juriului, data_decernarii) 
			VALUES (@idScriitor, @idPremiu, @motiv, @data);
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Laureati', 'Insert', CURRENT_TIMESTAMP)
		
		COMMIT TRAN
		SELECT 'Commited for Laureati'
	END TRY
	BEGIN CATCH
		ROLLBACK TRAN
		INSERT INTO Urmarire(nume_tabel, actiune, amprenta_temporala) VALUES ('Laureati', 'Rollback', CURRENT_TIMESTAMP)
		SELECT 'Rollback for Laureati'
	END CATCH
END
GO

SELECT * FROM Scriitori
SELECT * FROM Premii
SELECT * FROM Laureati
SELECT * FROM Urmarire

--COMMITED WITH SUCCESS
--@nume NVARCHAR(50),@prenume NVARCHAR(50),@premiu NVARCHAR(50),@valoare INT,@motiv NVARCHAR(250),@data INT
EXECUTE dbo.insertScriitoriPremiiLaureatiPartial
'Faulkner',
'Willian',
'Nobel',
448667,
'pentru viguroasa si unica sa contributie artistica la romanul american modern',
1949

SELECT * FROM Scriitori
SELECT * FROM Premii
SELECT * FROM Laureati
SELECT * FROM Urmarire

--ROLLBACK FOR Premii
EXECUTE dbo.insertScriitoriPremiiLaureatiPartial
'Russell',
'Bertrand',
'Nobel',
-7,
'...In principal pentru marele roman Casa Buddenbrook, o opera clasica a literaturii contemporane',
2929

SELECT * FROM Scriitori
SELECT * FROM Premii
SELECT * FROM Laureati
SELECT * FROM Urmarire

--ROLLBACK FOR Scriitori
EXECUTE dbo.insertScriitoriPremiiLaureatiPartial
'mann$',
'Thomas',
'Premiu ramas',
333,
'...In principal pentru marele roman Casa Buddenbrook, o opera clasica a literaturii contemporane',
1929

SELECT * FROM Scriitori
SELECT * FROM Premii
SELECT * FROM Laureati
SELECT * FROM Urmarire

DELETE FROM Urmarire