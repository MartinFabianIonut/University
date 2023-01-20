bits 32 
global start        

extern exit               
import exit msvcrt.dll   

; a - doubleword; b,c,d - byte; x - qword
;Interpretare cu semn
; De facut:            a-(7+x)/(b*b-c/d+2)

segment data use32 class=data
    ; ...
    a dd 8h
    ;b db 34h
    ;c db 5Ah
    ;d db 18h
    ;x dq 1930AD7EE0012F34h
    b db -1h
    c db 1h
    d db -1h
    x dq 5h

segment code use32 class=code
    start:
        ; ...
        
        xor eax, eax ; eax = 0
        xor edx, edx ; edx = 0
        xor ebx, ebx ; ebx = 0
        xor ecx, ecx ; ecx = 0
        
        mov al, byte [b]
        imul byte [b] ; ax = b*b
        
        mov bx, ax
        
        xor ax, ax
        
        mov al, byte [c]
        cbw
        idiv byte [d]
        
        mov cl, al
        
        sub bx, cx
        add bx, 2
        
        mov eax, dword [x] ; ebx = E0012F34h
        mov edx, dword [x + 4] ; ecx = 1930AD7Eh
        
        add eax, 7
        adc edx, 0
        
        idiv ebx
        
        mov ebx, eax
        mov ecx, edx
        
        xor eax, eax
        xor edx, edx
        
        mov eax, dword [a]
        
        sub eax, ebx
        sbb edx, ecx
    

        push    dword 0      
        call    [exit] 