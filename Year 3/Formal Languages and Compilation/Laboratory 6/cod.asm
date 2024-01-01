bits 32
global start

extern exit, printf, scanf
import exit msvcrt.dll
import printf msvcrt.dll
import scanf msvcrt.dll

segment code use32 class=code
	start:bits 32

global start

push dword format_afisare_temp1 
call [printf] 
add esp, 4 

push dword mama 
push dword format_citire 
call [scanf] 
add esp, 8 

push dword format_afisare_temp2 
call [printf] 
add esp, 4 

push dword tata 
push dword format_citire 
call [scanf] 
add esp, 8 

push dword format_afisare_temp3 
call [printf] 
add esp, 4 

push dword copil1 
push dword format_citire 
call [scanf] 
add esp, 8 

push dword format_afisare_temp4 
call [printf] 
add esp, 4 

push dword copil2 
push dword format_citire 
call [scanf] 
add esp, 8 



mov eax, [mama] 
add eax, [tata] 
mov [temp5], eax 


mov eax, [temp5] 
add eax, [copil1] 
mov [temp6], eax 


mov eax, [temp6] 
add eax, [copil2] 
mov [temp7], eax 


mov eax, [temp7] 
mov ebx, 4 
xor edx, edx 
div ebx 
mov [temp8], eax 

mov eax, [temp8]
mov [medie],eax
push dword format_afisare_temp9 
call [printf] 
add esp, 4 

mov eax, [medie] 
push dword eax 
push dword format_afisare_medie 
call [printf] 
add esp, 8 

push dword format_afisare_endl 
call [printf] 
add esp, 4 



mov eax, [copil1] 
imul eax, 100 
mov [temp10], eax 


mov eax, [temp10] 
mov ebx, [mama] 
xor edx, edx 
div ebx 
mov [temp11], eax 

mov eax, [temp11]
mov [catLaSuta],eax
push dword format_afisare_temp12 
call [printf] 
add esp, 4 

mov eax, [catLaSuta] 
push dword eax 
push dword format_afisare_catLaSuta 
call [printf] 
add esp, 8 

push dword format_afisare_temp13 
call [printf] 
add esp, 4 

push dword format_afisare_endl 
call [printf] 
add esp, 4 



mov eax, [copil2] 
imul eax, 100 
mov [temp14], eax 


mov eax, [temp14] 
mov ebx, [tata] 
xor edx, edx 
div ebx 
mov [temp15], eax 

mov eax, [temp15]
mov [catLaSuta2],eax
push dword format_afisare_temp16 
call [printf] 
add esp, 4 

mov eax, [catLaSuta2] 
push dword eax 
push dword format_afisare_catLaSuta2 
call [printf] 
add esp, 8 

push dword format_afisare_temp17 
call [printf] 
add esp, 4 

push dword format_afisare_endl 
call [printf] 
add esp, 4 



mov eax, [copil1] 
mov ebx, [copil2] 
xor edx, edx 
div ebx 
mov [temp18], edx 

mov eax, [temp18]
mov [restul],eax
push dword format_afisare_temp19 
call [printf] 
add esp, 4 

mov eax, [restul] 
push dword eax 
push dword format_afisare_restul 
call [printf] 
add esp, 8 

push dword format_afisare_endl 
call [printf] 
add esp, 4 

push dword 0
call [exit]

segment data use32 class=data
	copil2 dd 0
	copil1 dd 0
	tata dd 0
	mama dd 0
	temp5 dd 0
	temp6 dd 0
	temp7 dd 0
	temp8 dd 0
	medie dd 0
	temp10 dd 0
	temp11 dd 0
	catLaSuta dd 0
	temp14 dd 0
	temp15 dd 0
	catLaSuta2 dd 0
	temp18 dd 0
	restul dd 0
	format_citire db "%d", 0
	format_afisare_medie db "medie = %d", 0
	format_afisare_catLaSuta db "catLaSuta = %d", 0
	format_afisare_catLaSuta2 db "catLaSuta2 = %d", 0
	format_afisare_restul db "restul = %d", 0
	format_afisare_temp1 db "Introduceti varsta mamei: ", 0
	format_afisare_temp2 db "Introduceti varsta tatalui: ", 0
	format_afisare_temp3 db "Introduceti varsta primului copil: ", 0
	format_afisare_temp4 db "Introduceti varsta celui de-al doilea copil: ", 0
	format_afisare_temp9 db "Media aritmetica a varstelor este: ", 0
	format_afisare_temp12 db "Primul copil are: ", 0
	format_afisare_temp13 db " la suta din varsta mamei", 0
	format_afisare_temp16 db "Al doilea copil are: ", 0
	format_afisare_temp17 db " la suta din varsta tatalui", 0
	format_afisare_temp19 db "Restul impartirii varstei primului copil la varsta celui de-al doilea copil este: ", 0
	format_afisare_endl db "", 0Dh, 0Ah, 0
