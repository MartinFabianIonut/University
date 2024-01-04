
/* A Bison parser, made by GNU Bison 2.4.1.  */

/* Skeleton implementation for Bison's Yacc-like parsers in C
   
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

/* C LALR(1) parser skeleton written by Richard Stallman, by
   simplifying the original so-called "semantic" parser.  */

/* All symbols defined below should begin with yy or YY, to avoid
   infringing on user name space.  This should be done even for local
   variables, as they might otherwise be expanded by user macros.
   There are some unavoidable exceptions within include files to
   define necessary library symbols; they are noted "INFRINGES ON
   USER NAME SPACE" below.  */

/* Identify Bison output.  */
#define YYBISON 1

/* Bison version.  */
#define YYBISON_VERSION "2.4.1"

/* Skeleton name.  */
#define YYSKELETON_NAME "yacc.c"

/* Pure parsers.  */
#define YYPURE 0

/* Push parsers.  */
#define YYPUSH 0

/* Pull parsers.  */
#define YYPULL 1

/* Using locations.  */
#define YYLSP_NEEDED 0



/* Copy the first part of user declarations.  */

/* Line 189 of yacc.c  */
#line 1 "lab6.y"

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

char tempbuffer[1050];

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



/* Line 189 of yacc.c  */
#line 211 "lab6.tab.c"

/* Enabling traces.  */
#ifndef YYDEBUG
# define YYDEBUG 0
#endif

/* Enabling verbose error messages.  */
#ifdef YYERROR_VERBOSE
# undef YYERROR_VERBOSE
# define YYERROR_VERBOSE 1
#else
# define YYERROR_VERBOSE 0
#endif

/* Enabling the token table.  */
#ifndef YYTOKEN_TABLE
# define YYTOKEN_TABLE 0
#endif


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

/* Line 214 of yacc.c  */
#line 139 "lab6.y"

 char varname[10];
 attributes attrib;
 char strCode[250];



/* Line 214 of yacc.c  */
#line 291 "lab6.tab.c"
} YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
#endif


/* Copy the second part of user declarations.  */


/* Line 264 of yacc.c  */
#line 303 "lab6.tab.c"

#ifdef short
# undef short
#endif

#ifdef YYTYPE_UINT8
typedef YYTYPE_UINT8 yytype_uint8;
#else
typedef unsigned char yytype_uint8;
#endif

#ifdef YYTYPE_INT8
typedef YYTYPE_INT8 yytype_int8;
#elif (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
typedef signed char yytype_int8;
#else
typedef short int yytype_int8;
#endif

#ifdef YYTYPE_UINT16
typedef YYTYPE_UINT16 yytype_uint16;
#else
typedef unsigned short int yytype_uint16;
#endif

#ifdef YYTYPE_INT16
typedef YYTYPE_INT16 yytype_int16;
#else
typedef short int yytype_int16;
#endif

#ifndef YYSIZE_T
# ifdef __SIZE_TYPE__
#  define YYSIZE_T __SIZE_TYPE__
# elif defined size_t
#  define YYSIZE_T size_t
# elif ! defined YYSIZE_T && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
#  include <stddef.h> /* INFRINGES ON USER NAME SPACE */
#  define YYSIZE_T size_t
# else
#  define YYSIZE_T unsigned int
# endif
#endif

#define YYSIZE_MAXIMUM ((YYSIZE_T) -1)

#ifndef YY_
# if YYENABLE_NLS
#  if ENABLE_NLS
#   include <libintl.h> /* INFRINGES ON USER NAME SPACE */
#   define YY_(msgid) dgettext ("bison-runtime", msgid)
#  endif
# endif
# ifndef YY_
#  define YY_(msgid) msgid
# endif
#endif

/* Suppress unused-variable warnings by "using" E.  */
#if ! defined lint || defined __GNUC__
# define YYUSE(e) ((void) (e))
#else
# define YYUSE(e) /* empty */
#endif

/* Identity function, used to suppress warnings about constant conditions.  */
#ifndef lint
# define YYID(n) (n)
#else
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static int
YYID (int yyi)
#else
static int
YYID (yyi)
    int yyi;
#endif
{
  return yyi;
}
#endif

#if ! defined yyoverflow || YYERROR_VERBOSE

/* The parser invokes alloca or malloc; define the necessary symbols.  */

# ifdef YYSTACK_USE_ALLOCA
#  if YYSTACK_USE_ALLOCA
#   ifdef __GNUC__
#    define YYSTACK_ALLOC __builtin_alloca
#   elif defined __BUILTIN_VA_ARG_INCR
#    include <alloca.h> /* INFRINGES ON USER NAME SPACE */
#   elif defined _AIX
#    define YYSTACK_ALLOC __alloca
#   elif defined _MSC_VER
#    include <malloc.h> /* INFRINGES ON USER NAME SPACE */
#    define alloca _alloca
#   else
#    define YYSTACK_ALLOC alloca
#    if ! defined _ALLOCA_H && ! defined _STDLIB_H && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
#     include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
#     ifndef _STDLIB_H
#      define _STDLIB_H 1
#     endif
#    endif
#   endif
#  endif
# endif

# ifdef YYSTACK_ALLOC
   /* Pacify GCC's `empty if-body' warning.  */
#  define YYSTACK_FREE(Ptr) do { /* empty */; } while (YYID (0))
#  ifndef YYSTACK_ALLOC_MAXIMUM
    /* The OS might guarantee only one guard page at the bottom of the stack,
       and a page size can be as small as 4096 bytes.  So we cannot safely
       invoke alloca (N) if N exceeds 4096.  Use a slightly smaller number
       to allow for a few compiler-allocated temporary stack slots.  */
#   define YYSTACK_ALLOC_MAXIMUM 4032 /* reasonable circa 2006 */
#  endif
# else
#  define YYSTACK_ALLOC YYMALLOC
#  define YYSTACK_FREE YYFREE
#  ifndef YYSTACK_ALLOC_MAXIMUM
#   define YYSTACK_ALLOC_MAXIMUM YYSIZE_MAXIMUM
#  endif
#  if (defined __cplusplus && ! defined _STDLIB_H \
       && ! ((defined YYMALLOC || defined malloc) \
	     && (defined YYFREE || defined free)))
#   include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
#   ifndef _STDLIB_H
#    define _STDLIB_H 1
#   endif
#  endif
#  ifndef YYMALLOC
#   define YYMALLOC malloc
#   if ! defined malloc && ! defined _STDLIB_H && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
void *malloc (YYSIZE_T); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
#  ifndef YYFREE
#   define YYFREE free
#   if ! defined free && ! defined _STDLIB_H && (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
void free (void *); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
# endif
#endif /* ! defined yyoverflow || YYERROR_VERBOSE */


#if (! defined yyoverflow \
     && (! defined __cplusplus \
	 || (defined YYSTYPE_IS_TRIVIAL && YYSTYPE_IS_TRIVIAL)))

/* A type that is properly aligned for any stack member.  */
union yyalloc
{
  yytype_int16 yyss_alloc;
  YYSTYPE yyvs_alloc;
};

/* The size of the maximum gap between one aligned stack and the next.  */
# define YYSTACK_GAP_MAXIMUM (sizeof (union yyalloc) - 1)

/* The size of an array large to enough to hold all stacks, each with
   N elements.  */
# define YYSTACK_BYTES(N) \
     ((N) * (sizeof (yytype_int16) + sizeof (YYSTYPE)) \
      + YYSTACK_GAP_MAXIMUM)

/* Copy COUNT objects from FROM to TO.  The source and destination do
   not overlap.  */
# ifndef YYCOPY
#  if defined __GNUC__ && 1 < __GNUC__
#   define YYCOPY(To, From, Count) \
      __builtin_memcpy (To, From, (Count) * sizeof (*(From)))
#  else
#   define YYCOPY(To, From, Count)		\
      do					\
	{					\
	  YYSIZE_T yyi;				\
	  for (yyi = 0; yyi < (Count); yyi++)	\
	    (To)[yyi] = (From)[yyi];		\
	}					\
      while (YYID (0))
#  endif
# endif

/* Relocate STACK from its old location to the new one.  The
   local variables YYSIZE and YYSTACKSIZE give the old and new number of
   elements in the stack, and YYPTR gives the new location of the
   stack.  Advance YYPTR to a properly aligned location for the next
   stack.  */
# define YYSTACK_RELOCATE(Stack_alloc, Stack)				\
    do									\
      {									\
	YYSIZE_T yynewbytes;						\
	YYCOPY (&yyptr->Stack_alloc, Stack, yysize);			\
	Stack = &yyptr->Stack_alloc;					\
	yynewbytes = yystacksize * sizeof (*Stack) + YYSTACK_GAP_MAXIMUM; \
	yyptr += yynewbytes / sizeof (*yyptr);				\
      }									\
    while (YYID (0))

#endif

/* YYFINAL -- State number of the termination state.  */
#define YYFINAL  3
/* YYLAST -- Last index in YYTABLE.  */
#define YYLAST   108

/* YYNTOKENS -- Number of terminals.  */
#define YYNTOKENS  37
/* YYNNTS -- Number of nonterminals.  */
#define YYNNTS  29
/* YYNRULES -- Number of rules.  */
#define YYNRULES  61
/* YYNRULES -- Number of states.  */
#define YYNSTATES  109

/* YYTRANSLATE(YYLEX) -- Bison symbol number corresponding to YYLEX.  */
#define YYUNDEFTOK  2
#define YYMAXUTOK   291

#define YYTRANSLATE(YYX)						\
  ((unsigned int) (YYX) <= YYMAXUTOK ? yytranslate[YYX] : YYUNDEFTOK)

/* YYTRANSLATE[YYLEX] -- Bison symbol number corresponding to YYLEX.  */
static const yytype_uint8 yytranslate[] =
{
       0,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     1,     2,     3,     4,
       5,     6,     7,     8,     9,    10,    11,    12,    13,    14,
      15,    16,    17,    18,    19,    20,    21,    22,    23,    24,
      25,    26,    27,    28,    29,    30,    31,    32,    33,    34,
      35,    36
};

#if YYDEBUG
/* YYPRHS[YYN] -- Index of the first RHS symbol of rule number YYN in
   YYRHS.  */
static const yytype_uint8 yyprhs[] =
{
       0,     0,     3,     8,     9,    16,    22,    28,    33,    35,
      38,    41,    48,    54,    59,    61,    63,    65,    68,    70,
      74,    76,    79,    83,    87,    89,    91,    93,    95,    99,
     105,   109,   112,   114,   117,   119,   121,   123,   125,   127,
     131,   133,   137,   141,   145,   147,   149,   151,   153,   157,
     161,   165,   169,   175,   180,   183,   187,   188,   193,   194,
     199,   202
};

/* YYRHS -- A `-1'-separated list of the rules' RHS.  */
static const yytype_int8 yyrhs[] =
{
      38,     0,    -1,    39,    40,    41,    42,    -1,    -1,     7,
       8,    33,     9,    34,    40,    -1,     7,     8,    33,     9,
      34,    -1,    10,    35,    36,    11,    41,    -1,    10,    35,
      36,    11,    -1,    43,    -1,    43,    42,    -1,    44,    53,
      -1,    14,     3,    24,    46,    25,    11,    -1,    51,     3,
      22,    47,    23,    -1,    51,     3,    22,    23,    -1,    12,
      -1,    13,    -1,    15,    -1,    49,    46,    -1,    49,    -1,
      48,    20,    47,    -1,    48,    -1,    51,     3,    -1,    51,
      50,    11,    -1,     3,    20,    50,    -1,     3,    -1,    52,
      -1,     3,    -1,    45,    -1,     3,    26,    56,    -1,     3,
      26,    56,    20,    52,    -1,    24,    54,    25,    -1,    55,
      54,    -1,    55,    -1,    52,    11,    -1,    59,    -1,    49,
      -1,    60,    -1,    62,    -1,    57,    -1,    56,    27,    57,
      -1,    58,    -1,    57,    28,    58,    -1,    57,    29,    58,
      -1,    57,    30,    58,    -1,     3,    -1,     4,    -1,     5,
      -1,     6,    -1,    22,    56,    23,    -1,    16,    56,    11,
      -1,    17,    61,    11,    -1,    32,     3,    61,    -1,    32,
       3,    21,     3,    61,    -1,    32,     3,    21,     3,    -1,
      32,     3,    -1,    18,    63,    11,    -1,    -1,    31,    56,
      64,    63,    -1,    -1,    31,    19,    65,    63,    -1,    31,
      56,    -1,    31,    19,    -1
};

/* YYRLINE[YYN] -- source line where rule number YYN was defined.  */
static const yytype_uint16 yyrline[] =
{
       0,   167,   167,   189,   205,   206,   209,   210,   213,   214,
     217,   218,   221,   222,   225,   226,   227,   230,   231,   234,
     235,   238,   241,   244,   245,   246,   249,   250,   253,   260,
     263,   266,   267,   270,   271,   272,   273,   274,   277,   278,
     288,   289,   297,   305,   315,   321,   327,   333,   339,   346,
     349,   352,   353,   354,   355,   366,   370,   369,   386,   385,
     394,   411
};
#endif

#if YYDEBUG || YYERROR_VERBOSE || YYTOKEN_TABLE
/* YYTNAME[SYMBOL-NUM] -- String name of the symbol SYMBOL-NUM.
   First, the terminals, then, starting at YYNTOKENS, nonterminals.  */
static const char *const yytname[] =
{
  "$end", "error", "$undefined", "ID", "INTEGER", "REAL_NUMBER", "STRING",
  "HASH", "INCLUDE_CPP", "HEADER_CPP", "USING", "SEMICOLON", "INT",
  "DOUBLE", "STRUCT", "VOID", "RETURN", "CIN", "COUT", "ENDL", "COMMA",
  "DOT", "OPEN_PAR", "CLOSE_PAR", "OPEN_BRACE", "CLOSE_BRACE", "ASSIGN",
  "PLUS", "TIMES", "MOD", "DIV", "SHIFT_LEFT", "SHIFT_RIGHT", "LESS_THAN",
  "GREATER_THAN", "NAMESPACE", "ACTUAL_NAMESPACE", "$accept", "program",
  "preamble", "multiple_includes", "usings", "decl_functions", "function",
  "antet", "tip", "lista_decl", "lista_decl_antet", "decl_param", "decl",
  "sir_decl", "tipId", "atr", "corp", "instr_comp", "instr", "expr_aritm",
  "term", "factor", "instr_return", "instr_citire", "lista_de_citit",
  "instr_afisare", "lista_de_iesiri", "$@1", "$@2", 0
};
#endif

# ifdef YYPRINT
/* YYTOKNUM[YYLEX-NUM] -- Internal token number corresponding to
   token YYLEX-NUM.  */
static const yytype_uint16 yytoknum[] =
{
       0,   256,   257,   258,   259,   260,   261,   262,   263,   264,
     265,   266,   267,   268,   269,   270,   271,   272,   273,   274,
     275,   276,   277,   278,   279,   280,   281,   282,   283,   284,
     285,   286,   287,   288,   289,   290,   291
};
# endif

/* YYR1[YYN] -- Symbol number of symbol that rule YYN derives.  */
static const yytype_uint8 yyr1[] =
{
       0,    37,    38,    39,    40,    40,    41,    41,    42,    42,
      43,    43,    44,    44,    45,    45,    45,    46,    46,    47,
      47,    48,    49,    50,    50,    50,    51,    51,    52,    52,
      53,    54,    54,    55,    55,    55,    55,    55,    56,    56,
      57,    57,    57,    57,    58,    58,    58,    58,    58,    59,
      60,    61,    61,    61,    61,    62,    64,    63,    65,    63,
      63,    63
};

/* YYR2[YYN] -- Number of symbols composing right hand side of rule YYN.  */
static const yytype_uint8 yyr2[] =
{
       0,     2,     4,     0,     6,     5,     5,     4,     1,     2,
       2,     6,     5,     4,     1,     1,     1,     2,     1,     3,
       1,     2,     3,     3,     1,     1,     1,     1,     3,     5,
       3,     2,     1,     2,     1,     1,     1,     1,     1,     3,
       1,     3,     3,     3,     1,     1,     1,     1,     3,     3,
       3,     3,     5,     4,     2,     3,     0,     4,     0,     4,
       2,     2
};

/* YYDEFACT[STATE-NAME] -- Default rule to reduce with in state
   STATE-NUM when YYTABLE doesn't specify something else to do.  Zero
   means the default is an error.  */
static const yytype_uint8 yydefact[] =
{
       3,     0,     0,     1,     0,     0,     0,     0,     0,     0,
       0,    26,    14,    15,     0,    16,     2,     8,     0,    27,
       0,     0,     0,     0,     9,     0,    10,     0,     5,     7,
       0,    26,     0,     0,     0,    35,     0,     0,     0,    32,
      34,    36,    37,     0,     4,     6,     0,    18,     0,    44,
      45,    46,    47,     0,     0,    38,    40,     0,     0,     0,
       0,    24,     0,    25,    33,    30,    31,    13,     0,    20,
       0,     0,    17,    28,     0,    49,     0,     0,     0,     0,
      54,    50,    58,    56,    55,     0,    22,    12,     0,    21,
      11,     0,    48,    39,    41,    42,    43,     0,    51,     0,
       0,    23,    19,     0,    29,    53,    59,    57,    52
};

/* YYDEFGOTO[NTERM-NUM].  */
static const yytype_int8 yydefgoto[] =
{
      -1,     1,     2,     5,     8,    16,    17,    18,    19,    46,
      68,    69,    35,    62,    36,    37,    26,    38,    39,    54,
      55,    56,    40,    41,    58,    42,    60,   100,    99
};

/* YYPACT[STATE-NUM] -- Index in YYTABLE of the portion describing
   STATE-NUM.  */
#define YYPACT_NINF -79
static const yytype_int8 yypact[] =
{
     -79,    16,    -3,   -79,    17,    30,    12,    39,    19,    53,
      29,   -79,   -79,   -79,    66,   -79,   -79,    19,    49,   -79,
      72,    42,    67,    55,   -79,    26,   -79,    59,    -3,    30,
      34,    51,    14,    50,    52,   -79,    81,    74,    61,    26,
     -79,   -79,   -79,     0,   -79,   -79,    62,    34,    14,   -79,
     -79,   -79,   -79,    14,    -1,    38,   -79,    85,    78,     2,
      79,    35,    80,   -79,   -79,   -79,   -79,   -79,    69,    73,
      91,    84,   -79,    31,    37,   -79,    14,    14,    14,    14,
      27,   -79,    86,     3,   -79,    81,   -79,   -79,    34,   -79,
     -79,    93,   -79,    38,   -79,   -79,   -79,    95,   -79,    52,
      52,   -79,   -79,    51,   -79,    50,   -79,   -79,   -79
};

/* YYPGOTO[NTERM-NUM].  */
static const yytype_int8 yypgoto[] =
{
     -79,   -79,   -79,    71,    75,    83,   -79,   -79,   -79,    54,
      15,   -79,   -19,    20,    -8,   -35,   -79,    63,   -79,     4,
      32,    -7,   -79,   -79,   -78,   -79,   -46,   -79,   -79
};

/* YYTABLE[YYPACT[STATE-NUM]].  What to do in state STATE-NUM.  If
   positive, shift that token.  If negative, reduce the rule which
   number is the opposite.  If zero, do what YYDEFACT says.
   If YYTABLE_NINF, syntax error.  */
#define YYTABLE_NINF -62
static const yytype_int8 yytable[] =
{
      20,    63,    98,    11,     4,    49,    50,    51,    52,    20,
      75,    47,    12,    13,   -60,    15,     3,    49,    50,    51,
      52,    82,    11,    67,    53,     6,    76,   108,    47,    31,
      76,    12,    13,    14,    15,    70,    53,    11,    12,    13,
       7,    15,    32,    33,    34,     9,    12,    13,    97,    15,
      63,    91,    73,   106,   107,    85,   104,    74,    76,    57,
      92,    48,    21,    83,    76,    22,    77,    78,    79,    23,
      94,    95,    96,    25,    10,    27,    28,    48,    29,    30,
      70,    43,    57,    59,    61,    64,    65,    71,    80,    81,
      84,    86,    87,    88,    89,    90,   103,   -61,   105,    44,
      24,    72,    66,   102,    45,   101,     0,     0,    93
};

static const yytype_int8 yycheck[] =
{
       8,    36,    80,     3,     7,     3,     4,     5,     6,    17,
      11,    30,    12,    13,    11,    15,     0,     3,     4,     5,
       6,    19,     3,    23,    22,     8,    27,   105,    47,     3,
      27,    12,    13,    14,    15,    43,    22,     3,    12,    13,
      10,    15,    16,    17,    18,    33,    12,    13,    21,    15,
      85,    20,    48,    99,   100,    20,    91,    53,    27,    32,
      23,    26,     9,    59,    27,    36,    28,    29,    30,     3,
      77,    78,    79,    24,    35,     3,    34,    26,    11,    24,
      88,    22,    32,    31,     3,    11,    25,    25,     3,    11,
      11,    11,    23,    20,     3,    11,     3,    11,     3,    28,
      17,    47,    39,    88,    29,    85,    -1,    -1,    76
};

/* YYSTOS[STATE-NUM] -- The (internal number of the) accessing
   symbol of state STATE-NUM.  */
static const yytype_uint8 yystos[] =
{
       0,    38,    39,     0,     7,    40,     8,    10,    41,    33,
      35,     3,    12,    13,    14,    15,    42,    43,    44,    45,
      51,     9,    36,     3,    42,    24,    53,     3,    34,    11,
      24,     3,    16,    17,    18,    49,    51,    52,    54,    55,
      59,    60,    62,    22,    40,    41,    46,    49,    26,     3,
       4,     5,     6,    22,    56,    57,    58,    32,    61,    31,
      63,     3,    50,    52,    11,    25,    54,    23,    47,    48,
      51,    25,    46,    56,    56,    11,    27,    28,    29,    30,
       3,    11,    19,    56,    11,    20,    11,    23,    20,     3,
      11,    20,    23,    57,    58,    58,    58,    21,    61,    65,
      64,    50,    47,     3,    52,     3,    63,    63,    61
};

#define yyerrok		(yyerrstatus = 0)
#define yyclearin	(yychar = YYEMPTY)
#define YYEMPTY		(-2)
#define YYEOF		0

#define YYACCEPT	goto yyacceptlab
#define YYABORT		goto yyabortlab
#define YYERROR		goto yyerrorlab


/* Like YYERROR except do call yyerror.  This remains here temporarily
   to ease the transition to the new meaning of YYERROR, for GCC.
   Once GCC version 2 has supplanted version 1, this can go.  */

#define YYFAIL		goto yyerrlab

#define YYRECOVERING()  (!!yyerrstatus)

#define YYBACKUP(Token, Value)					\
do								\
  if (yychar == YYEMPTY && yylen == 1)				\
    {								\
      yychar = (Token);						\
      yylval = (Value);						\
      yytoken = YYTRANSLATE (yychar);				\
      YYPOPSTACK (1);						\
      goto yybackup;						\
    }								\
  else								\
    {								\
      yyerror (YY_("syntax error: cannot back up")); \
      YYERROR;							\
    }								\
while (YYID (0))


#define YYTERROR	1
#define YYERRCODE	256


/* YYLLOC_DEFAULT -- Set CURRENT to span from RHS[1] to RHS[N].
   If N is 0, then set CURRENT to the empty location which ends
   the previous symbol: RHS[0] (always defined).  */

#define YYRHSLOC(Rhs, K) ((Rhs)[K])
#ifndef YYLLOC_DEFAULT
# define YYLLOC_DEFAULT(Current, Rhs, N)				\
    do									\
      if (YYID (N))                                                    \
	{								\
	  (Current).first_line   = YYRHSLOC (Rhs, 1).first_line;	\
	  (Current).first_column = YYRHSLOC (Rhs, 1).first_column;	\
	  (Current).last_line    = YYRHSLOC (Rhs, N).last_line;		\
	  (Current).last_column  = YYRHSLOC (Rhs, N).last_column;	\
	}								\
      else								\
	{								\
	  (Current).first_line   = (Current).last_line   =		\
	    YYRHSLOC (Rhs, 0).last_line;				\
	  (Current).first_column = (Current).last_column =		\
	    YYRHSLOC (Rhs, 0).last_column;				\
	}								\
    while (YYID (0))
#endif


/* YY_LOCATION_PRINT -- Print the location on the stream.
   This macro was not mandated originally: define only if we know
   we won't break user code: when these are the locations we know.  */

#ifndef YY_LOCATION_PRINT
# if YYLTYPE_IS_TRIVIAL
#  define YY_LOCATION_PRINT(File, Loc)			\
     fprintf (File, "%d.%d-%d.%d",			\
	      (Loc).first_line, (Loc).first_column,	\
	      (Loc).last_line,  (Loc).last_column)
# else
#  define YY_LOCATION_PRINT(File, Loc) ((void) 0)
# endif
#endif


/* YYLEX -- calling `yylex' with the right arguments.  */

#ifdef YYLEX_PARAM
# define YYLEX yylex (YYLEX_PARAM)
#else
# define YYLEX yylex ()
#endif

/* Enable debugging if requested.  */
#if YYDEBUG

# ifndef YYFPRINTF
#  include <stdio.h> /* INFRINGES ON USER NAME SPACE */
#  define YYFPRINTF fprintf
# endif

# define YYDPRINTF(Args)			\
do {						\
  if (yydebug)					\
    YYFPRINTF Args;				\
} while (YYID (0))

# define YY_SYMBOL_PRINT(Title, Type, Value, Location)			  \
do {									  \
  if (yydebug)								  \
    {									  \
      YYFPRINTF (stderr, "%s ", Title);					  \
      yy_symbol_print (stderr,						  \
		  Type, Value); \
      YYFPRINTF (stderr, "\n");						  \
    }									  \
} while (YYID (0))


/*--------------------------------.
| Print this symbol on YYOUTPUT.  |
`--------------------------------*/

/*ARGSUSED*/
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_symbol_value_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep)
#else
static void
yy_symbol_value_print (yyoutput, yytype, yyvaluep)
    FILE *yyoutput;
    int yytype;
    YYSTYPE const * const yyvaluep;
#endif
{
  if (!yyvaluep)
    return;
# ifdef YYPRINT
  if (yytype < YYNTOKENS)
    YYPRINT (yyoutput, yytoknum[yytype], *yyvaluep);
# else
  YYUSE (yyoutput);
# endif
  switch (yytype)
    {
      default:
	break;
    }
}


/*--------------------------------.
| Print this symbol on YYOUTPUT.  |
`--------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_symbol_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep)
#else
static void
yy_symbol_print (yyoutput, yytype, yyvaluep)
    FILE *yyoutput;
    int yytype;
    YYSTYPE const * const yyvaluep;
#endif
{
  if (yytype < YYNTOKENS)
    YYFPRINTF (yyoutput, "token %s (", yytname[yytype]);
  else
    YYFPRINTF (yyoutput, "nterm %s (", yytname[yytype]);

  yy_symbol_value_print (yyoutput, yytype, yyvaluep);
  YYFPRINTF (yyoutput, ")");
}

/*------------------------------------------------------------------.
| yy_stack_print -- Print the state stack from its BOTTOM up to its |
| TOP (included).                                                   |
`------------------------------------------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_stack_print (yytype_int16 *yybottom, yytype_int16 *yytop)
#else
static void
yy_stack_print (yybottom, yytop)
    yytype_int16 *yybottom;
    yytype_int16 *yytop;
#endif
{
  YYFPRINTF (stderr, "Stack now");
  for (; yybottom <= yytop; yybottom++)
    {
      int yybot = *yybottom;
      YYFPRINTF (stderr, " %d", yybot);
    }
  YYFPRINTF (stderr, "\n");
}

# define YY_STACK_PRINT(Bottom, Top)				\
do {								\
  if (yydebug)							\
    yy_stack_print ((Bottom), (Top));				\
} while (YYID (0))


/*------------------------------------------------.
| Report that the YYRULE is going to be reduced.  |
`------------------------------------------------*/

#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yy_reduce_print (YYSTYPE *yyvsp, int yyrule)
#else
static void
yy_reduce_print (yyvsp, yyrule)
    YYSTYPE *yyvsp;
    int yyrule;
#endif
{
  int yynrhs = yyr2[yyrule];
  int yyi;
  unsigned long int yylno = yyrline[yyrule];
  YYFPRINTF (stderr, "Reducing stack by rule %d (line %lu):\n",
	     yyrule - 1, yylno);
  /* The symbols being reduced.  */
  for (yyi = 0; yyi < yynrhs; yyi++)
    {
      YYFPRINTF (stderr, "   $%d = ", yyi + 1);
      yy_symbol_print (stderr, yyrhs[yyprhs[yyrule] + yyi],
		       &(yyvsp[(yyi + 1) - (yynrhs)])
		       		       );
      YYFPRINTF (stderr, "\n");
    }
}

# define YY_REDUCE_PRINT(Rule)		\
do {					\
  if (yydebug)				\
    yy_reduce_print (yyvsp, Rule); \
} while (YYID (0))

/* Nonzero means print parse trace.  It is left uninitialized so that
   multiple parsers can coexist.  */
int yydebug;
#else /* !YYDEBUG */
# define YYDPRINTF(Args)
# define YY_SYMBOL_PRINT(Title, Type, Value, Location)
# define YY_STACK_PRINT(Bottom, Top)
# define YY_REDUCE_PRINT(Rule)
#endif /* !YYDEBUG */


/* YYINITDEPTH -- initial size of the parser's stacks.  */
#ifndef	YYINITDEPTH
# define YYINITDEPTH 200
#endif

/* YYMAXDEPTH -- maximum size the stacks can grow to (effective only
   if the built-in stack extension method is used).

   Do not make this value too large; the results are undefined if
   YYSTACK_ALLOC_MAXIMUM < YYSTACK_BYTES (YYMAXDEPTH)
   evaluated with infinite-precision integer arithmetic.  */

#ifndef YYMAXDEPTH
# define YYMAXDEPTH 10000
#endif



#if YYERROR_VERBOSE

# ifndef yystrlen
#  if defined __GLIBC__ && defined _STRING_H
#   define yystrlen strlen
#  else
/* Return the length of YYSTR.  */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static YYSIZE_T
yystrlen (const char *yystr)
#else
static YYSIZE_T
yystrlen (yystr)
    const char *yystr;
#endif
{
  YYSIZE_T yylen;
  for (yylen = 0; yystr[yylen]; yylen++)
    continue;
  return yylen;
}
#  endif
# endif

# ifndef yystpcpy
#  if defined __GLIBC__ && defined _STRING_H && defined _GNU_SOURCE
#   define yystpcpy stpcpy
#  else
/* Copy YYSRC to YYDEST, returning the address of the terminating '\0' in
   YYDEST.  */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static char *
yystpcpy (char *yydest, const char *yysrc)
#else
static char *
yystpcpy (yydest, yysrc)
    char *yydest;
    const char *yysrc;
#endif
{
  char *yyd = yydest;
  const char *yys = yysrc;

  while ((*yyd++ = *yys++) != '\0')
    continue;

  return yyd - 1;
}
#  endif
# endif

# ifndef yytnamerr
/* Copy to YYRES the contents of YYSTR after stripping away unnecessary
   quotes and backslashes, so that it's suitable for yyerror.  The
   heuristic is that double-quoting is unnecessary unless the string
   contains an apostrophe, a comma, or backslash (other than
   backslash-backslash).  YYSTR is taken from yytname.  If YYRES is
   null, do not copy; instead, return the length of what the result
   would have been.  */
static YYSIZE_T
yytnamerr (char *yyres, const char *yystr)
{
  if (*yystr == '"')
    {
      YYSIZE_T yyn = 0;
      char const *yyp = yystr;

      for (;;)
	switch (*++yyp)
	  {
	  case '\'':
	  case ',':
	    goto do_not_strip_quotes;

	  case '\\':
	    if (*++yyp != '\\')
	      goto do_not_strip_quotes;
	    /* Fall through.  */
	  default:
	    if (yyres)
	      yyres[yyn] = *yyp;
	    yyn++;
	    break;

	  case '"':
	    if (yyres)
	      yyres[yyn] = '\0';
	    return yyn;
	  }
    do_not_strip_quotes: ;
    }

  if (! yyres)
    return yystrlen (yystr);

  return yystpcpy (yyres, yystr) - yyres;
}
# endif

/* Copy into YYRESULT an error message about the unexpected token
   YYCHAR while in state YYSTATE.  Return the number of bytes copied,
   including the terminating null byte.  If YYRESULT is null, do not
   copy anything; just return the number of bytes that would be
   copied.  As a special case, return 0 if an ordinary "syntax error"
   message will do.  Return YYSIZE_MAXIMUM if overflow occurs during
   size calculation.  */
static YYSIZE_T
yysyntax_error (char *yyresult, int yystate, int yychar)
{
  int yyn = yypact[yystate];

  if (! (YYPACT_NINF < yyn && yyn <= YYLAST))
    return 0;
  else
    {
      int yytype = YYTRANSLATE (yychar);
      YYSIZE_T yysize0 = yytnamerr (0, yytname[yytype]);
      YYSIZE_T yysize = yysize0;
      YYSIZE_T yysize1;
      int yysize_overflow = 0;
      enum { YYERROR_VERBOSE_ARGS_MAXIMUM = 5 };
      char const *yyarg[YYERROR_VERBOSE_ARGS_MAXIMUM];
      int yyx;

# if 0
      /* This is so xgettext sees the translatable formats that are
	 constructed on the fly.  */
      YY_("syntax error, unexpected %s");
      YY_("syntax error, unexpected %s, expecting %s");
      YY_("syntax error, unexpected %s, expecting %s or %s");
      YY_("syntax error, unexpected %s, expecting %s or %s or %s");
      YY_("syntax error, unexpected %s, expecting %s or %s or %s or %s");
# endif
      char *yyfmt;
      char const *yyf;
      static char const yyunexpected[] = "syntax error, unexpected %s";
      static char const yyexpecting[] = ", expecting %s";
      static char const yyor[] = " or %s";
      char yyformat[sizeof yyunexpected
		    + sizeof yyexpecting - 1
		    + ((YYERROR_VERBOSE_ARGS_MAXIMUM - 2)
		       * (sizeof yyor - 1))];
      char const *yyprefix = yyexpecting;

      /* Start YYX at -YYN if negative to avoid negative indexes in
	 YYCHECK.  */
      int yyxbegin = yyn < 0 ? -yyn : 0;

      /* Stay within bounds of both yycheck and yytname.  */
      int yychecklim = YYLAST - yyn + 1;
      int yyxend = yychecklim < YYNTOKENS ? yychecklim : YYNTOKENS;
      int yycount = 1;

      yyarg[0] = yytname[yytype];
      yyfmt = yystpcpy (yyformat, yyunexpected);

      for (yyx = yyxbegin; yyx < yyxend; ++yyx)
	if (yycheck[yyx + yyn] == yyx && yyx != YYTERROR)
	  {
	    if (yycount == YYERROR_VERBOSE_ARGS_MAXIMUM)
	      {
		yycount = 1;
		yysize = yysize0;
		yyformat[sizeof yyunexpected - 1] = '\0';
		break;
	      }
	    yyarg[yycount++] = yytname[yyx];
	    yysize1 = yysize + yytnamerr (0, yytname[yyx]);
	    yysize_overflow |= (yysize1 < yysize);
	    yysize = yysize1;
	    yyfmt = yystpcpy (yyfmt, yyprefix);
	    yyprefix = yyor;
	  }

      yyf = YY_(yyformat);
      yysize1 = yysize + yystrlen (yyf);
      yysize_overflow |= (yysize1 < yysize);
      yysize = yysize1;

      if (yysize_overflow)
	return YYSIZE_MAXIMUM;

      if (yyresult)
	{
	  /* Avoid sprintf, as that infringes on the user's name space.
	     Don't have undefined behavior even if the translation
	     produced a string with the wrong number of "%s"s.  */
	  char *yyp = yyresult;
	  int yyi = 0;
	  while ((*yyp = *yyf) != '\0')
	    {
	      if (*yyp == '%' && yyf[1] == 's' && yyi < yycount)
		{
		  yyp += yytnamerr (yyp, yyarg[yyi++]);
		  yyf += 2;
		}
	      else
		{
		  yyp++;
		  yyf++;
		}
	    }
	}
      return yysize;
    }
}
#endif /* YYERROR_VERBOSE */


/*-----------------------------------------------.
| Release the memory associated to this symbol.  |
`-----------------------------------------------*/

/*ARGSUSED*/
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
static void
yydestruct (const char *yymsg, int yytype, YYSTYPE *yyvaluep)
#else
static void
yydestruct (yymsg, yytype, yyvaluep)
    const char *yymsg;
    int yytype;
    YYSTYPE *yyvaluep;
#endif
{
  YYUSE (yyvaluep);

  if (!yymsg)
    yymsg = "Deleting";
  YY_SYMBOL_PRINT (yymsg, yytype, yyvaluep, yylocationp);

  switch (yytype)
    {

      default:
	break;
    }
}

/* Prevent warnings from -Wmissing-prototypes.  */
#ifdef YYPARSE_PARAM
#if defined __STDC__ || defined __cplusplus
int yyparse (void *YYPARSE_PARAM);
#else
int yyparse ();
#endif
#else /* ! YYPARSE_PARAM */
#if defined __STDC__ || defined __cplusplus
int yyparse (void);
#else
int yyparse ();
#endif
#endif /* ! YYPARSE_PARAM */


/* The lookahead symbol.  */
int yychar;

/* The semantic value of the lookahead symbol.  */
YYSTYPE yylval;

/* Number of syntax errors so far.  */
int yynerrs;



/*-------------------------.
| yyparse or yypush_parse.  |
`-------------------------*/

#ifdef YYPARSE_PARAM
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
int
yyparse (void *YYPARSE_PARAM)
#else
int
yyparse (YYPARSE_PARAM)
    void *YYPARSE_PARAM;
#endif
#else /* ! YYPARSE_PARAM */
#if (defined __STDC__ || defined __C99__FUNC__ \
     || defined __cplusplus || defined _MSC_VER)
int
yyparse (void)
#else
int
yyparse ()

#endif
#endif
{


    int yystate;
    /* Number of tokens to shift before error messages enabled.  */
    int yyerrstatus;

    /* The stacks and their tools:
       `yyss': related to states.
       `yyvs': related to semantic values.

       Refer to the stacks thru separate pointers, to allow yyoverflow
       to reallocate them elsewhere.  */

    /* The state stack.  */
    yytype_int16 yyssa[YYINITDEPTH];
    yytype_int16 *yyss;
    yytype_int16 *yyssp;

    /* The semantic value stack.  */
    YYSTYPE yyvsa[YYINITDEPTH];
    YYSTYPE *yyvs;
    YYSTYPE *yyvsp;

    YYSIZE_T yystacksize;

  int yyn;
  int yyresult;
  /* Lookahead token as an internal (translated) token number.  */
  int yytoken;
  /* The variables used to return semantic value and location from the
     action routines.  */
  YYSTYPE yyval;

#if YYERROR_VERBOSE
  /* Buffer for error messages, and its allocated size.  */
  char yymsgbuf[128];
  char *yymsg = yymsgbuf;
  YYSIZE_T yymsg_alloc = sizeof yymsgbuf;
#endif

#define YYPOPSTACK(N)   (yyvsp -= (N), yyssp -= (N))

  /* The number of symbols on the RHS of the reduced rule.
     Keep to zero when no symbol should be popped.  */
  int yylen = 0;

  yytoken = 0;
  yyss = yyssa;
  yyvs = yyvsa;
  yystacksize = YYINITDEPTH;

  YYDPRINTF ((stderr, "Starting parse\n"));

  yystate = 0;
  yyerrstatus = 0;
  yynerrs = 0;
  yychar = YYEMPTY; /* Cause a token to be read.  */

  /* Initialize stack pointers.
     Waste one element of value and location stack
     so that they stay on the same level as the state stack.
     The wasted elements are never initialized.  */
  yyssp = yyss;
  yyvsp = yyvs;

  goto yysetstate;

/*------------------------------------------------------------.
| yynewstate -- Push a new state, which is found in yystate.  |
`------------------------------------------------------------*/
 yynewstate:
  /* In all cases, when you get here, the value and location stacks
     have just been pushed.  So pushing a state here evens the stacks.  */
  yyssp++;

 yysetstate:
  *yyssp = yystate;

  if (yyss + yystacksize - 1 <= yyssp)
    {
      /* Get the current used size of the three stacks, in elements.  */
      YYSIZE_T yysize = yyssp - yyss + 1;

#ifdef yyoverflow
      {
	/* Give user a chance to reallocate the stack.  Use copies of
	   these so that the &'s don't force the real ones into
	   memory.  */
	YYSTYPE *yyvs1 = yyvs;
	yytype_int16 *yyss1 = yyss;

	/* Each stack pointer address is followed by the size of the
	   data in use in that stack, in bytes.  This used to be a
	   conditional around just the two extra args, but that might
	   be undefined if yyoverflow is a macro.  */
	yyoverflow (YY_("memory exhausted"),
		    &yyss1, yysize * sizeof (*yyssp),
		    &yyvs1, yysize * sizeof (*yyvsp),
		    &yystacksize);

	yyss = yyss1;
	yyvs = yyvs1;
      }
#else /* no yyoverflow */
# ifndef YYSTACK_RELOCATE
      goto yyexhaustedlab;
# else
      /* Extend the stack our own way.  */
      if (YYMAXDEPTH <= yystacksize)
	goto yyexhaustedlab;
      yystacksize *= 2;
      if (YYMAXDEPTH < yystacksize)
	yystacksize = YYMAXDEPTH;

      {
	yytype_int16 *yyss1 = yyss;
	union yyalloc *yyptr =
	  (union yyalloc *) YYSTACK_ALLOC (YYSTACK_BYTES (yystacksize));
	if (! yyptr)
	  goto yyexhaustedlab;
	YYSTACK_RELOCATE (yyss_alloc, yyss);
	YYSTACK_RELOCATE (yyvs_alloc, yyvs);
#  undef YYSTACK_RELOCATE
	if (yyss1 != yyssa)
	  YYSTACK_FREE (yyss1);
      }
# endif
#endif /* no yyoverflow */

      yyssp = yyss + yysize - 1;
      yyvsp = yyvs + yysize - 1;

      YYDPRINTF ((stderr, "Stack size increased to %lu\n",
		  (unsigned long int) yystacksize));

      if (yyss + yystacksize - 1 <= yyssp)
	YYABORT;
    }

  YYDPRINTF ((stderr, "Entering state %d\n", yystate));

  if (yystate == YYFINAL)
    YYACCEPT;

  goto yybackup;

/*-----------.
| yybackup.  |
`-----------*/
yybackup:

  /* Do appropriate processing given the current state.  Read a
     lookahead token if we need one and don't already have one.  */

  /* First try to decide what to do without reference to lookahead token.  */
  yyn = yypact[yystate];
  if (yyn == YYPACT_NINF)
    goto yydefault;

  /* Not known => get a lookahead token if don't already have one.  */

  /* YYCHAR is either YYEMPTY or YYEOF or a valid lookahead symbol.  */
  if (yychar == YYEMPTY)
    {
      YYDPRINTF ((stderr, "Reading a token: "));
      yychar = YYLEX;
    }

  if (yychar <= YYEOF)
    {
      yychar = yytoken = YYEOF;
      YYDPRINTF ((stderr, "Now at end of input.\n"));
    }
  else
    {
      yytoken = YYTRANSLATE (yychar);
      YY_SYMBOL_PRINT ("Next token is", yytoken, &yylval, &yylloc);
    }

  /* If the proper action on seeing token YYTOKEN is to reduce or to
     detect an error, take that action.  */
  yyn += yytoken;
  if (yyn < 0 || YYLAST < yyn || yycheck[yyn] != yytoken)
    goto yydefault;
  yyn = yytable[yyn];
  if (yyn <= 0)
    {
      if (yyn == 0 || yyn == YYTABLE_NINF)
	goto yyerrlab;
      yyn = -yyn;
      goto yyreduce;
    }

  /* Count tokens shifted since error; after three, turn off error
     status.  */
  if (yyerrstatus)
    yyerrstatus--;

  /* Shift the lookahead token.  */
  YY_SYMBOL_PRINT ("Shifting", yytoken, &yylval, &yylloc);

  /* Discard the shifted token.  */
  yychar = YYEMPTY;

  yystate = yyn;
  *++yyvsp = yylval;

  goto yynewstate;


/*-----------------------------------------------------------.
| yydefault -- do the default action for the current state.  |
`-----------------------------------------------------------*/
yydefault:
  yyn = yydefact[yystate];
  if (yyn == 0)
    goto yyerrlab;
  goto yyreduce;


/*-----------------------------.
| yyreduce -- Do a reduction.  |
`-----------------------------*/
yyreduce:
  /* yyn is the number of a rule to reduce with.  */
  yylen = yyr2[yyn];

  /* If YYLEN is nonzero, implement the default value of the action:
     `$$ = $1'.

     Otherwise, the following line sets YYVAL to garbage.
     This behavior is undocumented and Bison
     users should not rely upon it.  Assigning to YYVAL
     unconditionally makes the parser a bit smaller, and it avoids a
     GCC warning that YYVAL may be used uninitialized.  */
  yyval = yyvsp[1-yylen];


  YY_REDUCE_PRINT (yyn);
  switch (yyn)
    {
        case 2:

/* Line 1455 of yacc.c  */
#line 168 "lab6.y"
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
        ;}
    break;

  case 3:

/* Line 1455 of yacc.c  */
#line 189 "lab6.y"
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
        ;}
    break;

  case 23:

/* Line 1455 of yacc.c  */
#line 244 "lab6.y"
    {addTempVariable((yyvsp[(1) - (3)].varname));;}
    break;

  case 24:

/* Line 1455 of yacc.c  */
#line 245 "lab6.y"
    {addTempVariable((yyvsp[(1) - (1)].varname));;}
    break;

  case 28:

/* Line 1455 of yacc.c  */
#line 254 "lab6.y"
    {
                        printf("%s\n",(yyvsp[(3) - (3)].attrib).code);
                        printf("mov eax, %s\n",(yyvsp[(3) - (3)].attrib).varn);
                        printf("mov %s,eax\n",(yyvsp[(1) - (3)].varname));
                        addTempVariable((yyvsp[(1) - (3)].varname));
                    ;}
    break;

  case 39:

/* Line 1455 of yacc.c  */
#line 279 "lab6.y"
    { 
                newTempName((yyval.attrib).varn);
                sprintf((yyval.attrib).code,"%s\n%s\n",(yyvsp[(1) - (3)].attrib).code,(yyvsp[(3) - (3)].attrib).code);
                sprintf(tempbuffer,ADD_ASM_FORMAT,(yyvsp[(1) - (3)].attrib).varn,(yyvsp[(3) - (3)].attrib).varn,(yyval.attrib).varn);
                strcat((yyval.attrib).code,tempbuffer);
                addTempVariable((yyval.attrib).varn); 
            ;}
    break;

  case 41:

/* Line 1455 of yacc.c  */
#line 290 "lab6.y"
    { 
            newTempName((yyval.attrib).varn);
            sprintf((yyval.attrib).code, "%s\n%s\n", (yyvsp[(1) - (3)].attrib).code, (yyvsp[(3) - (3)].attrib).code);
            sprintf(tempbuffer, MUL_ASM_FORMAT, (yyvsp[(1) - (3)].attrib).varn, (yyvsp[(3) - (3)].attrib).varn, (yyval.attrib).varn);
            strcat((yyval.attrib).code, tempbuffer);
            addTempVariable((yyval.attrib).varn); 
        ;}
    break;

  case 42:

/* Line 1455 of yacc.c  */
#line 298 "lab6.y"
    { 
            newTempName((yyval.attrib).varn);
            sprintf((yyval.attrib).code, "%s\n%s\n", (yyvsp[(1) - (3)].attrib).code, (yyvsp[(3) - (3)].attrib).code);
            sprintf(tempbuffer, MOD_ASM_FORMAT, (yyvsp[(1) - (3)].attrib).varn, (yyvsp[(3) - (3)].attrib).varn, (yyval.attrib).varn);
            strcat((yyval.attrib).code, tempbuffer);
            addTempVariable((yyval.attrib).varn); 
        ;}
    break;

  case 43:

/* Line 1455 of yacc.c  */
#line 306 "lab6.y"
    { 
            newTempName((yyval.attrib).varn);
            sprintf((yyval.attrib).code, "%s\n%s\n", (yyvsp[(1) - (3)].attrib).code, (yyvsp[(3) - (3)].attrib).code);
            sprintf(tempbuffer, DIV_ASM_FORMAT, (yyvsp[(1) - (3)].attrib).varn, (yyvsp[(3) - (3)].attrib).varn, (yyval.attrib).varn);
            strcat((yyval.attrib).code, tempbuffer);
            addTempVariable((yyval.attrib).varn); 
        ;}
    break;

  case 44:

/* Line 1455 of yacc.c  */
#line 316 "lab6.y"
    {
            strcpy((yyval.attrib).code,"");
            strcpy((yyval.attrib).varn,(yyvsp[(1) - (1)].varname)); 
            strcpy((yyval.attrib).type,"ID");
        ;}
    break;

  case 45:

/* Line 1455 of yacc.c  */
#line 322 "lab6.y"
    {
            strcpy((yyval.attrib).code,"");
            strcpy((yyval.attrib).varn,(yyvsp[(1) - (1)].varname)); 
            strcpy((yyval.attrib).type,"INTEGER");
        ;}
    break;

  case 46:

/* Line 1455 of yacc.c  */
#line 328 "lab6.y"
    {
            strcpy((yyval.attrib).code,"");
            strcpy((yyval.attrib).varn,(yyvsp[(1) - (1)].varname)); 
            strcpy((yyval.attrib).type,"REAL");
        ;}
    break;

  case 47:

/* Line 1455 of yacc.c  */
#line 334 "lab6.y"
    {
            strcpy((yyval.attrib).code,"");
            strcpy((yyval.attrib).varn,(yyvsp[(1) - (1)].varname)); 
            strcpy((yyval.attrib).type,"STRING");
        ;}
    break;

  case 48:

/* Line 1455 of yacc.c  */
#line 340 "lab6.y"
    {
            strcpy((yyval.attrib).code,(yyvsp[(2) - (3)].attrib).code);
            strcpy((yyval.attrib).varn,(yyvsp[(2) - (3)].attrib).varn);
        ;}
    break;

  case 51:

/* Line 1455 of yacc.c  */
#line 352 "lab6.y"
    {;;}
    break;

  case 52:

/* Line 1455 of yacc.c  */
#line 353 "lab6.y"
    {;;}
    break;

  case 53:

/* Line 1455 of yacc.c  */
#line 354 "lab6.y"
    {;;}
    break;

  case 54:

/* Line 1455 of yacc.c  */
#line 356 "lab6.y"
    { 
                    char cleanID[10];
                    removeBrackets(cleanID, (yyvsp[(2) - (2)].varname));
                    sprintf(tempbuffer, SCANF_ASM_FORMAT, cleanID);
                    printf("%s\n", tempbuffer);
                    strcat((yyval.attrib).code, tempbuffer);
                    addTempVariable((yyvsp[(2) - (2)].varname));
                ;}
    break;

  case 56:

/* Line 1455 of yacc.c  */
#line 370 "lab6.y"
    { 
                    char format[30];
                    computeFormatAfisare((yyvsp[(2) - (2)].attrib).varn, (yyvsp[(2) - (2)].attrib).type, format, sizeof(format));
                    if (strcmp((yyvsp[(2) - (2)].attrib).type, "ID") != 0) 
                    {
                        sprintf(tempbuffer, PRINTF_CONST_ASM_FORMAT, format);
                        printf("%s\n", tempbuffer);
                    }
                    else
                    {
                        sprintf(tempbuffer, PRINTF_ASM_FORMAT, (yyvsp[(2) - (2)].attrib), format);
                        printf("%s\n", tempbuffer);
                    }
                ;}
    break;

  case 57:

/* Line 1455 of yacc.c  */
#line 384 "lab6.y"
    {;;}
    break;

  case 58:

/* Line 1455 of yacc.c  */
#line 386 "lab6.y"
    { 
                    if (!endl) { 
                        endl = 1; 
                    }
                    sprintf(tempbuffer, PRINTF_ENDL_ASM_FORMAT);
                    printf("%s\n", tempbuffer);
                ;}
    break;

  case 59:

/* Line 1455 of yacc.c  */
#line 393 "lab6.y"
    {;;}
    break;

  case 60:

/* Line 1455 of yacc.c  */
#line 395 "lab6.y"
    {
                    char format[30];
                    computeFormatAfisare((yyvsp[(2) - (2)].attrib).varn, (yyvsp[(2) - (2)].attrib).type, format, sizeof(format));
                    if (strcmp((yyvsp[(2) - (2)].attrib).type, "ID") != 0) 
                    {
                        sprintf(tempbuffer, PRINTF_CONST_ASM_FORMAT, format);
                        printf("%s\n", tempbuffer);
                        strcat((yyval.attrib).code, tempbuffer);
                    }
                    else
                    {
                        sprintf(tempbuffer, PRINTF_ASM_FORMAT, (yyvsp[(2) - (2)].attrib), format);
                        printf("%s\n", tempbuffer);
                        strcat((yyval.attrib).code, tempbuffer);
                    }
                ;}
    break;

  case 61:

/* Line 1455 of yacc.c  */
#line 412 "lab6.y"
    {
                    if (!endl) { 
                        endl = 1; 
                    }
                    sprintf(tempbuffer, PRINTF_ENDL_ASM_FORMAT);
                    printf("%s\n", tempbuffer);
                    strcat((yyval.attrib).code, tempbuffer);
                ;}
    break;



/* Line 1455 of yacc.c  */
#line 1888 "lab6.tab.c"
      default: break;
    }
  YY_SYMBOL_PRINT ("-> $$ =", yyr1[yyn], &yyval, &yyloc);

  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);

  *++yyvsp = yyval;

  /* Now `shift' the result of the reduction.  Determine what state
     that goes to, based on the state we popped back to and the rule
     number reduced by.  */

  yyn = yyr1[yyn];

  yystate = yypgoto[yyn - YYNTOKENS] + *yyssp;
  if (0 <= yystate && yystate <= YYLAST && yycheck[yystate] == *yyssp)
    yystate = yytable[yystate];
  else
    yystate = yydefgoto[yyn - YYNTOKENS];

  goto yynewstate;


/*------------------------------------.
| yyerrlab -- here on detecting error |
`------------------------------------*/
yyerrlab:
  /* If not already recovering from an error, report this error.  */
  if (!yyerrstatus)
    {
      ++yynerrs;
#if ! YYERROR_VERBOSE
      yyerror (YY_("syntax error"));
#else
      {
	YYSIZE_T yysize = yysyntax_error (0, yystate, yychar);
	if (yymsg_alloc < yysize && yymsg_alloc < YYSTACK_ALLOC_MAXIMUM)
	  {
	    YYSIZE_T yyalloc = 2 * yysize;
	    if (! (yysize <= yyalloc && yyalloc <= YYSTACK_ALLOC_MAXIMUM))
	      yyalloc = YYSTACK_ALLOC_MAXIMUM;
	    if (yymsg != yymsgbuf)
	      YYSTACK_FREE (yymsg);
	    yymsg = (char *) YYSTACK_ALLOC (yyalloc);
	    if (yymsg)
	      yymsg_alloc = yyalloc;
	    else
	      {
		yymsg = yymsgbuf;
		yymsg_alloc = sizeof yymsgbuf;
	      }
	  }

	if (0 < yysize && yysize <= yymsg_alloc)
	  {
	    (void) yysyntax_error (yymsg, yystate, yychar);
	    yyerror (yymsg);
	  }
	else
	  {
	    yyerror (YY_("syntax error"));
	    if (yysize != 0)
	      goto yyexhaustedlab;
	  }
      }
#endif
    }



  if (yyerrstatus == 3)
    {
      /* If just tried and failed to reuse lookahead token after an
	 error, discard it.  */

      if (yychar <= YYEOF)
	{
	  /* Return failure if at end of input.  */
	  if (yychar == YYEOF)
	    YYABORT;
	}
      else
	{
	  yydestruct ("Error: discarding",
		      yytoken, &yylval);
	  yychar = YYEMPTY;
	}
    }

  /* Else will try to reuse lookahead token after shifting the error
     token.  */
  goto yyerrlab1;


/*---------------------------------------------------.
| yyerrorlab -- error raised explicitly by YYERROR.  |
`---------------------------------------------------*/
yyerrorlab:

  /* Pacify compilers like GCC when the user code never invokes
     YYERROR and the label yyerrorlab therefore never appears in user
     code.  */
  if (/*CONSTCOND*/ 0)
     goto yyerrorlab;

  /* Do not reclaim the symbols of the rule which action triggered
     this YYERROR.  */
  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);
  yystate = *yyssp;
  goto yyerrlab1;


/*-------------------------------------------------------------.
| yyerrlab1 -- common code for both syntax error and YYERROR.  |
`-------------------------------------------------------------*/
yyerrlab1:
  yyerrstatus = 3;	/* Each real token shifted decrements this.  */

  for (;;)
    {
      yyn = yypact[yystate];
      if (yyn != YYPACT_NINF)
	{
	  yyn += YYTERROR;
	  if (0 <= yyn && yyn <= YYLAST && yycheck[yyn] == YYTERROR)
	    {
	      yyn = yytable[yyn];
	      if (0 < yyn)
		break;
	    }
	}

      /* Pop the current state because it cannot handle the error token.  */
      if (yyssp == yyss)
	YYABORT;


      yydestruct ("Error: popping",
		  yystos[yystate], yyvsp);
      YYPOPSTACK (1);
      yystate = *yyssp;
      YY_STACK_PRINT (yyss, yyssp);
    }

  *++yyvsp = yylval;


  /* Shift the error token.  */
  YY_SYMBOL_PRINT ("Shifting", yystos[yyn], yyvsp, yylsp);

  yystate = yyn;
  goto yynewstate;


/*-------------------------------------.
| yyacceptlab -- YYACCEPT comes here.  |
`-------------------------------------*/
yyacceptlab:
  yyresult = 0;
  goto yyreturn;

/*-----------------------------------.
| yyabortlab -- YYABORT comes here.  |
`-----------------------------------*/
yyabortlab:
  yyresult = 1;
  goto yyreturn;

#if !defined(yyoverflow) || YYERROR_VERBOSE
/*-------------------------------------------------.
| yyexhaustedlab -- memory exhaustion comes here.  |
`-------------------------------------------------*/
yyexhaustedlab:
  yyerror (YY_("memory exhausted"));
  yyresult = 2;
  /* Fall through.  */
#endif

yyreturn:
  if (yychar != YYEMPTY)
     yydestruct ("Cleanup: discarding lookahead",
		 yytoken, &yylval);
  /* Do not reclaim the symbols of the rule which action triggered
     this YYABORT or YYACCEPT.  */
  YYPOPSTACK (yylen);
  YY_STACK_PRINT (yyss, yyssp);
  while (yyssp != yyss)
    {
      yydestruct ("Cleanup: popping",
		  yystos[*yyssp], yyvsp);
      YYPOPSTACK (1);
    }
#ifndef yyoverflow
  if (yyss != yyssa)
    YYSTACK_FREE (yyss);
#endif
#if YYERROR_VERBOSE
  if (yymsg != yymsgbuf)
    YYSTACK_FREE (yymsg);
#endif
  /* Make sure YYID is used.  */
  return YYID (yyresult);
}



/* Line 1675 of yacc.c  */
#line 422 "lab6.y"


int
main(int argc,char *argv[]) {
    yyparse();
    return 0;
}

