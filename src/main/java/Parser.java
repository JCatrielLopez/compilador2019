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
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    1,    1,    2,    2,    2,    2,
    4,    4,    3,    3,    3,    3,    6,    6,    7,    7,
    7,    7,    8,    8,    8,    5,    5,    5,    5,   13,
   13,   13,   13,   13,   13,   13,   13,   13,   12,   12,
   12,   12,   12,   12,   11,   11,   11,   11,   11,   11,
   11,   11,   11,   11,   11,   11,   11,   11,   11,   14,
   14,   14,   14,   14,   15,   15,   15,   15,   17,   17,
   17,   17,   17,   17,   10,   10,   10,   10,   10,   16,
   16,   16,   16,   16,   19,   19,   19,   19,   19,   19,
   19,   20,   20,   20,   21,   21,   21,   21,   21,   21,
   21,   21,   18,   18,   18,   18,    9,    9,
};
final static short yylen[] = {                            2,
    1,    1,    2,    1,    2,    1,    4,    3,    4,    4,
    2,    1,    3,    2,    3,    3,    1,    1,    3,    3,
    1,    1,    4,    3,    4,    1,    1,    1,    1,    5,
    5,    4,    4,    5,    4,    2,    3,    5,    5,    5,
    5,    5,    5,    5,    5,    7,    5,    5,    5,    5,
    5,    7,    7,    7,    7,    7,    7,    7,    7,    3,
    2,    2,    1,    2,    3,    3,    3,    3,    1,    1,
    1,    1,    1,    1,    4,    3,    3,    4,    4,    3,
    3,    1,    3,    3,    3,    3,    1,    3,    3,    3,
    3,    1,    1,    3,    3,    2,    3,    2,    3,    2,
    3,    1,    1,    4,    4,    4,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   17,   18,    0,    0,    0,    0,    0,
    0,    2,    6,    0,   12,    0,    0,   22,   26,   27,
   28,   29,    0,    0,    0,  107,    0,    0,    0,    0,
   93,    0,    0,    0,    0,    0,   87,    0,    0,    0,
    0,   92,   36,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    3,    5,    0,   11,    0,    0,
   14,    0,    0,    0,   73,   72,   74,    0,    0,   69,
   70,   71,    0,    0,  108,    0,   64,    0,    0,   15,
    0,    0,    0,   61,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   24,    0,   76,    0,    8,    0,   16,
   13,    0,   20,    0,   77,    0,   88,   90,    0,    0,
    0,    0,    0,   94,    0,   60,    9,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   89,   85,   91,
   86,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   32,    0,   33,    0,   35,    0,    0,    0,    0,    0,
  104,    0,   10,    7,    0,    0,   79,   78,   75,    0,
   96,    0,   98,    0,  100,    0,   31,   40,    0,   47,
    0,   51,  106,  105,    0,    0,   50,   48,    0,    0,
    0,   49,   45,   38,   34,   30,   43,   41,   44,   42,
   39,   25,   23,  101,   95,   97,   99,    0,    0,    0,
    0,    0,    0,    0,    0,   52,   59,   58,   56,   53,
   57,   54,   55,   46,
};
final static short yydgoto[] = {                         10,
   11,   12,   13,   14,   15,   16,   17,   18,   31,   19,
   20,   21,   22,   32,   33,   34,   73,   23,   36,   37,
  124,
};
final static short yysindex[] = {                       557,
  159,   13,  -30,    0,    0,   35,  -66,  219,  473,    0,
  574,    0,    0,  -98,    0, -210,   78,    0,    0,    0,
    0,    0, -193,  451,  -39,    0, -206,   79,  -75,   81,
    0,  584,   55,  526, -193,    7,    0,  443,  -17,  131,
  614,    0,    0,   64,  -24,  515, -141,  169,   30,   68,
   57,   54,  375,  159,    0,    0,   24,    0,   66,    1,
    0, -139,  144,  521,    0,    0,    0,  -11,  -11,    0,
    0,    0,  219, -175,    0,   97,    0,  114,  101,    0,
  172,  624, -115,    0,  219,  241,  328,  334,  338,  344,
 -111,  350,  109, -117,  110,  127,   -8,  111,  624,  624,
  634,   84,   85,    0,   93,    0,  -56,    0,   53,    0,
    0,   66,    0,  -12,    0,   14,    0,    0,   90,  143,
  -35,   -9,   -1,    0,  112,    0,    0,  125,  624,  132,
   90,   30,    7,   30,    7,   30,   90,    0,    0,    0,
    0,  624,  135,  100,  104,  644,  136,  362,  654,  -51,
    0,  142,    0,  -50,    0,  148,  150,  121,  -48,    0,
    0,    0,    0,    0,  118,  120,    0,    0,    0,  161,
    0,  177,    0,  179,    0,  192,    0,    0,  -25,    0,
    2,    0,    0,    0,  134,   17,    0,    0,   18,  147,
 -140,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  181,  206,  224,
  225,  228,  233,  239,  -46,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
  243,    0,    0,    0,    0,    0,  -32,    0,    0,    0,
  299,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  472,    0,    0,    0,    0,    0,
    0,    0,  594,    0,  505,    3,    0,    0,  -41,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   34,    0,    0,    0,    0,    0,    0,   23,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  604,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  397,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   45,    0,    0,    0,    0,    0,    0,  184,  -19,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  194,   25,   47,   69,   91,  207,  217,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  478,
    0,  499,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  663,  295,  642,   42,    0,  102,  246,  -16,    0,
    0,    0,    0,  115,   10,    6,  280,  596,   77,  -53,
    0,
};
final static int YYTABLESIZE=926;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        103,
  103,  103,  164,  103,  172,  103,   74,  193,  196,   45,
  201,   21,  224,   50,  117,  118,   98,  103,  103,  103,
  103,  102,  102,  102,   48,  102,   21,  102,   74,   68,
  174,  105,  154,   27,   69,  139,  141,   78,  176,  102,
  102,  102,  102,   82,   62,   82,  167,   82,   89,   78,
  153,   48,   40,   90,   59,   58,   86,   27,   87,  111,
   75,   82,   82,   82,   82,   83,   21,   83,  116,   83,
   58,   68,  169,   92,   40,  145,   69,   63,  119,   27,
  120,   21,  108,   83,   83,   83,   83,   80,   19,   80,
  131,   80,  166,  137,   58,   84,   28,   27,  121,  122,
  123,   27,   30,   19,   95,   80,   80,   80,   80,   84,
   86,   84,   87,   84,  100,  214,   41,   60,  215,   77,
   47,   62,  101,   27,   62,  112,  106,   84,   84,   84,
   84,   81,   86,   81,   87,   81,   61,  125,  148,   80,
  149,  150,  129,  130,   92,  104,  142,  143,   28,   81,
   81,   81,   81,   27,  126,   30,  109,   51,    2,  127,
   28,    3,  133,  135,    6,   27,   52,  152,  151,  155,
  177,   77,    8,   28,   57,   27,  160,  161,   27,  199,
   51,    2,  170,  178,    3,  162,   28,    6,   27,   52,
  180,   27,  183,  182,  187,    8,  184,   79,   28,  163,
  194,  204,  115,   27,  192,  195,  197,  200,  198,  223,
  202,   28,  203,   27,  103,  103,   27,  205,  103,  206,
  171,  103,  103,  103,   66,   43,  103,  103,  103,  103,
  103,   96,  207,  208,   67,   44,  102,  102,  103,  216,
  102,   97,    4,  102,  102,  102,  173,   68,  102,  102,
  102,  102,  102,   39,  175,   26,  110,   65,   82,   82,
  209,  104,   82,   27,  217,   82,   82,   82,   38,  168,
   82,   82,   82,   82,   82,  211,  212,   39,   21,   26,
   83,   83,  218,  219,   83,   27,  220,   83,   83,   83,
   46,  221,   83,   83,   83,   83,   83,  222,    1,   39,
   19,   26,   80,   80,  103,   56,   80,  113,  165,   80,
   80,   80,   64,   88,   80,   80,   80,   80,   80,   26,
    0,   39,    0,   26,   84,   84,    0,    0,   84,    0,
    0,   84,   84,   84,   64,    0,   84,   84,   84,   84,
   84,    0,    0,   39,   76,   26,   81,   81,    0,    0,
   81,    0,    0,   81,   81,   81,    0,    0,   81,   81,
   81,   81,   81,    0,   24,    2,  146,  147,    3,    0,
    0,    6,   27,   39,    0,   26,   24,    2,   27,    8,
    3,    0,   27,    6,    0,   39,   64,   26,   27,   24,
    2,    8,  210,    3,   27,   39,    6,   26,   39,  114,
   26,    0,   24,    2,    8,  213,    3,    0,   39,    6,
   26,   39,    0,   26,   24,    2,    0,    8,    3,    0,
  188,    6,    0,   25,  102,   26,    0,   24,    2,    8,
    0,    3,    0,  103,    6,   26,   39,    0,   26,   66,
   66,    0,    8,   66,    0,    0,   66,   66,   66,   67,
   67,    0,    0,   67,   66,   66,   67,   67,   67,    0,
    0,    0,   68,   68,   67,   67,   68,    0,    0,   68,
   68,   68,   65,   65,   49,    0,   65,   68,   68,   65,
   65,   65,    0,   39,   68,   26,    0,   65,   65,   69,
   28,    0,   68,    0,    0,   27,  132,   69,    0,    0,
    0,    0,   70,   72,   71,   39,    0,   26,    0,    0,
   70,   72,   71,  103,  103,   21,  103,    0,  103,  106,
  106,   25,  106,    0,  106,    0,    0,    0,    0,    0,
   21,  103,  103,  103,    0,    0,   25,  106,  106,  106,
  105,  105,   23,  105,    0,  105,   92,   92,    0,   92,
    0,   92,    0,    0,    0,    0,   68,   23,  105,  105,
  105,   69,   68,    0,   92,   92,   92,   69,   86,    0,
   87,    0,    0,    0,   70,   72,   71,    0,    0,    0,
   70,   72,   71,  134,    0,   70,   72,   71,    0,  136,
    0,    0,   39,  138,   26,    0,   35,   42,   39,  140,
   26,   42,   39,   42,   26,  144,    0,    0,   39,    0,
   26,    0,    0,    0,  103,    0,   26,   81,    2,   42,
    0,    3,    0,   42,    6,    0,   52,    0,    0,    0,
   51,    2,    8,    9,    3,   42,    0,    6,    0,   52,
    0,    0,   29,    0,    0,    8,   42,  107,    0,   35,
   53,    0,   37,   37,    0,    0,   37,    0,   42,   37,
    0,   37,    0,   42,   42,    0,    0,   37,   42,   37,
    0,    0,    0,   55,    0,    0,   35,    0,    0,    0,
   42,   42,   42,   42,   42,   42,    0,    0,   35,    0,
    0,    0,    0,    0,   83,   29,    0,    0,   81,    2,
   91,    0,    3,   94,    0,    6,   64,   52,    0,    0,
   65,   66,   67,    8,    9,   39,    0,   26,   65,   66,
   67,    0,   29,    0,    0,    0,    0,  103,   51,    2,
    0,    0,    3,  106,   29,    6,    0,   52,    0,  103,
  103,  103,  103,    8,  128,  106,  106,  106,  106,    0,
    0,    0,    0,   35,  105,    0,    0,    0,    0,    0,
   92,  156,  157,  159,    0,    0,  105,  105,  105,  105,
    0,    0,   92,   92,   92,    0,    0,    0,   99,    0,
   35,   85,   65,   66,   67,   35,    0,    0,   65,   66,
   67,  179,    0,   65,   66,   67,    0,    0,    0,   29,
    0,    0,    0,    0,  181,    0,    0,    0,  186,    0,
  189,  191,    1,    2,    0,    0,    3,    4,    5,    6,
    0,    7,    0,    0,    0,    0,   29,    8,    9,   54,
    2,   29,    0,    3,    4,    5,    6,    0,    7,   81,
    2,    0,    0,    3,    8,    9,    6,   82,   52,   63,
   63,    0,    0,   63,    8,    9,   63,   63,   63,   62,
   62,    0,    0,   62,   63,   63,   62,   62,   62,   93,
    2,    0,    0,    3,   62,   62,    6,    0,   52,   81,
    2,    0,    0,    3,    8,    9,    6,    0,   52,  158,
    2,    0,    0,    3,    8,    9,    6,    0,   52,  185,
    2,    0,    0,    3,    8,    9,    6,    0,   52,  190,
    2,    0,    0,    3,    8,    9,    6,    0,   52,    0,
    0,    0,    0,    0,    8,    9,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   59,   45,   40,   47,   46,   59,   59,   40,
   59,   44,   59,    8,   68,   69,   41,   59,   60,   61,
   62,   41,   42,   43,   91,   45,   59,   47,   46,   42,
   40,   48,   41,   45,   47,   89,   90,   28,   40,   59,
   60,   61,   62,   41,   44,   43,   59,   45,   42,   40,
   59,   91,   40,   47,  265,   14,   43,   45,   45,   59,
  267,   59,   60,   61,   62,   41,   44,   43,   63,   45,
   29,   42,   59,   91,   40,   92,   47,  271,   73,   45,
  256,   59,   59,   59,   60,   61,   62,   41,   44,   43,
   85,   45,  109,   88,   53,   41,   40,   45,  274,  275,
  276,   45,    1,   59,   41,   59,   60,   61,   62,   41,
   43,   43,   45,   45,  256,  256,    2,   16,  259,   41,
    6,   44,  264,   45,   44,  265,   59,   59,   60,   61,
   62,   41,   43,   43,   45,   45,   59,   41,  256,   59,
  258,  259,  258,  259,   91,   93,  258,  259,   40,   59,
   60,   61,   62,   45,   41,   54,   91,  256,  257,   59,
   40,  260,   86,   87,  263,   45,  265,   41,   59,   59,
   59,   41,  271,   40,  273,   45,   93,   93,   45,   59,
  256,  257,   40,   59,  260,   93,   40,  263,   45,  265,
   59,   45,   93,   59,   59,  271,   93,  273,   40,  256,
   59,   41,   59,   45,  256,  256,   59,  256,   59,  256,
   93,   40,   93,   45,  256,  257,   45,   41,  260,   41,
  256,  263,  264,  265,   41,  256,  268,  269,  270,  271,
  272,  256,   41,  259,   41,  266,  256,  257,  271,   59,
  260,  266,    0,  263,  264,  265,  256,   41,  268,  269,
  270,  271,  272,  265,  256,  267,  256,   41,  256,  257,
  259,   93,  260,   45,   59,  263,  264,  265,  256,  256,
  268,  269,  270,  271,  272,  259,  259,  265,  256,  267,
  256,  257,   59,   59,  260,   45,   59,  263,  264,  265,
  256,   59,  268,  269,  270,  271,  272,   59,    0,  265,
  256,  267,  256,  257,  271,   11,  260,   62,  256,  263,
  264,  265,  256,   34,  268,  269,  270,  271,  272,  267,
   -1,  265,   -1,  267,  256,  257,   -1,   -1,  260,   -1,
   -1,  263,  264,  265,  256,   -1,  268,  269,  270,  271,
  272,   -1,   -1,  265,  266,  267,  256,  257,   -1,   -1,
  260,   -1,   -1,  263,  264,  265,   -1,   -1,  268,  269,
  270,  271,  272,   -1,  256,  257,  258,  259,  260,   -1,
   -1,  263,   45,  265,   -1,  267,  256,  257,   45,  271,
  260,   -1,   45,  263,   -1,  265,  256,  267,   45,  256,
  257,  271,  259,  260,   45,  265,  263,  267,  265,  256,
  267,   -1,  256,  257,  271,  259,  260,   -1,  265,  263,
  267,  265,   -1,  267,  256,  257,   -1,  271,  260,   -1,
   59,  263,   -1,  265,  256,  267,   -1,  256,  257,  271,
   -1,  260,   -1,  265,  263,  267,  265,   -1,  267,  256,
  257,   -1,  271,  260,   -1,   -1,  263,  264,  265,  256,
  257,   -1,   -1,  260,  271,  272,  263,  264,  265,   -1,
   -1,   -1,  256,  257,  271,  272,  260,   -1,   -1,  263,
  264,  265,  256,  257,  256,   -1,  260,  271,  272,  263,
  264,  265,   -1,  265,   42,  267,   -1,  271,  272,   47,
   40,   -1,   42,   -1,   -1,   45,  256,   47,   -1,   -1,
   -1,   -1,   60,   61,   62,  265,   -1,  267,   -1,   -1,
   60,   61,   62,   42,   43,   44,   45,   -1,   47,   42,
   43,   44,   45,   -1,   47,   -1,   -1,   -1,   -1,   -1,
   59,   60,   61,   62,   -1,   -1,   59,   60,   61,   62,
   42,   43,   44,   45,   -1,   47,   42,   43,   -1,   45,
   -1,   47,   -1,   -1,   -1,   -1,   42,   59,   60,   61,
   62,   47,   42,   -1,   60,   61,   62,   47,   43,   -1,
   45,   -1,   -1,   -1,   60,   61,   62,   -1,   -1,   -1,
   60,   61,   62,  256,   -1,   60,   61,   62,   -1,  256,
   -1,   -1,  265,  256,  267,   -1,    1,    2,  265,  256,
  267,    6,  265,    8,  267,  256,   -1,   -1,  265,   -1,
  267,   -1,   -1,   -1,  265,   -1,  267,  256,  257,   24,
   -1,  260,   -1,   28,  263,   -1,  265,   -1,   -1,   -1,
  256,  257,  271,  272,  260,   40,   -1,  263,   -1,  265,
   -1,   -1,    1,   -1,   -1,  271,   51,  273,   -1,   54,
    9,   -1,  256,  257,   -1,   -1,  260,   -1,   63,  263,
   -1,  265,   -1,   68,   69,   -1,   -1,  271,   73,  273,
   -1,   -1,   -1,   11,   -1,   -1,   81,   -1,   -1,   -1,
   85,   86,   87,   88,   89,   90,   -1,   -1,   93,   -1,
   -1,   -1,   -1,   -1,   32,   54,   -1,   -1,  256,  257,
   38,   -1,  260,   41,   -1,  263,  256,  265,   -1,   -1,
  268,  269,  270,  271,  272,  265,   -1,  267,  268,  269,
  270,   -1,   81,   -1,   -1,   -1,   -1,  256,  256,  257,
   -1,   -1,  260,  256,   93,  263,   -1,  265,   -1,  268,
  269,  270,  271,  271,   82,  268,  269,  270,  271,   -1,
   -1,   -1,   -1,  158,  256,   -1,   -1,   -1,   -1,   -1,
  256,   99,  100,  101,   -1,   -1,  268,  269,  270,  271,
   -1,   -1,  268,  269,  270,   -1,   -1,   -1,  264,   -1,
  185,  256,  268,  269,  270,  190,   -1,   -1,  268,  269,
  270,  129,   -1,  268,  269,  270,   -1,   -1,   -1,  158,
   -1,   -1,   -1,   -1,  142,   -1,   -1,   -1,  146,   -1,
  148,  149,  256,  257,   -1,   -1,  260,  261,  262,  263,
   -1,  265,   -1,   -1,   -1,   -1,  185,  271,  272,  256,
  257,  190,   -1,  260,  261,  262,  263,   -1,  265,  256,
  257,   -1,   -1,  260,  271,  272,  263,  264,  265,  256,
  257,   -1,   -1,  260,  271,  272,  263,  264,  265,  256,
  257,   -1,   -1,  260,  271,  272,  263,  264,  265,  256,
  257,   -1,   -1,  260,  271,  272,  263,   -1,  265,  256,
  257,   -1,   -1,  260,  271,  272,  263,   -1,  265,  256,
  257,   -1,   -1,  260,  271,  272,  263,   -1,  265,  256,
  257,   -1,   -1,  260,  271,  272,  263,   -1,  265,  256,
  257,   -1,   -1,  260,  271,  272,  263,   -1,  265,   -1,
   -1,   -1,   -1,   -1,  271,  272,
};
}
final static short YYFINAL=10;
final static short YYMAXTOKEN=276;
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
"BEGIN","END","FIRST","LAST","LENGTH",
};
final static String yyrule[] = {
"$accept : programa",
"programa : lista_sentencias_declarativas",
"programa : bloque_sentencias_ejecutables",
"programa : lista_sentencias_declarativas bloque_sentencias_ejecutables",
"programa : error",
"lista_sentencias_declarativas : lista_sentencias_declarativas sentencia_declarativa",
"lista_sentencias_declarativas : sentencia_declarativa",
"bloque_sentencias_ejecutables : BEGIN lista_sentencias_ejecutables END ';'",
"bloque_sentencias_ejecutables : lista_sentencias_ejecutables END ';'",
"bloque_sentencias_ejecutables : error lista_sentencias_ejecutables END ';'",
"bloque_sentencias_ejecutables : BEGIN lista_sentencias_ejecutables END error",
"lista_sentencias_ejecutables : lista_sentencias_ejecutables sentencia_ejecutable",
"lista_sentencias_ejecutables : sentencia_ejecutable",
"sentencia_declarativa : tipo lista_variables ';'",
"sentencia_declarativa : lista_variables ';'",
"sentencia_declarativa : error lista_variables ';'",
"sentencia_declarativa : tipo lista_variables error",
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
"sentencia_impresion : error '(' CADENA ')' ';'",
"sentencia_impresion : PRINT CADENA ')' ';'",
"sentencia_impresion : PRINT '(' CADENA ';'",
"sentencia_impresion : PRINT '(' CADENA ')' error",
"sentencia_impresion : PRINT '(' ')' ';'",
"sentencia_impresion : PRINT error",
"sentencia_impresion : PRINT '(' error",
"sentencia_impresion : PRINT '(' error ')' ';'",
"sentencia_control : WHILE condicion DO bloque_sentencias_ejecutables ';'",
"sentencia_control : error condicion DO bloque_sentencias_ejecutables ';'",
"sentencia_control : WHILE condicion error bloque_sentencias_ejecutables ';'",
"sentencia_control : WHILE condicion DO bloque_sentencias_ejecutables error",
"sentencia_control : WHILE error DO bloque_sentencias_ejecutables ';'",
"sentencia_control : WHILE condicion DO error ';'",
"sentencia_seleccion : IF condicion bloque_sentencias_ejecutables END_IF ';'",
"sentencia_seleccion : IF condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF ';'",
"sentencia_seleccion : error condicion bloque_sentencias_ejecutables END_IF ';'",
"sentencia_seleccion : IF condicion bloque_sentencias_ejecutables error ';'",
"sentencia_seleccion : IF condicion bloque_sentencias_ejecutables END_IF error",
"sentencia_seleccion : IF condicion error END_IF ';'",
"sentencia_seleccion : IF error bloque_sentencias_ejecutables END_IF ';'",
"sentencia_seleccion : error condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF ';'",
"sentencia_seleccion : IF condicion bloque_sentencias_ejecutables error bloque_sentencias_ejecutables END_IF ';'",
"sentencia_seleccion : IF condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables error ';'",
"sentencia_seleccion : IF condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF error",
"sentencia_seleccion : IF condicion error ELSE bloque_sentencias_ejecutables END_IF ';'",
"sentencia_seleccion : IF condicion bloque_sentencias_ejecutables ELSE error END_IF ';'",
"sentencia_seleccion : IF condicion error ELSE error END_IF ';'",
"sentencia_seleccion : IF error bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF ';'",
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
"sentencia_asignacion : ASIGN expresion ';'",
"sentencia_asignacion : id ASIGN ';'",
"sentencia_asignacion : id ASIGN expresion error",
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

//#line 187 "gramatica.y"

//TODO Codigo JAVA

private Lexer al;

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
    System.out.println("Linea " + al.getLineNumber() + ": (Parser) " + s);
}
//#line 574 "Parser.java"
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
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "programa OK (delcarativas solas)"), Color.YELLOW);}
break;
case 2:
//#line 29 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "programa OK (bloque ejecutables)"), Color.YELLOW);}
break;
case 3:
//#line 30 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "programa OK (declarativas y ejecutables)"), Color.YELLOW);}
break;
case 4:
//#line 31 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "programa error"), Color.YELLOW);}
break;
case 8:
//#line 39 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: BEGIN"), Color.RED);}
break;
case 9:
//#line 40 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: BEGIN"), Color.RED);}
break;
case 10:
//#line 42 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 14:
//#line 52 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Type not declared"), Color.RED);}
break;
case 15:
//#line 53 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Unknown type declared."), Color.RED);}
break;
case 16:
//#line 54 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 24:
//#line 68 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing colection size"), Color.RED);}
break;
case 25:
//#line 69 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in colection size"), Color.RED);}
break;
case 30:
//#line 78 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio una impresion OK"), Color.YELLOW);}
break;
case 31:
//#line 79 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: PRINT"), Color.RED);}
break;
case 32:
//#line 80 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 33:
//#line 81 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 34:
//#line 82 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 35:
//#line 83 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing chain."), Color.RED);}
break;
case 36:
//#line 84 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in argument."), Color.RED);}
break;
case 37:
//#line 85 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in argument."), Color.RED);}
break;
case 38:
//#line 86 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Character chain not declared."), Color.RED);}
break;
case 39:
//#line 89 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio un WHILE OK"), Color.YELLOW);}
break;
case 40:
//#line 90 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: WHILE"), Color.RED);}
break;
case 41:
//#line 91 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: DO"), Color.RED);}
break;
case 42:
//#line 92 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 43:
//#line 93 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in condition"), Color.RED);}
break;
case 44:
//#line 94 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in while"), Color.RED);}
break;
case 45:
//#line 97 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio un IF OK"), Color.YELLOW);}
break;
case 46:
//#line 98 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio un IF OK"), Color.YELLOW);}
break;
case 47:
//#line 99 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: IF"), Color.RED);}
break;
case 48:
//#line 100 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: END_IF"), Color.RED);}
break;
case 49:
//#line 101 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 50:
//#line 102 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in bloque IF"), Color.RED);}
break;
case 51:
//#line 103 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in condition"), Color.RED);}
break;
case 52:
//#line 104 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: IF"), Color.RED);}
break;
case 53:
//#line 105 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: ELSE"), Color.RED);}
break;
case 54:
//#line 106 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: END_IF"), Color.RED);}
break;
case 55:
//#line 107 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 56:
//#line 108 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in bloque IF"), Color.RED);}
break;
case 57:
//#line 109 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in bloque ELSE"), Color.RED);}
break;
case 58:
//#line 110 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in bloque IF and ELSE"), Color.RED);}
break;
case 59:
//#line 111 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in condition"), Color.RED);}
break;
case 61:
//#line 115 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 62:
//#line 116 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 63:
//#line 117 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing parenthesis in condition"), Color.RED);}
break;
case 64:
//#line 118 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in condition."), Color.RED);}
break;
case 66:
//#line 122 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term in operation."), Color.RED);}
break;
case 67:
//#line 123 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing comparator in operation."), Color.RED);}
break;
case 68:
//#line 124 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term in operation."), Color.RED);}
break;
case 75:
//#line 135 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio una asignacion    OK"), Color.YELLOW);}
break;
case 76:
//#line 136 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing ID on assign."), Color.RED);}
break;
case 77:
//#line 137 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing right-hand term on assign."), Color.RED);}
break;
case 78:
//#line 139 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 79:
//#line 140 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR right-hand term on assign."), Color.RED);}
break;
case 83:
//#line 147 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '+' operation."), Color.RED);}
break;
case 84:
//#line 148 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '-' operation."), Color.RED);}
break;
case 88:
//#line 154 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '*' operation."), Color.RED);}
break;
case 89:
//#line 155 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '*' operation."), Color.RED);}
break;
case 90:
//#line 156 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '/' operation."), Color.RED);}
break;
case 91:
//#line 157 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '/' operation."), Color.RED);}
break;
case 96:
//#line 166 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing characters: ( or ))"), Color.RED);}
break;
case 98:
//#line 168 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing characters: ( or ))"), Color.RED);}
break;
case 100:
//#line 170 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing characters: ( or ))"), Color.RED);}
break;
case 101:
//#line 171 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Unknown function."), Color.RED);}
break;
case 102:
//#line 172 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Unknown function."), Color.RED);}
break;
case 106:
//#line 178 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing index on array"), Color.RED);}
break;
//#line 987 "Parser.java"
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
public Parser(Lexer al)
{this.al=al;
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
