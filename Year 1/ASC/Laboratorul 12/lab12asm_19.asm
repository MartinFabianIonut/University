bits 32

global _siruri

extern _afisare

segment data public data use32
    adresaSir dd 0
    lungimeSir dd 0
    lungimeSirNegative dd 0
    lungimeSirPozitive dd 0
    N times 100 dd 0
    P times 100 dd 0
    mesaj_n db "Sirul numerelor negative: ",0
    mesaj_p db "Sirul numerelor pozitive: ",0


segment code public code use32
; void siruri(int sir[100], int dimensiune)

_siruri:
    ; creare cadru de stiva pentru programul apelat
    push ebp
    mov ebp, esp
    
    sub esp,4*204 ; alocare de spatiu pentru variabilele locale
    
    mov eax, [ebp + 8]  ; eax <- adresa sirului de numere 
    mov [adresaSir],eax
    
    mov ecx, [ebp + 12] ; ecx <- dimensiune sir
    mov [lungimeSir],ecx
    
    xor ebx, ebx ; folosit pentru pozitia numerelor negative
    xor edx, edx ; folosit pentru pozitia numerelor pozitive
    
    mov esi, [adresaSir]
    cld
    parcurgere:
        lodsd
        cmp eax,dword 0
        jge pozitive
        
        negative:
            mov [N + ebx*4],eax
            inc ebx
            jmp mai_departe
        
        pozitive:
            mov [P + edx*4],eax
            inc edx
        
        mai_departe:
            sub dword [lungimeSir],1
            cmp dword [lungimeSir],0
            ja parcurgere
           
        
    continua:
    ;void afisare(char mesaj[],int un_sir[], int dim)
    
    mov dword [lungimeSirNegative],ebx
    mov dword [lungimeSirPozitive],edx
    
    push dword [lungimeSirNegative]
    push dword N
    push dword mesaj_n
    call _afisare
    add esp, 4*3
    
    push dword [lungimeSirPozitive]
    push dword P
    push dword mesaj_p
    call _afisare
    add esp, 4*3
    
    add esp, 4*204
    
    ; refacem cadrul de stiva pentru programul apelant
    mov esp, ebp
    pop ebp

    ret