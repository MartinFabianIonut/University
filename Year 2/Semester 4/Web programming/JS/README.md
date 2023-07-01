# LABORATOR JAVASCRIPT

## Probleme

1. Să se scrie o pagină HTML ce conține două liste cu mai multe elemente fiecare, create cu ajutorul tagului `<select>`. La un dublu-click pe un element al primei liste, acesta va fi mutat în lista a doua și invers. Nu se vor folosi biblioteci de funcții, jQuery, pluginuri, etc.
2. Un formular web va permite unui utilizator să-și introducă numele, data nașterii, vârsta și adresa de e-mail. La apăsarea unui buton "Trimite" se vor valida toate aceste câmpuri dacă sunt completate și dacă sunt completate corect. Dacă da, se va afișa un mesaj "Datele sunt completate corect", în caz contrar, se va afișa un mesaj de genul "Câmpurile nume și vârsta nu sunt completate corect", aceste câmpuri fiind "încercuite" într-o bordură roșie. Toate aceste validări vor fi implementate pe client în JavaScript. Nu se vor folosi biblioteci de funcții, jQuery, pluginuri, etc.
3. Să se implementeze folosind JavaScript următoarea problemă. O matrice cu număr par de elemente, reprezentată vizual sub forma unui tabel, conține perechi de numere inițial ascunse. Dacă utilizatorul dă click pe două celule ale tabelului ce conțin numere egale, acestea vor fi afișate și vor rămâne afișate. Dacă numerele conținute în cele două celule nu sunt egale, vor fi ascunse din nou după un număr de 2, 3 secunde. Jocul se termină când toate perechile de numere au fost ghicite. După prima implementare a jocului, se va crea o nouă versiune în care numerele vor fi înlocuite cu imagini (ce conțin fructe spre exemplu, sau "profi" de pe pagina facultății). Problema mai este recunoscută și sub numele de Memory Game. Nu se vor folosi biblioteci de funcții, jQuery, pluginuri, etc.
4. Să se scrie o pagină HTML ce conține un tabel cu cel puțin trei coloane și cel puțin trei linii. Prima coloană reprezintă antetul. Când utilizatorul va da click pe o celulă din antet, elementele din tabel se vor sorta crescător sau descrescător în funcție de valorile prezente pe linia corespunzătoare antetului pe care s-a dat click. Codul JavaScript va fi reutilizabil și va permite crearea unui comportament de tabel sortabil pentru mai multe tabele existente în cadrul paginii. Nu se vor folosi biblioteci de funcții, jQuery, pluginuri, etc. Exemplu de tabel ce se dorește a fi sortat (sortarea se va face alfabetic după numele fructului, după preț sau după cantitate):
   <table>
    <tr>
        <th>Fructe</th>
        <td>Mere</td>
        <td>Pere</td>
    </tr>
    <tr>
        <th>Pret</th>
        <td>3</td>
        <td>4</td>
    </tr>
    <tr>
        <th>Cantitate</th>
        <td>8</td>
        <td>6</td>
    </tr>
    </table>

   După rezolvarea problemei, implementați o nouă variantă în care capul de tabel este orizontal, nu vertical. Nu se vor folosi biblioteci de funcții, jQuery, plugin-uri, etc.

5. Într-o pagină HTML există o listă descrisă cu ajutorul tagului `<ol>`. Fiecare element din listă (`<li>`) conține o imagine (`<img>`), și un link (`<a>`). Elementele listei, mai puțin primul dintre ele, nu sunt vizibile inițial (se poate folosi în acest sens CSS). Afișarea unui element din listă presupune afișarea imaginii și a textului ca link peste imagine (a se vedea ca exemplu carousel-ul din pagina facultății). După n secunde, printr-un efect de tranziție, va fi afișat următorul element din listă. Se vor implementa și două butoane Next și Previous care vor permite afișarea elementelor următoare sau anterioare fără a se aștepta trecerea celor n secunde. Nu se vor folosi biblioteci de funcții, jQuery, plugin-uri, etc.

6. O pagină HTML va conține un tabel cu n linii și n coloane. Celulele tabelului conțin numere de la 1 la n^2 într-o ordine aleatoare. Una dintre celule este liberă. Folosind JavaScript, să se creeze în cadrul paginii un joc de tip puzzle care, la apăsarea tastelor săgeți, va interschimba celula liberă cu celula adiacentă (corespunzătoare tastei sus, jos, stânga, dreapta apăsată). Nu se vor folosi biblioteci de funcții, jQuery, plugin-uri, etc.

   Puzzle-ul inițial:

   <table>
    <tr>
        <td>3</td>
        <td>8</td>
        <td>9</td>
        <td>5</td>
    </tr>
    <tr>
        <td>7</td>
        <td>13</td>
        <td>6</td>
        <td>15</td>
    </tr>
    <tr>
        <td>10</td>
        <td></td>
        <td>14</td>
        <td>4</td>
    </tr>
    <tr>
        <td>2</td>
        <td>11</td>
        <td>1</td>
        <td>12</td>
    </tr>
    </table>

   Puzzle-ul rezolvat:

    <table>
    <tr>
        <td>1</td>
        <td>2</td>
        <td>3</td>
        <td>4</td>
    </tr>
    <tr>
        <td>5</td>
        <td>6</td>
        <td>7</td>
        <td>8</td>
    </tr>
    <tr>
        <td>9</td>
        <td>10</td>
        <td>11</td>
        <td>12</td>
    </tr>
    <tr>
        <td>13</td>
        <td>14</td>
        <td>15</td>
        <td></td>
    </tr>
    </table>

   Cerința facultativă pentru cei ce se "plictisesc" 😊: După implementarea inițială, se va crea o a doua variantă a jocului, unde, în loc de numere, celulele tabelului vor conține sprite-uri CSS obținute din cadrul unei aceleași imagini pătrate. Numărul n se va da la începutul jocului, la fel și URL-ul imaginii ce se va folosi în sprite-urile CSS ca și fundal pe celulele tabelului. Nu se vor folosi biblioteci de funcții, jQuery, pluginuri, etc.

## Referințe:

- [JAVASCRIPT Tutorial](http://www.w3schools.com/js/default.asp)
