SELECT * FROM UrmarireConcurenta
GO

DECLARE @ISOLATION_LEVEL_READ_UNCOMMITTED INT = 1;
DECLARE @ISOLATION_LEVEL_READ_COMMITTED INT = 2;
DECLARE @ISOLATION_LEVEL_REPEATABLE_READ INT = 3;
DECLARE @ISOLATION_LEVEL_SERIALIZABLE INT = 4;
DECLARE @ISOLATION_LEVEL_SNAPSHOT INT = 5;

GO

-- simulare dirty reads 

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
--SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
BEGIN TRAN;
	SELECT * FROM Scriitori;
	DECLARE @ISOLATION_LEVEL_READ_UNCOMMITTED INT = 1;
	IF (SELECT transaction_isolation_level FROM sys.dm_exec_sessions WHERE session_id = @@SPID) = @ISOLATION_LEVEL_READ_UNCOMMITTED
		EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 2', N'dirty reads: select Scriitori -> exista Cãrtãrescu la id=6' 
	ELSE
		EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 2', N'dirty reads: select Scriitori -> NU exista Cãrtãrescu la id=6' 
	SELECT * FROM UrmarireConcurenta 
COMMIT TRAN;
GO

-- simulare unrepeatable reads
SELECT * FROM Scriitori
BEGIN TRAN;
	WAITFOR DELAY '00:00:03';
	UPDATE Scriitori SET prenume='William' WHERE id_scriitor=31;
	--UPDATE Scriitori SET prenume='Abraham' WHERE id_scriitor=31;
	EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 2', N'unrepeatable reads: update WILLIAM FAULKNER' 
COMMIT TRAN;
SELECT * FROM UrmarireConcurenta 


-- simulare phantom reads
BEGIN TRAN;
	WAITFOR DELAY '00:00:03';
	INSERT INTO Scriitori (nume, prenume) VALUES ('Scriitor', 'Mare');
	EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 2','phantom reads: insert Scriitor'
COMMIT TRAN;


-- simulare deadlock
SELECT deadlock_priority FROM sys.dm_exec_sessions WHERE session_id = @@SPID
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
BEGIN TRY
    BEGIN TRAN;
		UPDATE Premii SET valoare=1111 WHERE premiu = 'Nobel';
		EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 2','deadlock BUG: first -> update Premii'
		WAITFOR DELAY '00:00:05';
		UPDATE Scriitori SET prenume='Will' WHERE id_scriitor=31;
		EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 2','deadlock BUG: last -> update Scriitori'
	COMMIT TRAN;
END TRY
BEGIN CATCH
    IF ERROR_NUMBER() = 1205
    BEGIN
        ROLLBACK; 
        BEGIN TRAN;
			UPDATE Premii SET valoare=1111 WHERE premiu = 'Nobel';
			EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 2','deadlock SOLVED: first -> update Premii'
			WAITFOR DELAY '00:00:05';
			UPDATE Scriitori SET prenume='Will' WHERE id_scriitor=31;
			EXECUTE dbo.insertIntoUrmarireConcurenta 'tranzactia 2','deadlock SOLVED: last -> update Scriitori'
		COMMIT TRAN;
    END
END CATCH

SELECT * FROM Premii
SELECT * FROM Scriitori