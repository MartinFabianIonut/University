bits 32 
global start  

;de facut: (a-b-c)+(a-c-d-d), unde a,b,c,d - words       


extern exit               
import exit msvcrt.dll    

segment data use32 class=data
    a dw 72
    b dw 0x1A
    c dw 10
    d dw 4

segment code use32 class=code
    start:
        mov ax, [a]
        sub ax, [b]
        sub ax, [c]
        mov bx, [a]
        sub bx, [c]
        sub bx, [d]
        sub bx, [d]
        add ax, bx
    
        push    dword 0   
        call    [exit]      
