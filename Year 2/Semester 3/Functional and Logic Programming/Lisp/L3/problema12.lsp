; 12. Definiti o functie care inlocuieste un nod cu altul intr-un arbore n-ar
; reprezentat sub forma (radacina lista_noduri_subarb1...lista_noduri_subarbn)
; Ex: arborelele este (a (b (c)) (d) (e (f))) si nodul 'b se inlocuieste cu
; nodul 'g => arborele (a (g (c)) (d) (e (f)))


; Modelul matematic:
;                   inlocuire(Radacina Sub1...Subn  DeInlocuit Inlocuitor) = {
;                                                                             Inlocuitor, Radacina = DeInlocuit si n = 1
;                                                                             Radacina, Radacina != DeInlocuit si n = 1
;                                                                             inlocuire(RadacinaluiSub1 DeInlocuit Inlocuitor) + inlocuire(RadacinaluiSub2 DeInlocuit Inlocuitor) + ... +inlocuire(RadacinaluiSubn DeInlocuit Inlocuitor)  , (Radacina Sub1...Subn) e lista
;                                                                             }

; inlocuiere(Arbore: lista eterogena, DeInlocuit: atom, Inlocuitor: atom)
; Radacina Sub1...Subn - forma de memorare a arborelui n-ar Arbore, cu radacina, urmata de lista tuturor subarborilor sai
; DeInlocuit - ce dorim sa inlocuim in Abore
; Inlocuitor - cu ce dorim sa inlocuim in Abore pe DeInlocuit
; Functia inlocuieste aparitia lui DeInlocuit in Abore cu Inlocuitor (!toate aparitiile, daca sunt mai multe)


(defun inlocuire (Arbore DeInlocuit Inlocuitor)
    (cond
        ( 
            (and (atom Arbore) ;am ajuns la un nod de tip radacina
            (eq Arbore DeInlocuit) ) Inlocuitor ; si returnez Inlocuitor daca Radacina subarborelui este egala cu DeInlocuit
        )  

        (
            (atom Arbore) Arbore ; altfel returnez Arbore (numar)     
        )
 
        ( (listp Arbore) ;daca este subarbore (lista)
          (mapcar #'(lambda (A) (inlocuire A DeInlocuit Inlocuitor)) Arbore) ; merg cu functia anonima lambda pe fiecare dintre 
          ; subarbori -> lista_noduri_subarb1...lista_noduri_subarbn, datorita mapcar
        
        ) 
    )
)

; Cazuri de testare:
; (inlocuire '(A (B) (C)) 'R 'T )
; (inlocuire '(A (B) (C)) 'C 'T )
; (inlocuire '(A (B (D) (C)) (C)) 'C 'T )
; (inlocuire '(A (B (E) (F) (G) (H)) (C) (D (J))) 'H 'X)
; (inlocuire '(1 (2 (5) (6)) (3 (7) (8 (9) (10) (11) (12))) (4)) '11 '24)
 