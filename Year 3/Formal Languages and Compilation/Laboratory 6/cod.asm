bits 32
global start

extern exit, printf, scanf
import exit msvcrt.dll
import printf msvcrt.dll
import scanf msvcrt.dll

segment code use32 class=code
	start:bits 32

global start

push dword a 
push dword format_citire 
call [scanf] 
add esp, 8 



mov eax, [a] 
imul eax, 3 
mov [temp1], eax 


mov eax, [temp1] 
add eax, 21 
mov [temp2], eax 

mov eax, [temp2]
mov [b],eax


mov eax, [b] 
mov ebx, [a] 
xor edx, edx 
div ebx 
mov [temp3], eax 

mov eax, [temp3]
mov [c],eax


mov eax, [a] 
mov ebx, [c] 
xor edx, edx 
div ebx 
mov [temp4], edx 


mov eax, [temp4] 
add eax, [b] 
mov [temp5], eax 



mov eax, 2 
imul eax, 3 
mov [temp6], eax 

mov eax, [temp5] 
mov ebx, [temp6] 
xor edx, edx 
div ebx 
mov [temp7], eax 

mov eax, [temp7]
mov [d],eax
mov eax, [c] 
push dword eax 
push dword format_afisare_c 
call [printf] 
add esp, 8 

mov eax, [d] 
push dword eax 
push dword format_afisare_d 
call [printf] 
add esp, 8 

push dword 0
call [exit]

segment data use32 class=data
	d dd 0
	c dd 0
	b dd 0
	a dd 0
	temp1 dd 0
	temp2 dd 0
	temp3 dd 0
	temp4 dd 0
	temp5 dd 0
	temp6 dd 0
	temp7 dd 0
	format_citire db "%d", 0
	format_afisare_c db "c = %d", 0
	format_afisare_d db "d = %d", 0
