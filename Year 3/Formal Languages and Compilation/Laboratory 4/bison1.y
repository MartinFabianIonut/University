%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int lineNumber = 1;
int errorFound = 0;
int yylex();
void yyerror(const char* s);

%}

%token ID CONST INCLUDE_CPP HEADER_CPP USING SEMICOLON INT DOUBLE STRUCT VOID IF WHILE FOR DO RETURN CIN COUT ENDL NEQ EQ LT GT LE GE
%token COMMA DOT OPEN_PAR CLOSE_PAR OPEN_BRACE CLOSE_BRACE ASSIGN
%token PLUS TIMES MOD SHIFT_LEFT SHIFT_RIGHT AND OR NOT
%left PLUS
%left TIMES MOD
%nonassoc NEQ EQ LT GT LE GE

%%

program: multiple_includes usings decl_functions
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

sir_decl: ID COMMA sir_decl
        | ID
        | atr
        ;

tipId: ID
     | tip
     ;

atr: ID ASSIGN expr_aritm
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

expr_aritm: expr_aritm PLUS expr_aritm
          | expr_aritm TIMES expr_aritm
          | expr_aritm MOD expr_aritm
          | ID
          | CONST
          | ID DOT ID
          | apel_f
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

lista_de_citit: SHIFT_RIGHT ID lista_de_citit
              | SHIFT_RIGHT ID DOT ID lista_de_citit
              | SHIFT_RIGHT ID DOT ID
              | SHIFT_RIGHT ID
              ;

instr_afisare: COUT lista_de_iesiri SEMICOLON
            ;

lista_de_iesiri: SHIFT_LEFT expr_aritm lista_de_iesiri
               | SHIFT_LEFT expr_aritm
               | SHIFT_LEFT ENDL 
               ;

%%

void yyerror(const char* msg) {
    fprintf(stderr, "Parser error at line %d: %s\n", lineNumber, msg);
}

int main() {
    yyparse();
    printf("Parsing successful!\n");
    return 0;
}
