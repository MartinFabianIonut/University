bits 32 

global start        

extern exit, in_baza_16_si_2               
import exit msvcrt.dll    

segment data use32 class=data public
    sir dd 11,172,999,27,3723,10201,521
    lungime_sir dd ($-sir)/4
    
; Se da un sir de numere. Sa se afiseze valorile in baza 16 si in baza 2.
segment code use32 class=code public 
    start:
        xor ecx, ecx
        mov ecx, [lungime_sir]
        mov esi, sir
        cld
        
        un_for:
        
            push ecx
            
            lodsd ; pune in eax dublucuvantul de la esi (se va face si incrementarea esi corespunzatoare parcurgerii sirului)
            push esi
            
            push dword eax
            call in_baza_16_si_2
            add esp, 4
            
            pop esi
            pop ecx 
            
        loop un_for
        
        
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
