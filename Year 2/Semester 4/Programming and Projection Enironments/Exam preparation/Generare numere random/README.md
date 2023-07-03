# Aplicație

Proiectați și implementați o aplicație client-server pentru următoarea problemă.

Un joc cu 3 jucători numit Cauta obiectele. Trei utilizatori autentificați pot juca acest joc. Fiecare jucător propune 3 poziții (din 9 posibile) pentru obiectele sale și fiecare jucător încearcă să ghicească pozițiile obiectelor celorlalți jucători. Jucătorul/Jucătorii care obține/obțin cele mai multe puncte după 3 runde, câștigă jocul.

Fiecare jucător poate să facă următoarele:

1. _Începere joc_. Pentru a porni un jnoc, jucătorul își introduce aliasul asociat. După verificarea că aliasul corespunde unui jucător înregistrat în aplicație, începe jocul și jucătorul va vedea cele 5 poziții și valoarea fiecărei poziții.

2. _Generare număr_. Fiecare jucător generează aleator un număr _n_ între 1 si 3 (1, 2 sau 3), prin apăsarea unui buton. După ce numărul este generat, acesta este trimis la server și jucătorul va înainta cu n poziții în lista pozițiilor afișate (e posibil să treacă iarași pe la start). Dacă noua poziție nu a fost deja ocupată, jucătorul va plăti serverului valoarea poziției respective și va deveni ocupantul acelei poziții. Dacă noua poziție depăseste cele 5 poziții și jucătorul trece din nou pe la start, acesta va primi 5 lei de la server. Dacă noua poziție a mai fost vizitată de jucător, acesta rămâne cu aceeași sumă de bani. Cerința 2 se repetă de încă 2 ori.

3. _Final joc_. După repetarea de 3 ori a cerinței 2 jocul se termină, iar jucătorul va putea vizualiza suma de bani obținută și poziția lui în clasamentul tuturor jocurilor finalizate până atunci. Pentru fiecare joc, în clasament se va afișa alias-ul jucătorului, data și ora la care a început jocul și suma obținută. Dacă în timp ce un jucător execută cele 3 generări de numere, un alt jucător a finalizat un joc, clasamentul se va actualiza automat pentru a include și rezultatul respectiv.

4. Un serviciu REST care, pentru un anumit jucător, permite vizualizarea tuturor jocurilor finalizate și a rezultatului obținut.
5. Un serviciu REST care permite adăugarea unei noi configurații de joc (valorile celor 5 poziții).

## Cerințe:

- Aplicația poate fi dezvoltată în orice limbaj de programare (Java, C#, etc).
- Datele vor fi preluate/salvate dintr-o bază de date relațională (fiecare mutare, rezultate etc).
- Pentru o entitate (exceptând jucător) se va folosi un instrument ORM pentru stocarea datelor (nu se acceptă SpringJPA sau alte instrumente de tip micro-ORM).
- Pentru testarea serviciilor REST se va folosi o extensie a unui browser web/aplicație (Postman, REST client, etc).

### Barem aplicație:

- Începere joc - 0.75p
- Determinarea punctajului dupa alegerea unei poziții - 0.5p
- Finalizarea jocului 0.5 p
- Actualizarea clasamentului la finalul jocului - 0.75 p
- Actualizarea automata a clasamentului pentru alți jucatori - 1p
- Serviciu REST 1 - 1p
- Serviciu REST 2 - 1p
- Folosire ORM (cu execuție) -1p
- Structura bazei de date si popularea ei - 0.5p
- Arhitectura si proiectare -0.5 p
- Jurnalizare şi fişiere de configurare - 0.5 p
