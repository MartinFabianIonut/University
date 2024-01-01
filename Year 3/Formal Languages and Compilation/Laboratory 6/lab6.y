%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "attrib.h"
#include "codeASM.h"

int tempnr = 1;
void newTempName(char* s){
  sprintf(s,"[temp%d]",tempnr);
  tempnr++;
}

char tempbuffer[250];

struct Variable {
    char name[30];
};

struct Variable variables[100];
char printVariables[100][30];
char printTypes[100][30];
char printConstants[100][30];
char messages[100][250];
int varCount = 0;
int printVarCount = 0;
int printConstCount = 0;
int endl = 0;

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
        if (strcmp(printVariables[i], name) == 0) {
            return 1;
        }
    }
    return 0;
}

int printConstantsExists(char* name) {
    for (int i = 0; i < printConstCount; ++i) {
        if (strcmp(printConstants[i], name) == 0) {
            return 1;
        }
    }
    return 0;
}

void computeFormatAfisare(char* name, char* code, char* format, size_t formatSize) {
    snprintf(format, formatSize, "format_afisare_");

    if (strcmp(code, "ID") == 0) {
        char cleanName[20];
        removeBrackets(cleanName, name);
        strncat(format, cleanName, formatSize - strlen(format) - 1);

        if (!printVariableExists(format)) {
            strcpy(printVariables[printVarCount], format);
            printVarCount++;
        }
    } else {
        char buffer[20];
        newTempName(buffer);
        removeBrackets(buffer, buffer);
        strncat(format, buffer, formatSize - strlen(format) - 1);

        if (!printConstantsExists(format)) {
            strcpy(printConstants[printConstCount], format);

            if (strcmp(code, "STRING") == 0) {
                strcpy(messages[printConstCount], name);
            } else {
                char quotationName[20];  
                strcpy(quotationName, "\"");
                strcat(quotationName, name);
                strcat(quotationName, "\"");
                strcpy(messages[printConstCount], quotationName);
            }
            printConstCount++;
        }
    }
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
    char cleanTemp[30];
    removeBrackets(cleanTemp, temp);

    if (!variableExists(cleanTemp)) {
        addVariable(cleanTemp);
    }
}


int lineNumber = 1;
int errorFound = 0;

extern int yylex(); 
extern char *yytext;

void
yyerror(char *s) {
  printf("\n \n \nMy error: \n");
  printf("Syntax error on line #%d: %s\n", lineNumber, s);
  printf("Last token was \"%s\"\n", yytext);
  exit(1);
}

%}


%union {
 char varname[10];
 attributes attrib;
 char strCode[250];
}


%token <varname> ID
%token <varname> INTEGER
%token <varname> REAL_NUMBER
%token <varname> STRING


%type <attrib> expr_aritm
%type <attrib> lista_de_citit
%type <attrib> lista_de_iesiri

%token INCLUDE_CPP HEADER_CPP USING SEMICOLON INT DOUBLE STRUCT VOID RETURN CIN COUT ENDL
%token COMMA DOT OPEN_PAR CLOSE_PAR OPEN_BRACE CLOSE_BRACE ASSIGN
%token PLUS TIMES MOD DIV SHIFT_LEFT SHIFT_RIGHT
%left PLUS
%left TIMES MOD DIV

%%

program: preamble multiple_includes usings decl_functions
        {
            printf("push dword 0\n");
            printf("call [exit]\n\n");
            printf("segment data use32 class=data\n");
            for (int i = 0; i < varCount; ++i) {
                printf("\t%s dd 0\n", variables[i].name);
            }
            printf("\tformat_citire db \"%%d\", 0\n");
            for (int i = 0; i < printVarCount; ++i) {
                printf("\t%s db \"%s = %%d\", 0\n", printVariables[i], getVariableNameAfterLastUnderscore(printVariables[i]));
            }
            for (int i = 0; i < printConstCount; ++i) {
                printf("\t%s db %s, 0\n", printConstants[i], messages[i]);
            }
            if (endl) {
                printf("\tformat_afisare_endl db \"\", 0Dh, 0Ah, 0");
            }
        }
        ;

preamble:
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

atr: ID ASSIGN expr_aritm 
                    {
                        printf("%s\n",$3.code);
                        printf("mov eax, %s\n",$3.varn);
                        printf("mov %s,eax\n",$1);
                        addTempVariable($1);
                    }
    | ID ASSIGN expr_aritm COMMA atr
   ;

corp: OPEN_BRACE instr_comp CLOSE_BRACE
    ;

instr_comp: instr instr_comp
          | instr
          ;

instr: atr SEMICOLON
     | instr_return
     | decl
     | instr_citire
     | instr_afisare
     ;

expr_aritm: expr_aritm PLUS expr_aritm 
                                    { 
                                        newTempName($$.varn);
                                        sprintf($$.code,"%s\n%s\n",$1.code,$3.code);
                                        sprintf(tempbuffer,ADD_ASM_FORMAT,$1.varn,$3.varn,$$.varn);
                                        strcat($$.code,tempbuffer);
                                        addTempVariable($$.varn); 
                                    }
            | expr_aritm TIMES expr_aritm 
                { 
                    newTempName($$.varn);
                    sprintf($$.code, "%s\n%s\n", $1.code, $3.code);
                    sprintf(tempbuffer, MUL_ASM_FORMAT, $1.varn, $3.varn, $$.varn);
                    strcat($$.code, tempbuffer);
                    addTempVariable($$.varn); 
                }
            | expr_aritm MOD expr_aritm 
                { 
                    newTempName($$.varn);
                    sprintf($$.code, "%s\n%s\n", $1.code, $3.code);
                    sprintf(tempbuffer, MOD_ASM_FORMAT, $1.varn, $3.varn, $$.varn);
                    strcat($$.code, tempbuffer);
                    addTempVariable($$.varn); 
                }
            | expr_aritm DIV expr_aritm 
                { 
                    newTempName($$.varn);
                    sprintf($$.code, "%s\n%s\n", $1.code, $3.code);
                    sprintf(tempbuffer, DIV_ASM_FORMAT, $1.varn, $3.varn, $$.varn);
                    strcat($$.code, tempbuffer);
                    addTempVariable($$.varn); 
                }
            | OPEN_PAR expr_aritm CLOSE_PAR 
                {
                    strcpy($$.code,$2.code);
                    strcpy($$.varn,$2.varn);
                }
            | ID 
                {
                    strcpy($$.code,"");
                    strcpy($$.varn,$1); 
                    strcpy($$.type,"ID");
                }
            | INTEGER 
                {
                    strcpy($$.code,"");
                    strcpy($$.varn,$1); 
                    strcpy($$.type,"INTEGER");
                }
            | REAL_NUMBER 
                {
                    strcpy($$.code,"");
                    strcpy($$.varn,$1); 
                    strcpy($$.type,"REAL");
                }
            | STRING 
                {
                    strcpy($$.code,"");
                    strcpy($$.varn,$1); 
                    strcpy($$.type,"STRING");
                }
            ;

instr_return: RETURN expr_aritm SEMICOLON
            ;

instr_citire: CIN lista_de_citit SEMICOLON
            ;

lista_de_citit: SHIFT_RIGHT ID lista_de_citit {;}
              | SHIFT_RIGHT ID DOT ID lista_de_citit {;}
              | SHIFT_RIGHT ID DOT ID {;}
              | SHIFT_RIGHT ID 
                { 
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

lista_de_iesiri: SHIFT_LEFT expr_aritm 
                { 
                    char format[30];
                    computeFormatAfisare($2.varn, $2.type, format, sizeof(format));
                    if (strcmp($2.type, "ID") != 0) 
                    {
                        sprintf(tempbuffer, PRINTF_CONST_ASM_FORMAT, format);
                        printf("%s\n", tempbuffer);
                    }
                    else
                    {
                        sprintf(tempbuffer, PRINTF_ASM_FORMAT, $2, format);
                        printf("%s\n", tempbuffer);
                    }
                } 
                    lista_de_iesiri {;} 
                | SHIFT_LEFT ENDL 
                { 
                    if (!endl) { 
                        endl = 1; 
                    }
                    sprintf(tempbuffer, PRINTF_ENDL_ASM_FORMAT);
                    printf("%s\n", tempbuffer);
                } 
                    lista_de_iesiri {;}
                | SHIFT_LEFT expr_aritm 
                {
                    char format[30];
                    computeFormatAfisare($2.varn, $2.type, format, sizeof(format));
                    if (strcmp($2.type, "ID") != 0) 
                    {
                        sprintf(tempbuffer, PRINTF_CONST_ASM_FORMAT, format);
                        printf("%s\n", tempbuffer);
                        strcat($$.code, tempbuffer);
                    }
                    else
                    {
                        sprintf(tempbuffer, PRINTF_ASM_FORMAT, $2, format);
                        printf("%s\n", tempbuffer);
                        strcat($$.code, tempbuffer);
                    }
                }
                | SHIFT_LEFT ENDL 
                {
                    if (!endl) { 
                        endl = 1; 
                    }
                    sprintf(tempbuffer, PRINTF_ENDL_ASM_FORMAT);
                    printf("%s\n", tempbuffer);
                    strcat($$.code, tempbuffer);
                }
               ;

%%

int
main(int argc,char *argv[]) {
    yyparse();
    return 0;
}
