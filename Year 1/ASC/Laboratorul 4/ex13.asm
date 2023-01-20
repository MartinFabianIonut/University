bits 32 
global start        

extern exit               
import exit msvcrt.dll   

; Dandu-se 4 octeti, sa se obtina in AX suma numerelor intregi reprezentate de bitii 4-6 ai celor 4 octeti. 

segment data use32 class=data
    ; ...
    a db 11010110b ; de aici iau 101
    b db 00011100b ; de aici iau 001
    c db 01110001b ; de aici iau 111
    d db 10111001b ; de aici iau 011 
    s db 0xD6,1Ch,71h,0xB9

segment code use32 class=code
    start:
        ; ...
        xor eax,eax
        xor ebx,ebx
        xor ecx,ecx ; le fac 0 ca sa le vad mai frumos in debuger
        
        mov bl, byte [a]
        and bl, 01110000b ; pastrez doar bitii 4-6
        mov cl, 4 
        shr bl, cl ; adica fac shiftare cu 4 pozitii spre dreapta, primul bit va fi 0, urmat de cei 3 cautati
        
        mov bh, 0
        add ax, bx ; ax = 00000101b = 5h
        
        mov bl, byte [b]
        and bl, 01110000b ; pastrez doar bitii 4-6
        mov cl, 4 
        shr bl, cl ; adica fac shiftare cu 4 pozitii spre dreapta
        
        mov bh, 0
        add ax, bx ; ax = 00000110b = 6h
        
        mov bl, byte [c]
        and bl, 01110000b ; pastrez doar bitii 4-6
        mov cl, 4 
        shr bl, cl ; adica fac shiftare cu 4 pozitii spre dreapta
        
        mov bh, 0
        add ax, bx ; ax = 00001101b = Dh
        
        mov bl, byte [d]
        and bl, 01110000b ; pastrez doar bitii 4-6
        mov cl, 4 
        shr bl, cl ; adica fac shiftare cu 4 pozitii spre dreapta
        
        mov bh, 0
        add ax, bx  ; ax = 00010000b = 10h
        
        mov ax, 0
        mov esi,0
        
    Repeta:
        mov bl, byte [s+esi]
        and bl, 01110000b ; pastrez doar bitii 4-6
        mov cl, 4 
        shr bl, cl ; adica fac shiftare cu 4 pozitii spre dreapta, primul bit va fi 0, urmat de cei 3 cautati
        mov bh, 0
        add ax, bx
        inc esi
        cmp esi, 4
    jb Repeta
    

        push    dword 0    
        call    [exit]      
