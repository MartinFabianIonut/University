%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "attrib.h"
#include "codeASM.h"

struct Variable {
    char name[20];
};

struct Variable variables[100];
struct Variable printVariables[100];
int varCount = 0;
int printVarCount = 0;

void addVariable(char* name) {
    strcpy(variables[varCount].name, name);
    varCount++;
}

void removeBrackets(char* dest, const char* source) {
    int j = 0;
    for (int i = 0; source[i] != '\0'; ++i) {
        if (source[i] != '[' && source[i] != ']') {
            dest[j++] = source[i];
        }
    }
    dest[j] = '\0';
}

int printVariableExists(char* name) {
    for (int i = 0; i < printVarCount; ++i) {
        if (strcmp(printVariables[i].name, name) == 0) {
            return 1;
        }
    }
    return 0;
}

char* computeFormatAfisare(char* name){
        char* format = (char*)malloc(20*sizeof(char));
        strcpy(format,"format_afisare_");
        char* cleanName = (char*)malloc(10*sizeof(char));
        removeBrackets(cleanName,name);
        strcat(format,cleanName);
        if (!printVariableExists(format)) {
            strcpy(printVariables[printVarCount].name, format);
            printVarCount++;
        }
        return format;
}

char* getVariableNameAfterLastUnderscore(char* name) {
    char* lastUnderscore = strrchr(name, '_');
    return lastUnderscore + 1;
}

int variableExists(char* name) {
    for (int i = 0; i < varCount; ++i) {
        if (strcmp(variables[i].name, name) == 0) {
            return 1;
        }
    }
    return 0;
}

void addTempVariable(char* temp) {
    char cleanTemp[10];
    removeBrackets(cleanTemp, temp);

    if (!variableExists(cleanTemp)) {
        addVariable(cleanTemp);
    }
}


int lineNumber = 1;
int errorFound = 0;

extern int yylex(); /* lexical analyzer generated from lex.l */
extern char *yytext; /* last token, defined in lex.l  */

void
yyerror(char *s) {
  printf("\n \n \nMy error: \n");
  printf( "Syntax error on line #%d: %s\n", lineNumber, s);
  printf( "Last token was \"%s\"\n", yytext);
  exit(1);
}

int tempnr = 1;
void newTempName(char* s){
  sprintf(s,"[temp%d]",tempnr);
  tempnr++;
}

char tempbuffer[250];

%}


%union {
 char varname[10];
 attributes attrib;
 char strCode[250];
}


%token <varname> ID
%token <varname> CONST

%type <attrib> expr_aritm
%type <attrib> lista_de_citit
%type <attrib> lista_de_iesiri

%token INCLUDE_CPP HEADER_CPP USING SEMICOLON INT DOUBLE STRUCT VOID IF WHILE FOR DO RETURN CIN COUT ENDL NEQ EQ LT GT LE GE
%token COMMA DOT OPEN_PAR CLOSE_PAR OPEN_BRACE CLOSE_BRACE ASSIGN
%token PLUS TIMES MOD DIV SHIFT_LEFT SHIFT_RIGHT AND OR NOT
%left PLUS
%left TIMES MOD DIV
%nonassoc NEQ EQ LT GT LE GE

%%

program: preamble multiple_includes usings decl_functions
        {
            // La finalul analizei, afișăm variabilele
            printf("push dword 0\n");
            printf("call [exit]\n\n");
            printf("segment data use32 class=data\n");
            for (int i = 0; i < varCount; ++i) {
                printf("\t%s dd 0\n", variables[i].name);
            }
            printf("\tformat_citire db \"%%d\", 0\n");
            for (int i = 0; i < printVarCount; ++i) {
                printf("\t%s db \"%s = %%d\", 0\n", printVariables[i].name, getVariableNameAfterLastUnderscore(printVariables[i].name));
            }

        }
        ;

preamble: /* Adaugă instrucțiunile dorite aici */
        {
            printf("bits 32\n");
            printf("global start\n");
            printf("\n");
            printf("extern exit, printf, scanf\n");
            printf("import exit msvcrt.dll\n");
            printf("import printf msvcrt.dll\n");
            printf("import scanf msvcrt.dll\n");
            printf("\n");
            printf("segment code use32 class=code\n");
            printf("\tstart:bits 32\n");
            printf("\n");
            printf("global start\n\n");
        }
        ;

multiple_includes: INCLUDE_CPP HEADER_CPP multiple_includes
                 | INCLUDE_CPP HEADER_CPP
                 ;

usings: USING SEMICOLON usings
      | USING SEMICOLON
      ;

decl_functions: function
              | function decl_functions
              ;

function: antet corp
        | STRUCT ID OPEN_BRACE lista_decl CLOSE_BRACE SEMICOLON
        ;

antet: tipId ID OPEN_PAR lista_decl_antet CLOSE_PAR 
     | tipId ID OPEN_PAR CLOSE_PAR
     ;

tip: INT
   | DOUBLE
   | VOID
   ;

lista_decl: decl lista_decl 
        | decl 
        ;

lista_decl_antet: decl_param COMMA lista_decl_antet
          | decl_param
          ;

decl_param: tipId ID 
          ;

decl: tipId sir_decl SEMICOLON
    ;

sir_decl: ID COMMA sir_decl {addTempVariable($1);}
        | ID {addTempVariable($1);}
        | atr
        ;

tipId: ID
     | tip
     ;

atr: ID ASSIGN expr_aritm {
                    printf("%s\n",$3.code);
                    printf("mov eax, %s\n",$3.varn);
                    printf("mov %s,eax\n",$1);
                   }
    | ID ASSIGN expr_aritm COMMA atr
   ;

corp: OPEN_BRACE instr_comp CLOSE_BRACE
    ;

instr_comp: instr instr_comp
          | instr
          ;

instr: atr SEMICOLON
     | instr_if
     | instr_cicl
     | instr_return
     | decl
     | instr_citire
     | instr_afisare
     | apel_f SEMICOLON
     ;

apel_f: ID OPEN_PAR lista_param CLOSE_PAR
      | ID OPEN_PAR CLOSE_PAR
      ;

lista_param: expr_aritm lista_param 
           | expr_aritm
           ;

instr_if: IF OPEN_PAR cond CLOSE_PAR corp
        ;

instr_cicl: WHILE OPEN_PAR cond CLOSE_PAR corp
          | FOR OPEN_PAR decl cond SEMICOLON atrFor CLOSE_PAR corp
          | DO corp WHILE OPEN_PAR cond CLOSE_PAR SEMICOLON
          ;

atrFor: ID ASSIGN expr_aritm
        ;

cond: expr_aritm op_rel expr_aritm
    | expr_aritm
    ;

expr_aritm: expr_aritm PLUS expr_aritm { 
                     newTempName($$.varn);
                     sprintf($$.code,"%s\n%s\n",$1.code,$3.code);
                     sprintf(tempbuffer,ADD_ASM_FORMAT,$1.varn,$3.varn,$$.varn);
                     strcat($$.code,tempbuffer);
                     addTempVariable($$.varn); 
                     }
          | expr_aritm TIMES expr_aritm { 
                     newTempName($$.varn);
                     sprintf($$.code, "%s\n%s\n", $1.code, $3.code);
                     sprintf(tempbuffer, MUL_ASM_FORMAT, $1.varn, $3.varn, $$.varn);
                     strcat($$.code, tempbuffer);addTempVariable($$.varn); 
                   }
          | expr_aritm MOD expr_aritm { 
                     newTempName($$.varn);
                     sprintf($$.code, "%s\n%s\n", $1.code, $3.code);
                     sprintf(tempbuffer, MOD_ASM_FORMAT, $1.varn, $3.varn, $$.varn);
                     strcat($$.code, tempbuffer);addTempVariable($$.varn); 
                   }
          | expr_aritm DIV expr_aritm { 
                     newTempName($$.varn);
                     sprintf($$.code, "%s\n%s\n", $1.code, $3.code);
                     sprintf(tempbuffer, DIV_ASM_FORMAT, $1.varn, $3.varn, $$.varn);
                     strcat($$.code, tempbuffer);addTempVariable($$.varn); 
                   }
          | OPEN_PAR expr_aritm CLOSE_PAR {
                     strcpy($$.code,$2.code);
                     strcpy($$.varn,$2.varn);
                   }
          | ID {
                strcpy($$.code,"");
                strcpy($$.varn,$1); 
                }
          | CONST {
                strcpy($$.code,"");
                strcpy($$.varn,$1); 
                }
          ;

op_rel: NEQ
      | EQ
      | LT
      | GT
      | LE
      | GE
      ;

instr_return: RETURN expr_aritm SEMICOLON
            ;

instr_citire: CIN lista_de_citit SEMICOLON
            ;

lista_de_citit: SHIFT_RIGHT ID lista_de_citit {;}
              | SHIFT_RIGHT ID DOT ID lista_de_citit {;}
              | SHIFT_RIGHT ID DOT ID {;}
              | SHIFT_RIGHT ID { 
                                char cleanID[10];
                                removeBrackets(cleanID, $2);
                                sprintf(tempbuffer, SCANF_ASM_FORMAT, cleanID);
                                printf("%s\n", tempbuffer);
                                strcat($$.code, tempbuffer);
                                addTempVariable($2);
                              }
              ;

instr_afisare: COUT lista_de_iesiri SEMICOLON
            ;

lista_de_iesiri: SHIFT_LEFT expr_aritm lista_de_iesiri {;}
               | SHIFT_LEFT expr_aritm {
                        char* format = computeFormatAfisare($2.varn);
                        sprintf(tempbuffer, PRINTF_ASM_FORMAT, $2.varn, format);
                        printf("%s\n", tempbuffer);
                        strcat($$.code, tempbuffer);
               }
               | SHIFT_LEFT ENDL {;}
               ;

%%

int
main(int argc,char *argv[]) {
    yyparse();
    return 0;
}
