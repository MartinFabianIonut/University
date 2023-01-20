bits 32 
global start        


extern exit               
import exit msvcrt.dll   

segment data use32 class=data
    A dd 0x5D2FE78C, 0x9E262130, 0x20C24B0A, 0xD08CC144
    lg EQU ($-A)/4
    NOU TIMES lg dd 0
    
    ; echivalente ale numerelor din hexa - in binar:
    ; 0x5D2FE78C = 0101 1101 0010 1111 1110 0111 1000 1100 b - 19 biti de unu
    ; 0x9E262130 = 1001 1110 0010 0110 0010 0001 0011 0000 - 12 biti de unu
    ; 0x20C24B0A= 0010 0000 1100 0010 0100 1011 0000 1010 b - 10 biti de unu
    ; 0xD08CC144 = 1101 0000 1000 1100 1100 0001 0100 0100 b - 11 biti de unu
    
    ;Dandu-se un sir de dublucuvinte, sa se obtina un alt sir de dublucuvinte in care se vor 
    ;pastra doar dublucuvintele din primul sir care au un numar par de biti cu valoare 1.

segment code use32 class=code
    start:
        
        mov ECX, lg
        cld ; DF = 0, adica mergem de la stanga la dreapta
        
        mov ESI, A
        mov EDI, NOU
        
    REPETA:
        lodsd
        mov EBP, EAX
        mov BH, 0 
        mov BL, 32
        mov DH, 0 ; aici voi pune numarul de biti de unu
        
        clc ; CF = 0
        
        NR_BITI_DE_UNU:
            
            shl EBP, 00000001b ; folosesc constanta, pt ca in ECX am nr de pasi
            adc DH, 0
            inc BH
            cmp BH, BL
        JB NR_BITI_DE_UNU
        
        test DH, 1 ; verific daca e numar par sau impar in DH --- pt par ZF = 1, pt impar ZF = 0
        JNZ OMITE ; daca ZF=0, deci nr impar, omit
        
        ADAUGA:
            stosd 
        
        OMITE:
    LOOP REPETA
    
            
        push    dword 0     
        call    [exit]      