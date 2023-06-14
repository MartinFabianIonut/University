bits 32 
global start        

extern exit               
import exit msvcrt.dll    

; a - doubleword; b,c,d - byte; x - qword
;Interpretare cu semn
; De facut:            a-(7+x)/(b*b-c/d+2)

segment data use32 class=data
    ; ...
    a dd 8
    b db -2
    c db 1
    d db -1
    x dq 9
    
; CUM ARE IN MEMORIE?
; a: 08h 00h 00h 00h b: FEh c: 01h d: FFh x: 09h 00h 00h 00h 00h 00h 00h 00h

segment code use32 class=code
    start:
        ; ...
        
        xor eax, eax ; eax = 0
        xor edx, edx ; edx = 0
        xor ebx, ebx ; ebx = 0
        xor ecx, ecx ; ecx = 0
        
        mov al, byte [b]
        imul byte [b] ; ax = b*b = 4h
        
        mov bx, ax ; bx = 4h
        
        xor ax, ax ; ax = 0h
        
        mov al, byte [c] ; al = 1h
        cbw
        idiv byte [d] ; al = FFh
        
        cbw ; ax = FFFFh
        
        mov cx, ax ; cx = FFFFh
        
        sub bx, cx ; bx = 4 - FFFF = 0005h
        add bx, 2 ; bx = 07 ---> ebx = 7h
        
        mov eax, dword [x] ; eax = 0000 0009h
        mov edx, dword [x + 4] ; edx = 0h
        
        add eax, 7 ; eax = 9+7 = 16(10) = 10h
        adc edx, dword 0 ; edx = 0h
        
        idiv ebx ; eax = cat = 2h, edx = rest = 2h
        cdq ; eax = 2h, edx = 0h
        
        mov ebx, eax ; ebx = 2h
        mov ecx, edx ; ecx = 0h
        
        xor eax, eax
        xor edx, edx
        
        mov eax, dword [a] ; eax = 8h
        
        sub eax, ebx ; eax = 6h ---> rezultatul
        sbb edx, dword 0 ; edx = 0
    
        
        push    dword 0      
        call    [exit]       