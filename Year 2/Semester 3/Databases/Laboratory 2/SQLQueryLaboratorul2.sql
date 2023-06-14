--selectez cartile care au aparut la edituri
SELECT C.titlu AS [Titlu carte], [Nume editura] = E.editura  
FROM Carti C, Edituri E 
WHERE C.id_editura = E.id_editura;

--selectez cartile care au aparut la edituri din Romania
SELECT C.titlu AS [Titlu carte], [Nume editura din Romania] = E.editura  
FROM Carti C, Edituri E, Tari T 
WHERE C.id_editura = E.id_editura AND T.id_tara = E.id_tara AND T.tara=N'România';

--selectez cartile si scriitorii lor
SELECT [Nume autor] = S.prenume+' '+S.nume ,  C.titlu AS [Titlu carte]
FROM Carti C, Scriitori S, Publicatii P
WHERE C.id_carte = P.id_carte AND P.id_scriitor=S.id_scriitor
ORDER BY S.nume;

--selectez cartile care au aparut dupa 2015
SELECT C.titlu AS [Titlu carte], [A aparut in anul]=A.anul 
FROM Carti C, Ani_aparitie A 
WHERE C.id_an = A.id_an AND A.anul > 2015;

--selectez editurile care au fost infiintate inainte de 1900
SELECT [Nume editura] = E.editura, [Infiintata in anul]=E.anul_infiintarii  
FROM Edituri E
WHERE E.anul_infiintarii < 1900;

--cate carti au o anumita stare, grupate dupa stare
SELECT S.id_stare, S.tip_stare AS [Starea],COUNT(*)AS [Cate carti sunt in starea aceasta] 
FROM Stari_ale_produselor S, Carti C
WHERE C.id_stare = S.id_stare 
GROUP BY S.id_stare, S.tip_stare;

--cati bani castiga o editura
SELECT E.id_editura, E.editura AS Editura, SUM(C.stoc*C.pret) AS [Castig] 
FROM Edituri E, Carti C
WHERE C.id_editura=E.id_editura 
GROUP BY E.id_editura, E.editura 
HAVING SUM(C.stoc*C.pret) > 5000
ORDER BY E.id_editura;

--cate carti au culoare si sunt mai mult de 2 carti care au acea culoare
SELECT CU.id_culoare, CU.culoare, COUNT(*)AS [Cate carti au culoarea aceasta] 
FROM Carti C, CartiColorate CC, Culori CU
WHERE CU.id_culoare = CC.id_culoare AND CC.id_carte = C.id_carte 
GROUP BY CU.id_culoare, CU.culoare
HAVING COUNT(*)>2;

--cartile care au 5 stele ca recenzie
SELECT DISTINCT C.titlu
FROM Carti C INNER JOIN Recenzii R ON C.id_carte = R.id_carte
WHERE R.stele = 5;

--tarile de unde am edituri
SELECT DISTINCT T.tara
FROM Tari T INNER JOIN Edituri E ON E.id_tara = T.id_tara;

ALTER TABLE Laureati
ADD data_decernarii INT;

--selectez scriitorii care au primit premii dupa 1982
-- m-n
SELECT  Laureat = S.prenume+' '+S.nume , [Al premiului] = P.premiu, L.data_decernarii 
FROM Scriitori S
	INNER JOIN Laureati L ON S.id_scriitor = L.id_scriitor
	INNER JOIN Premii P ON L.id_premiu=P.id_premiu
WHERE L.data_decernarii < 2000
ORDER BY L.data_decernarii;

--selectez cartile care au primit premii
SELECT Titlul = C.titlu, Laureat = S.prenume+' '+S.nume , [Al premiului] = P.premiu
FROM Carti C
	INNER JOIN Publicatii PU ON C.id_carte = PU.id_carte
	INNER JOIN Scriitori S ON PU.id_scriitor = S.id_scriitor
	INNER JOIN Laureati L ON S.id_scriitor = L.id_scriitor
	INNER JOIN Premii P ON L.id_premiu=P.id_premiu
ORDER BY L.data_decernarii;

--selectez cartile cu ceva culoare de la edituri
-- m-n
SELECT Culoarea = CU.culoare, Editura = E.editura
FROM Carti C
	INNER JOIN CartiColorate CC ON CC.id_carte=C.id_carte
	INNER JOIN Culori CU ON CU.id_culoare = CC.id_culoare
	INNER JOIN Edituri E ON C.id_editura=E.id_editura
ORDER BY E.editura;

--selectez cartile din categoria Proza, apartinand curentului literar Postmodernist,
--si afisez si numele scriitorului 
SELECT Titlul = C.titlu, Autorul = S.prenume+' '+S.nume
FROM  Carti C 
			INNER JOIN GenuriLiterare G ON C.id_gen = G.id_gen
			INNER JOIN CurenteLiterare CL ON C.id_curent = CL.id_curent
			INNER JOIN Publicatii P ON P.id_carte = C.id_carte
			INNER JOIN Scriitori S ON S.id_scriitor = P.id_scriitor
WHERE G.gen_literar = 'Prozã. Roman' AND CL.curent_literar='Postmodernism';
