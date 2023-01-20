bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit,fopen,fread,printf,fclose,               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll
import fopen msvcrt.dll 
import fread msvcrt.dll 
import printf msvcrt.dll 
import fclose msvcrt.dll     ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    ; ...
    nume_fisier db "tema_Martin.txt", 0
    mod_acces db "r", 0
    descriptor resd 1
    numar_caractere_citite dd 0
    text resb 101
    cate_caractere_sunt_speciale dd 0
    caractere_normale db "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 0
    e_normal dd 0
    mesaj db "In fisierul dat sunt %d caractere speciale.", 0
    
    
; Se da un fisier text. Sa se citeasca continutul fisierului, sa se contorizeze numarul de caractere speciale si sa se afiseze aceasta valoare. Numele fisierului text este definit in segmentul de date.
; Conventie: orice caracter diferit de litera (mare ori mica) sau cifra este caracter special!!!
    
; our code starts here
segment code use32 class=code
    start:
        ; ...
        ; FILE * fopen(const char* nume_fisier, const char * mod_acces)
        
        push dword mod_acces
        push dword nume_fisier
        call [fopen]
        add esp, 4*2 
        
        cmp eax, 0 ; in eax e 0 daca ceva nu a mers bine, altfel e descriptorul
        je gata
        
        mov [descriptor], eax 
        
        bucla:
            push dword [descriptor]
            push dword 100
            push dword 1
            push dword text
            call [fread]
            add esp, 4*4
            
            ; eax = numar de caractere
            cmp eax, 0          ; daca numarul de caractere citite este 0, am terminat de parcurs fisierul
            je iesi_din_bucla 

            mov [numar_caractere_citite], eax ; salvam numarul de caractere citie
            mov ecx, [numar_caractere_citite] ; il punem in ecx, pentru a putea parcurge intr-un loop acele caractere citite
            
            mov esi, text ; ne vom folosi mai departe de lodsb, deci avem nevoie de esi
            cld
            
            un_for:
                lodsb
                mov bl, al ; salvam in bl, pentru ca trebuie sa verificam daca acel caracter este normal sau special mai jos
                
                mov dword [e_normal], 0 ; presupunem ca avem un caracter curent special
                
                push ecx ; aceasta si urmatoarea linie sunt folosite pentru ca schimbam valorile din ecx si esi, de care avem nevoie pentru un_for
                push esi
                
                mov ecx, 62 ; numarul de caractere normale
                mov esi, caractere_normale
                cld
                
                alt_for: 
                    lodsb
                    cmp al,bl
                    jne mai_departe 
                    mov dword [e_normal],1 ; aceasta linie se executa in cazul in care caracterul curent este normal
                    mai_departe:
                loop alt_for
                
                cmp dword [e_normal],0 ; verificam daca e normal sau special
                jne continua ; daca e normal, nu se intampla nimic
                
                add dword [cate_caractere_sunt_speciale], 1 ; daca e special, adaugam la numarul de caractere speciale 1
                
                continua:
                
                pop esi ; restabilim valorile de dinaintea verificarii caracterului curent ale lui esi si ecx, neapart in aceasta ordine
                pop ecx
                
            loop un_for
        jmp bucla
            
            
        iesi_din_bucla:
        push dword [descriptor]
        call [fclose] ; aici inchidem fisierul deschis doar pentru citire
        add esp, 4
        
        push dword [cate_caractere_sunt_speciale]
        push dword mesaj
        call [printf] ; afisam cate caractere sunt speciale
        add esp, 4*2
        
        gata:
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
