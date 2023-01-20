bits 32
global start        

extern exit               
import exit msvcrt.dll   

; a - doubleword; b,c,d - byte; x - qword
;Interpretare fara semn
; De facut:            a-(7+x)/(b*b-c/d+2)

segment data use32 class=data
    ; ...
    a dd 15
    b db 3
    c db 5
    d db 2
    x dq 5

; CUM ARE IN MEMORIE?
; a: 0Fh 00h 00h 00h b: 03h c: 05h d: 02h x: 05h 00h 00h 00h 00h 00h 00h 00h

segment code use32 class=code
    start:
        ; ...
        
        xor eax, eax ; eax = 0
        xor edx, edx ; edx = 0
        xor ebx, ebx ; ebx = 0
        xor ecx, ecx ; ecx = 0
        
        mov al, byte [b] ; al = 3h
        mul byte [b] ; ax = b*b = 9h
        
        mov bx, ax ; bx = ax = 9h
        
        xor ax, ax ; ax = 0
        
        mov al, byte [c] ; al = c = 5h
        div byte [d] ; ah = 1h, al = c/d = 2h
        
        mov cl, al ; cl = al = 2h
        
        sub bx, cx ; bx = bx - cx = b*b-c/d = 7h
        add bx, 2 ; bx = bx + 2 = b*b-c/d+2 = 9h
        
        mov eax, dword [x] ; eax = 5h
        mov edx, dword [x + 4] ; edx = 0h
        
        add eax, 7 ; eax = eax + 7 = Ch
        adc edx, dword 0 ; edx = 0
        
        div ebx ; eax = cat = 1h, edx = rest = 3h
        
        mov ebx, eax ; ebx = 1h
        
        xor eax, eax
        xor edx, edx
        
        mov eax, dword [a] ; eax = Fh
        
        sub eax, ebx ; eax = eax - ebx = Fh - 1h = Eh
        sbb edx, dword 0 ; edx = 0
        
       
        push    dword 0     
        call    [exit] 