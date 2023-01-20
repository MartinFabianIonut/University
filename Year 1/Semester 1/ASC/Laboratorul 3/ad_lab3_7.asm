bits 32 
global start        

extern exit               
import exit msvcrt.dll    

;a - byte, b - word, c - double word, d - qword 
;interpretare fara semn
;de facut:              c-(d+d+d)+(a-b)     

segment data use32 class=data
    a db 12h
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
        
        mov eax, dword[c] ; eax = c = 4A56C119h
        
        mov ebx, dword[d] ; ebx = E0012F34h
        mov ecx, dword[d + 4] ; ecx = 1930AD7Eh

        add ebx, dword[d] ; ebx = E0012F34h + E0012F34h = C0025E68h , iar CF=1
        adc ecx, dword[d + 4] ; ecx = 1930AD7Eh + 1930AD7Eh + CF = 32615AFDh , iar CF = 0

        add ebx, dword[d] ; ebx = C0025E68h + E0012F34h = A0038D9Ch , iar CF = 1
        adc ecx, dword[d + 4] ; ecx = 32615AFDh + 1930AD7Eh + CF = 4B92087Ch , iar CF = 0
        
        sub eax, ebx ; eax = eax - ebx = 4A56C119h - A0038D9Ch = AA53337Dh, iar CF = 1
        sbb edx, ecx ; edx = edx - ecx - CF = 0 - 4B92087Ch - 1 = B46DF783h
        
        xor ebx, ebx ; ebx = 0
        xor ecx, ecx ; ecx = 0
        
        mov bl, byte [a] ; bl = a = 12h
        mov cx, word [b] ; cx = b = 1302h
        
        sub bl, cl ; bl = bl - cl = 12h - 02h = 10h , iar CF = 0
        sbb bh, ch ; bh = bh - ch - CF = EDh
        ; bx = ED10h , iar ebx = 0000ED10h
        
        add eax, ebx ; eax = eax + ebx = AA53337Dh + 0000ED10h = AA54208Dh, iar CF = 0
        adc edx, dword 0 ; edx = edx + 0 + CF = edx = B46DF783h
        
    
        push    dword 0     
        call    [exit]       