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


import javax.swing.*;

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
    0,    0,    0,    0,    1,    1,    4,    4,    2,    2,
    2,    2,    2,    2,    6,    6,    3,    3,    3,    3,
    3,    7,    7,    8,    8,    8,    8,    9,    9,    9,
    5,    5,    5,    5,   14,   14,   14,   14,   14,   14,
   14,   14,   14,   14,   14,   13,   13,   13,   13,   13,
   13,   13,   12,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   15,   15,   15,
   15,   15,   16,   16,   16,   16,   18,   18,   18,   18,
   18,   18,   11,   11,   11,   11,   17,   17,   17,   17,
   17,   20,   20,   20,   20,   20,   20,   20,   21,   21,
   21,   22,   22,   22,   22,   22,   22,   22,   22,   19,
   19,   19,   19,   10,   10,
};
final static short yylen[] = {                            2,
    1,    1,    2,    1,    2,    1,    1,    1,    4,    4,
    4,    4,    4,    3,    2,    1,    3,    2,    3,    3,
    2,    1,    1,    3,    3,    1,    1,    4,    3,    4,
    1,    1,    1,    1,    5,    5,    5,    5,    4,    4,
    4,    5,    4,    3,    5,    5,    5,    5,    5,    5,
    3,    5,    5,    7,    5,    5,    5,    5,    5,    7,
    7,    7,    7,    7,    7,    7,    3,    3,    2,    2,
    1,    2,    3,    3,    3,    3,    1,    1,    1,    1,
    1,    1,    4,    4,    3,    3,    3,    3,    1,    3,
    3,    3,    3,    1,    3,    3,    3,    3,    1,    1,
    3,    3,    2,    3,    2,    3,    2,    3,    1,    1,
    4,    4,    4,    1,    2,
};
final static short yydefred[] = {                         0,
    0,   22,   23,    0,    0,    0,    0,    2,    6,    0,
    0,   27,    0,    0,    0,    0,    0,   21,   16,    0,
    0,   31,   32,   33,   34,    0,    0,    0,    0,    0,
    0,    3,    5,    0,    0,   18,    0,    0,    0,  114,
    0,    0,    0,  100,    0,    0,    0,   99,    0,   94,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   15,   19,    0,    0,   29,    0,    0,    0,    0,    0,
   14,   20,   17,    0,   25,   81,   80,   82,    0,    0,
   77,   78,   79,    0,    0,    0,    0,  115,    0,   72,
    0,    0,    0,    7,    0,    8,   69,    0,    0,    0,
    0,    0,    0,   67,    0,    0,    0,   44,    0,    0,
    0,    0,    0,    0,    0,   51,    0,    0,    0,    0,
    0,   10,    0,   85,    0,   30,   28,   12,    0,    0,
   11,   13,    9,   95,   97,    0,    0,    0,    0,    0,
  101,   84,    0,   68,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   96,   92,   98,   93,
    0,    0,    0,    0,    0,    0,    0,   39,   43,    0,
    0,   40,    0,   41,    0,    0,    0,    0,    0,    0,
  111,    0,   83,  113,  112,    0,  103,    0,  105,    0,
  107,    0,   38,   47,    0,   55,    0,   58,    0,   57,
   56,    0,    0,    0,   59,   53,   42,   36,   45,   35,
   37,   49,   48,   50,   52,   46,  108,  102,  104,  106,
    0,    0,    0,    0,    0,    0,    0,   60,   65,   63,
   61,   64,   62,   66,   54,
};
final static short yydgoto[] = {                          6,
    7,   94,    9,   95,   96,   20,   10,   11,   12,   44,
   22,   23,   24,   25,   45,   46,   47,   84,   48,   49,
   50,  141,
};
final static short yysindex[] = {                      -103,
  335,    0,    0,  -80,  493,    0,  476,    0,    0, -127,
   80,    0,   22,   44,  -11,   48,  -26,    0,    0,  312,
  123,    0,    0,    0,    0, -179,  115,  -40,   17,  -73,
  335,    0,    0,   59,  137,    0, -133,  454,  -21,    0,
  -44, -121,   70,    0,  301,   99,  413,    0,   -4,    0,
  384,  150,  403,   89,  113,   92,  451, -240,  -43,   97,
    0,    0,  232,   84,    0,   96,  116,  252,    4,  -53,
    0,    0,    0,  -80,    0,    0,    0,    0,    3,    3,
    0,    0,    0,  -44, -140,   -2,   15,    0,  161,    0,
  166,  165,  429,    0, -146,    0,    0,  -44,  267,  273,
  274,  277,  289,    0,   24,  128,   85,    0,  135,  -33,
  168,  -13,  152,  173,  429,    0,  429,  446,  122,  126,
  154,    0,   -2,    0,   37,    0,    0,    0,  160,  163,
    0,    0,    0,    0,    0,  -22,  111,  -36,  -30,  -10,
    0,    0,  215,    0,  110, -179,  231,  429,  233,  -22,
   -2,   -4,   -2,   -4,   -2,  -22,    0,    0,    0,    0,
  429,  237,  429,  240,  324,  471,  -50,    0,    0,  246,
  255,    0,  -39,    0,  262,  268,  272,  140,   -7,    0,
    0,    0,    0,    0,    0,  292,    0,  299,    0,  321,
    0,  322,    0,    0,   94,    0,  106,    0,  130,    0,
    0,  131,  153, -165,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  311,  333,  339,  342,  343,  345,    9,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
  408,    0,    0,  142,    0,    0,  414,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  148,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -28,    0,
    0,    0,    0,    0,    0,  350,    0,    0,   16,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  208,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  364,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  290,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  177,   -6,    0,    0,    0,
    0,    0,    0,    0,    0,  444,    0,    0,    0,  197,
   38,   60,   82,  104,  207,  220,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -37,
    0,  -32,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   95,  416,   13,  573,  422,    0,   41,  392,   58,
    0,    0,    0,    0,   57,  145,    6,  388,  581,  256,
    7,    0,
};
final static int YYTABLESIZE=784;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         43,
   42,   42,   26,  188,   42,  133,   30,  170,  206,  190,
   27,   28,  110,  110,  110,  117,  110,   26,  110,  210,
   99,   30,  100,  118,   85,  169,   28,  173,   56,  192,
  110,  110,  110,  110,  109,  109,  109,  102,  109,   79,
  109,   21,  103,   43,   80,  172,   87,   42,   42,   65,
   35,  216,  109,  109,  109,  109,   89,   99,   89,  100,
   89,   43,  131,  105,   59,  107,   42,  235,  125,   68,
   53,   21,   58,  142,   89,   89,   89,   89,   90,   99,
   90,  100,   90,   52,   66,  134,  135,   52,   42,  136,
  226,   63,   42,  227,    8,  183,   90,   90,   90,   90,
   87,   32,   87,  150,   87,  147,  156,   68,  158,  160,
   90,  148,  149,  114,   42,  137,  121,   72,   87,   87,
   87,   87,   91,   37,   91,  130,   91,  176,   34,  177,
  179,   74,  113,  138,  139,  140,   42,    4,   36,   97,
   91,   91,   91,   91,   88,   88,   88,  108,   88,   43,
  186,   79,    1,  109,   42,  122,   80,    2,    3,   42,
  195,    4,   88,   88,   88,   88,   37,   43,    5,   81,
   83,   82,   42,  197,  128,  199,  126,  202,  204,   43,
   37,   62,   69,   14,   42,   26,   15,   91,  127,   16,
   90,   29,   43,  168,   42,   73,   91,   42,  214,   70,
   26,  143,  132,   71,   43,  205,  144,   65,  171,   42,
  174,   86,  119,  175,  180,   38,  209,   74,  181,  187,
   39,  120,   40,   40,   39,  189,   40,  110,  110,  110,
   41,  110,   67,  113,  110,  110,  110,   75,  112,  110,
  110,  110,  110,  110,   54,  191,  182,   76,  215,  109,
  109,   24,  184,  109,   55,  185,  109,  109,  109,   38,
   73,  109,  109,  109,  234,  109,   24,   39,   39,   40,
   40,   89,   89,  193,   41,   89,   42,   38,   89,   89,
   89,  161,  162,   89,   89,   89,   39,   89,   40,  194,
  124,  196,   41,   90,   90,  198,   42,   90,  200,   51,
   90,   90,   90,   57,  207,   90,   90,   90,   39,   90,
   40,   42,   39,  208,   40,   87,   87,   42,   42,   87,
  211,   42,   87,   87,   87,   38,  212,   87,   87,   87,
  213,   87,  217,   42,   39,   89,   40,   91,   91,  218,
  165,   91,  166,  167,   91,   91,   91,  110,   86,   91,
   91,   91,  221,   91,  152,  154,  111,  112,   40,   88,
   88,  219,  220,   88,  222,   38,   88,   88,   88,  228,
   64,   88,   88,   88,   39,   88,   40,   76,   77,   78,
   41,   40,  201,  145,   14,  163,  164,   15,  223,  224,
   16,  229,   39,   18,   40,  145,   14,  230,   41,   15,
  231,  232,   16,  233,   39,   38,   40,    4,  145,   14,
   41,  225,   15,    1,   39,   16,   40,   39,  110,   40,
  145,   14,   33,   41,   15,   79,   30,   16,   75,   39,
   80,   40,   74,   74,  101,   41,   74,    0,    0,   74,
   74,   74,  104,   81,   83,   82,    0,    0,   74,    0,
    0,    0,   75,   75,    0,   99,   75,  100,    0,   75,
   75,   75,   76,   76,    0,    0,   76,    0,   75,   76,
   76,   76,   81,   83,   82,   73,   73,    0,   76,   73,
    0,    0,   73,   73,   73,   99,   99,  123,   99,    0,
   99,   73,   79,    0,    0,   79,   39,   80,   40,    0,
   80,    0,    0,   99,   99,   99,    0,  129,    0,  116,
   81,   83,   82,   81,   83,   82,  120,    0,   40,    0,
    0,    0,  151,    0,    0,    0,    0,    0,  153,  155,
    0,   39,  157,   40,    0,    0,    0,   39,   39,   40,
   40,   39,    0,   40,  159,   86,   86,   86,   86,   86,
    0,    0,   86,   39,   86,   40,   92,   14,    0,    0,
   15,    0,   86,   16,   93,   29,   86,   13,   14,    0,
    0,   15,    5,   19,   16,    0,   29,   19,    0,   92,
   14,   26,    0,   15,   60,   26,   16,    0,   29,    0,
   13,   14,   61,    0,   15,    5,    0,   16,    0,   17,
   26,    0,   61,   19,    0,   71,   71,    0,    0,   71,
   26,   26,   71,   71,   71,    0,    0,    0,    0,   70,
   70,   71,    0,   70,    0,   26,   70,   70,   70,    0,
    0,   26,    0,   26,    0,   70,    0,    0,    0,   92,
   14,    0,    0,   15,    0,    0,   16,    0,   29,    0,
    0,   76,   77,   78,    0,    5,    0,    0,  106,   14,
    0,    0,   15,    0,   19,   16,    0,   29,   98,    0,
    0,    0,  146,   26,    5,    0,    0,    0,   19,    0,
   76,   77,   78,    0,   92,   14,  146,    0,   15,    0,
    0,   16,    0,   29,    0,   26,    0,   26,   26,   99,
    5,  178,   14,    0,    0,   15,    0,    0,   16,    0,
   29,   99,   99,   99,  115,    0,    0,    5,   76,   77,
   78,   76,   77,   78,    0,    0,  203,   14,   26,    0,
   15,   31,    0,   16,    0,   29,    2,    3,    0,    0,
    4,   26,    5,   26,    0,   26,   26,    5,   28,   14,
   19,    0,   15,    0,    0,   16,    0,   29,  146,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   19,    0,    0,    0,    0,
    0,    0,    0,  146,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   45,   45,   44,   40,   45,   59,   44,   41,   59,   40,
   91,   44,   41,   42,   43,  256,   45,   59,   47,   59,
   43,   59,   45,  264,   46,   59,   59,   41,   40,   40,
   59,   60,   61,   62,   41,   42,   43,   42,   45,   42,
   47,    1,   47,   40,   47,   59,   41,   45,   45,   93,
   10,   59,   59,   60,   61,   62,   41,   43,   43,   45,
   45,   40,   59,   51,   91,   53,   45,   59,   63,   91,
   14,   31,   16,   59,   59,   60,   61,   62,   41,   43,
   43,   45,   45,   40,   27,   79,   80,   40,   45,   84,
  256,  271,   45,  259,    0,   59,   59,   60,   61,   62,
   41,    7,   43,   98,   45,   93,  101,   91,  102,  103,
   41,  258,  259,   56,   45,  256,   59,   59,   59,   60,
   61,   62,   41,   44,   43,   68,   45,  115,  256,  117,
  118,  265,   41,  274,  275,  276,   45,  265,   59,   41,
   59,   60,   61,   62,   41,  267,   43,   59,   45,   40,
   40,   42,  256,   41,   45,   59,   47,  261,  262,   45,
  148,  265,   59,   60,   61,   62,   44,   40,  272,   60,
   61,   62,   45,  161,   59,  163,   93,  165,  166,   40,
   44,   59,  256,  257,   45,   44,  260,   43,   93,  263,
   41,  265,   40,   59,   45,   59,   52,   45,   59,  273,
   59,   41,  256,  277,   40,  256,   41,   93,   41,   45,
   59,  256,  256,   41,   93,  256,  256,   41,   93,  256,
  265,  265,  267,  267,  265,  256,  267,  256,  257,  271,
  271,  260,  273,  271,  263,  264,  265,   41,  271,  268,
  269,  270,  271,  272,  256,  256,   93,   41,  256,  256,
  257,   44,   93,  260,  266,   93,  263,  264,  265,  256,
   41,  268,  269,  270,  256,  272,   59,  265,  265,  267,
  267,  256,  257,   59,  271,  260,   45,  256,  263,  264,
  265,  258,  259,  268,  269,  270,  265,  272,  267,   59,
   59,   59,  271,  256,  257,   59,   45,  260,   59,  256,
  263,  264,  265,  256,   59,  268,  269,  270,  265,  272,
  267,   45,  265,   59,  267,  256,  257,   45,   45,  260,
   59,   45,  263,  264,  265,  256,   59,  268,  269,  270,
   59,  272,   41,   45,  265,  266,  267,  256,  257,   41,
  256,  260,  258,  259,  263,  264,  265,  256,   59,  268,
  269,  270,  259,  272,   99,  100,  265,  266,  267,  256,
  257,   41,   41,  260,  259,  256,  263,  264,  265,   59,
  256,  268,  269,  270,  265,  272,  267,  268,  269,  270,
  271,  267,   59,  256,  257,  258,  259,  260,  259,  259,
  263,   59,  265,   59,  267,  256,  257,   59,  271,  260,
   59,   59,  263,   59,  265,  256,  267,    0,  256,  257,
  271,  259,  260,    0,  265,  263,  267,  265,  271,  267,
  256,  257,    7,  271,  260,   42,    5,  263,   37,  265,
   47,  267,  256,  257,   47,  271,  260,   -1,   -1,  263,
  264,  265,   59,   60,   61,   62,   -1,   -1,  272,   -1,
   -1,   -1,  256,  257,   -1,   43,  260,   45,   -1,  263,
  264,  265,  256,  257,   -1,   -1,  260,   -1,  272,  263,
  264,  265,   60,   61,   62,  256,  257,   -1,  272,  260,
   -1,   -1,  263,  264,  265,   42,   43,  256,   45,   -1,
   47,  272,   42,   -1,   -1,   42,  265,   47,  267,   -1,
   47,   -1,   -1,   60,   61,   62,   -1,  256,   -1,   59,
   60,   61,   62,   60,   61,   62,  265,   -1,  267,   -1,
   -1,   -1,  256,   -1,   -1,   -1,   -1,   -1,  256,  256,
   -1,  265,  256,  267,   -1,   -1,   -1,  265,  265,  267,
  267,  265,   -1,  267,  256,  256,  257,  258,  259,  260,
   -1,   -1,  263,  265,  265,  267,  256,  257,   -1,   -1,
  260,   -1,  273,  263,  264,  265,  277,  256,  257,   -1,
   -1,  260,  272,    1,  263,   -1,  265,    5,   -1,  256,
  257,    1,   -1,  260,  273,    5,  263,   -1,  265,   -1,
  256,  257,   20,   -1,  260,  272,   -1,  263,   -1,  265,
   20,   -1,   30,   31,   -1,  256,  257,   -1,   -1,  260,
   30,   31,  263,  264,  265,   -1,   -1,   -1,   -1,  256,
  257,  272,   -1,  260,   -1,   45,  263,  264,  265,   -1,
   -1,   51,   -1,   53,   -1,  272,   -1,   -1,   -1,  256,
  257,   -1,   -1,  260,   -1,   -1,  263,   -1,  265,   -1,
   -1,  268,  269,  270,   -1,  272,   -1,   -1,  256,  257,
   -1,   -1,  260,   -1,   92,  263,   -1,  265,  256,   -1,
   -1,   -1,   92,   93,  272,   -1,   -1,   -1,  106,   -1,
  268,  269,  270,   -1,  256,  257,  106,   -1,  260,   -1,
   -1,  263,   -1,  265,   -1,  115,   -1,  117,  118,  256,
  272,  256,  257,   -1,   -1,  260,   -1,   -1,  263,   -1,
  265,  268,  269,  270,  264,   -1,   -1,  272,  268,  269,
  270,  268,  269,  270,   -1,   -1,  256,  257,  148,   -1,
  260,  256,   -1,  263,   -1,  265,  261,  262,   -1,   -1,
  265,  161,  272,  163,   -1,  165,  166,  272,  256,  257,
  178,   -1,  260,   -1,   -1,  263,   -1,  265,  178,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  203,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  203,
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
"programa : lista_sentencias_declarativas",
"programa : bloque_sentencias_ejecutables",
"programa : lista_sentencias_declarativas bloque_sentencias_ejecutables",
"programa : error",
"lista_sentencias_declarativas : lista_sentencias_declarativas sentencia_declarativa",
"lista_sentencias_declarativas : sentencia_declarativa",
"bloque_sentencias : bloque_sentencias_ejecutables",
"bloque_sentencias : sentencia_ejecutable",
"bloque_sentencias_ejecutables : BEGIN lista_sentencias_ejecutables END ';'",
"bloque_sentencias_ejecutables : error lista_sentencias_ejecutables END ';'",
"bloque_sentencias_ejecutables : BEGIN lista_sentencias_ejecutables error ';'",
"bloque_sentencias_ejecutables : BEGIN error END ';'",
"bloque_sentencias_ejecutables : BEGIN lista_sentencias_ejecutables END error",
"bloque_sentencias_ejecutables : BEGIN lista_sentencias_ejecutables \"EOF\"",
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
"lista_variables : ID",
"lista_variables : coleccion",
"coleccion : ID '[' cte ']'",
"coleccion : ID '[' ']'",
"coleccion : ID '[' error ']'",
"sentencia_ejecutable : sentencia_asignacion",
"sentencia_ejecutable : sentencia_seleccion",
"sentencia_ejecutable : sentencia_control",
"sentencia_ejecutable : sentencia_impresion",
"sentencia_impresion : PRINT '(' CADENA ')' ';'",
"sentencia_impresion : PRINT '(' ID ')' ';'",
"sentencia_impresion : PRINT '(' cte ')' ';'",
"sentencia_impresion : error '(' CADENA ')' ';'",
"sentencia_impresion : PRINT CADENA ')' ';'",
"sentencia_impresion : PRINT '(' CADENA ';'",
"sentencia_impresion : PRINT '(' ')' ';'",
"sentencia_impresion : PRINT '(' error ')' ';'",
"sentencia_impresion : PRINT '(' error ';'",
"sentencia_impresion : PRINT error ';'",
"sentencia_impresion : PRINT '(' CADENA ')' error",
"sentencia_control : WHILE condicion DO bloque_sentencias ';'",
"sentencia_control : error condicion DO bloque_sentencias ';'",
"sentencia_control : WHILE condicion error bloque_sentencias ';'",
"sentencia_control : WHILE error DO bloque_sentencias ';'",
"sentencia_control : WHILE condicion DO error ';'",
"sentencia_control : WHILE error ';'",
"sentencia_control : WHILE condicion DO bloque_sentencias error",
"sentencia_seleccion : IF condicion bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF condicion bloque_sentencias ELSE bloque_sentencias END_IF ';'",
"sentencia_seleccion : error condicion bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF condicion bloque_sentencias error ';'",
"sentencia_seleccion : IF condicion error END_IF ';'",
"sentencia_seleccion : IF error bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF condicion bloque_sentencias END_IF error",
"sentencia_seleccion : error condicion bloque_sentencias ELSE bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF condicion bloque_sentencias error bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF condicion bloque_sentencias ELSE bloque_sentencias error ';'",
"sentencia_seleccion : IF condicion error ELSE bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF condicion bloque_sentencias ELSE error END_IF ';'",
"sentencia_seleccion : IF error bloque_sentencias ELSE bloque_sentencias END_IF ';'",
"sentencia_seleccion : IF condicion bloque_sentencias ELSE bloque_sentencias END_IF error",
"sentencia_seleccion : IF error ';'",
"condicion : '(' comparacion ')'",
"condicion : comparacion ')'",
"condicion : '(' comparacion",
"condicion : comparacion",
"condicion : '(' ')'",
"comparacion : expresion comparador expresion",
"comparacion : error comparador expresion",
"comparacion : expresion error expresion",
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
"sentencia_asignacion : id ASIGN error",
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

//#line 194 "gramatica.y"

//TODO Codigo JAVA

private Lexer al;
private boolean verbose;

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
      Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "P", "|", s), Color.RED);
}


public void check_range(String cte) {

        int new_cte;
        if (Long.parseLong(cte) < Math.pow(2, 15)) {
            new_cte = Integer.valueOf(cte);
        } else {
            Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "WARNING Constante fuera de rango: " + cte), Color.YELLOW);
            Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "WARNING Se va a reemplazar por el valor: -" + Math.pow(2, 15)), Color.YELLOW);
            new_cte = (int) Math.pow(2, 15) - 1;
        }

        String new_lex = "-" + new_cte;
        Token old_token = SymbolTable.getLex(cte);

        if (!SymbolTable.contains(new_lex)) {
            Token t = new Token(old_token.getID(), new_lex, "CTE NEG");
            SymbolTable.add(t);

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
            SymbolTable.remove(old_token.getLex());
        } else {
            old_token.addAttr("contador", String.valueOf(contador));
        }
    }
//#line 596 "Parser.java"
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
//#line 28 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Programa OK"), Color.RESET);}
break;
case 2:
//#line 29 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Programa OK (E)"), Color.RESET);}
break;
case 3:
//#line 30 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Programa OK (D y E)"), Color.RESET);}
break;
case 4:
//#line 31 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en programa"), Color.RED);}
break;
case 10:
//#line 43 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave BEGIN."), Color.RED);}
break;
case 11:
//#line 44 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave END."), Color.RED);}
break;
case 12:
//#line 45 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque de ejecucion."), Color.RED);}
break;
case 13:
//#line 46 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta literal ';'."), Color.RED);}
break;
case 14:
//#line 47 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave 'END;'."), Color.RED);}
break;
case 18:
//#line 55 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Tipo no declarado."), Color.RED);}
break;
case 19:
//#line 56 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Tipo no reconocido."), Color.RED);}
break;
case 20:
//#line 57 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en lista de variables."), Color.RED);}
break;
case 21:
//#line 58 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia declarativa."), Color.RED);}
break;
case 29:
//#line 72 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la declaracion de la coleccion."), Color.RED);}
break;
case 30:
//#line 73 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la declaracion de la coleccion."), Color.RED);}
break;
case 35:
//#line 82 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia PRINT OK."), Color.RESET);}
break;
case 38:
//#line 85 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave PRINT."), Color.RED);}
break;
case 39:
//#line 86 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal '('."), Color.RED);}
break;
case 40:
//#line 87 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ')'."), Color.RED);}
break;
case 41:
//#line 88 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la cadena a imprimir."), Color.RED);}
break;
case 42:
//#line 89 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la cadena a imprimir."), Color.RED);}
break;
case 43:
//#line 90 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la cadena a imprimir."), Color.RED);}
break;
case 44:
//#line 91 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia PRINT."), Color.RED);}
break;
case 45:
//#line 92 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ';'."), Color.RED);}
break;
case 46:
//#line 95 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia WHILE OK"), Color.RESET);}
break;
case 47:
//#line 96 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave WHILE."), Color.RED);}
break;
case 48:
//#line 97 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave DO."), Color.RED);}
break;
case 49:
//#line 98 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de WHILE."), Color.RED);}
break;
case 50:
//#line 99 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia WHILE."), Color.RED);}
break;
case 51:
//#line 100 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia WHILE."), Color.RED);}
break;
case 52:
//#line 101 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ';'."), Color.RED);}
break;
case 53:
//#line 104 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia IF OK."), Color.RESET);}
break;
case 54:
//#line 105 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia IF-ELSE OK."), Color.RESET);}
break;
case 55:
//#line 106 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en sentencia IF."), Color.RED);}
break;
case 56:
//#line 107 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en sentencia IF."), Color.RED);}
break;
case 57:
//#line 108 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque IF."), Color.RED);}
break;
case 58:
//#line 109 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de IF."), Color.RED);}
break;
case 59:
//#line 110 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ';'."), Color.RED);}
break;
case 60:
//#line 111 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave IF."), Color.RED);}
break;
case 61:
//#line 112 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave ELSE."), Color.RED);}
break;
case 62:
//#line 113 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave END_IF."), Color.RED);}
break;
case 63:
//#line 114 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque IF."), Color.RED);}
break;
case 64:
//#line 115 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque ELSE."), Color.RED);}
break;
case 65:
//#line 116 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion."), Color.RED);}
break;
case 66:
//#line 117 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ';'."), Color.RED);}
break;
case 67:
//#line 118 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en sentencia IF."), Color.RED);}
break;
case 69:
//#line 122 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el caracter ("), Color.RED);}
break;
case 70:
//#line 123 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el caracter )"), Color.RED);}
break;
case 71:
//#line 124 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR faltan parentesis en la condicion"), Color.RED);}
break;
case 72:
//#line 125 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion."), Color.RED);}
break;
case 74:
//#line 129 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la comparacion."), Color.RED);}
break;
case 75:
//#line 130 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la comparacion."), Color.RED);}
break;
case 76:
//#line 131 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la comparacion."), Color.RED);}
break;
case 83:
//#line 142 "gramatica.y"
{if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia ASIGN OK."), Color.RESET);}
break;
case 84:
//#line 143 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el ID de la asignacion."), Color.RED);}
break;
case 85:
//#line 144 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el lado derecho de la asignacion."), Color.RED);}
break;
case 86:
//#line 146 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ';'."), Color.RED);}
break;
case 90:
//#line 153 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '+'"), Color.RED);}
break;
case 91:
//#line 154 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '-'"), Color.RED);}
break;
case 95:
//#line 160 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '*'"), Color.RED);}
break;
case 96:
//#line 161 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '*"), Color.RED);}
break;
case 97:
//#line 162 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '/'"), Color.RED);}
break;
case 98:
//#line 163 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '/'"), Color.RED);}
break;
case 103:
//#line 172 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion FIRST."), Color.RED);}
break;
case 105:
//#line 174 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion LAST."), Color.RED);}
break;
case 107:
//#line 176 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion LENGTH."), Color.RED);}
break;
case 108:
//#line 177 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Funcion desconocida."), Color.RED);}
break;
case 109:
//#line 178 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Funcion desconocida."), Color.RED);}
break;
case 113:
//#line 184 "gramatica.y"
{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el subindice de la coleccion."), Color.RED);}
break;
case 115:
//#line 188 "gramatica.y"
{String cte = val_peek(0).sval;
								             check_range(val_peek(0).sval);}
break;
//#line 1026 "Parser.java"
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
public Parser(Lexer al, boolean verbose)
{
  this.al = al;
  this.verbose = verbose;
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
