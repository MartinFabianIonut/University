# Aplicație

Proiectaţi şi implementați o aplicație client-server pentru următoarea problemă. (8p)

Un joc cu un jucător. La începutul jocului, sistemul va alege aleator 5 poziții, reprezentând gropi, pentru o tablă de joc având dimensiunea de 4 linii x 4 coloane. Se generează aleator cel puțin o groapă pentru fiecare linie. Scopul jocului este ca jucătorul să ajungă de la prima linie a tablei de joc până la ultima linie fără a cădea în groapă. Jucătorul poate să facă următoarele:

1. **Începere joc.** Pentru a porni un joc, jucătorul își introduce aliasul asociat. După verificarea că aliasul corespunde unui jucător înregistrat în aplicație, este afișată o tablă de joc goală, clasamentul tuturor jocurilor finalizate şi începe jocul de pe linia 1.
   ·
2. **Alegere poziție.** Jucătorul alege o poziție de pe tabla de joc (pentru fiecare linie) unde crede că nu este groapă (se alege pe rând o poziție pentru linia 1, linia 2, linia 3 și linia 4). Dacă la poziția aleasă de jucător este o groapă, jocul se termină. Dacă la poziția respectivă nu este groapă jucătorul primeşte un număr de puncte egal cu numărul liniei pe care se află şi se mută la următoarea linie.
3. **Final joc.** Jocul se termină fie la alegerea unei poziții la care este o groapă, fie când se alege o poziție de pe linia 4 unde nu este groapă. La finalul jocului, jucătorul va putea vizualiza numărul de puncte obținute, pozițiile gropilor şi poziția lui în clasamentul tuturor jocurilor finalizate până atunci. Pentru fiecare joc, în clasament se va afişa alias-ul jucătorului, numărul de puncte obținute şi durata jocului (în secunde). Dacă în timp ce un jucător alege pozițiile, un alt jucător a finalizat un joc, clasamentul se va actualiza automat pentru a include şi rezultatul respectiv. Jocurile vor fi afişate în ordine descrescătoare după numărul de puncte şi durata jocului.
4. Un serviciu REST care pentru un anumit jucător, permite vizualizarea tuturor jocurilor finalizate de el. Pentru fiecare joc se afişează configurația tablei, pozițiile propuse, punctajul obținut și durata jocului.
5. Un serviciu REST care permite, pentru un anumit joc, adăugarea unei noi poziții propuse. De exemplu, pentru jocul 5 se va adăuga poziția (3,2).

## Cerinte:

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
