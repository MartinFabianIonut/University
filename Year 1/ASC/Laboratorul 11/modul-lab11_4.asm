bits 32 

global in_baza_16_si_2        

extern printf    
import printf msvcrt.dll  

segment data use32 class=data public
    ; ...
    format_16 db "Numarul %u are echivalentul %X in baza 16, respectiv ",0
    format_2 db "%d",0
    format_22 db "%d ceva",0
    mesaj db " in baza 2.",10,13,0
    baza_doi resd 32
    ceva dd 0

segment code use32 class=code public
    in_baza_16_si_2:
        
        mov edx, [esp+4]
        
        push dword edx
        push dword edx
        push dword format_16
        call [printf]
        add esp, 4*3
        
        mov edx, [esp+4]
        mov eax, edx
        xor edx, edx
        ;edx:eax - numarul 
        
        mov ecx, 32
        frr:
            mov dword [baza_doi+ecx-1],0; zerorizarea pt fiecare afisare
        loop frr
        
        mov esi, 31
        un_fel: 
            xor edx,edx
            
            mov ebx, 2
            div ebx
            
            
            mov dword [baza_doi+esi*4],edx
            dec esi
            
            xor edx,edx
            
            cmp eax,0 ; catul
            je afara
            
            jmp un_fel
            
        afara:
        
        
        mov ecx, 32
        
        mov ebx,-1
        afisare:
            push ecx
            
            inc ebx
            push ebx
            
            push dword [baza_doi + ebx*4]
            push dword format_2
            call [printf]
            add esp, 4*2
            
            pop ebx
            pop ecx
        loop afisare
        
        push dword mesaj
        call [printf]
        add esp, 4
        
        ret