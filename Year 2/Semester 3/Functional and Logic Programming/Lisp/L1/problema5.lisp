; (load "test.lisp")

;Problema 5
;a) Definiti o functie care interclaseaza cu pastrarea dublurilor doua liste liniare sortate.

; Modelul matematic: 
;                  interclasare(l11...l1n, l21...l2n) = { 
;                                                       l2, l1=[] 
;                                                       l1, l2=[]
;                                                       l11 + interclasare(l12...l1n, l21...l2n), l11<l21
;                                                       l21 + interclasare(l11...l1n, l22...l2n), altfel
;                                                       }
; interclasare(L1: lista, L2: lista)
; L1 - prima lista cu elemente sortate
; L2 - a doua lista cu elemente sortate
; Functia returneaza o lista formata din interclasarea lui L1 cu L2, cu pastrarea dublurilor

(defun interclasare( l1 l2 )
    (cond
        ((null l1) l2)
        ((null l2) l1)
        ((<(car l1)(car l2)) (cons (car l1) (interclasare (cdr l1) l2)))
        (T (cons (car l2) (interclasare l1 (cdr l2))))
    )
)

; Cazuri de testare:
; (interclasare '() '())
; (interclasare '() '(1 3 6 8))
; (interclasare '(1 2 4 5) '())
; (interclasare '(1 2 4 5) '(1 3 6 8))
; (interclasare '(-12 -11 -5 2 4 5) '(-12 -12 3 6 88))

; ---------------------------------------------------------------------------------------------------------
; b) Definiti o functie care substituie un element E prin elementele unei liste L1 la toate nivelurile unei liste date L.

; Modelul matematic:
;                   substituie(l1...ln, E, L11...L1n) = {
;                                                        [], l=[]
;                                                        L11..L1n + substituie(l2...ln, E, L11...L1n), l1 e nr si l1=E
;                                                        l1 + substituie(l2...ln, E, L11...L1n), l1 e atom
;                                                        substituie(l1, E, L11...L1n) + substituie(l2...ln, E, L11...L1n), altfel (merge pe nivelurile inferioare)
;                                                       }
; substituie(L: lista, E: intreg, L1: lista)
; L - lista initiala, neliniara
; E - numarul pe care doresc sa il substitui cu elementele listei L1
; L1 - lista care contine elemenetele cu care va fi inlocuit elementul E din lista L
; Functia substituie toate aparitiile elementului E in lista L cu elemenetele listei L1

(defun substituie( L E L1 )
    (cond 
        ((null L) nil)
        ( (and (numberp(car L)) (= E (car L))) (append L1 (substituie (cdr L) E L1 ) ) ) ;cons daca vreau ca lista, nu ca elemente
        (  (atom(car L)) (cons (car L) (substituie (cdr L) E L1 ) ) )
        (T (cons (substituie  (car L) E L1 ) (substituie  (cdr L) E L1 )) )
    )
)

; Cazuri de testare:
; (substituie '(1 2 3) '2 '(100 101))
; (substituie '(1 2 3) '2 '((100) 101))
; (substituie '(1 2 3) '2 '((100 (103 104)) 101))
; (substituie '(1 2 (4 3 2 (2)) 3) '2 '(100 101))
; (substituie '(1 2 (4 3 2 (2)) 3) '3 '(100 (102) 101))

; ---------------------------------------------------------------------------------------------------------
; c) Definiti o functie care determina suma a doua numere in reprezentare de lista si calculeaza numarul zecimal corespunzator sumei.

; Modelul matematic:
;                   inversa_listei(L1..Ln, Col) = {
;                                                  Col, L = []
;                                                  inversa_listei(L2..Ln, L1 + Col), altfel
;                                                 }
; inversa_listei(L: lista, Col: lista)
; L - lista care se doreste a fi inversata
; Col - lista - metoda variabilei colectoare - in ea se vor depune elemenetele listei L in ordine inversa (fiind vida initial)
; Functia inverseaza lista L 

(defun inversa_listei(L Col)
    (cond
        ( (null L) Col)
        (T (inversa_listei (cdr L) (cons (car L) Col)) )
    )
)
; Cazuri de testare:
; (inversa_listei '(1 2 3) '())
; (inversa_listei '(1 2 3 2 1) '())

; Modelul matematic:
;                   nr(L1...Ln) = {
;                                  L1, n = 1
;                                  L1 + nr(L2...Ln)*10, altfel
;                                 }
; nr(L: lista)
; L - lista liniara numerica (contine doar cifre)
; Functia returneaza numarul format din cifrele listei L, in ordine inversa (motiv pentru care am nevoie de functie de inversare a unei liste)

(defun nr (L)
    (cond
        ( (and (numberp(car L)) (null (cdr L))) (car L))
        (T (+ (car L) (* (nr (cdr L)) 10) ))
    )
)

; Cazuri de testare:
; (nr '(1 2 3))
; (nr '(1 2 3 2))


; Modelul matematic:
;                   suma(l11...l1n, l21...l2n) = nr(inversa_listei(l11...l1n)) + nr(inversa_listei(l21...l2n))
; suma(l1: lista, l2: lista)
; l1 - lista care contine cifre 
; l2 - lista care contine cifre 
; Functia returneaza suma a doua numere in reprezentare de lista

(defun suma(l1 l2)
    (+ (nr (inversa_listei l1 '())) (nr ( inversa_listei l2 '())))
)

; Cazuri de testare:
; (suma '(1 2) '(3 4))
; (suma '(1 2) '(3 4 5))
; (suma '(1 2) '(3 0 9))

; ---------------------------------------------------------------------------------------------------------
; d) Definiti o functie care intoarce cel mai mare divizor comun al numerelor dintr-o lista liniara.

; Modelul matematic:
;                   cmmdc(a,b) = {
;                                 a, a=b
;                                 a+b, a=0 || b=0
;                                 cmmdc(a-b,b), a>b, b>0
;                                 cmmdc(a,b-a), b>a, a>0
;                                }
; cmmdc(a: intreg, b: intreg)
; a - un numar intreg
; b - un numar intreg
; Functia returneaza cel mai mare divizor comun al numerelor a si b-a

(defun cmmdc(a b)
    (cond
        ( (= a b) a)
        ( (or (= a 0) (= b 0)) (+ a b))
        ( (and (> b 0) (> a b) ) (cmmdc (- a b) b) )
        ( T (and (> a 0) (> b a) ) (cmmdc a (- b a)) )
        
    )
)

; Cazuri de testare:
; (cmmdc '0 '0)
; (cmmdc '2 '0)
; (cmmdc '0 '3)
; (cmmdc '2 '3)
; (cmmdc '2 '2)
; (cmmdc '27 '81)
; (cmmdc '27 '36)

; Modelul matematic:
;                   cmmdc_lista(l1...ln) = {
;                                           [], l=[]
;                                           l1, n=1
;                                           cmmdc_lista(cmmdc(l1,l2),l3...ln), altfel
;                                          }
; cmmdc_lista(L: lista)
; L - lista de numere, liniara
; Functia returneaza cel mai mare divizor comun al tuturor numerelor din lista L

(defun cmmdc_lista (L)
    (cond
        ((null L) nil)
        ((= (length L) 1) (car L) )
        ((cmmdc_lista ( cons (cmmdc (car L) (car (cdr L))) (cdr (cdr L)) )))
    )
)

; Cazuri de testare:
; (cmmdc_lista '(2 3 4))
; (cmmdc_lista '(27 54 81))
; (cmmdc_lista '(27 54 81 18))
; (cmmdc_lista '(100 100 100 200))