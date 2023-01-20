bits 32
global start        

extern exit              
import exit msvcrt.dll    

; a - byte, b - word, c - double word, d - qword
; interpretare cu semn
; De facut:           c-(d+a)+(b+c)

segment data use32 class=data
    ; ...
    a db 4
    b dw -15
    c dd 22
    d dq 12

; CUM ARE IN MEMORIE?
; a: 04h  b: F1h FFh  c: 16h 00h 00h 00h  d: 0Ch 00h 00h 00h 00h 00h 00h 00h

segment code use32 class=code
    start:
        ; ...
        xor eax, eax ; eax = 0
        xor edx, edx ; edx = 0
        xor ebx, ebx ; ebx = 0
        xor ecx, ecx ; ecx = 0
        
        mov al, byte [a] ; al = 4h
        cbw ; ax = al
        cwde ; eax = ax
        cdq ; edx:eax = eax
        
        mov ebx, dword [d] ; ebx = Ch
        mov ecx, dword [d + 4] ; ecx = 0h
        
        add ebx, eax ; ebx = 10h
        adc ecx, edx ; 0
        
        xor eax, eax
        
        mov eax, dword [c] ; eax = 16h
        cdq
        
        sub eax, ebx ; eax = 6h
        sbb edx, ecx ; edx = 0h
        ;
        
        mov ebx, eax ; ebx = 6h
        mov ecx, edx ; 0h
        
        xor eax, eax 
        xor edx, edx 
        
        mov ax, word [b] ; ax = FFF1h
        cwde ; eax = FFFFFFF1h
        
        add eax, dword [c] ; eax = FFFF FFF1h + 16h = 7h (CF=1) (1 0000 007)
        
        add ebx, eax ; ebx = 6 + 7 = Dh ---> acesta e rezultatul
        adc ecx, dword 0
        
    
        push    dword 0     
        call    [exit]  