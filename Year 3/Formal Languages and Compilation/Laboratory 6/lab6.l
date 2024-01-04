%{
#include <stdio.h>
#include "attrib.h"
#include "lab6.tab.h"
#include <string.h>

extern int lineNumber;
extern int errorFound;

%}

%option noyywrap

INTEGER      [+-]?((0[bB][01]+(u|U|l|L|z|Z|uz|uZ|Uz|UZ|zu|zU|Zu|ZU|ul|uL|Ul|UL|lu|lU|Lu|LU|ll|lL|Ll|LL|llu|llU|lLu|lLU|Llu|LLu|LLU|ull|ulL|uLl|uLL|Ull|UlL|ULl|ULL)?)|(0[0-7]+(u|U|l|L|z|Z|uz|uZ|Uz|UZ|zu|zU|Zu|ZU|ul|uL|Ul|UL|lu|lU|Lu|LU|ll|lL|Ll|LL|llu|llU|lLu|lLU|Llu|LLu|LLU|ull|ulL|uLl|uLL|Ull|UlL|ULl|ULL)?)|(0[xX][0-9a-fA-F]+(u|U|l|L|z|Z|uz|uZ|Uz|UZ|zu|zU|Zu|ZU|ul|uL|Ul|UL|lu|lU|Lu|LU|ll|lL|Ll|LL|llu|llU|lLu|lLU|Llu|LLu|LLU|ull|ulL|uLl|uLL|Ull|UlL|ULl|ULL)?)|([1-9][0-9]*(u|U|l|L|z|Z|uz|uZ|Uz|UZ|zu|zU|Zu|ZU|ul|uL|Ul|UL|lu|lU|Lu|LU|ll|lL|Ll|LL|llu|llU|lLu|lLU|Llu|LLu|LLU|ull|ulL|uLl|uLL|Ull|UlL|ULl|ULL)?)|(0(u|U|l|L|z|Z|uz|uZ|Uz|UZ|zu|zU|Zu|ZU|ul|uL|Ul|UL|lu|lU|Lu|LU|ll|lL|Ll|LL|llu|llU|lLu|lLU|Llu|LLu|LLU|ull|ulL|uLl|uLL|Ull|UlL|ULl|ULL)?))
REAL_NUMBER  [+-]?((0(l|f|f16|f32|f64|f128|bf16|L|F|F16|F32|F64|F128|BF16)?|[1-9][0-9]*)(\.[0-9]+)?([eE][+-]?[0-9]+)?(l|f|f16|f32|f64|f128|bf16|L|F|F16|F32|F64|F128|BF16)?|([0-9]+\.[0-9]*)([eE][+-]?[0-9]+)?(l|f|f16|f32|f64|f128|bf16|L|F|F16|F32|F64|F128|BF16)?)
IDENTIFIER   [a-zA-Z_][a-zA-Z0-9_]{0,249}  
STRING       \"[^\"]*\"

%%

"int"       { return INT; }
"double"    { return DOUBLE; }
"struct"    { return STRUCT; }
"void"      { return VOID; }
"return"    { return RETURN; }
"cout"      { return COUT; }
"cin"       { return CIN; }
"endl"      { return ENDL; }
"'\\n'"     { return ENDL; }
"\"\\n\""   { return ENDL; }
"#"         { return HASH; }
"include"   { return INCLUDE_CPP; }
";"         { return SEMICOLON; }
","         { return COMMA; }
"("         { return OPEN_PAR;}
")"         { return CLOSE_PAR; }
"{"         { return OPEN_BRACE; }
"}"         { return CLOSE_BRACE; }
"="         { return ASSIGN; }
"+"         { return PLUS; }
"*"         { return TIMES; }
"%"         { return MOD; }
"/"         { return DIV; }
"<<"        { return SHIFT_LEFT; }
">>"        { return SHIFT_RIGHT; }
"."         { return DOT; }
"<"         { return LESS_THAN; }
">"         { return GREATER_THAN; }
"iostream"  { return HEADER_CPP; }
"fstream"   { return HEADER_CPP; }
"using"     { return USING; }
"namespace" { return NAMESPACE; }
"std"       { return ACTUAL_NAMESPACE; }


{IDENTIFIER}    {
                  strcpy(yylval.varname, "[");
                  strcat(yylval.varname, yytext);
                  strcat(yylval.varname, "]");
                  return ID;
                }
{INTEGER}       {
                  strcpy(yylval.varname,yytext);    
                  return INTEGER;
                }
{REAL_NUMBER}   {
                  strcpy(yylval.varname,yytext);    
                  return REAL_NUMBER;
                }
{STRING}        { 
                  strcpy(yylval.varname,yytext); 
                  return STRING; 
                }
[^ \t\n;]{251,} { printf("Lexical error - identifier is too long at line %d: %s\n", lineNumber, yytext); errorFound = 1; return ID; }
"{"[^}\n]*"}"   { ; }
[ \t]+          
[\n]            { lineNumber++; }
.               { printf("Lexical error at line %d: %s\n", lineNumber, yytext); errorFound = 1; }
%%

