bits 32 
global start        

extern exit               
import exit msvcrt.dll    

; a - doubleword; b,c,d - byte; x - qword
;Interpretare fara semn
; De facut:            a-(7+x)/(b*b-c/d+2)

segment data use32 class=data
    ; ...
    a dd 4A56C119h
    ;b db 34h
    ;c db 5Ah
    ;d db 18h
    ;x dq 1930AD7EE0012F34h
    b db 1h
    c db 1h
    d db 1h
    x dq 5h

segment code use32 class=code
    start:
        ; ...
        
        xor eax, eax ; eax = 0
        xor edx, edx ; edx = 0
        xor ebx, ebx ; ebx = 0
        xor ecx, ecx ; ecx = 0
        
        mov al, byte [b]
        mul byte [b] ; ax = b*b = 0A90h
        
        mov bx, ax ; bx = ax = 0A90h
        
        xor ax, ax ; ax = 0
        
        mov al, byte [c] ; al = c = 5Ah
        div byte [d] ; ah = 12h, al = c/d = 03h
        
        mov cl, al ; cl = al = 03h
        
        sub bx, cx ; bx = bx - cx = b*b-c/d = 0A8Dh
        add bx, 2 ; bx = bx + 2 = b*b-c/d+2 = 0A8Fh
        
        mov eax, dword [x] ; eax = E0012F34h
        mov edx, dword [x + 4] ; edx = 1930AD7Eh
        
        add eax, 7 ; eax = eax + 7 = E0012F3Bh
        adc edx, 0
        
        div ebx
        
        mov ebx, eax
        mov ecx, edx
        
        xor eax, eax
        xor edx, edx
        
        mov eax, dword [a]
        
        sub eax, ebx
        sbb edx, ecx
        
   
        push    dword 0      
        call    [exit]   