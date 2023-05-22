CREATE TABLE UrmarireConcurenta
(
	id INT PRIMARY KEY IDENTITY,
	nume_tranzactie NVARCHAR(20),
	actiune NVARCHAR(100),
	amprenta_temporala TIME
);
GO

SELECT * FROM Scriitori
SELECT * FROM UrmarireConcurenta
DELETE FROM UrmarireConcurenta

GO

DECLARE @ISOLATION_LEVEL_READ_UNCOMMITTED INT = 1;
DECLARE @ISOLATION_LEVEL_READ_COMMITTED INT = 2;
DECLARE @ISOLATION_LEVEL_REPEATABLE_READ INT = 3;
DECLARE @ISOLATION_LEVEL_SERIALIZABLE INT = 4;
DECLARE @ISOLATION_LEVEL_SNAPSHOT INT = 5;

GO


CREATE OR ALTER PROCEDURE insertIntoUrmarireConcurenta(
	@nume_tranzactie NVARCHAR(20), 
	@actiune NVARCHAR(100)
)
AS
BEGIN
	INSERT INTO UrmarireConcurenta(nume_tranzactie, actiune, amprenta_temporala) 
		VALUES (@nume_tranzactie, @actiune, CURRENT_TIMESTAMP);
END
GO

-- simulare dirty reads 

BEGIN TRAN;
	EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 1', N'dirty reads: update Scriitori cu numele Cãrtãrescu la id=6'
	SAVE TRAN savepoint;
	UPDATE Scriitori SET nume=N'Cãrtãrescu' WHERE id_scriitor=6;
	WAITFOR DELAY '00:00:07';
ROLLBACK TRAN savepoint;


-- simulare unrepeatable reads

--SET TRANSACTION ISOLATION LEVEL READ COMMITTED 
SET TRANSACTION ISOLATION LEVEL REPEATABLE READ 
BEGIN TRAN;
	SELECT * FROM Scriitori;
	DECLARE @ISOLATION_LEVEL_READ_COMMITTED INT = 2;
	IF (SELECT transaction_isolation_level FROM sys.dm_exec_sessions WHERE session_id = @@SPID) = @ISOLATION_LEVEL_READ_COMMITTED
		EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 1', 'unrepeatable reads BUG: select Scriitori inceput'
	ELSE
		EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 1', 'unrepeatable reads SOLVED: select Scriitori inceput'
	WAITFOR DELAY '00:00:08';
	SELECT * FROM Scriitori;
	IF (SELECT transaction_isolation_level FROM sys.dm_exec_sessions WHERE session_id = @@SPID) = @ISOLATION_LEVEL_READ_COMMITTED
		EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 1', 'unrepeatable reads BUG: select Scriitori final' 
	ELSE
		EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 1', 'unrepeatable reads SOLVED: select Scriitori final' 
COMMIT TRAN;


SELECT * FROM sys.sysprocesses WHERE open_tran = 1

GO
-- simulare phantom reads
--SET TRANSACTION ISOLATION LEVEL REPEATABLE READ
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE 
BEGIN TRAN;
	DECLARE @ISOLATION_LEVEL_REPEATABLE_READ INT = 3;
	SELECT * FROM Scriitori WHERE id_scriitor BETWEEN 1 AND 100;
	IF (SELECT transaction_isolation_level FROM sys.dm_exec_sessions WHERE session_id = @@SPID) = @ISOLATION_LEVEL_REPEATABLE_READ
		EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 1', 'phantom reads BUG: select Scriitori inceput de la 1 la 100' 
	ELSE
		EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 1', 'phantom reads SOLVED: select Scriitori inceput de la 1 la 100' 
	WAITFOR DELAY '00:00:06';
	SELECT * FROM Scriitori WHERE id_scriitor BETWEEN 1 AND 100;
	IF (SELECT transaction_isolation_level FROM sys.dm_exec_sessions WHERE session_id = @@SPID) = @ISOLATION_LEVEL_REPEATABLE_READ
		EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 1', 'phantom reads BUG: select Scriitori final de la 1 la 100' 
	ELSE
		EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 1', 'phantom reads SOLVED: select Scriitori final de la 1 la 100' 
COMMIT TRAN;

GO

-- simulare deadlock
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
SET DEADLOCK_PRIORITY 1
BEGIN TRY
	BEGIN TRAN;
		UPDATE Scriitori SET prenume='William' WHERE id_scriitor=31;
		IF (SELECT deadlock_priority FROM sys.dm_exec_sessions WHERE session_id = @@SPID) > 0 
		EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 1','deadlock SOLVED: first -> update Scriitori'
		WAITFOR DELAY '00:00:05';
		UPDATE Premii SET valoare=1001 WHERE premiu = 'Nobel';
		IF (SELECT deadlock_priority FROM sys.dm_exec_sessions WHERE session_id = @@SPID) > 0
			EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 1','deadlock SOLVED: last -> update Premii'
	COMMIT TRAN;
END TRY
BEGIN CATCH
	IF ERROR_NUMBER() = 1205
		BEGIN
			rollback;
			BEGIN TRAN;
			EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 1','deadlock BUG: first -> update Scriitori'
			EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 1','deadlock BUG: last -> update Premii'
			COMMIT TRAN;
		END
END CATCH

SELECT deadlock_priority FROM sys.dm_exec_sessions WHERE session_id = @@SPID
SELECT * FROM UrmarireConcurenta

SELECT * FROM Premii
SELECT * FROM Scriitori

go
CREATE OR ALTER PROCEDURE updatesForScriitoriAndPremii
AS
BEGIN
	SET DEADLOCK_PRIORITY 1
	BEGIN TRAN;
		UPDATE Scriitori SET prenume='Augutin' WHERE id_scriitor=31;
		WAITFOR DELAY '00:00:05';
		UPDATE Premii SET valoare=111 WHERE premiu = 'Nobel';
	COMMIT TRAN;
END

go
CREATE OR ALTER PROCEDURE updatesForPremiiAndScriitori
AS
BEGIN
	BEGIN TRAN;
		UPDATE Premii SET valoare=222 WHERE premiu = 'Nobel';
		WAITFOR DELAY '00:00:05';
		UPDATE Scriitori SET prenume='Felician' WHERE id_scriitor=31;
	COMMIT TRAN;
END