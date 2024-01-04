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

push dword a 
push dword format_citire 
call [scanf] 
add esp, 8 



mov eax, [a] 
imul eax, 3 
mov [temp2], eax 


mov eax, [temp2] 
add eax, 21 
mov [temp3], eax 

mov eax, [temp3]
mov [b],eax
push dword format_afisare_temp4 
call [printf] 
add esp, 4 

mov eax, [b] 
push dword eax 
push dword format_afisare_b 
call [printf] 
add esp, 8 

push dword format_afisare_endl 
call [printf] 
add esp, 4 



mov eax, [b] 
mov ebx, [a] 
xor edx, edx 
div ebx 
mov [temp5], eax 

mov eax, [temp5]
mov [c],eax
push dword format_afisare_temp6 
call [printf] 
add esp, 4 

mov eax, [c] 
push dword eax 
push dword format_afisare_c 
call [printf] 
add esp, 8 

push dword format_afisare_endl 
call [printf] 
add esp, 4 



mov eax, [a] 
mov ebx, [c] 
xor edx, edx 
div ebx 
mov [temp7], edx 



mov eax, [b] 
mov ebx, 2 
xor edx, edx 
div ebx 
mov [temp8], eax 


mov eax, [temp8] 
imul eax, 3 
mov [temp9], eax 

mov eax, [temp7] 
add eax, [temp9] 
mov [temp10], eax 


mov eax, [temp10] 
add eax, 2 
mov [temp11], eax 

mov eax, [temp11]
mov [d],eax
push dword format_afisare_temp12 
call [printf] 
add esp, 4 

mov eax, [d] 
push dword eax 
push dword format_afisare_d 
call [printf] 
add esp, 8 

push dword format_afisare_endl 
call [printf] 
add esp, 4 



mov eax, [d] 
add eax, [b] 
mov [temp13], eax 


mov eax, [temp13] 
imul eax, [c] 
mov [temp14], eax 

mov eax, [temp14]
mov [a],eax
push dword format_afisare_temp15 
call [printf] 
add esp, 4 

mov eax, [a] 
push dword eax 
push dword format_afisare_a 
call [printf] 
add esp, 8 

push dword format_afisare_endl 
call [printf] 
add esp, 4 

push dword 0
call [exit]

segment data use32 class=data
	d dd 0
	c dd 0
	b dd 0
	a dd 0
	temp2 dd 0
	temp3 dd 0
	temp5 dd 0
	temp7 dd 0
	temp8 dd 0
	temp9 dd 0
	temp10 dd 0
	temp11 dd 0
	temp13 dd 0
	temp14 dd 0
	format_citire db "%d", 0
	format_afisare_b db "b = %d", 0
	format_afisare_c db "c = %d", 0
	format_afisare_d db "d = %d", 0
	format_afisare_a db "a = %d", 0
	format_afisare_temp1 db "Introduceti un numar a = ", 0
	format_afisare_temp4 db "Am calculat b = a * 3 + 21, iar rezultatul este: ", 0
	format_afisare_temp6 db "Am calculat c = b / a, iar rezultatul este: ", 0
	format_afisare_temp12 db "Am calculat d = a [mod] c + b / 2 * 3 + 2, iar rezultatul este: ", 0
	format_afisare_temp15 db "Am calculat a = (d + b) * c, iar rezultatul este: ", 0
	format_afisare_endl db "", 0Dh, 0Ah, 0
