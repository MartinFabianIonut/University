bits 32 
global start        

extern exit               
import exit msvcrt.dll    

; a - byte, b - word, c - double word, d - qword
; interpretare cu semn
; De facut:           c-(d+a)+(b+c)

segment data use32 class=data
    ; ...
    a db -12h
    b dw 1302h
    c dd 4A56C119h
    d dq 1930AD7EE0012F34h

; CUM ARE IN MEMORIE?
; a: 12h  b: 02h 13h  c: 19h C1h 56h 4Ah  d: 34h 2Fh 01h E0h 7Eh ADh 30h 19h

segment code use32 class=code
    start:
        ; ...
        xor eax, eax ; eax = 0
        xor edx, edx ; edx = 0
        xor ebx, ebx ; ebx = 0
        xor ecx, ecx ; ecx = 0
        
        mov al, byte [a]
        cbw
        cwde
        cdq
        
        mov ebx, dword [d] ; ebx = E0012F34h
        mov ecx, dword [d + 4] ; ecx = 1930AD7Eh
        
        add ebx, eax ; E0012F46
        adc ecx, edx
        
        xor eax, eax
        
        mov eax, dword [c] ; 4A56C119h
        cdq
        
        sub eax, ebx ; 6A5591D3
        sbb edx, ecx ; E6CF5281
        
        mov ebx, eax ; 6A5591D3
        mov ecx, edx ; E6CF5281
        
        xor eax, eax 
        xor edx, edx
        
        mov ax, word [b] ; 1302h
        cwde
        
        add eax, dword [c] ; 4A56D41B
        
        add ebx, eax ; B4AC65EE
        adc ecx, dword 0

        push    dword 0      
        call    [exit]     