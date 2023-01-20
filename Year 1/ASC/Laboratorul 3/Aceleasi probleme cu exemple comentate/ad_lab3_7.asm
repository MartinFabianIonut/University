bits 32 
global start        

extern exit              
import exit msvcrt.dll    

;a - byte, b - word, c - double word, d - qword 
;interpretare fara semn
;de facut:              c-(d+d+d)+(a-b)     

segment data use32 class=data
    a db 4
    b dw 6
    c dd 48
    d dq 12
    
; CUM ARE IN MEMORIE?
; a: 04h  b: 06h 00h  c: 30h 00h 00h 00h  d: 0Ch 00h 00h 00h 00h 00h 00h 00h

segment code use32 class=code
    start:
        ; ...
        xor eax, eax ; eax = 0
        xor edx, edx ; edx = 0
        xor ebx, ebx ; ebx = 0
        xor ecx, ecx ; ecx = 0
        
        mov eax, dword[c] ; eax = c = 30h
        
        mov ebx, dword[d] ; ebx = C
        mov ecx, dword[d + 4] ; ecx = 0

        add ebx, dword[d] ; ebx = C + C = 18h , iar CF = 0
        adc ecx, dword[d + 4] ; ecx = 0

        add ebx, dword[d] ; ebx = 18 + C = 24h , iar CF = 0
        adc ecx, dword[d + 4] ; ecx = 0
        
        sub eax, ebx ; eax = eax - ebx = 30 - 24 = C , iar CF = 0
        sbb edx, ecx ; edx = 0
        
        xor ebx, ebx ; ebx = 0
        xor ecx, ecx ; ecx = 0
        
        mov bl, byte [a] ; bl = a = 4
        mov cx, word [b] ; cx = b = 6
        
        sub bl, cl ; bl = bl - cl = 4 - 6 = FE , iar CF = 1
        sbb bh, ch ; bh = bh - ch - CF = FF (CF ramane 1)
        ; bx = FFFEh , iar ebx = 0000FFFEh
        
        add eax, ebx ; eax = eax + ebx = 0000000Ch + 0000FFFEh = 0001000Ah, iar CF = 0
        adc edx, dword 0 ; edx = edx + 0 + CF = 0
        
        ;rezultatul e in eax ---> 0001000Ah
        
        push    dword 0      
        call    [exit]    