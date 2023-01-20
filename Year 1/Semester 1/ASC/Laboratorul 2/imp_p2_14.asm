bits 32 
global start      

; de facut: a*d*e/(f-5), unde a,d - bytes, iar e,f - words  

extern exit               
import exit msvcrt.dll    

segment data use32 class=data
    a db 2
    d db 3
    e dw 12
    f dw 10

segment code use32 class=code
    start:
        mov al, [a]
        mul byte [d] ; ax=a*d
        mul word [e] ; dx:ax=a*d*e
        
        mov bx, [f] ; bx=f=10
        sub bx, 5 ; bx=f-5=5
        
        mov [f], bx ; f=5
        
        div word [f] ; ax=dx:ax/f=14   dx=dx:ax%f=2
    
        push    dword 0      
        call    [exit]     