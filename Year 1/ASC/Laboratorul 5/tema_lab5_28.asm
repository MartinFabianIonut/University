bits 32

global start        

extern exit
import exit msvcrt.dll    

segment data use32 class=data
    
    S1 db '+', '4', '2', 'a', '8', '4', 'X', '5'
    lgS1 EQU ($-S1+2)/3
    S2 db 'a', '4', '5'
    lgS2 EQU $-S2
    lungime EQU lgS1+lgS2
    D TIMES lungime db 0

    ;Se dau doua siruri de caractere S1 si S2. Sa se construiasca sirul D prin concatenarea elementelor de pe pozitiile multiplu de 3 din sirul S1 cu elementele sirului S2 in ordine inversa
    
segment code use32 class=code
    start:
        
        mov ECX, lgS1
        mov ESI, 0
        
    REPETA:
        mov AL, [S1+ESI*3]
        mov [D+ESI], AL
        inc ESI
    LOOP REPETA
        
        mov ECX, lgS2
        
    REPETA2:
        mov AL, [S2+ECX-1]
        mov [D+ESI], AL
        inc ESI
    LOOP REPETA2
        
        push    dword 0
        call    [exit]