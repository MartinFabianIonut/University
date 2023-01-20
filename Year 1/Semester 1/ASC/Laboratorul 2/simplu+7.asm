bits 32 
global start        

extern exit               
import exit msvcrt.dll    

segment data use32 class=data
    a dd 256
    unu dw 1

segment code use32 class=code
    start:
        xor eax, eax
        xor edx, edx

        push dword [a]
        pop ax
        pop dx

        div word [unu]
    
        push    dword 0      
        call    [exit]