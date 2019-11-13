//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
package parser;
import globals.*;
import globals.Error;
import assembler.*;
import lexer.Lexer;
import lexer.Token;
import java.util.ArrayList;
import java.util.Stack;
//#line 26 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IF=257;
public final static short ELSE=258;
public final static short END_IF=259;
public final static short PRINT=260;
public final static short INT=261;
public final static short ULONG=262;
public final static short WHILE=263;
public final static short DO=264;
public final static short ID=265;
public final static short CADENA=266;
public final static short CTE=267;
public final static short MAYOR_IGUAL=268;
public final static short MENOR_IGUAL=269;
public final static short DISTINTO=270;
public final static short ASIGN=271;
public final static short BEGIN=272;
public final static short END=273;
public final static short FIRST=274;
public final static short LAST=275;
public final static short LENGTH=276;
public final static short EOF=277;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    1,    1,    2,    2,    2,    2,
    2,    2,    5,    5,    4,    4,    3,    3,    3,    3,
    3,    7,    7,    8,    8,    8,    8,    8,    8,    9,
    9,    9,    6,    6,    6,    6,   11,   11,   11,   11,
   11,   11,   11,   11,   11,   11,   11,   11,   12,   12,
   12,   12,   12,   12,   12,   15,   13,   13,   13,   13,
   13,   13,   13,   13,   13,   13,   13,   13,   13,   13,
   13,   17,   18,   19,   16,   16,   16,   16,   16,   20,
   20,   20,   20,   22,   22,   22,   22,   22,   22,   14,
   14,   14,   14,   21,   21,   21,   21,   21,   24,   24,
   24,   24,   24,   24,   24,   25,   25,   25,   26,   26,
   26,   26,   26,   26,   26,   26,   23,   23,   23,   23,
   10,   10,
};
final static short yylen[] = {                            2,
    1,    1,    2,    1,    2,    1,    4,    4,    4,    4,
    4,    3,    1,    1,    2,    1,    3,    2,    3,    3,
    2,    1,    1,    3,    3,    2,    2,    1,    1,    4,
    3,    4,    1,    1,    1,    1,    5,    5,    5,    5,
    4,    4,    4,    5,    4,    3,    3,    5,    5,    5,
    5,    5,    5,    3,    5,    1,    5,    7,    5,    5,
    5,    5,    5,    7,    7,    7,    7,    7,    7,    7,
    3,    1,    1,    1,    3,    2,    2,    1,    2,    3,
    3,    2,    3,    1,    1,    1,    1,    1,    1,    4,
    4,    3,    4,    3,    3,    1,    3,    3,    3,    3,
    1,    3,    3,    3,    3,    1,    1,    3,    3,    2,
    3,    2,    3,    2,    3,    1,    1,    4,    4,    4,
    1,    2,
};
final static short yydefred[] = {                         0,
    0,   22,   23,    0,    0,    0,    0,    2,    6,    0,
    0,   29,    0,    0,    0,    0,    0,   21,    0,   16,
    0,   33,   34,   35,   36,    0,    0,    0,    0,    0,
    0,    3,    5,    0,    0,    0,   18,    0,   27,    0,
    0,  121,    0,    0,    0,  107,    0,    0,    0,    0,
    0,  106,    0,  101,    0,    0,   72,    0,    0,    0,
    0,    0,    0,   56,    0,    0,   15,   19,    0,    0,
   31,    0,    0,    0,    0,    0,   12,   20,   17,    0,
   25,   88,   87,   89,    0,    0,   84,   85,   86,    0,
    0,    0,    0,  122,    0,   79,    0,    0,    0,   13,
   73,   14,    0,   76,   82,    0,    0,    0,    0,    0,
   71,    0,    0,    0,   47,   46,    0,    0,    0,    0,
    0,    0,    0,   54,    0,    0,    0,    0,    0,    8,
    0,   92,    0,   32,   30,   10,    0,    0,    9,   11,
    7,  102,  104,    0,    0,    0,    0,    0,  108,   91,
    0,   75,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  103,   99,  105,  100,    0,    0,    0,
    0,    0,    0,    0,   41,   45,    0,    0,   42,    0,
   43,    0,    0,    0,    0,    0,    0,  118,    0,   93,
   90,  120,  119,    0,  110,    0,  112,    0,  114,    0,
   40,   50,   74,    0,   59,    0,   62,    0,   61,   60,
    0,    0,    0,   63,   57,   44,   38,   48,   37,   39,
   52,   51,   53,   55,   49,  115,  109,  111,  113,    0,
    0,    0,    0,    0,    0,    0,   64,   69,   67,   65,
   68,   66,   70,   58,
};
final static short yydgoto[] = {                          6,
    7,  100,    9,   19,  203,  102,   10,   11,   12,   46,
   22,   23,   24,   25,   47,   48,   49,  103,  204,   50,
   51,   90,   52,   53,   54,  149,
};
final static short yysindex[] = {                       194,
  -48,    0,    0,  -83,  290,    0,  316,    0,    0, -139,
  -43,    0,   22,   44,   11,   48,  -61,    0,  344,    0,
   26,    0,    0,    0,    0, -213,  -33,  -40,  -23,  329,
  -48,    0,    0,   13,   27,  -83,    0, -170,    0,  441,
  -44,    0,  252, -165,   70,    0, -140,    0,  462,   91,
  461,    0,   89,    0,  384,  150,    0,  483,   95,  -14,
   92,  438, -191,    0,  251,  101,    0,    0,  274,   53,
    0,   74,  116,  269,    4,  -53,    0,    0,    0,  -83,
    0,    0,    0,    0,    3,    3,    0,    0,    0,  252,
 -166,  106,   37,    0,  149,    0,  160,  462,  165,    0,
    0,    0, -120,    0,    0,  273,  277,  289,  295,  296,
    0,   97,  128,  -70,    0,    0,  148,    5,  172,   33,
  202,  212,  462,    0,  462,  500,  163,  181,  206,    0,
  -21,    0,   69,    0,    0,    0,  228,  234,    0,    0,
    0,    0,    0,  157,  225,  -36,  -30,  -17,    0,    0,
  246,    0,  272,  110, -213,  462,  284,  106,   89,  106,
   89,  106,  157,    0,    0,    0,    0,  462,  294,  462,
  303,  311,  510,  -52,    0,    0,  304,  306,    0,  -50,
    0,  312,  330,  331,  140,  -37,    0,    0,    0,    0,
    0,    0,    0,  249,    0,  308,    0,  351,    0,  353,
    0,    0,    0,  139,    0,  142,    0,  143,    0,    0,
  145,  153,  -77,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  349,
  355,  360,  364,  368,  370,  -35,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
  435,    0,    0,   47,    0,    0,  439,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  167,    0,
    0,    0,    0,    0,    0,  117,    0,    0,    0,    0,
  -28,    0,    0,    0,    0,    0,    0,  362,    0,  373,
    0,    0,   16,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  118,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  405,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  177,   -6,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  431,    0,    0,   38,   60,   82,
  104,  197,  207,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -19,    0,  -16,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   87,  440,  443,   58,  568,    0,   19,   31,  113,
    0,    0,    0,    0,  436,  231,  437,  156,   24,   73,
   61,  407,  565,  176,   49,    0,
};
final static int YYTABLESIZE=782;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         45,
   38,   91,   28,  196,   44,  141,  215,   27,  219,  198,
   18,   44,  117,  117,  117,   37,  117,   28,  117,   21,
   85,  225,  200,  244,   32,   86,  117,   30,   35,   65,
  117,  117,  117,  117,  116,  116,  116,  190,  116,   32,
  116,   39,   30,   45,  116,  177,   74,   44,   44,   21,
   61,   39,  116,  116,  116,  116,   96,   69,   96,   71,
   96,   45,  139,  176,  125,   39,   44,   74,   81,   38,
   38,   78,  126,  180,   96,   96,   96,   96,   97,  106,
   97,  107,   97,   56,   68,   79,    8,   56,   44,  145,
   28,  179,   44,   32,   80,  150,   97,   97,   97,   97,
   94,   94,   94,   93,   94,   28,  101,  146,  147,  148,
   96,  106,  101,  107,   44,  101,   34,   97,   94,   94,
   94,   94,   98,   98,   98,    4,   98,  191,   97,  133,
  109,  104,  121,  142,  143,  110,   44,  156,  157,   72,
   98,   98,   98,   98,   95,  134,   95,   85,   95,   45,
  144,   85,   86,  115,   44,  153,   86,  165,  167,  130,
   26,   24,   95,   95,   95,   95,  135,   45,  163,   87,
   89,   88,   44,  122,  136,   26,   24,  129,  235,   45,
  183,  236,  184,  186,   44,  172,  138,  173,  174,  151,
   96,  206,   45,  208,   44,  211,  213,   44,  223,  106,
  152,  107,  140,  214,   45,  218,  175,   13,   14,   44,
  112,   15,  178,  114,   16,   40,   17,   81,  224,  195,
  243,   36,   70,   28,   41,  197,   42,  117,  117,  117,
   43,  117,   73,   42,  117,  117,  117,   83,  199,  117,
  117,  117,  117,  117,   57,   32,   64,   80,   30,  116,
  116,  120,  182,  116,  119,  187,  116,  116,  116,   40,
  181,  116,  116,  116,  194,  116,   59,   41,   41,   42,
   42,   96,   96,  188,   43,   96,   60,   40,   96,   96,
   96,  159,  161,   96,   96,   96,   41,   96,   42,  226,
   36,   36,   43,   97,   97,   44,   44,   97,  189,   55,
   97,   97,   97,   62,  201,   97,   97,   97,   41,   97,
   42,   28,   41,   44,   42,   94,   94,   44,   44,   94,
  192,   44,   94,   94,   94,   40,  193,   94,   94,   94,
  202,   94,  132,   44,   41,   95,   42,   98,   98,   44,
   44,   98,  205,   71,   98,   98,   98,  118,  227,   98,
   98,   98,  207,   98,  168,  169,  119,  120,   42,   95,
   95,  209,  216,   95,  217,   40,   95,   95,   95,  210,
  220,   95,   95,   95,   41,   95,   42,   82,   83,   84,
   43,   26,   24,  154,   14,  170,  171,   15,  221,  222,
   16,  228,   41,  229,   42,  154,   14,  230,   43,   15,
  231,  232,   16,  233,   41,   40,   42,  237,  154,   14,
   43,  234,   15,  238,   41,   16,   42,   41,  239,   42,
  154,   14,  240,   43,   15,   85,  241,   16,  242,   41,
   86,   42,   81,   81,    4,   43,   81,  117,    1,   81,
   81,   81,  111,   87,   89,   88,   33,   30,   81,    1,
   58,   63,   83,   83,    2,    3,   83,  108,    4,   83,
   83,   83,   80,   80,    0,    5,   80,    0,   83,   80,
   80,   80,  106,  106,    0,  106,    0,  106,   80,   85,
    0,    0,   85,    0,   86,    0,    0,   86,    0,    0,
  106,  106,  106,    0,    0,    0,  124,   87,   89,   88,
   87,   89,   88,  106,    0,  107,  127,   92,    0,    0,
    0,    0,    0,    0,    0,  128,   41,   42,   42,    0,
   87,   89,   88,    0,  137,    0,    0,    0,  158,  131,
    0,    0,  160,  128,    0,   42,    0,   41,   41,   42,
   42,   41,    0,   42,  162,   28,   14,    0,    0,   15,
  164,  166,   16,   41,   29,   42,    0,    0,    0,   41,
   41,   42,   42,    0,    0,   26,   99,   14,   20,   26,
   15,   31,   20,   16,    0,   29,    2,    3,    0,    0,
    4,    0,    5,   26,   75,   14,   67,    5,   15,    0,
    0,   16,    0,   29,   26,   26,    0,   67,   20,   13,
   14,   76,    0,   15,    0,   77,   16,    0,   29,    0,
    0,    0,    0,   26,    0,    0,   66,   72,   72,   26,
    0,   72,   26,    0,   72,   56,   72,    0,   78,   78,
    0,    0,   78,   72,    0,   78,   78,   78,    0,   99,
   14,    0,    0,   15,   78,    0,   16,    0,   29,    0,
    0,   82,   83,   84,    0,    5,    0,    0,    0,    0,
   77,   77,   26,  155,   77,    0,   20,   77,   77,   77,
    0,    0,    0,    0,    0,    0,   77,  155,    0,    0,
   20,    0,    0,    0,    0,    0,  106,   26,    0,   26,
   26,    0,    0,    0,    0,    0,    0,    0,  106,  106,
  106,  123,    0,    0,    0,   82,   83,   84,   82,   83,
   84,    0,    0,    0,    0,    0,  105,   99,   14,    0,
   26,   15,    0,    0,   16,    0,   29,    0,   82,   83,
   84,    0,   26,    5,   26,    0,   26,   26,  113,   14,
    0,    0,   15,    0,    0,   16,    0,   29,    0,  155,
    0,    0,   20,    0,    5,  185,   14,    0,    0,   15,
    0,    0,   16,    0,   29,  212,   14,    0,    0,   15,
    0,    5,   16,    0,   29,    0,  155,    0,    0,   20,
    0,    5,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   44,   46,   44,   40,   45,   59,   59,   91,   59,   40,
   59,   45,   41,   42,   43,   59,   45,   59,   47,    1,
   42,   59,   40,   59,   44,   47,   41,   44,   10,   91,
   59,   60,   61,   62,   41,   42,   43,   59,   45,   59,
   47,   11,   59,   40,   59,   41,   91,   45,   45,   31,
   40,   21,   59,   60,   61,   62,   41,  271,   43,   93,
   45,   40,   59,   59,  256,   35,   45,   91,   38,   44,
   44,   59,  264,   41,   59,   60,   61,   62,   41,   43,
   43,   45,   45,   40,   59,   59,    0,   40,   45,  256,
   44,   59,   45,    7,  265,   59,   59,   60,   61,   62,
   41,  267,   43,   43,   45,   59,   49,  274,  275,  276,
   41,   43,   55,   45,   45,   58,  256,   45,   59,   60,
   61,   62,   41,  264,   43,  265,   45,   59,   56,   69,
   42,   41,   41,   85,   86,   47,   45,  258,  259,   27,
   59,   60,   61,   62,   41,   93,   43,   42,   45,   40,
   90,   42,   47,   59,   45,   98,   47,  109,  110,   59,
   44,   44,   59,   60,   61,   62,   93,   40,  108,   60,
   61,   62,   45,   61,   59,   59,   59,   65,  256,   40,
  123,  259,  125,  126,   45,  256,   74,  258,  259,   41,
   41,  168,   40,  170,   45,  172,  173,   45,   59,   43,
   41,   45,  256,  256,   40,  256,   59,  256,  257,   45,
   55,  260,   41,   58,  263,  256,  265,   41,  256,  256,
  256,  265,  256,  265,  265,  256,  267,  256,  257,  271,
  271,  260,  273,  267,  263,  264,  265,   41,  256,  268,
  269,  270,  271,  272,   14,  265,   16,   41,  265,  256,
  257,  271,   41,  260,  271,   93,  263,  264,  265,  256,
   59,  268,  269,  270,   40,  272,  256,  265,  265,  267,
  267,  256,  257,   93,  271,  260,  266,  256,  263,  264,
  265,  106,  107,  268,  269,  270,  265,  272,  267,   41,
  265,  265,  271,  256,  257,   45,   45,  260,   93,  256,
  263,  264,  265,  256,   59,  268,  269,  270,  265,  272,
  267,  265,  265,   45,  267,  256,  257,   45,   45,  260,
   93,   45,  263,  264,  265,  256,   93,  268,  269,  270,
   59,  272,   59,   45,  265,  266,  267,  256,  257,   45,
   45,  260,   59,   93,  263,  264,  265,  256,   41,  268,
  269,  270,   59,  272,  258,  259,  265,  266,  267,  256,
  257,   59,   59,  260,   59,  256,  263,  264,  265,   59,
   59,  268,  269,  270,  265,  272,  267,  268,  269,  270,
  271,  265,  265,  256,  257,  258,  259,  260,   59,   59,
  263,   41,  265,   41,  267,  256,  257,  259,  271,  260,
  259,  259,  263,  259,  265,  256,  267,   59,  256,  257,
  271,  259,  260,   59,  265,  263,  267,  265,   59,  267,
  256,  257,   59,  271,  260,   42,   59,  263,   59,  265,
   47,  267,  256,  257,    0,  271,  260,  271,    0,  263,
  264,  265,   59,   60,   61,   62,    7,    5,  272,  256,
   14,   16,  256,  257,  261,  262,  260,   51,  265,  263,
  264,  265,  256,  257,   -1,  272,  260,   -1,  272,  263,
  264,  265,   42,   43,   -1,   45,   -1,   47,  272,   42,
   -1,   -1,   42,   -1,   47,   -1,   -1,   47,   -1,   -1,
   60,   61,   62,   -1,   -1,   -1,   59,   60,   61,   62,
   60,   61,   62,   43,   -1,   45,  256,  256,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  265,  265,  267,  267,   -1,
   60,   61,   62,   -1,  256,   -1,   -1,   -1,  256,  256,
   -1,   -1,  256,  265,   -1,  267,   -1,  265,  265,  267,
  267,  265,   -1,  267,  256,  256,  257,   -1,   -1,  260,
  256,  256,  263,  265,  265,  267,   -1,   -1,   -1,  265,
  265,  267,  267,   -1,   -1,    1,  256,  257,    1,    5,
  260,  256,    5,  263,   -1,  265,  261,  262,   -1,   -1,
  265,   -1,  272,   19,  256,  257,   19,  272,  260,   -1,
   -1,  263,   -1,  265,   30,   31,   -1,   30,   31,  256,
  257,  273,   -1,  260,   -1,  277,  263,   -1,  265,   -1,
   -1,   -1,   -1,   49,   -1,   -1,  273,  256,  257,   55,
   -1,  260,   58,   -1,  263,  264,  265,   -1,  256,  257,
   -1,   -1,  260,  272,   -1,  263,  264,  265,   -1,  256,
  257,   -1,   -1,  260,  272,   -1,  263,   -1,  265,   -1,
   -1,  268,  269,  270,   -1,  272,   -1,   -1,   -1,   -1,
  256,  257,   98,   99,  260,   -1,   99,  263,  264,  265,
   -1,   -1,   -1,   -1,   -1,   -1,  272,  113,   -1,   -1,
  113,   -1,   -1,   -1,   -1,   -1,  256,  123,   -1,  125,
  126,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  268,  269,
  270,  264,   -1,   -1,   -1,  268,  269,  270,  268,  269,
  270,   -1,   -1,   -1,   -1,   -1,  256,  256,  257,   -1,
  156,  260,   -1,   -1,  263,   -1,  265,   -1,  268,  269,
  270,   -1,  168,  272,  170,   -1,  172,  173,  256,  257,
   -1,   -1,  260,   -1,   -1,  263,   -1,  265,   -1,  185,
   -1,   -1,  185,   -1,  272,  256,  257,   -1,   -1,  260,
   -1,   -1,  263,   -1,  265,  256,  257,   -1,   -1,  260,
   -1,  272,  263,   -1,  265,   -1,  212,   -1,   -1,  212,
   -1,  272,
};
}
final static short YYFINAL=6;
final static short YYMAXTOKEN=277;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"IF","ELSE","END_IF","PRINT","INT","ULONG",
"WHILE","DO","ID","CADENA","CTE","MAYOR_IGUAL","MENOR_IGUAL","DISTINTO","ASIGN",
"BEGIN","END","FIRST","LAST","LENGTH","\"EOF\"",
};
final static String yyrule[] = {
"$accept : programa",
"programa : bloque_declarativo",
"programa : bloque_ejecutable",
"programa : bloque_declarativo bloque_ejecutable",
"programa : error",
"bloque_declarativo : bloque_declarativo sentencia_declarativa",
"bloque_declarativo : sentencia_declarativa",
"bloque_ejecutable : BEGIN lista_sentencias_ejecutables END ';'",
"bloque_ejecutable : error lista_sentencias_ejecutables END ';'",
"bloque_ejecutable : BEGIN lista_sentencias_ejecutables error ';'",
"bloque_ejecutable : BEGIN error END ';'",
"bloque_ejecutable : BEGIN lista_sentencias_ejecutables END error",
"bloque_ejecutable : BEGIN lista_sentencias_ejecutables \"EOF\"",
"bloque_sentencias : bloque_ejecutable",
"bloque_sentencias : sentencia_ejecutable",
"lista_sentencias_ejecutables : lista_sentencias_ejecutables sentencia_ejecutable",
"lista_sentencias_ejecutables : sentencia_ejecutable",
"sentencia_declarativa : tipo lista_variables ';'",
"sentencia_declarativa : lista_variables ';'",
"sentencia_declarativa : error lista_variables ';'",
"sentencia_declarativa : tipo error ';'",
"sentencia_declarativa : error ';'",
"tipo : INT",
"tipo : ULONG",
"lista_variables : lista_variables ',' ID",
"lista_variables : lista_variables ',' coleccion",
"lista_variables : lista_variables ID",
"lista_variables : lista_variables coleccion",
"lista_variables : ID",
"lista_variables : coleccion",
"coleccion : ID '[' cte ']'",
"coleccion : ID '[' ']'",
"coleccion : ID '[' error ']'",
"sentencia_ejecutable : sentencia_impresion",
"sentencia_ejecutable : sentencia_control",
"sentencia_ejecutable : sentencia_seleccion",
"sentencia_ejecutable : sentencia_asignacion",
"sentencia_impresion : PRINT '(' CADENA ')' ';'",
"sentencia_impresion : PRINT '(' ID ')' ';'",
"sentencia_impresion : PRINT '(' cte ')' ';'",
"sentencia_impresion : error '(' CADENA ')' ';'",
"sentencia_impresion : PRINT CADENA ')' ';'",
"sentencia_impresion : PRINT '(' CADENA ';'",
"sentencia_impresion : PRINT '(' ')' ';'",
"sentencia_impresion : PRINT '(' error ')' ';'",
"sentencia_impresion : PRINT '(' error ';'",
"sentencia_impresion : PRINT CADENA ';'",
"sentencia_impresion : PRINT error ';'",
"sentencia_impresion : PRINT '(' CADENA ')' error",
"sentencia_control : WHILE condicion_while DO bloque_sentencias ';'",
"sentencia_control : error condicion_while DO bloque_sentencias ';'",
"sentencia_control : WHILE condicion_while error bloque_sentencias ';'",
"sentencia_control : WHILE error DO bloque_sentencias ';'",
"sentencia_control : WHILE condicion_while DO error ';'",
"sentencia_control : WHILE error ';'",
"sentencia_control : WHILE condicion_while DO bloque_sentencias error",
"condicion_while : condicion",
"sentencia_seleccion : IF condicion_if bloque_then END_IF ';'",
"sentencia_seleccion : IF condicion_if bloque_then ELSE bloque_else END_IF ';'",
"sentencia_seleccion : error condicion_if bloque_then END_IF ';'",
"sentencia_seleccion : IF condicion_if bloque_then error ';'",
"sentencia_seleccion : IF condicion_if error END_IF ';'",
"sentencia_seleccion : IF error bloque_then END_IF ';'",
"sentencia_seleccion : IF condicion_if bloque_then END_IF error",
"sentencia_seleccion : error condicion_if bloque_then ELSE bloque_else END_IF ';'",
"sentencia_seleccion : IF condicion_if bloque_then error bloque_else END_IF ';'",
"sentencia_seleccion : IF condicion_if bloque_then ELSE bloque_else error ';'",
"sentencia_seleccion : IF condicion_if error ELSE bloque_else END_IF ';'",
"sentencia_seleccion : IF condicion_if bloque_then ELSE error END_IF ';'",
"sentencia_seleccion : IF error bloque_then ELSE bloque_else END_IF ';'",
"sentencia_seleccion : IF condicion_if bloque_then ELSE bloque_else END_IF error",
"sentencia_seleccion : IF error ';'",
"condicion_if : condicion",
"bloque_then : bloque_sentencias",
"bloque_else : bloque_sentencias",
"condicion : '(' comparacion ')'",
"condicion : comparacion ')'",
"condicion : '(' comparacion",
"condicion : comparacion",
"condicion : '(' ')'",
"comparacion : expresion comparador expresion",
"comparacion : error comparador expresion",
"comparacion : expresion error",
"comparacion : expresion comparador error",
"comparador : '<'",
"comparador : '>'",
"comparador : '='",
"comparador : MENOR_IGUAL",
"comparador : MAYOR_IGUAL",
"comparador : DISTINTO",
"sentencia_asignacion : id ASIGN expresion ';'",
"sentencia_asignacion : error ASIGN expresion ';'",
"sentencia_asignacion : id ASIGN ';'",
"sentencia_asignacion : id ASIGN error ';'",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"expresion : expresion '+' error",
"expresion : expresion '-' error",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"termino : error '*' factor",
"termino : termino '*' error",
"termino : error '/' factor",
"termino : termino '/' error",
"factor : id",
"factor : cte",
"factor : ID '.' funcion",
"funcion : FIRST '(' ')'",
"funcion : FIRST error",
"funcion : LAST '(' ')'",
"funcion : LAST error",
"funcion : LENGTH '(' ')'",
"funcion : LENGTH error",
"funcion : error '(' ')'",
"funcion : error",
"id : ID",
"id : ID '[' ID ']'",
"id : ID '[' cte ']'",
"id : ID '[' error ']'",
"cte : CTE",
"cte : '-' CTE",
};

//#line 300 "gramatica.y"

private Lexer al;
private boolean verbose;

public Parser(Lexer al, boolean as_verbose) {
  this.al = al;
  this.verbose = verbose;
}

// Analisis sintactico
public StringBuffer estructuras_sintacticas = new StringBuffer();
private Stack<String> tipos = new Stack<String>();
String type;

public int yylex() {
	if (al.notEOF()) {
		int valor = al.yylex();
		if (valor != -1) // error
			return valor;
		while (al.notEOF()) {
			valor = al.yylex();
			if (valor != -1)
				return valor;
		}
	}
	return 0;
}

public void yyerror(String s) {
      Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "P", "|", s));
}


public void check_range(String cte) {

        int new_cte;
        if (Long.parseLong(cte) <= Math.pow(2, 15)) {
            new_cte = Integer.valueOf(cte);
        } else {
            Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "WARNING Constante fuera de rango: " + cte));
            Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "WARNING Se va a reemplazar por el valor: -" + Math.pow(2, 15)));
            new_cte = (int) Math.pow(2, 15) - 1;
        }

        String new_lex = "-" + new_cte;
        lexer.Token old_token = globals.SymbolTable.getLex(cte);

        if (!globals.SymbolTable.contains(new_lex)) {
            lexer.Token t = new lexer.Token(old_token.getID(), new_lex, "CTE NEG");
            globals.SymbolTable.add(t);

            if (t.getAttr("contador") == null) {
                t.addAttr("contador", "1");
            }
            else {
                int contador = Integer.parseInt(t.getAttr("contador")) + 1 ;
                t.addAttr("contador", String.valueOf(contador));
            }
        }

        int contador = Integer.parseInt(old_token.getAttr("contador")) - 1 ;
        if( contador == 0) {
            globals.SymbolTable.remove(old_token.getLex());
        } else {
            old_token.addAttr("contador", String.valueOf(contador));
        }
    }
//TODO uso por parametro.
public void addVariable(String lex){
    if (!SymbolTable.contains(lex)){
        Token token = new Token(SymbolTable.getID("id"), lex, "ID");
        token.addAttr("Use", "VARIABLE");
        token.addAttr("Type", type);
        SymbolTable.add(token);
    }
    else{
        Error.add(
            String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR La variable " + lex + " ya se encuentra declarada.")
        );
    }
}

public Integer checkTypes(String exp1, String exp2) {
        String tipo1 = "", tipo2 = "";
        if (!tipos.isEmpty())
            tipo1 = tipos.pop();
        if (!tipos.isEmpty())
            tipo2 = tipos.pop();

        if (tipo1 != tipo2)
        {
            tipos.push("ULONG");

            if (tipo1 == "INT") { //TODO _CONV no es un nombre muy descriptivo. Cambiarlo? INT -> ULONG es la unica conversion posible.
                Terceto t = new Terceto("_CONV", exp2, "null");
                AdminTercetos.add(t);
                return 2; //Indice del argumento a convertir.
            }
            else {
                Terceto t = new Terceto("_CONV", exp1, "null");
                AdminTercetos.add(t);
                return 1;
            }
        } else
            tipos.push(tipo1);
        return 0;
}

public String crearTercetoOperacion(String op, String arg1, String arg2){
	Integer conv = checkTypes(arg1, arg2);
	String t = tipos.pop();

	if (conv == 1)
		arg1 = AdminTercetos.last().getId();
	if (conv == 2)
		arg2 = AdminTercetos.last().getId();
	tipos.push(t);

	Terceto terceto = new Terceto(op, arg1, arg2);
	AdminTercetos.add(terceto);
	return terceto.getId();
}
//#line 681 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 39 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se definio bien el programa."));}
break;
case 2:
//#line 40 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se definio bien el programa con sent. ejecutables"));}
break;
case 3:
//#line 41 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se definio bien el programa con sent. ejecutables y declarativas."));}
break;
case 4:
//#line 42 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en programa"));}
break;
case 8:
//#line 50 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave BEGIN."));}
break;
case 9:
//#line 51 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave END."));}
break;
case 10:
//#line 52 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque de ejecucion."));}
break;
case 11:
//#line 53 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta literal ';'."));}
break;
case 12:
//#line 54 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave 'END;'."));}
break;
case 18:
//#line 66 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Tipo no declarado."));}
break;
case 19:
//#line 67 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Tipo no reconocido."));}
break;
case 20:
//#line 68 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en lista de variables."));}
break;
case 21:
//#line 69 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia declarativa."));}
break;
case 22:
//#line 72 "gramatica.y"
{type = "INT";}
break;
case 23:
//#line 73 "gramatica.y"
{type = "LONG INT";}
break;
case 24:
//#line 76 "gramatica.y"
{addVariable(val_peek(0).sval);}
break;
case 25:
//#line 77 "gramatica.y"
{addVariable(val_peek(0).sval);}
break;
case 26:
//#line 78 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR falta ',' entre variables."));}
break;
case 27:
//#line 79 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR falta ',' entre variables."));}
break;
case 28:
//#line 80 "gramatica.y"
{addVariable(val_peek(0).sval);}
break;
case 29:
//#line 81 "gramatica.y"
{addVariable(val_peek(0).sval);}
break;
case 30:
//#line 85 "gramatica.y"
{
							if (!SymbolTable.contains(val_peek(3).sval)){
							    Token t = new Token(SymbolTable.getID("id"), val_peek(3).sval, "coleccion");
							    t.addAttr("size", val_peek(1).sval);
							    t.addAttr("Use", "COLECCION");
							    t.addAttr("Type", type);
							    t.addAttr("Elements", new ArrayList<>());
							    SymbolTable.add(t);
							}
							else
							    Error.add(
								    String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR La variable " + val_peek(1).sval + " ya se encuentra declarada.")
							    );
						}
break;
case 31:
//#line 99 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el tamaño de la declaracion."));}
break;
case 32:
//#line 100 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la declaracion del tamaño de la coleccion."));}
break;
case 37:
//#line 111 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia Print."));}
break;
case 40:
//#line 114 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave PRINT."));}
break;
case 41:
//#line 115 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal '('."));}
break;
case 42:
//#line 116 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ')'."));}
break;
case 43:
//#line 117 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la cadena a imprimir."));}
break;
case 44:
//#line 118 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la cadena a imprimir."));}
break;
case 45:
//#line 119 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la cadena a imprimir."));}
break;
case 46:
//#line 120 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR faltan los parentesis."));}
break;
case 47:
//#line 121 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia PRINT."));}
break;
case 48:
//#line 122 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ';'"));}
break;
case 49:
//#line 126 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia While."));}
break;
case 50:
//#line 127 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave WHILE."));}
break;
case 51:
//#line 128 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave DO."));}
break;
case 52:
//#line 129 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de WHILE."));}
break;
case 53:
//#line 130 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el bloque de sentencias WHILE."));}
break;
case 54:
//#line 131 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia WHILE."));}
break;
case 55:
//#line 132 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ';'."));}
break;
case 57:
//#line 139 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia If.."));}
break;
case 58:
//#line 140 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia If-Else."));}
break;
case 59:
//#line 141 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR al comienzo de la sentencia If."));}
break;
case 60:
//#line 142 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR al final de la sentencia If."));}
break;
case 61:
//#line 143 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el bloque de sentencias IF."));}
break;
case 62:
//#line 144 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de IF."));}
break;
case 63:
//#line 145 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ';'."));}
break;
case 64:
//#line 146 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave IF."));}
break;
case 65:
//#line 147 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave ELSE."));}
break;
case 66:
//#line 148 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave END_IF."));}
break;
case 67:
//#line 149 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque THEN de la sentencia IF."));}
break;
case 68:
//#line 150 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque ELSE de la sentencia IF."));}
break;
case 69:
//#line 151 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de la sentencia IF-ELSE."));}
break;
case 70:
//#line 152 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ';'."));}
break;
case 71:
//#line 153 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en sentencia IF."));}
break;
case 76:
//#line 166 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el caracter ("));}
break;
case 77:
//#line 167 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el caracter )"));}
break;
case 78:
//#line 168 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR faltan ambos parentesis en la condicion"));}
break;
case 79:
//#line 169 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion."));}
break;
case 81:
//#line 174 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el lado izquierdo de la comparacion."));}
break;
case 82:
//#line 175 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta comparador."));}
break;
case 83:
//#line 176 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el lado derecho de la comparacion."));}
break;
case 84:
//#line 179 "gramatica.y"
{ yyval = new ParserVal("<");}
break;
case 85:
//#line 180 "gramatica.y"
{ yyval = new ParserVal(">");}
break;
case 86:
//#line 181 "gramatica.y"
{ yyval = new ParserVal("=");}
break;
case 90:
//#line 189 "gramatica.y"
{
							    String old_id = val_peek(3).sval;
							    if (!SymbolTable.contains(old_id)){
								Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + val_peek(3).sval + " no declarada."));
							    }
							    else{
							       crearTercetoOperacion("ASIGN", val_peek(3).sval, val_peek(1).sval);
							    }

							    if (this.verbose)
								Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia Asign."));
                                    			}
break;
case 91:
//#line 201 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el ID de la asignacion."));}
break;
case 92:
//#line 202 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el lado derecho de la asignacion."));}
break;
case 93:
//#line 203 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el lado derecho de la asignacion."));}
break;
case 94:
//#line 208 "gramatica.y"
{ yyval = new ParserVal(crearTercetoOperacion("+", val_peek(2).sval, val_peek(0).sval)); }
break;
case 95:
//#line 209 "gramatica.y"
{ yyval = new ParserVal(crearTercetoOperacion("-", val_peek(2).sval, val_peek(0).sval)); }
break;
case 97:
//#line 211 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '+'"));}
break;
case 98:
//#line 212 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '-'"));}
break;
case 99:
//#line 215 "gramatica.y"
{ yyval = new ParserVal(crearTercetoOperacion("*", val_peek(2).sval, val_peek(0).sval)); }
break;
case 100:
//#line 216 "gramatica.y"
{ yyval = new ParserVal(crearTercetoOperacion("/", val_peek(2).sval, val_peek(0).sval)); }
break;
case 102:
//#line 218 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '*'"));}
break;
case 103:
//#line 219 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '*"));}
break;
case 104:
//#line 220 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '/'"));}
break;
case 105:
//#line 221 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '/'"));}
break;
case 110:
//#line 231 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion FIRST."));}
break;
case 112:
//#line 233 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion LAST."));}
break;
case 114:
//#line 235 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion LENGTH."));}
break;
case 115:
//#line 236 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Funcion desconocida."));}
break;
case 116:
//#line 237 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Funcion desconocida."));}
break;
case 117:
//#line 243 "gramatica.y"
{
                                            if(SymbolTable.contains(val_peek(0).sval))
                                                yyval = new ParserVal(val_peek(0).sval);
                                            else
                                               Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + val_peek(0).sval + " no declarada."));
                                       }
break;
case 118:
//#line 249 "gramatica.y"
{
						     if(SymbolTable.contains(val_peek(3).sval)){
							if (SymbolTable.contains(val_peek(1).sval)){
							    Token col_t = SymbolTable.getLex(val_peek(1).sval);
							    Token t = SymbolTable.getLex(val_peek(1).sval);
							    if (t.getAttr("type").equals("INT")) {
								yyval = new ParserVal(col_t.getLex()+"["+t.getLex()+"]");
							    }
							    else
								Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + val_peek(1).sval + " no es de tipo INT."));
							}
							else
							    Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + val_peek(1).sval + " no declarada."));
						     }
						     else
                                                        Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + val_peek(3).sval + " no declarada."));
                                                }
break;
case 119:
//#line 266 "gramatica.y"
{
						       if(SymbolTable.contains(val_peek(1).sval)){
							  if (SymbolTable.contains(val_peek(3).sval)){
							      Token t = SymbolTable.getLex(val_peek(3).sval);
							      if (t.getAttr("type").equals("INT"))
								  yyval = new ParserVal(t.getLex()+"["+val_peek(1).sval+"]");
							      else
								  Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + val_peek(3).sval + " no es de tipo INT."));
							  }
							  else
							      Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + val_peek(3).sval + " no declarada."));
						       }
						       else
							  Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + val_peek(1).sval + " no declarada."));
						  }
break;
case 120:
//#line 281 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el subindice de la coleccion. Se esperaba un INT."));}
break;
case 121:
//#line 284 "gramatica.y"
{
                                            String cte = val_peek(0).sval;
                                            check_range(cte);
                                            yyval = val_peek(0);
                                            tipos.push(type);
                                        }
break;
case 122:
//#line 290 "gramatica.y"
{
						String cte = val_peek(0).sval;
										check_range(cte);
										yyval = new ParserVal("-" + cte);
						tipos.push(type);
                                           }
break;
//#line 1263 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
