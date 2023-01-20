bits 32 
global start        

; declare external functions needed by our program
extern exit, printf, scanf               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
import printf msvcrt.dll
import scanf msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions


segment data use32 class=data
    ; ...
    a dd 0
    b dd 0
    suma dd 0
    mesaj_citire_a db "Introdu numarul a = ", 0
    mesaj_citire_b db "Introdu numarul b = ", 0
    format_citire db "%d", 0
    format_afisare db "Suma a+b este egala cu numarul %X in baza 16.", 0

    
;Sa se citeasca de la tastatura doua numere a si b (in baza 10) si sa se calculeze a+b. Sa se afiseze rezultatul adunarii in baza 16
    
segment code use32 class=code
    start:
        ; ...
        ; int printf(const char * format, variabila_1, constanta_2, ...);
        push dword mesaj_citire_a
        call [printf]
        add esp, 4
        
        ; int scanf(const char * format, adresa_variabila_1, ...);
        
        push dword a
        push dword format_citire
        call [scanf]
        add esp, 4*2
        
        
        push dword mesaj_citire_b
        call [printf]
        add esp, 4
        
        push dword b
        push dword format_citire
        call [scanf]
        add esp, 4*2
        
        mov eax, [a]
        mov ebx, [b]
        
        mov [suma], eax
        add [suma], ebx
        
        push dword [suma]
        push dword format_afisare
        call [printf]
        add esp, 4*2
        
   
        push    dword 0     
        call    [exit]   