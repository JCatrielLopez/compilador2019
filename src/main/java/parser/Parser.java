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
public static ParserVal yylval;//the 'lval' (result) I got from yylex()
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
public final static short IGUAL=271;
public final static short ASIGN=272;
public final static short BEGIN=273;
public final static short END=274;
public final static short FIRST=275;
public final static short LAST=276;
public final static short LENGTH=277;
public final static short EOF=278;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    1,    1,    2,    2,    2,    2,
    2,    2,    5,    5,    4,    4,    3,    3,    3,    3,
    3,    7,    7,    8,    8,    8,    8,    8,    8,    9,
    9,    9,    6,    6,    6,    6,   11,   11,   11,   11,
   11,   11,   11,   11,   11,   11,   11,   11,   12,   12,
   12,   12,   12,   12,   12,   15,   16,   13,   13,   13,
   13,   13,   13,   13,   13,   13,   13,   13,   13,   13,
   13,   13,   18,   19,   20,   21,   17,   17,   17,   17,
   17,   22,   22,   22,   22,   24,   24,   24,   24,   24,
   24,   14,   14,   14,   14,   23,   23,   23,   23,   23,
   26,   26,   26,   26,   26,   26,   26,   27,   27,   27,
   28,   28,   28,   28,   28,   28,   28,   28,   25,   25,
   25,   25,   10,   10,
};
final static short yylen[] = {                            2,
    1,    1,    2,    1,    2,    1,    4,    4,    4,    4,
    4,    3,    1,    1,    2,    1,    3,    2,    3,    3,
    2,    1,    1,    3,    3,    2,    2,    1,    1,    4,
    3,    4,    1,    1,    1,    1,    5,    5,    5,    5,
    4,    4,    4,    5,    4,    3,    3,    5,    5,    5,
    5,    5,    5,    3,    5,    1,    1,    5,    7,    5,
    5,    5,    5,    5,    7,    7,    7,    7,    7,    7,
    7,    3,    1,    1,    1,    1,    3,    2,    2,    1,
    2,    3,    3,    2,    3,    1,    1,    1,    1,    1,
    1,    4,    4,    3,    4,    3,    3,    1,    3,    3,
    3,    3,    1,    3,    3,    3,    3,    1,    1,    3,
    3,    2,    3,    2,    3,    2,    3,    1,    1,    4,
    4,    4,    1,    2,
};
final static short yydefred[] = {                         0,
    0,   22,   23,    0,    0,    0,    0,    2,    6,    0,
    0,   29,    0,    0,    0,   56,    0,   21,    0,   16,
    0,   33,   34,   35,   36,    0,    0,    0,    0,    0,
    0,    0,    3,    5,    0,    0,    0,   18,    0,   27,
    0,    0,  123,    0,    0,    0,  109,    0,    0,    0,
    0,    0,  108,    0,  103,    0,    0,   73,    0,    0,
    0,    0,    0,    0,   15,   19,    0,    0,   57,    0,
    0,   31,    0,    0,    0,    0,    0,   12,   20,   17,
    0,   25,   90,   89,   91,   88,    0,    0,   86,   87,
    0,    0,    0,    0,  124,    0,   81,    0,    0,    0,
   13,   74,   14,    0,   78,   84,    0,    0,    0,    0,
    0,   72,    0,    0,    0,   47,   46,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    8,    0,   54,    0,
    0,    0,   94,    0,   32,   30,   10,    0,    0,    9,
   11,    7,  104,  106,    0,    0,    0,    0,    0,  110,
   93,    0,   77,    0,    0,    0,   75,    0,    0,    0,
    0,    0,    0,    0,    0,  105,  101,  107,  102,    0,
    0,    0,    0,    0,    0,    0,   41,   45,    0,    0,
   42,    0,   43,    0,    0,  120,    0,    0,    0,    0,
    0,   95,   92,  122,  121,    0,  112,    0,  114,    0,
  116,    0,   40,   50,   60,   76,    0,   63,    0,   62,
    0,   61,    0,   64,   58,    0,    0,   44,   38,   48,
   37,   39,   52,   51,   53,   55,   49,  117,  111,  113,
  115,    0,    0,    0,    0,    0,    0,    0,   65,   70,
   68,   66,   69,   67,   71,   59,
};
final static short yydgoto[] = {                          6,
    7,  101,    9,   19,  206,  103,   10,   11,   12,   47,
   22,   23,   24,   25,   26,   48,   49,   50,  104,  159,
  207,   51,   52,   91,   53,   54,   55,  150,
};
final static short yysindex[] = {                       258,
  223,    0,    0,  -87,  336,    0,  292,    0,    0, -134,
    9,    0,  165,  222,   21,    0,  -83,    0,  -86,    0,
   44,    0,    0,    0,    0,  257, -242,  -43,  -40,  -19,
 -114,  223,    0,    0,    7,   48,  -87,    0, -176,    0,
  307,  -22,    0,  244, -148,  269,    0, -132,    0,  391,
  115,  342,    0,  106,    0,  234,  228,    0,  402,   99,
  -32,  296,  -44,  107,    0,    0,  274, -123,    0,  352,
   76,    0,   79,  116,  245,  133,  -48,    0,    0,    0,
  -87,    0,    0,    0,    0,    0,  152,  152,    0,    0,
  244, -191,  398,   67,    0,  155,    0,  161,  391,  166,
    0,    0,    0,   98,    0,    0,  262,  272,  284,  302,
  317,    0,  148,  123,  211,    0,    0,  135,  -28,  162,
    4,  156,  178,  160,  163,  177,    0,  391,    0,  391,
  435,   20,    0,   80,    0,    0,    0,  229,  249,    0,
    0,    0,    0,    0,   -2,  214,  -34,  -30,  -17,    0,
    0,  242,    0,  294,  105, -242,    0,  312,  391,  398,
  106,  398,  106,  398,   -2,    0,    0,    0,    0,  319,
  391,  325,  391,  -56,  -42,  446,    0,    0,  332,  335,
    0,  -38,    0,  355,    0,    0,    0,  356,  365,  136,
   -8,    0,    0,    0,    0,  386,    0,  387,    0,  393,
    0,  400,    0,    0,    0,    0,  189,    0,  192,    0,
  199,    0,  201,    0,    0,  153, -231,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  390,  403,  407,  416,  418,  423,    5,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
  485,    0,    0,   62,    0,    0,  492,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -37,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  224,
    0,    0,    0,    0,    0,    0,   65,    0,    0,    0,
    0,  -27,    0,    0,    0,    0,    0,    0,  -74,    0,
  368,    0,    0,   15,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   85,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  379,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  179,   -5,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  414,    0,    0,    0,   35,
   55,   75,   95,  190,  208,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -25,    0,  -15,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  180,  491,  501,   31,  555,    0,   38,   72,  -16,
    0,    0,    0,    0,    0,  482,   87,  502,   43,  457,
  429,   25,  -18,  463,  590,   77,   17,    0,
};
final static int YYTABLESIZE=806;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         46,
   45,   45,  212,   28,   45,  198,   28,   63,  118,  200,
  142,   73,  179,  119,  119,  119,  215,  119,   32,  119,
  221,   28,  202,   92,  237,   94,  117,  238,   30,   70,
  178,  119,  119,   32,  119,  118,  118,  118,   21,  118,
  107,  118,  108,   30,  182,  123,  126,   36,   72,   72,
  227,  134,   39,  118,  118,   98,  118,   98,  139,   98,
   62,   87,  181,  246,  146,   79,   88,   38,   75,   21,
   98,   75,  145,   98,   98,   99,   98,   99,  192,   99,
  102,   98,   40,  147,  148,  149,  102,   39,   81,  102,
  165,   39,   40,   99,   99,   96,   99,   96,  113,   96,
   58,  115,   66,  143,  144,   28,   80,   40,   26,  107,
   82,  108,   69,   96,   96,  100,   96,  100,   95,  100,
   28,   35,  107,   26,  108,  151,  167,  169,   24,  154,
    4,   99,  130,  100,  100,   97,  100,   97,  193,   97,
  131,   76,   14,   24,   46,   15,   87,  110,   16,   45,
   30,   88,  111,   97,   97,  105,   97,  116,  188,   77,
  189,  191,   46,   78,   89,  127,   90,   45,  135,   13,
   14,  136,   46,   15,  137,   46,   16,   45,   30,    8,
   45,   73,   73,  161,  163,   73,   33,   64,   73,   57,
   73,  140,   46,  177,  225,  152,   45,   45,   73,  100,
   14,  153,  180,   15,   46,   46,   16,  141,   30,   45,
   45,  124,   71,  214,  183,   41,    5,  220,  184,   83,
  125,  197,   43,   43,   42,  199,   43,   28,  119,  119,
   85,   44,  119,   74,  119,  119,  119,  119,  201,   32,
  119,  119,  119,  119,  119,  119,  122,  226,   82,   30,
  118,  118,  185,  196,  118,  186,  121,  118,  118,  118,
  245,   57,  118,  118,  118,  118,   45,  118,   97,  187,
   98,   98,   45,   37,   98,   87,   60,   98,   98,   98,
   88,   18,   98,   98,   98,   98,   61,   98,   45,   45,
   99,   99,  112,   89,   99,   90,   57,   99,   99,   99,
  203,   45,   99,   99,   99,   99,   45,   99,   37,   97,
   96,   96,   37,   45,   96,   87,   45,   96,   96,   96,
   88,  194,   96,   96,   96,   96,   28,   96,   45,   26,
  100,  100,  129,   89,  100,   90,  122,  100,  100,  100,
   45,  195,  100,  100,  100,  100,   45,  100,   87,   24,
   97,   97,  204,   88,   97,  157,  158,   97,   97,   97,
   41,   45,   97,   97,   97,   97,   89,   97,   90,   42,
  205,   43,   83,   84,   85,   86,   44,  208,  155,   14,
  157,  172,   15,  210,  107,   16,  108,   42,   41,   43,
  218,  155,   14,  219,   44,   15,   45,   42,   16,   43,
   42,   89,   43,   90,   44,  157,  170,   44,  155,   14,
  133,  236,   15,  222,  223,   16,   42,   42,   43,   43,
   41,  155,   14,  224,   44,   15,  228,  229,   16,   42,
   42,   43,   43,  230,   83,   83,   44,   44,   83,   87,
  231,   83,   83,   83,   88,   85,   85,  232,  239,   85,
  233,   83,   85,   85,   85,  108,  108,  234,  108,  235,
  108,  240,   85,   82,   82,  241,  174,   82,  157,  175,
   82,   82,   82,  108,  242,  108,  243,   56,   13,   14,
   82,  244,   15,   41,    4,   16,   42,   17,   43,  100,
   14,    1,   42,   15,   43,  119,   16,   34,   30,   93,
  138,   83,   84,   85,   86,   31,    5,   68,   42,  125,
   43,   43,   67,    1,  109,   59,    0,  160,    2,    3,
    0,   42,    4,   43,   41,    0,   42,  162,   43,    0,
    5,    0,    0,   42,   96,   43,   42,  128,   43,  164,
    0,   83,   84,   85,   86,    0,    0,   32,   42,    0,
   43,  119,    2,    3,    0,   20,    4,  166,    0,   20,
  120,  121,   43,    0,    5,    0,   42,    0,   43,  171,
  173,  176,  168,   65,   83,   84,   85,   86,    0,    0,
    0,   42,    0,   43,    0,   65,   20,    0,    0,    0,
   27,   29,   14,    0,   27,   15,    0,  106,   16,  209,
   30,  211,  213,    0,  217,    0,    0,  132,   27,   83,
   84,   85,   86,    0,    0,    0,   42,    0,   43,    0,
   27,   27,    0,   80,   80,    0,    0,   80,    0,    0,
   80,   80,   80,    0,   79,   79,    0,    0,   79,   27,
   80,   79,   79,   79,    0,   27,  100,   14,   27,    0,
   15,   79,    0,   16,   20,   30,    0,  114,   14,    0,
    0,   15,    0,    5,   16,    0,   30,    0,   20,  108,
    0,    0,    0,    0,    5,    0,    0,    0,    0,    0,
    0,  108,  108,  108,  108,    0,    0,    0,   27,  156,
  190,   14,    0,    0,   15,    0,    0,   16,    0,   30,
    0,  216,   14,  156,    0,   15,    0,    5,   16,    0,
   30,    0,    0,    0,    0,    0,    0,   27,    5,   27,
   27,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   20,    0,    0,    0,   27,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   27,    0,   27,   27,    0,   27,    0,    0,    0,    0,
   20,    0,    0,    0,    0,    0,    0,    0,    0,  156,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  156,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   45,   45,   59,   91,   45,   40,   44,   91,   41,   40,
   59,   28,   41,   41,   42,   43,   59,   45,   44,   47,
   59,   59,   40,   46,  256,   44,   59,  259,   44,  272,
   59,   59,   60,   59,   62,   41,   42,   43,    1,   45,
   43,   47,   45,   59,   41,   62,   63,   10,   93,   93,
   59,   70,   44,   59,   60,   41,   62,   43,   75,   45,
   40,   42,   59,   59,  256,   59,   47,   59,   91,   32,
   46,   91,   91,   59,   60,   41,   62,   43,   59,   45,
   50,   57,   11,  275,  276,  277,   56,   44,  265,   59,
  109,   44,   21,   59,   60,   41,   62,   43,   56,   45,
   14,   59,   59,   87,   88,   44,   59,   36,   44,   43,
   39,   45,   26,   59,   60,   41,   62,   43,  267,   45,
   59,  256,   43,   59,   45,   59,  110,  111,   44,   99,
  265,  264,  256,   59,   60,   41,   62,   43,   59,   45,
  264,  256,  257,   59,   40,  260,   42,   42,  263,   45,
  265,   47,   47,   59,   60,   41,   62,   59,  128,  274,
  130,  131,   40,  278,   60,   59,   62,   45,   93,  256,
  257,   93,   40,  260,   59,   40,  263,   45,  265,    0,
   45,  256,  257,  107,  108,  260,    7,  274,  263,  264,
  265,   59,   40,   59,   59,   41,   45,   45,  273,  256,
  257,   41,   41,  260,   40,   40,  263,  256,  265,   45,
   45,  256,  256,  256,   59,  256,  273,  256,   41,   41,
  265,  256,  267,  267,  265,  256,  267,  265,  256,  257,
   41,  272,  260,  274,  272,  263,  264,  265,  256,  265,
  268,  269,  270,  271,  272,  273,  272,  256,   41,  265,
  256,  257,   93,   40,  260,   93,  272,  263,  264,  265,
  256,   40,  268,  269,  270,  271,   45,  273,   41,   93,
  256,  257,   45,  265,  260,   42,  256,  263,  264,  265,
   47,   59,  268,  269,  270,  271,  266,  273,   45,   45,
  256,  257,   59,   60,  260,   62,   40,  263,  264,  265,
   59,   45,  268,  269,  270,  271,   45,  273,  265,   41,
  256,  257,  265,   45,  260,   42,   45,  263,  264,  265,
   47,   93,  268,  269,  270,  271,  265,  273,   45,  265,
  256,  257,   59,   60,  260,   62,   41,  263,  264,  265,
   45,   93,  268,  269,  270,  271,   45,  273,   42,  265,
  256,  257,   59,   47,  260,  258,  259,  263,  264,  265,
  256,   45,  268,  269,  270,  271,   60,  273,   62,  265,
   59,  267,  268,  269,  270,  271,  272,   59,  256,  257,
  258,  259,  260,   59,   43,  263,   45,  265,  256,  267,
   59,  256,  257,   59,  272,  260,   45,  265,  263,  267,
  265,   60,  267,   62,  272,  258,  259,  272,  256,  257,
   59,  259,  260,   59,   59,  263,  265,  265,  267,  267,
  256,  256,  257,   59,  272,  260,   41,   41,  263,  265,
  265,  267,  267,   41,  256,  257,  272,  272,  260,   42,
   41,  263,  264,  265,   47,  256,  257,  259,   59,  260,
  259,  273,  263,  264,  265,   42,   43,  259,   45,  259,
   47,   59,  273,  256,  257,   59,  256,  260,  258,  259,
  263,  264,  265,   60,   59,   62,   59,  256,  256,  257,
  273,   59,  260,  256,    0,  263,  265,  265,  267,  256,
  257,    0,  265,  260,  267,  272,  263,    7,  265,  256,
  256,  268,  269,  270,  271,    5,  273,   26,  265,  265,
  267,  267,  256,  256,   52,   14,   -1,  256,  261,  262,
   -1,  265,  265,  267,  256,   -1,  265,  256,  267,   -1,
  273,   -1,   -1,  265,  266,  267,  265,  264,  267,  256,
   -1,  268,  269,  270,  271,   -1,   -1,  256,  265,   -1,
  267,  256,  261,  262,   -1,    1,  265,  256,   -1,    5,
  265,  266,  267,   -1,  273,   -1,  265,   -1,  267,  113,
  114,  115,  256,   19,  268,  269,  270,  271,   -1,   -1,
   -1,  265,   -1,  267,   -1,   31,   32,   -1,   -1,   -1,
    1,  256,  257,   -1,    5,  260,   -1,  256,  263,  171,
  265,  173,  174,   -1,  176,   -1,   -1,  256,   19,  268,
  269,  270,  271,   -1,   -1,   -1,  265,   -1,  267,   -1,
   31,   32,   -1,  256,  257,   -1,   -1,  260,   -1,   -1,
  263,  264,  265,   -1,  256,  257,   -1,   -1,  260,   50,
  273,  263,  264,  265,   -1,   56,  256,  257,   59,   -1,
  260,  273,   -1,  263,  100,  265,   -1,  256,  257,   -1,
   -1,  260,   -1,  273,  263,   -1,  265,   -1,  114,  256,
   -1,   -1,   -1,   -1,  273,   -1,   -1,   -1,   -1,   -1,
   -1,  268,  269,  270,  271,   -1,   -1,   -1,   99,  100,
  256,  257,   -1,   -1,  260,   -1,   -1,  263,   -1,  265,
   -1,  256,  257,  114,   -1,  260,   -1,  273,  263,   -1,
  265,   -1,   -1,   -1,   -1,   -1,   -1,  128,  273,  130,
  131,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  190,   -1,   -1,   -1,  159,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  171,   -1,  173,  174,   -1,  176,   -1,   -1,   -1,   -1,
  216,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  190,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  216,
};
}
final static short YYFINAL=6;
final static short YYMAXTOKEN=278;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'",null,"'>'",null,null,null,null,null,null,null,null,null,null,null,null,
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
"WHILE","DO","ID","CADENA","CTE","MAYOR_IGUAL","MENOR_IGUAL","DISTINTO","IGUAL",
"ASIGN","BEGIN","END","FIRST","LAST","LENGTH","\"EOF\"",
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
"sentencia_control : while condicion_while DO bloque_sentencias ';'",
"sentencia_control : error condicion_while DO bloque_sentencias ';'",
"sentencia_control : while condicion_while error bloque_sentencias ';'",
"sentencia_control : while error DO bloque_sentencias ';'",
"sentencia_control : while condicion_while DO error ';'",
"sentencia_control : while error ';'",
"sentencia_control : while condicion_while DO bloque_sentencias error",
"while : WHILE",
"condicion_while : condicion",
"sentencia_seleccion : IF condicion_if bloque_then END_IF ';'",
"sentencia_seleccion : IF condicion_if bloque_then else bloque_else END_IF ';'",
"sentencia_seleccion : error condicion_if bloque_then END_IF ';'",
"sentencia_seleccion : IF condicion_if bloque_then error ';'",
"sentencia_seleccion : IF condicion_if error END_IF ';'",
"sentencia_seleccion : IF error bloque_then END_IF ';'",
"sentencia_seleccion : IF condicion_if bloque_then END_IF error",
"sentencia_seleccion : error condicion_if bloque_then else bloque_else END_IF ';'",
"sentencia_seleccion : IF condicion_if bloque_then error bloque_else END_IF ';'",
"sentencia_seleccion : IF condicion_if bloque_then else bloque_else error ';'",
"sentencia_seleccion : IF condicion_if error else bloque_else END_IF ';'",
"sentencia_seleccion : IF condicion_if bloque_then else error END_IF ';'",
"sentencia_seleccion : IF error bloque_then else bloque_else END_IF ';'",
"sentencia_seleccion : IF condicion_if bloque_then else bloque_else END_IF error",
"sentencia_seleccion : IF error ';'",
"condicion_if : condicion",
"bloque_then : bloque_sentencias",
"else : ELSE",
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
"comparador : IGUAL",
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

//#line 355 "gramatica.y"

private Lexer al;
private boolean verbose;

public Parser(Lexer al, boolean as_verbose) {
  this.al = al;
  this.verbose = as_verbose;
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


public void check_range(String cte, boolean negativo) {

        int new_cte;
        lexer.Token old_token = globals.SymbolTable.getLex(cte);

        if(negativo){
        	if (Long.parseLong(cte) <= Math.pow(2, 15)) {
		    new_cte = Integer.valueOf(cte);
		} else {
		    Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "WARNING Constante fuera de rango: -" + cte));
		    Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "WARNING Se va a reemplazar por el valor: -" + Math.pow(2, 15)));
		    new_cte = (int) Math.pow(2, 15);
		}
		String new_lex = "-" + new_cte;
                if (!globals.SymbolTable.contains(new_lex)) {
                    lexer.Token t = new lexer.Token(old_token.getID(), new_lex, "CTE NEG");
                    globals.SymbolTable.add(t);
        	    t.addAttr("type", "INT");
        	    t.addAttr("use", "CTE NEG");
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
                tipos.push("INT");
        }else{
        	if (Long.parseLong(cte) >= Math.pow(2, 15)) {
		   old_token.addAttr("type", "ULONG");
		   tipos.push("ULONG");
		}else{
		   tipos.push("INT");
		}
		old_token.addAttr("use", "CTE POS");
        }
    }

public void addVariable(String lex, String use){
    	lexer.Token token = globals.SymbolTable.getLex(lex);
 	if(token.getAttr("use") == null){
 		token.addAttr("use", use);
 		token.addAttr("type", type);
 	}else{
 		Error.add(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR La variable " + lex + " ya se encuentra declarada."));
 	}
}

public Integer checkTypes(String exp1, String exp2) {
        String tipo1 = "", tipo2 = "";
        if (!tipos.isEmpty())
            tipo2 = tipos.pop();
        if (!tipos.isEmpty())
            tipo1 = tipos.pop();

        if (tipo1 != tipo2)
        {
            tipos.push("ULONG");

            if (tipo2 == "INT") {
                Terceto t = new Terceto("_CONV", exp2);
                AdminTercetos.add(t);
                return 2; //Indice del argumento a convertir.
            }
            else {
                Terceto t = new Terceto("_CONV", exp1);
                AdminTercetos.add(t);
                return 1;
            }
        } else
            tipos.push(tipo1);
        return 0;
}

public String crearTercetoOperacion(String op, String arg1, String arg2){
	Integer conv = checkTypes(arg1, arg2);
	if (conv == 1)
		arg1 = AdminTercetos.last().getId();
	if (conv == 2)
		arg2 = AdminTercetos.last().getId();
	Terceto terceto = new Terceto(op, arg1, arg2);
	String tipo = tipos.pop();
	terceto.setType(tipo);
	tipos.push(tipo);
	AdminTercetos.add(terceto);
	return terceto.getId();
}
//#line 694 "Parser.java"
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
//#line 40 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Programa compuesto por bloque declarativo."));}
break;
case 2:
//#line 41 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Programa compuesto por bloque ejecutable."));}
break;
case 3:
//#line 42 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Programa compuesto por bloques ejecutables y declarativas."));}
break;
case 4:
//#line 43 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en programa"));}
break;
case 8:
//#line 51 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave BEGIN."));}
break;
case 9:
//#line 52 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave END."));}
break;
case 10:
//#line 53 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque de ejecucion."));}
break;
case 11:
//#line 54 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta literal ';'."));}
break;
case 12:
//#line 55 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave 'END;'."));}
break;
case 18:
//#line 67 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Tipo no declarado."));}
break;
case 19:
//#line 68 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Tipo no reconocido."));}
break;
case 20:
//#line 69 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en lista de variables."));}
break;
case 21:
//#line 70 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia declarativa."));}
break;
case 22:
//#line 73 "gramatica.y"
{type = "INT";}
break;
case 23:
//#line 74 "gramatica.y"
{type = "ULONG";}
break;
case 24:
//#line 77 "gramatica.y"
{addVariable(val_peek(0).sval, "VARIABLE");}
break;
case 25:
//#line 78 "gramatica.y"
{addVariable(val_peek(0).sval, "COLECCION");}
break;
case 26:
//#line 79 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR falta ',' entre variables."));}
break;
case 27:
//#line 80 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR falta ',' entre variables."));}
break;
case 28:
//#line 81 "gramatica.y"
{addVariable(val_peek(0).sval, "VARIABLE");}
break;
case 29:
//#line 82 "gramatica.y"
{addVariable(val_peek(0).sval, "COLECCION");}
break;
case 30:
//#line 85 "gramatica.y"
{
							Token coleccion = SymbolTable.getLex(val_peek(3).sval);
							String tipo_cte = tipos.pop();
							if(tipo_cte.equals("INT")){
								if (coleccion.getAttr("use") == null){
									if (Integer.parseInt(val_peek(1).sval) >= 0){
										coleccion.addAttr("size", val_peek(1).sval);
									}else
										Error.add(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR tamaño del arreglo no puede ser negativo."));
								}
								else
							    		Error.add(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR La variable " + val_peek(3).sval + " ya se encuentra declarada."));
							}else
								Error.add(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR tamaño del arreglo debe ser un entero."));

						}
break;
case 31:
//#line 101 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el tamaño de la declaracion."));}
break;
case 32:
//#line 102 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la declaracion del tamaño de la coleccion."));}
break;
case 37:
//#line 112 "gramatica.y"
{AdminTercetos.add(new Terceto("PRINT", val_peek(2).sval)); if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia Print."));}
break;
case 38:
//#line 113 "gramatica.y"
{AdminTercetos.add(new Terceto("PRINT", val_peek(2).sval));}
break;
case 39:
//#line 114 "gramatica.y"
{AdminTercetos.add(new Terceto("PRINT", val_peek(2).sval));}
break;
case 40:
//#line 115 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave PRINT."));}
break;
case 41:
//#line 116 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal '('."));}
break;
case 42:
//#line 117 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ')'."));}
break;
case 43:
//#line 118 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la cadena a imprimir."));}
break;
case 44:
//#line 119 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la cadena a imprimir."));}
break;
case 45:
//#line 120 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la cadena a imprimir."));}
break;
case 46:
//#line 121 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR faltan los parentesis."));}
break;
case 47:
//#line 122 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia PRINT."));}
break;
case 48:
//#line 123 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ';'"));}
break;
case 49:
//#line 126 "gramatica.y"
{	String terceto_inc = AdminTercetos.pop();
											AdminTercetos.get(terceto_inc).setOperando2("["+String.valueOf(AdminTercetos.cantTercetos() + 2)+"]");
											terceto_inc = AdminTercetos.pop();
											AdminTercetos.add(new Terceto("BI", terceto_inc, null));
											AdminTercetos.add(new Terceto ("Label", "["+(AdminTercetos.cantTercetos()+1)+"]", null));
											if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia While."));
										}
break;
case 50:
//#line 133 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave WHILE."));}
break;
case 51:
//#line 134 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave DO."));}
break;
case 52:
//#line 135 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de WHILE."));}
break;
case 53:
//#line 136 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el bloque de sentencias WHILE."));}
break;
case 54:
//#line 137 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia WHILE."));}
break;
case 55:
//#line 138 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ';'."));}
break;
case 56:
//#line 141 "gramatica.y"
{AdminTercetos.add(new Terceto ("Label","["+(AdminTercetos.cantTercetos()+1)+"]", null));
					 AdminTercetos.push(AdminTercetos.last().getId());}
break;
case 57:
//#line 145 "gramatica.y"
{AdminTercetos.add(new Terceto("BF", val_peek(0).sval, null)); AdminTercetos.push(AdminTercetos.last().getId());}
break;
case 58:
//#line 148 "gramatica.y"
{	String terceto_inc = AdminTercetos.pop();
										AdminTercetos.get(terceto_inc).setOperando2("["+String.valueOf(AdminTercetos.cantTercetos() + 1)+"]");
										AdminTercetos.add(new Terceto ("Label","["+(AdminTercetos.cantTercetos()+1)+"]", null));
										if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia If.."));}
break;
case 59:
//#line 152 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia If-Else."));}
break;
case 60:
//#line 153 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR al comienzo de la sentencia If."));}
break;
case 61:
//#line 154 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR al final de la sentencia If."));}
break;
case 62:
//#line 155 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el bloque de sentencias IF."));}
break;
case 63:
//#line 156 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de IF."));}
break;
case 64:
//#line 157 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ';'."));}
break;
case 65:
//#line 158 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave IF."));}
break;
case 66:
//#line 159 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave ELSE."));}
break;
case 67:
//#line 160 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave END_IF."));}
break;
case 68:
//#line 161 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque THEN de la sentencia IF."));}
break;
case 69:
//#line 162 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque ELSE de la sentencia IF."));}
break;
case 70:
//#line 163 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de la sentencia IF-ELSE."));}
break;
case 71:
//#line 164 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ';'."));}
break;
case 72:
//#line 165 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en sentencia IF."));}
break;
case 73:
//#line 168 "gramatica.y"
{AdminTercetos.add(new Terceto("BF", val_peek(0).sval, null)); AdminTercetos.push(AdminTercetos.last().getId());}
break;
case 75:
//#line 174 "gramatica.y"
{String terceto_inc = AdminTercetos.pop();
					AdminTercetos.get(terceto_inc).setOperando2("["+String.valueOf(AdminTercetos.cantTercetos() + 2)+"]");
					AdminTercetos.add(new Terceto("BI", null, null));
					AdminTercetos.push(AdminTercetos.last().getId());
					AdminTercetos.add(new Terceto ("Label","["+(AdminTercetos.cantTercetos()+1)+"]", null));}
break;
case 76:
//#line 181 "gramatica.y"
{	String terceto_inc = AdminTercetos.pop();
							AdminTercetos.get(terceto_inc).setOperando1("["+String.valueOf(AdminTercetos.cantTercetos() + 1)+"]");
							AdminTercetos.add(new Terceto ("Label","["+(AdminTercetos.cantTercetos()+1)+"]", null));
						    }
break;
case 77:
//#line 187 "gramatica.y"
{yyval = val_peek(1);}
break;
case 78:
//#line 188 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el caracter ("));}
break;
case 79:
//#line 189 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el caracter )"));}
break;
case 80:
//#line 190 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR faltan ambos parentesis en la condicion"));}
break;
case 81:
//#line 191 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion."));}
break;
case 82:
//#line 194 "gramatica.y"
{yyval = new ParserVal(crearTercetoOperacion(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval));}
break;
case 83:
//#line 195 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el lado izquierdo de la comparacion."));}
break;
case 84:
//#line 196 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta comparador."));}
break;
case 85:
//#line 197 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el lado derecho de la comparacion."));}
break;
case 86:
//#line 200 "gramatica.y"
{ yyval = new ParserVal("<");}
break;
case 87:
//#line 201 "gramatica.y"
{ yyval = new ParserVal(">");}
break;
case 88:
//#line 202 "gramatica.y"
{ yyval = val_peek(0);}
break;
case 89:
//#line 203 "gramatica.y"
{ yyval = val_peek(0);}
break;
case 90:
//#line 204 "gramatica.y"
{ yyval = val_peek(0);}
break;
case 91:
//#line 205 "gramatica.y"
{ yyval = val_peek(0);}
break;
case 92:
//#line 208 "gramatica.y"
{
							    boolean conversion = false;
							    String tipo_exp="";
							    String tipo_id="";
							    if (!tipos.isEmpty())
							    	tipo_exp = tipos.pop();
							    if (!tipos.isEmpty())
							    	tipo_id = tipos.pop();
							    if(tipo_id == "INT"){
							    	if(tipo_exp == "ULONG"){
							    		Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR tipos incompatibles."));
							    	}
							    }else{
								if (tipo_exp == "INT") {
									conversion = true;
									Terceto t = new Terceto("_CONV", val_peek(1).sval);
									t.setType("ULONG"); /*TODO Verificar si es necesario setear el tipo aca.*/
									AdminTercetos.add(t);
							 	}
							    }
							    Terceto t = new Terceto(":=", val_peek(3).sval);
							    if(conversion)
								t.setOperando2(AdminTercetos.last().getId());
							    else
								t.setOperando2(val_peek(1).sval);
							    t.setType(tipo_id);
							    AdminTercetos.add(t);
							    if (this.verbose)
								Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia Asign."));
                                    			}
break;
case 93:
//#line 238 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el ID de la ."));}
break;
case 94:
//#line 239 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el lado derecho de la asignacion."));}
break;
case 95:
//#line 240 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el lado derecho de la asignacion."));}
break;
case 96:
//#line 244 "gramatica.y"
{ yyval = new ParserVal(crearTercetoOperacion("+", val_peek(2).sval, val_peek(0).sval)); }
break;
case 97:
//#line 245 "gramatica.y"
{ yyval = new ParserVal(crearTercetoOperacion("-", val_peek(2).sval, val_peek(0).sval)); }
break;
case 99:
//#line 247 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '+'"));}
break;
case 100:
//#line 248 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '-'"));}
break;
case 101:
//#line 251 "gramatica.y"
{ yyval = new ParserVal(crearTercetoOperacion("*", val_peek(2).sval, val_peek(0).sval)); }
break;
case 102:
//#line 252 "gramatica.y"
{ yyval = new ParserVal(crearTercetoOperacion("/", val_peek(2).sval, val_peek(0).sval)); }
break;
case 104:
//#line 254 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '*'"));}
break;
case 105:
//#line 255 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '*"));}
break;
case 106:
//#line 256 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '/'"));}
break;
case 107:
//#line 257 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '/'"));}
break;
case 110:
//#line 262 "gramatica.y"
{	if(SymbolTable.getLex(val_peek(2).sval).getAttr("use") == "COLECCION"){
								if(val_peek(0).sval == "_length"){
									tipos.push("INT");
								}else{
									tipos.push(SymbolTable.getLex(val_peek(2).sval).getAttr("type"));
								}
								Terceto t = new Terceto("call", val_peek(0).sval, val_peek(2).sval);
                                String tipo = tipos.pop();
                                t.setType(tipo);
                                tipos.push(tipo);
								AdminTercetos.add(t);
								yyval = new ParserVal(t.getId());
							}else{
								Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR " + val_peek(2).sval + " no es una coleccion."));
							}
						 }
break;
case 111:
//#line 277 "gramatica.y"
{yyval = new ParserVal("_first");}
break;
case 112:
//#line 278 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion FIRST."));}
break;
case 113:
//#line 279 "gramatica.y"
{yyval = new ParserVal("_last");}
break;
case 114:
//#line 280 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion LAST."));}
break;
case 115:
//#line 281 "gramatica.y"
{yyval = new ParserVal("_length");}
break;
case 116:
//#line 282 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion LENGTH."));}
break;
case 117:
//#line 283 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Funcion desconocida."));}
break;
case 118:
//#line 284 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Funcion desconocida."));}
break;
case 119:
//#line 287 "gramatica.y"
{
                                            if(SymbolTable.getLex(val_peek(0).sval).getAttr("use") != null){
                                            	yyval = val_peek(0);
                                                tipos.push(SymbolTable.getLex(val_peek(0).sval).getAttr("type"));
                                            }
                                            else
                                               Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + val_peek(0).sval + " no declarada."));
                                       }
break;
case 120:
//#line 295 "gramatica.y"
{
						    Token coleccion = SymbolTable.getLex(val_peek(3).sval);
						    Token tamaño = SymbolTable.getLex(val_peek(1).sval);
						     if(coleccion.getAttr("use") != null){
							if(coleccion.getAttr("use") == "COLECCION"){
								if (tamaño.getAttr("use") != null){
								    if (tamaño.getAttr("type").equals("INT")) {
									yyval = new ParserVal(val_peek(3).sval+"["+val_peek(1).sval+"]");
									tipos.push(coleccion.getAttr("type"));
								    }
								    else
									Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + val_peek(1).sval + " no es de tipo INT."));
								}
								else
								    Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + val_peek(1).sval + " no declarada."));
						     	}
						     	else
								Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR " + val_peek(3).sval + " no es una coleccion."));
						     }
						     else
                                                        Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + val_peek(3).sval + " no declarada."));
                                                }
break;
case 121:
//#line 317 "gramatica.y"
{
							Token coleccion = SymbolTable.getLex(val_peek(3).sval);
					    		Token tamaño = SymbolTable.getLex(val_peek(1).sval);
					       		if(coleccion.getAttr("use") != null){
					       		  if(coleccion.getAttr("use") == "COLECCION"){
								if (tamaño.getAttr("use") != null){
							      		if (tamaño.getAttr("type").equals("INT")){
								  		yyval = new ParserVal(val_peek(3).sval+"["+val_peek(1).sval+"]");
								  		tipos.pop(); /*elimino el tipo de indice*/
							  			tipos.push(coleccion.getAttr("type"));
							      		} else
								  		Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + val_peek(1).sval + " no es de tipo INT."));
							  	} else
							      		Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + val_peek(1).sval + " no declarada."));
					       		  } else {
					       		  	Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR " + val_peek(3).sval + " no es una coleccion."));
					       		  }
						       }
						       else
							  Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + val_peek(3).sval + " no declarada."));
						  }
break;
case 122:
//#line 338 "gramatica.y"
{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el subindice de la coleccion. Se esperaba un INT."));}
break;
case 123:
//#line 341 "gramatica.y"
{
                                            String cte = val_peek(0).sval;
                                            check_range(cte, false);
                                            yyval = val_peek(0);
                                        }
break;
case 124:
//#line 346 "gramatica.y"
{
						String cte = val_peek(0).sval;
						check_range(cte, true);
						yyval = new ParserVal("-" + cte);
                                           }
break;
//#line 1400 "Parser.java"
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
