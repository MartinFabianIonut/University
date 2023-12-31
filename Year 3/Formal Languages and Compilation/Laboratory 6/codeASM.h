#ifndef CODEASM_H
#define CODEASM_H

#define ADD_ASM_FORMAT "\
mov eax, %s \n\
add eax, %s \n\
mov %s, eax \n\
"

#define MUL_ASM_FORMAT "\
mov eax, %s \n\
imul eax, %s \n\
mov %s, eax \n\
"

#define DIV_ASM_FORMAT "\
mov eax, %s \n\
mov ebx, %s \n\
xor edx, edx \n\
div ebx \n\
mov %s, eax \n\
"

#define MOD_ASM_FORMAT "\
mov eax, %s \n\
mov ebx, %s \n\
xor edx, edx \n\
div ebx \n\
mov %s, edx \n\
"

#define PRINTF_ASM_FORMAT "\
mov eax, %s \n\
push dword eax \n\
push dword %s \n\
call [printf] \n\
add esp, 8 \n\
"

#define SCANF_ASM_FORMAT "\
push dword %s \n\
push dword format_citire \n\
call [scanf] \n\
add esp, 8 \n\
"

#endif
