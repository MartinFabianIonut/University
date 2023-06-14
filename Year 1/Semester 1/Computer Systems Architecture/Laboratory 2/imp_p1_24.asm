bits 32 
global start    

; de facut: (10*a-5*b)+(d-5*c), unde a,b,c - bytes si d - word    

extern exit               
import exit msvcrt.dll    

segment data use32 class=data
    a db 5
    b db 2
    c db 4
    d dw 20
    
segment code use32 class=code
    start:
        
        mov al, 5 ; al=5
        mul byte [b] ; ax=al*b=10
        
        mov bx, ax ; bx=ax=10
        
        mov al, 10 ; al=10
        mul byte [a] ; ax=al*a=50
        
        sub ax, bx ; ax=ax-bx=50-10=40
        
        mov cx, ax ; cx=ax=40
        
        mov bx, [d] ; bx=d=20
        mov al, 5 ; al = 5
        mul byte [c] ; ax=al*c=5*4=20
        
        sub bx, ax ; bx=bx-ax=20-20=0
        
        add cx, bx ; cx=cx+bx=40
        
        mov ax, cx ; ax=40
    
        push    dword 0    
        call    [exit]    