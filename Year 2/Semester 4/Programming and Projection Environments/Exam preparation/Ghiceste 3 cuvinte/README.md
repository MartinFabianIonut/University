# Aplicație

Proiectaţi şi implementați o aplicație client-server pentru următoarea problemă. (9p)

Un joc cu un jucator. La inceputul jocului, sistemul va alege aleator din baza de date o configurație (o colectie de litere şi 3 cuvinte posibile distincte, formate din cel putin 2 litere), iar jucătorul trebuie să ghicească cele 3 cuvinte. De exemplu: pentru literele 'a', 'r' şi 'e', cuvintele posibile ar putea fi: 'aer', 'ar' şi 'rea'. Scopul este ca jucatorul să ghicească cât mai multe cuvinte şi så obțină cât mai multe puncte pâná la finalul jocului.

Jucătorul poate să facă urmatoarele:

1. **Începere joc.** Pentru a porni un joc, jucătorul își introduce aliasul asociat. După verificarea că aliasul corespunde unui jucător înregistrat în aplicație, sunt afişate literele posibile şi începe jocul.
   ·
2. **Introducere cuvânt.** Fiecare jucător introduce un cuvânt format doar din literele afişate. Dacă cuvântul este printre cele 3 posibile, jucătorul primeşte un numãr de puncte egal cu numărul de litere din cuvânt şi se afişează mesajul "Cuvânt ghicit". Dacă cuvântul nu este printre cele 3 posibile, jucătorul primeşte un număr de puncte egal cu numărul de litere ghicite pe poziția corectă corespunzătoare unuia dintre cuvintele neghicite până atunci. De exemplu, presupunem că cuvântul 'ar' a fost ghicit deja, iar jucătorul introduce cuvântul 'are'. Numărul de litere care se potrivesc pentru 'aer 'este 1 (litera 'a' apare pe poziția corectă), iar numărul de litere care se potrivesc pentru 'rea' este 0 (nicio literă nu apare pe poziţia corectă). În acest caz, jucătorul va primi 1 punct. Dacă există litere care se potrivesc în ambele cuvinte, se alege numărul maxim.
3. **Final joc.** Jocul se termină după repetarea de 3 ori a cerinței 2. La finalul jocului, jucătorul va putea vizualiza cele 3 cuvinte posibile, numărul total de puncte obținute şi poziția lui în clasamentul tuturor jocurilor finalizate până atunci. Pentru fiecare joc, în clasament se va afişa alias-ul jucătorului, data și ora la care a început jocul, punctajul obtinut. Dacă în timp ce un jucător introduce cuvinte, un alt jucător a finalizat un joc, clasamentul se va actualiza automat pentru a include şi rezultatul respectiv.
4. Un serviciu REST care pentru un anumit jucător, permite vizualizarea tuturor jocurilor finalizate, a punctajului obţinut, numărul si lista cuvintelor ghicite.
5. Un serviciu REST care permite adăugarea unei noi configurații pentru joc (litere şi 3 cuvinte posibile).

## Cerinte:

- Aplicația poate fi dezvoltată în orice limbaj de programare (Java, C#, etc).
- Datele vor fi preluate/salvate dintr-o bază de date relațională (fiecare mutare, rezultate etc).
- Pentru o entitate (exceptând jucător) se va folosi un instrument ORM pentru stocarea datelor (nu se acceptă SpringJPA sau alte instrumente de tip micro-ORM).
- Pentru testarea serviciilor REST se va folosi o extensie a unui browser web/aplicație (Postman, REST client, etc).

### Barem aplicație:

- Începere joc - 1 p
- Determinarea punctajului dupa introducerea unui cuvânt - 1 p
- Finalizarea jocului 0.5 p
- Actualizarea clasamentului la finalul jocului - 1 p
- Actualizarea automata a clasamentului pentru alți jucatori - 1p
- Serviciu REST 1 - 1p
- Serviciu REST 2 - 1p
- Folosire ORM (cu execuție) -1p
- Structura bazei de date si popularea ei - 0.5p
- Arhitectura si proiectare -0.5 p
- Jurnalizare şi fişiere de configurare - 0.5 p
