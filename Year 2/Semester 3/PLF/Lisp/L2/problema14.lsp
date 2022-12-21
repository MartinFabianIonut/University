; (load "l2.lsp")

; Problema 14. 
; Sa se construiasca lista nodurilor unui arbore de tipul (2) parcurs in postordine.

; Modelul matematic:
;                   postordine(l1 l2 l3) = {
;                                          [], l=[]
;                                          postordine(l2) + postordine(l3) + l1, altfel
;                                          }
; postordine(L: lista eterogena)
; l1 - nod 
; l2 - lista din stanga (aborele din stanga nodului l1)
; l3 - arborele din dreapta nodului l1, lista
; Functia returneaza lista nodurilor arborelui parcurs in postordine SDR


(defun postordine(L)
    (cond
        ((null L) nil)
        (t (append (postordine (cadr L)) (postordine (caddr L)) (list(car L))))
    )
)

; Cazuri de testare:
; (postordine '())
; (postordine '(A))
; (postordine '(A (B) (C (D) (E))))
; (postordine '(A (B (C) (D () (E))) (F () (G () (H (I (K)) (J))))))




; Pentru mine:

(defun preordine(L)
    (cond
        ((null L) nil)
        (t (append (list(car L)) (preordine (cadr L)) (preordine (caddr L)) ))
    )
)

(defun inordine(L)
    (cond
        ((null L) nil)
        (t (append (inordine (cadr L)) (list (car L)) (inordine (caddr L)) ))
    )
)