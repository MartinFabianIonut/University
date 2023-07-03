# Aplicație

Proiectați și implementați o aplicație client-server pentru următoarea problemă.

Un joc cu un jucator. La începutul jocului, sistemul va alege aleator din baza de date o configurație (poziție și un indiciu) pentru o tablă de joc având dimensiunea de 4 linii x 4 coloane, iar jucătorul trebuie să ghicească poziția indiciului. Scopul este ca jucătorul să ghicească poziția indiciului prin cât mai puține încercări. Jucătorul poate să facă următoarele:

1. _Începere joc_. Pentru a porni un jnoc, jucătorul își introduce aliasul asociat. După verificarea că aliasul corespunde unui jucător înregistrat în aplicație, este afişată o tablă de joc goală şi începe jocul.

2. _Alegere poziție_. Fiecare jucător alege o poziție de pe tabla de joc unde crede că este indiciul. Dacă indiciul este la poziția aleasă de jucător, jocul se termină şi jucătorul poate vizualiza indiciul. Dacă indiciul nu este la poziția respectivă, sistemul afişează pe poziția respectivă distanţa de la aceea poziție până la indiciu (distanţa euclidiană).

3. _Final joc_. Jocul se termină fie la ghicirea poziției indiciului, fie după repetarea de cel mult 4 ori a cerinței. La finalul jocului, jucătorul va putea vizualiza poziția indiciului, numărul de încercări până a ghicit poziția (10 dacă nu a ghicit pozitia indiciului) şi poziția lui în clasamentul tuturor jocurilor finalizate până atunci. Pentru fiecare joc, în clasament se va afişa alias-ul jucătorului, data și ora la care a inceput jocul, numărul de încercări până la ghicirea poziției indiciului și textul indiciului (şirul vid dacă nu a ghicit poziția). Dacă în timp ce un jucător alege pozițiile, un alt jucător a finalizat un joc, clasamentul se va actualiza automat pentru a include şi rezultatul respectiv.

4. Un serviciu REST care pentru un anumit jucător, permite vizualizarea tuturor jocurilor finalizate de el. Pentru fiecare joc se afişează numărul de încercări, pozițiile propuse şi textul indiciului (şirul vid daca nu a fost ghicită poziția).
5. Un serviciu REST care permite adăugarea unei noi configurații pentru joc (poziție pe tablă şi text indiciu). Exemplu: Pentru configurația (2, 2, "Mergi la ușa 4!"), indiciul va fi plasat pe linia 2, coloana 2 şi va avea textul "Mergi la ușa 4!".

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
