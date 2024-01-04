
/* A Bison parser, made by GNU Bison 2.4.1.  */

/* Skeleton interface for Bison's Yacc-like parsers in C
   
      Copyright (C) 1984, 1989, 1990, 2000, 2001, 2002, 2003, 2004, 2005, 2006
   Free Software Foundation, Inc.
   
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.
   
   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */


/* Tokens.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     ID = 258,
     INTEGER = 259,
     REAL_NUMBER = 260,
     STRING = 261,
     HASH = 262,
     INCLUDE_CPP = 263,
     HEADER_CPP = 264,
     USING = 265,
     SEMICOLON = 266,
     INT = 267,
     DOUBLE = 268,
     STRUCT = 269,
     VOID = 270,
     RETURN = 271,
     CIN = 272,
     COUT = 273,
     ENDL = 274,
     COMMA = 275,
     DOT = 276,
     OPEN_PAR = 277,
     CLOSE_PAR = 278,
     OPEN_BRACE = 279,
     CLOSE_BRACE = 280,
     ASSIGN = 281,
     PLUS = 282,
     TIMES = 283,
     MOD = 284,
     DIV = 285,
     SHIFT_LEFT = 286,
     SHIFT_RIGHT = 287,
     LESS_THAN = 288,
     GREATER_THAN = 289,
     NAMESPACE = 290,
     ACTUAL_NAMESPACE = 291
   };
#endif



#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef union YYSTYPE
{

/* Line 1676 of yacc.c  */
#line 139 "lab6.y"

 char varname[10];
 attributes attrib;
 char strCode[250];



/* Line 1676 of yacc.c  */
#line 96 "lab6.tab.h"
} YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
#endif

extern YYSTYPE yylval;


