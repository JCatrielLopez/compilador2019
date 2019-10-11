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
    2,    2,    4,    4,    3,    3,    6,    6,    7,    7,
    7,    7,    8,    5,    5,    5,    5,   13,   12,   11,
   11,   14,   14,   14,   14,   14,   15,   15,   15,   15,
   17,   17,   17,   17,   17,   17,   10,   10,   10,   10,
   10,   10,   16,   16,   16,   16,   16,   16,   16,   19,
   19,   19,   19,   19,   19,   19,   20,   20,   20,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   18,
   18,   18,   18,    9,    9,
};
final static short yylen[] = {                            2,
    1,    1,    2,    1,    2,    1,    4,    3,    4,    3,
    4,    1,    2,    1,    3,    2,    1,    1,    3,    3,
    1,    1,    4,    1,    1,    1,    1,    5,    5,    5,
    7,    3,    2,    2,    1,    1,    3,    3,    3,    3,
    1,    1,    1,    1,    1,    1,    4,    3,    3,    3,
    4,    4,    3,    3,    3,    3,    3,    3,    1,    3,
    3,    3,    3,    3,    3,    1,    1,    1,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    1,
    4,    4,    4,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   17,   18,    0,    0,    0,    0,    0,
    0,    2,    6,    0,   14,    0,    0,   22,   24,   25,
   26,   27,    0,    0,    0,    0,    0,   84,    0,    0,
   68,    0,    0,    0,   67,    0,   66,    0,    0,    0,
    0,    0,    0,    0,    3,    5,    0,   13,    0,    0,
   16,    0,    0,    0,    0,    0,   45,   44,   46,    0,
    0,    0,    0,   41,   42,   43,    0,    0,   85,    0,
    0,    0,   33,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   48,   10,    0,    8,    0,   15,
    0,   20,    0,   50,    0,   49,    0,    9,    0,    0,
    0,   62,   64,    0,    0,    0,    0,    0,   69,   32,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   63,
   60,   65,   61,    0,    0,   83,   81,    0,   11,    7,
    0,   51,   52,   47,   82,    0,    0,    0,    0,    0,
    0,    0,    0,   30,   28,   29,   23,   79,   71,   72,
   70,   74,   75,   73,   77,   78,   76,    0,   31,
};
final static short yydgoto[] = {                         10,
   11,   12,   13,   14,   15,   16,   17,   18,   31,   19,
   20,   21,   22,   32,   33,   34,   67,   35,   36,   37,
  109,
};
final static short yysindex[] = {                       -93,
  287,   59,   12,    0,    0,   59,  -84,  126,  287,    0,
  -76,    0,    0,  269,    0, -224,   17,    0,    0,    0,
    0,    0,   81,  -20,  278,  393,  -37,    0, -205,  164,
    0,  265,   32,  385,    0,   33,    0, -187, -181,  183,
  165,  -32, -114,  287,    0,    0,   34,    0,    6,   19,
    0, -180,  -45,  -10,  183,   44,    0,    0,    0,  196,
  196,   -9,   -9,    0,    0,    0,  126, -156,    0,  393,
   64, -241,    0,  126,  200,  204,  212,  218,  219,   74,
  265,   31,   42,   47,    0,    0,  -54,    0,  -33,    0,
    6,    0,   80,    0,   10,    0,   48,    0,  119,   33,
   33,    0,    0,   -5,  107,  -21,   -6,   11,    0,    0,
  265,   91,   -5,  119,   33,  119,   33,  165,   -5,    0,
    0,    0,    0,  106,  123,    0,    0,    0,    0,    0,
   69,    0,    0,    0,    0,  147,  151,  -12,  163,   15,
  172,   16,  -40,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  155,    0,
};
final static short yyrindex[] = {                         0,
  223,    0,    0,    0,    0,    0,  -29,    0,    0,    0,
  237,    0,    0,    1,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  103,    0,  -66,  -39,    0,    0,    0,
    0,    0,  236,    0,    0,  -17,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   58,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  246,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   62,    0,    0,    0,    0,    0,    0,    0,    0,    5,
   27,    0,    0,  132,    0,    0,    0,    0,    0,    0,
    0,    0,  142,   49,   71,   93,  115,  152,  162,    0,
    0,    0,    0,    0,    0,    0,    0,   37,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   26,  267,   30,   70,    0,  263,  228,  -30,    0,
    0,    0,    0,  275,  252,   24,  251,  480,  140,   66,
    0,
};
final static int YYTABLESIZE=663;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         29,
   12,   80,   80,   80,  130,   80,   40,   80,   68,   84,
   75,   29,   76,   94,   21,   80,  111,  112,  138,   80,
   80,   80,   80,   59,   97,   59,   85,   59,  151,   21,
   25,   42,   75,  140,   76,   29,   45,   75,   43,   76,
   49,   59,   59,   59,   59,   55,   54,   55,   96,   55,
  142,   38,   75,   55,   76,  154,  157,   72,  131,   12,
   52,   69,   52,   55,   55,   55,   55,   57,  134,   57,
   55,   57,   73,   25,   78,   51,   95,   90,   80,   79,
   23,   82,   81,   48,   91,   57,   57,   57,   57,   56,
  104,   56,   88,   56,   48,   23,   89,  113,   30,  105,
  119,   21,   98,   29,  110,   19,  125,   56,   56,   56,
   56,   53,   48,   53,  124,   53,   21,  106,  107,  108,
   19,   62,   60,  126,   61,   29,   63,  102,  103,   53,
   53,   53,   53,   58,  127,   58,  143,   58,  132,  128,
  135,   86,    2,  121,  123,    3,  136,   80,    6,  144,
   24,   58,   58,   58,   58,   54,    8,   54,   87,   54,
   62,  147,    1,    2,  145,   63,    3,    4,    5,    6,
   29,    7,   38,   54,   54,   54,   54,    8,    9,   44,
    2,  146,   39,    3,    4,    5,    6,  148,    7,   36,
   36,  149,   40,   36,    8,    9,   36,   36,   36,  100,
  101,  129,   37,  152,   36,   36,   62,   60,   29,   61,
   93,   63,  155,  159,  115,  117,   80,   80,  158,   27,
   80,   28,    4,   80,   80,   80,   80,   29,   80,   80,
   80,   80,   80,   28,  137,   80,    1,   80,   59,   59,
   29,   80,   59,  150,   29,   59,   59,   59,   29,  139,
   59,   59,   59,   59,   59,   27,   29,   28,   12,   12,
   55,   55,   29,   29,   55,  133,  141,   55,   55,   55,
  153,  156,   55,   55,   55,   55,   55,   46,   50,   92,
   39,   71,   57,   57,   77,    0,   57,    0,    0,   57,
   57,   57,   82,    0,   57,   57,   57,   57,   57,    0,
    0,   82,    0,   82,   56,   56,    0,   82,   56,    0,
    0,   56,   56,   56,   26,    0,   56,   56,   56,   56,
   56,    0,    0,   27,    0,   28,   53,   53,    0,    0,
   53,    0,    0,   53,   53,   53,   41,    0,   53,   53,
   53,   53,   53,    0,    0,   27,    0,   28,   58,   58,
    0,   53,   58,    0,    0,   58,   58,   58,   80,    0,
   58,   58,   58,   58,   58,    0,    0,   80,    0,   80,
   54,   54,    0,   80,   54,    0,    0,   54,   54,   54,
    0,   41,   54,   54,   54,   54,   54,   38,   38,    0,
   27,   38,   28,    0,   38,   38,   38,   39,   39,    0,
    0,   39,   38,   38,   39,   39,   39,   40,   40,    0,
    0,   40,   39,   39,   40,   40,   40,   37,   37,   70,
    0,   37,   40,   40,   37,   37,   37,   75,   27,   76,
   28,    0,   37,   37,   62,   60,    0,   61,   82,   63,
    0,    0,    0,    0,   64,   66,   65,   83,    0,   28,
    0,   99,   64,   66,   65,  114,    0,    0,    0,  116,
   27,    0,   28,    0,   27,    0,   28,  118,   27,    0,
   28,    0,    0,  120,  122,    0,   27,    0,   28,   23,
   23,    0,   27,   27,   28,   28,    0,    0,   23,    0,
   23,   35,   35,   23,    0,   35,    0,    0,   35,   35,
   35,   34,   34,    0,   23,   34,   35,   35,   34,   34,
   34,   23,    0,    0,    0,    0,   34,   34,    0,    0,
   44,    2,   23,   23,    3,    2,    0,    6,    3,   24,
    0,    6,    0,   24,    2,    8,    9,    3,    0,    8,
    6,   47,   24,    2,    0,    0,    3,    0,    8,    6,
   56,   24,    0,    0,    0,    0,    0,    8,    0,    0,
   23,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   23,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   74,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   57,   58,   59,    0,    0,    0,    0,    0,
   57,   58,   59,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         45,
    0,   41,   42,   43,   59,   45,   91,   47,   46,   40,
   43,   45,   45,   59,   44,   45,  258,  259,   40,   59,
   60,   61,   62,   41,   55,   43,   59,   45,   41,   59,
    1,    8,   43,   40,   45,   45,   11,   43,    9,   45,
  265,   59,   60,   61,   62,   41,   23,   43,   59,   45,
   40,   40,   43,   91,   45,   41,   41,   32,   89,   59,
   44,  267,   44,   59,   60,   61,   62,   41,   59,   43,
   91,   45,   41,   44,   42,   59,   53,   59,  266,   47,
   44,   45,  264,   14,  265,   59,   60,   61,   62,   41,
   67,   43,   59,   45,   25,   59,   91,   74,   40,  256,
   77,   44,   59,   45,   41,   44,   81,   59,   60,   61,
   62,   41,   43,   43,   41,   45,   59,  274,  275,  276,
   59,   42,   43,   93,   45,   45,   47,   62,   63,   59,
   60,   61,   62,   41,   93,   43,  111,   45,   59,   93,
   93,  256,  257,   78,   79,  260,   40,   45,  263,   59,
  265,   59,   60,   61,   62,   41,  271,   43,  273,   45,
   42,   93,  256,  257,   59,   47,  260,  261,  262,  263,
   45,  265,   41,   59,   60,   61,   62,  271,  272,  256,
  257,   59,   41,  260,  261,  262,  263,   41,  265,  256,
  257,   41,   41,  260,  271,  272,  263,  264,  265,   60,
   61,  256,   41,   41,  271,  272,   42,   43,   45,   45,
  256,   47,   41,   59,   75,   76,  256,  257,  259,  265,
  260,  267,    0,  263,  264,  265,  256,   45,  268,  269,
  270,  271,  272,  267,  256,  265,    0,  267,  256,  257,
   45,  271,  260,  256,   45,  263,  264,  265,   45,  256,
  268,  269,  270,  271,  272,  265,   45,  267,  258,  259,
  256,  257,   45,   45,  260,  256,  256,  263,  264,  265,
  256,  256,  268,  269,  270,  271,  272,   11,   16,   52,
    6,   30,  256,  257,   34,   -1,  260,   -1,   -1,  263,
  264,  265,  256,   -1,  268,  269,  270,  271,  272,   -1,
   -1,  265,   -1,  267,  256,  257,   -1,  271,  260,   -1,
   -1,  263,  264,  265,  256,   -1,  268,  269,  270,  271,
  272,   -1,   -1,  265,   -1,  267,  256,  257,   -1,   -1,
  260,   -1,   -1,  263,  264,  265,  256,   -1,  268,  269,
  270,  271,  272,   -1,   -1,  265,   -1,  267,  256,  257,
   -1,  271,  260,   -1,   -1,  263,  264,  265,  256,   -1,
  268,  269,  270,  271,  272,   -1,   -1,  265,   -1,  267,
  256,  257,   -1,  271,  260,   -1,   -1,  263,  264,  265,
   -1,  256,  268,  269,  270,  271,  272,  256,  257,   -1,
  265,  260,  267,   -1,  263,  264,  265,  256,  257,   -1,
   -1,  260,  271,  272,  263,  264,  265,  256,  257,   -1,
   -1,  260,  271,  272,  263,  264,  265,  256,  257,  256,
   -1,  260,  271,  272,  263,  264,  265,   43,  265,   45,
  267,   -1,  271,  272,   42,   43,   -1,   45,  256,   47,
   -1,   -1,   -1,   -1,   60,   61,   62,  265,   -1,  267,
   -1,  256,   60,   61,   62,  256,   -1,   -1,   -1,  256,
  265,   -1,  267,   -1,  265,   -1,  267,  256,  265,   -1,
  267,   -1,   -1,  256,  256,   -1,  265,   -1,  267,    0,
    1,   -1,  265,  265,  267,  267,   -1,   -1,    9,   -1,
   11,  256,  257,   14,   -1,  260,   -1,   -1,  263,  264,
  265,  256,  257,   -1,   25,  260,  271,  272,  263,  264,
  265,   32,   -1,   -1,   -1,   -1,  271,  272,   -1,   -1,
  256,  257,   43,   44,  260,  257,   -1,  263,  260,  265,
   -1,  263,   -1,  265,  257,  271,  272,  260,   -1,  271,
  263,  273,  265,  257,   -1,   -1,  260,   -1,  271,  263,
  273,  265,   -1,   -1,   -1,   -1,   -1,  271,   -1,   -1,
   81,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  111,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  256,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  268,  269,  270,   -1,   -1,   -1,   -1,   -1,
  268,  269,  270,
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
"bloque_sentencias_ejecutables : BEGIN lista_sentencias_ejecutables error",
"bloque_sentencias_ejecutables : BEGIN lista_sentencias_ejecutables END error",
"bloque_sentencias_ejecutables : lista_sentencias_ejecutables",
"lista_sentencias_ejecutables : lista_sentencias_ejecutables sentencia_ejecutable",
"lista_sentencias_ejecutables : sentencia_ejecutable",
"sentencia_declarativa : tipo lista_variables ';'",
"sentencia_declarativa : lista_variables ';'",
"tipo : INT",
"tipo : ULONG",
"lista_variables : lista_variables ',' ID",
"lista_variables : lista_variables ',' coleccion",
"lista_variables : ID",
"lista_variables : coleccion",
"coleccion : ID '[' cte ']'",
"sentencia_ejecutable : sentencia_asignacion",
"sentencia_ejecutable : sentencia_seleccion",
"sentencia_ejecutable : sentencia_control",
"sentencia_ejecutable : sentencia_impresion",
"sentencia_impresion : PRINT '(' CADENA ')' ';'",
"sentencia_control : WHILE condicion DO bloque_sentencias_ejecutables ';'",
"sentencia_seleccion : IF condicion bloque_sentencias_ejecutables END_IF ';'",
"sentencia_seleccion : IF condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF ';'",
"condicion : '(' comparacion ')'",
"condicion : comparacion ')'",
"condicion : '(' comparacion",
"condicion : comparacion",
"condicion : error",
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
"sentencia_asignacion : id expresion ';'",
"sentencia_asignacion : id ASIGN ';'",
"sentencia_asignacion : id ASIGN error ';'",
"sentencia_asignacion : id ASIGN expresion error",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : error '+' termino",
"expresion : expresion '+' error",
"expresion : error '-' termino",
"expresion : expresion '-' error",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : error '*' factor",
"termino : termino '*' error",
"termino : error '/' factor",
"termino : termino '/' error",
"termino : factor",
"factor : id",
"factor : cte",
"factor : ID '.' funcion",
"funcion : FIRST '(' ')'",
"funcion : FIRST error ')'",
"funcion : FIRST '(' error",
"funcion : LAST '(' ')'",
"funcion : LAST error ')'",
"funcion : LAST '(' error",
"funcion : LENGTH '(' ')'",
"funcion : LENGTH error ')'",
"funcion : LENGTH '(' error",
"funcion : error '(' ')'",
"id : ID",
"id : ID '[' ID ']'",
"id : ID '[' cte ']'",
"id : ID '[' error ']'",
"cte : CTE",
"cte : '-' CTE",
};

//#line 181 "gramatica.y"

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
//#line 474 "Parser.java"
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
//#line 41 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: END"), Color.RED);}
break;
case 11:
//#line 42 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 12:
//#line 43 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Falta delimitadores del bloque (begin/end)"), Color.RED);}
break;
case 16:
//#line 52 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Type not declared"), Color.RED);}
break;
case 28:
//#line 79 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio una impresion OK"), Color.YELLOW);}
break;
case 30:
//#line 93 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio un IF OK"), Color.YELLOW);}
break;
case 33:
//#line 105 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 34:
//#line 106 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 35:
//#line 107 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing parenthesis in condition"), Color.RED);}
break;
case 36:
//#line 108 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Condition not declared."), Color.RED);}
break;
case 38:
//#line 112 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term in operation."), Color.RED);}
break;
case 39:
//#line 113 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing comparator in operation."), Color.RED);}
break;
case 40:
//#line 114 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term in operation."), Color.RED);}
break;
case 47:
//#line 125 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio una asignacion    OK"), Color.YELLOW);}
break;
case 48:
//#line 126 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing ID on assign."), Color.RED);}
break;
case 49:
//#line 127 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing := on assign."), Color.RED);}
break;
case 50:
//#line 128 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing right-hand term on assign."), Color.RED);}
break;
case 51:
//#line 129 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR right-hand term on assign."), Color.RED);}
break;
case 52:
//#line 130 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 55:
//#line 135 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '+' operation."), Color.RED);}
break;
case 56:
//#line 136 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '+' operation."), Color.RED);}
break;
case 57:
//#line 137 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '-' operation."), Color.RED);}
break;
case 58:
//#line 138 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '-' operation."), Color.RED);}
break;
case 62:
//#line 145 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '*' operation."), Color.RED);}
break;
case 63:
//#line 146 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '*' operation."), Color.RED);}
break;
case 64:
//#line 147 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '/' operation."), Color.RED);}
break;
case 65:
//#line 148 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '/' operation."), Color.RED);}
break;
case 71:
//#line 158 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 72:
//#line 159 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 74:
//#line 161 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 75:
//#line 162 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 77:
//#line 164 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 78:
//#line 165 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 79:
//#line 166 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Unknown function."), Color.RED);}
break;
case 83:
//#line 172 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing index on array"), Color.RED);}
break;
//#line 787 "Parser.java"
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
