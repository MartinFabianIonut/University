# LABORATOR AJAX

## Probleme:

1. Să se scrie o pagină HTML care conține două liste cu stații de plecare și stații de sosire pentru trenuri. Server-side se întreține o bază de date cu înregistrări de forma:

   (Oras1, Oras2)

   (Oras1, Oras3)

   (Oras2, Oras4)

   (Oras2, Oras5)

   (Oras6, Oras7)

   În momentul în care utilizatorul selectează o stație de plecare în prima componentă a listei, a doua listă se va actualiza folosind AJAX cu lista stațiilor în care se poate ajunge din orașul selectat în prima listă.

2. O tabelă dintr-o bază de date menținută server-side are următoarele atribute: Nume, Prenume, Telefon, E-mail. Înregistrările din această tabelă vor fi afișate pe client paginat câte 3 pe pagină, împreună cu două butoane Next și Previous. La acționarea butoanelor Next și Previous se vor afișa următoarele 3, respectiv anterioarele 3 înregistrări din baza de date, care vor fi aduse pe client printr-un apel AJAX. Butoanele Next și Previous trebuie să devină disabled dacă în urma acționării acestora nu se mai pot aduce înregistrări noi pe client.
3. Într-o tabelă a unei baze de date menținută server-side există înregistrări indexate după un anumit câmp cheie (spre exemplu id). Să se scrie o pagină HTML care conține o listă a cărei valori sunt valorile atributului cheie. Pagina HTML conține și un formular care conține, pentru elementul selectat din listă, valorile corespunzătoare ale celorlalte atribute din baza de date. La modificarea elementului selectat în listă se vor actualiza valorile din formular. La modificarea valorilor din formular, un buton "Save" inițial disabled va deveni activ, iar acționarea sa duce la salvarea datelor server-side. La modificarea datelor din formular și selectarea altui element cheie din listă fără salvarea datelor din formular, utilizatorul va fi atenționat că datele s-au modificat și dacă se dorește în prealabil salvarea acestora. Problema va fi implementată folosind AJAX.
4. Să se scrie o pagină HTML care conține un tabel de 3×3 care reprezintă o tablă de X-0. Aleator va începe să joace fie calculatorul, fie jucătorul. La un click într-o căsuță a tabelului, aceasta se va completa cu X sau 0 în funcție dacă a început sau nu jucătorul uman. După acțiunea jucătorului, se va apela prin AJAX un script server-side care face următoarea mutare. Tot server-side se va face verificarea terminării jocului.
5. Folosind JavaScript, să se creeze o componentă de tipul Tree-View. Nodurile acestei componente vor conține nume de directoare, iar frunzele nume de fișiere. Această componentă va fi creată cu ajutorul AJAX și va reflecta structura de directoare server-side (la un click pe un nod director, se va apela un script server-side care va întoarce browserului conținutul directorului respectiv). La un click pe un nod frunză (fișier), se va vizualiza în pagina HTML și conținutul fișierului. Pe client, Tree-View-ul va fi implementat sub forma unei liste de liste (de liste, etc.) create cu ajutorul tag-urilor \<ul\> și \<li\>.
6. Să se scrie o pagină HTML care conține o serie de combo boxuri legate de proprietățile articolelor vândute într-un magazin online IT. Să se implementeze folosind AJAX un mecanism de filtrare a articolelor dorite de utilizator după proprietățile acestora. Spre exemplu, clientul va putea filtra notebook-urile din baza de date după următoarele proprietăți: producător, procesor, memorie, capacitate HDD, placă video, etc.

### Referințe:

- [AJAX Tutorial](https://www.w3schools.com/xml/ajax_intro.asp)
- [jQuery – AJAX Introduction](http://www.w3schools.com/jquery/jquery_ajax_intro.asp)
- [jQuery AJAX Methods](http://www.w3schools.com/jquery/jquery_ref_ajax.asp)
