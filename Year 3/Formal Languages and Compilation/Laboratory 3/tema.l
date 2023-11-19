%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* Definirea structurilor pentru FIP, TS_ID și TS_CONST */

int errorFound = 0;
int lineNumber = 1;

typedef struct {
    char* name;
    int code;
} Atom;

typedef struct{
	int atomCode;
    int TSCode;
}FIP;

typedef struct{
	char* name;
	int codAtomTS;
}TS;

FIP* fip;
TS* ts_id;
TS* ts_const;

Atom listOfAtoms[100];
int listOfAtomsIndex = 0;
char* listOfIdNames[100];
int listOfIdNamesIndex = 0;
char* listOfConstNames[100];
int listOfConstNamesIndex = 0;
char* lexicalAtoms[100];
int lexicalAtomsIndex = 0;

int FIPIndex = 0;
int TS_IDIndex = 0;
int TS_CONSTIndex = 0;

int existsInList(char* name, char** list, int listIndex) {
    for (int i = 0; i < listIndex; i++) {
        if (strcmp(name, list[i]) == 0) {
            return 1;
        }
    }
    return 0;
}

void addToLexicalAtoms(char* atomName) {
    lexicalAtoms[lexicalAtomsIndex] = strdup(atomName);
    lexicalAtomsIndex++;
}

void addToListOfAtoms(char* atomName, int atomCode) {
     addToLexicalAtoms(atomName);
     for (int i = 0; i < listOfAtomsIndex; i++) {
        if (strcmp(atomName, listOfAtoms[i].name) == 0) {
            return;
        }
    }
    listOfAtoms[listOfAtomsIndex].name = strdup(atomName);
    listOfAtoms[listOfAtomsIndex].code = atomCode;
    listOfAtomsIndex++;
}

void addToListOfIdNames(char* name) {
    addToLexicalAtoms(name);
    if (existsInList(name, listOfIdNames, listOfIdNamesIndex) == 1) {
        return;
    }
    listOfIdNames[listOfIdNamesIndex] = strdup(name);
    listOfIdNamesIndex++;
}

void addToListOfConstNames(char* name) {
    addToLexicalAtoms(name);
    if (existsInList(name, listOfConstNames, listOfConstNamesIndex) == 1) {
        return;
    }
    listOfConstNames[listOfConstNamesIndex] = strdup(name);
    listOfConstNamesIndex++;
}

int hashCode(char* name, int size) {
    int sum = 0;
    for (int i = 0; i < strlen(name); i++) {
        sum += name[i];
    }
    return sum % size;
}

void createTS_ID() {
    /* Alloc memory for TS_ID */
    ts_id = (TS*)malloc(listOfIdNamesIndex * sizeof(TS));
    for (int i = 0; i < listOfIdNamesIndex; i++) {
        ts_id[i].name = NULL;
    }
    int index;
    for (int i = 0; i < listOfIdNamesIndex; i++) {
        index = hashCode(listOfIdNames[i], listOfIdNamesIndex);
        while (ts_id[index].name != NULL && strcmp(ts_id[index].name, listOfIdNames[i]) != 0) {
            index = (index + 1) % listOfIdNamesIndex;
        }
        ts_id[index].name = strdup(listOfIdNames[i]);
        ts_id[index].codAtomTS = index;
    }
    TS_IDIndex = listOfIdNamesIndex;
}

void createTS_CONST() {
    /* alloc memory for TS_CONST */
    ts_const = (TS*)malloc(listOfConstNamesIndex * sizeof(TS));
    for (int i = 0; i < listOfConstNamesIndex; i++) {
        ts_const[i].name = NULL;
    }
    int index;
    for (int i = 0; i < listOfConstNamesIndex; i++) {
        index = hashCode(listOfConstNames[i], listOfConstNamesIndex);
        while (ts_const[index].name != NULL && strcmp(ts_const[index].name, listOfConstNames[i]) != 0) {
            index  = (index + 1) % listOfConstNamesIndex;
        }
        ts_const[index].name = strdup(listOfConstNames[i]);
        ts_const[index].codAtomTS = index;
    }
    TS_CONSTIndex = listOfConstNamesIndex;
}

void createFIP(){
    printf("listOfAtomsIndex = %d\n", listOfAtomsIndex);
    printf("lexicalAtomsIndex = %d\n", lexicalAtomsIndex);
    printf("TS_IDIndex = %d\n", TS_IDIndex);
    printf("TS_CONSTIndex = %d\n", TS_CONSTIndex);
    /* alloc memory for FIP */
    fip = (FIP*)malloc(lexicalAtomsIndex * sizeof(FIP));
    for(int i = 0; i < lexicalAtomsIndex; i++){
        int isAtom = 0;
        for (int j = 0; j < listOfAtomsIndex; j++) {
            if (strcmp(lexicalAtoms[i], listOfAtoms[j].name) == 0) {
                fip[FIPIndex].atomCode = listOfAtoms[j].code;
                fip[FIPIndex].TSCode = -1;
                isAtom = 1;
                FIPIndex++;
                break;
            }
        }
        if (isAtom == 0){
            for (int j = 0; j < TS_IDIndex; j++) {
                if (strcmp(lexicalAtoms[i], ts_id[j].name) == 0) {
                    fip[FIPIndex].atomCode = 0;
                    fip[FIPIndex].TSCode = ts_id[j].codAtomTS;
                    FIPIndex++;
                    break;
                }
            }
            for (int j = 0; j < TS_CONSTIndex; j++) {
                if (strcmp(lexicalAtoms[i], ts_const[j].name) == 0) {
                    fip[FIPIndex].atomCode = 1;
                    fip[FIPIndex].TSCode = ts_const[j].codAtomTS;
                    FIPIndex++;
                    break;
                }
            }
        }
    }
    printf("FIPIndex = %d\n", FIPIndex);
}

void printFIP() {
    printf("\nFIP:\n");
    for (int i = 0; i < FIPIndex; i++) {
        printf("[%d : %d]\n", fip[i].atomCode, fip[i].TSCode);
    }
}

void printTS_ID() {
    printf("\nTS_ID:\n");
    for (int i = 0; i < TS_IDIndex; i++) {
        printf("[%s : %d]\n", ts_id[i].name, ts_id[i].codAtomTS);
    }
}

void printTS_CONST() {
    printf("\nTS_CONST:\n");
    for (int i = 0; i < TS_CONSTIndex; i++) {
        printf("[%s : %d]\n", ts_const[i].name, ts_const[i].codAtomTS);
    }
}

void dealloc() {
    for (int i = 0; i < listOfAtomsIndex; i++) {
        free(listOfAtoms[i].name);
    }
    for (int i = 0; i < listOfIdNamesIndex; i++) {
        free(listOfIdNames[i]);
    }
    for (int i = 0; i < listOfConstNamesIndex; i++) {
        free(listOfConstNames[i]);
    }
    for (int i = 0; i < lexicalAtomsIndex; i++) {
        free(lexicalAtoms[i]);
    }
    free(fip);
    free(ts_id);
    free(ts_const);
}

%}

INTEGER      [+-]?((0[bB][01]+(u|U|l|L|ul|uL|Ul|UL|lu|lU|Lu|LU|llu|llU|LL|LLu|LLU)?)|(0[0-7]+(u|U|l|L|ul|uL|Ul|UL|lu|lU|Lu|LU|llu|llU|LL|LLu|LLU)?)|(0[xX][0-9a-fA-F]+(u|U|l|L|ul|uL|Ul|UL|lu|lU|Lu|LU|llu|llU|LLu|LL|LLU)?)|([1-9][0-9]*(u|U|l|L|ul|uL|Ul|UL|lu|lU|Lu|LU|llu|llU|LLu|LL|LLU)?)|(0(u|U|l|L|ul|uL|Ul|UL|lu|lU|Lu|LU|llu|llU|LLu|LL|LLU)?))
REAL_NUMBER  [+-]?((0(l|f|f16|f32|f64|f128|bf16|L|F|F16|F32|F64|F128|BF16)?|[1-9][0-9]*)(\.[0-9]+)?([eE][+-]?[0-9]+)?(l|f|f16|f32|f64|f128|bf16|L|F|F16|F32|F64|F128|BF16)?|([0-9]+\.[0-9]*)([eE][+-]?[0-9]+)?(l|f|f16|f32|f64|f128|bf16|L|F|F16|F32|F64|F128|BF16)?)
IDENTIFIER   [a-zA-Z_][a-zA-Z0-9_]{0,249}  
STRING       \".*\"

%option noyywrap

%%

"if"        { addToListOfAtoms(yytext, 2); }
"else"      { addToListOfAtoms(yytext, 3); }
"while"     { addToListOfAtoms(yytext, 4); }
"for"       { addToListOfAtoms(yytext, 5); }
"do"        { addToListOfAtoms(yytext, 6); }
"continue"  { addToListOfAtoms(yytext, 7); }
"bool"      { addToListOfAtoms(yytext, 8); }
"break"     { addToListOfAtoms(yytext, 9); }
"true"      { addToListOfAtoms(yytext, 10); }
"fals"      { addToListOfAtoms(yytext, 11); }
"int"       { addToListOfAtoms(yytext, 16); }
"float"     { addToListOfAtoms(yytext, 17); }
"double"    { addToListOfAtoms(yytext, 18); }
"struct"    { addToListOfAtoms(yytext, 19); }
"void"      { addToListOfAtoms(yytext, 21); }
"return"    { addToListOfAtoms(yytext, 22); }
"include"   { addToListOfAtoms(yytext, 23); }
"define"    { addToListOfAtoms(yytext, 24); }
"using"     { addToListOfAtoms(yytext, 25); }
"namespace" { addToListOfAtoms(yytext, 26); }
"std"       { addToListOfAtoms(yytext, 27); }
"cout"      { addToListOfAtoms(yytext, 28); }
"cin"       { addToListOfAtoms(yytext, 29); }
"endl"      { addToListOfAtoms(yytext, 30); }
"("         { addToListOfAtoms(yytext, 31); }
")"         { addToListOfAtoms(yytext, 32); }
"{"         { addToListOfAtoms(yytext, 35); }
"}"         { addToListOfAtoms(yytext, 36); }
";"         { addToListOfAtoms(yytext, 37); }
","         { addToListOfAtoms(yytext, 38); }
"."         { addToListOfAtoms(yytext, 39); }
"+"         { addToListOfAtoms(yytext, 40); }
"*"         { addToListOfAtoms(yytext, 42); }
"%"         { addToListOfAtoms(yytext, 44); }
"="         { addToListOfAtoms(yytext, 45); }
"=="        { addToListOfAtoms(yytext, 46); }
"!="        { addToListOfAtoms(yytext, 47); }
">"         { addToListOfAtoms(yytext, 48); }
"<"         { addToListOfAtoms(yytext, 49); }
">="        { addToListOfAtoms(yytext, 50); }
"<="        { addToListOfAtoms(yytext, 51); }
"&&"        { addToListOfAtoms(yytext, 52); }
"||"        { addToListOfAtoms(yytext, 53); }
"!"         { addToListOfAtoms(yytext, 54); }
"<<"        { addToListOfAtoms(yytext, 59); }
">>"        { addToListOfAtoms(yytext, 60); }
"#"         { addToListOfAtoms(yytext, 67); }
"iostream"  { addToListOfAtoms(yytext, 68); }
"fstream"   { addToListOfAtoms(yytext, 69); }
"ifstream"  { addToListOfAtoms(yytext, 70); }
"ofstream"  { addToListOfAtoms(yytext, 71); }
[^ \t\n]{251,} { printf("Lexical error - identifier is too long at line %d: %s\n", lineNumber, yytext); errorFound = 1; }
{IDENTIFIER} { addToListOfIdNames(yytext); }
{INTEGER}    { addToListOfConstNames(yytext); }
{REAL_NUMBER} { addToListOfConstNames(yytext); }
{STRING}     { addToListOfConstNames(yytext); }
"{"[^}\n]*"}"     /* eat up one-line comments */
[ \t]+          /* eat up whitespace */
[\n]      { lineNumber++; }
.            { printf("Lexical error at line %d: %s\n", lineNumber, yytext); errorFound = 1; }


%%



int main( argc, argv )
int argc;
char **argv;
{
    ++argv, --argc; /* skip over program name */ 
    printf("argc: %d\n", argc);
    printf("argv: %s\n", argv[0]);
    if ( argc > 0 ) 
    	yyin = fopen( argv[0], "r" ); 
    else
        return 1;

	/* yylex() - this is the main flex function which runs the Rule Section*/ 
	while( yylex() != 0  && errorFound == 0) {
        /* yylex() returns 0 when EOF is reached */
    }

    if (errorFound == 0) {
        createTS_ID();
        createTS_CONST();
        createFIP();
        printFIP();
        printTS_ID();
        printTS_CONST();
        dealloc();
    }
    return 0;
}
