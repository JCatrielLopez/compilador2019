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
    0,    0,    0,    1,    1,    2,    2,    2,    4,    4,
    3,    3,    3,    6,    6,    7,    7,    7,    7,    8,
    8,    8,    5,    5,    5,    5,    5,   13,   13,   13,
   13,   13,   13,   12,   12,   12,   12,   11,   11,   11,
   11,   11,   11,   11,   11,   11,   14,   14,   14,   14,
   14,   15,   15,   15,   15,   17,   17,   17,   17,   17,
   17,   10,   10,   10,   10,   16,   16,   16,   16,   16,
   16,   19,   19,   19,   19,   19,   19,   20,   20,   20,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   21,
   18,   18,   18,   18,   18,   18,    9,    9,
};
final static short yylen[] = {                            2,
    1,    1,    2,    2,    1,    4,    4,    4,    2,    1,
    3,    2,    3,    1,    1,    3,    3,    1,    1,    4,
    3,    3,    1,    1,    1,    1,    1,    5,    5,    4,
    4,    5,    5,    5,    5,    5,    5,    5,    7,    5,
    5,    5,    7,    7,    7,    7,    3,    2,    2,    1,
    1,    3,    3,    3,    3,    1,    1,    1,    1,    1,
    1,    4,    4,    4,    4,    3,    3,    3,    3,    3,
    1,    3,    3,    3,    3,    3,    1,    1,    1,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    1,    4,    3,    4,    4,    4,    1,    2,
};
final static short yydefred[] = {                         0,
   14,   15,    0,    0,    0,    0,    2,    5,    0,    0,
   19,   97,    0,    0,    0,    0,    0,    0,    0,    0,
   26,    0,   10,   23,   24,   25,   27,    0,    3,    4,
    0,   12,    0,   98,    0,   22,    0,    0,    0,    0,
   79,    0,    0,    0,   78,    0,   77,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    9,    0,   13,   11,
    0,   17,   20,   60,   59,   61,    0,    0,    0,    0,
   56,   57,   58,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   48,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   93,    0,    0,    0,
    7,    8,    6,    0,    0,    0,    0,    0,   73,   75,
    0,    0,    0,    0,    0,    0,   80,   63,    0,   47,
    0,    0,    0,    0,    0,    0,   70,    0,    0,   74,
   72,   76,    0,    0,    0,   30,    0,   31,    0,    0,
    0,   96,   94,   92,    0,   64,   65,   62,   95,    0,
    0,    0,    0,    0,    0,    0,   29,   35,    0,   40,
   41,    0,    0,   42,   38,   33,   32,   28,   36,   37,
   34,   90,   82,   83,   81,   85,   86,   84,   88,   89,
   87,    0,    0,    0,    0,   43,   44,   45,   46,   39,
};
final static short yydgoto[] = {                          5,
    6,    7,   21,   22,   23,    9,   10,   11,   41,   24,
   25,   26,   27,   42,   43,   44,   74,   45,   46,   47,
  117,
};
final static short yysindex[] = {                       -69,
    0,    0,    8,  -55,    0,  -69,    0,    0, -224,   37,
    0,    0, -165,  -13,   12,   42,   70,    2,   70,   58,
    0,  -79,    0,    0,    0,    0,    0, -160,    0,    0,
   15,    0, -144,    0,   38,    0,   46,   18,   71,   45,
    0, -172,   93,   52,    0,   26,    0,   74, -134,  109,
 -195,  -94,   60,   75,   35,  -47,    0,   78,    0,    0,
    8,    0,    0,    0,    0,    0,   88,   88,   85,   85,
    0,    0,    0,   71,   75, -129,  324,   83,   46,  124,
  127, -134,   59,    0,   71,   91,  -71,   92,   95,  -65,
  -42,  139,  171,   11, -134, -134,    0,  125,  -56,  150,
    0,    0,    0,  116,   98,  157,   26,   26,    0,    0,
  227,  153,  222,  -37,  -35,  -32,    0,    0,  207,    0,
  218, -134,  220,  227,  157,   26,    0,  324,  227,    0,
    0,    0,  -46, -134,  -45,    0,  223,    0,  -43,  225,
  -36,    0,    0,    0,    0,    0,    0,    0,    0,  249,
  254,   -7,  256,   -6,  258,    5,    0,    0,   44,    0,
    0,   65, -158,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  245,  246,  260,   13,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,   73,    0,    0,  328,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -29,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  107,    0, -108,  -41,    0,    0,
    0,    0,  -84,    0,    0,  -34,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  108,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -77,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   80,
    0,    0,    0,    0,    0,    0,  -12,   -5,    0,    0,
  -31,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -24,   17,   24,    0,  -19,  -17,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    7,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  326,   39,    0,  311,    0,  337,  316,  115,    0,
    0,    0,    0,  257,  149,   86,  317,  100,   87,  -26,
    0,
};
final static int YYTABLESIZE=460;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         91,
   91,   91,  152,   91,  154,   91,   71,  156,   71,   53,
   71,  103,  161,  165,   18,  168,   54,   91,   91,   91,
   91,   55,  171,   52,   71,   71,   71,   71,   67,   18,
   67,   13,   67,  175,  178,   69,  144,   69,    8,   69,
    3,   51,  109,  110,   30,  181,   67,   67,   67,   67,
   20,  139,   13,   69,   69,   69,   69,   68,   33,   68,
   93,   68,  131,   76,   66,   20,   66,   89,   66,  138,
   94,  190,   90,   60,   40,   68,   68,   68,   68,   13,
   33,   40,   66,   66,   66,   66,   13,   69,   67,   13,
   68,   82,   70,  101,   86,   32,   87,  184,   14,    4,
  185,   34,   13,   28,   36,   71,   73,   72,   75,   48,
   58,   71,   73,   72,   13,   13,   18,   15,   13,   13,
   61,   28,   13,   21,   78,   86,  113,   87,   35,   13,
   63,   18,   13,   84,   15,   13,   13,    4,   21,   13,
   86,  118,   87,  105,  114,  115,  116,   51,   54,   92,
   21,   16,   97,  107,  108,   51,  148,   69,   67,  111,
   68,   95,   70,   51,  119,   21,   16,  120,  100,   96,
  124,   50,  126,  129,  146,   15,   55,   17,   49,   50,
   18,    1,    2,   19,  127,   20,   49,   50,   81,  112,
  132,    1,    2,   56,   49,    3,   81,  136,   69,  143,
   16,   17,    4,   70,   18,    1,    2,   19,  102,   20,
  164,  137,  167,  133,   91,  134,  135,  142,  151,  170,
  153,   71,   91,  155,   53,    4,   91,   91,   91,   71,
   91,   54,   53,   71,   71,   71,   55,   71,   52,   54,
   53,   91,  145,   67,   55,  149,   52,   54,  174,  177,
   69,   67,   55,   12,   52,   67,   67,   67,   69,   67,
  180,  150,   69,   69,   69,  157,   69,   50,  189,   86,
   59,   87,   68,   49,   12,   52,  158,   95,  160,   66,
   68,  166,   53,  169,   68,   68,   68,   66,   68,  172,
   37,   66,   66,   66,  173,   66,  176,   37,  179,   38,
   79,   12,  182,  186,  187,   39,   38,   85,   12,   38,
   80,   12,   39,   64,   65,   66,  122,  123,  188,   64,
   65,   66,   53,  183,   12,   37,   77,    1,   18,   79,
   98,   29,   57,  104,   38,   38,   12,   12,   38,   99,
   12,   12,   38,  106,   12,   31,  125,  128,   62,   38,
  130,   12,   38,  147,   12,   38,   38,   12,   12,   38,
   88,   12,   21,   16,    0,   69,   67,   83,   68,    0,
   70,    0,    0,    0,   91,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  121,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  140,  141,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  159,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  162,  163,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   40,   45,   40,   47,   41,   40,   43,   41,
   45,   59,   59,   59,   44,   59,   41,   59,   60,   61,
   62,   41,   59,   41,   59,   60,   61,   62,   41,   59,
   43,   45,   45,   41,   41,   41,   93,   43,    0,   45,
  265,   40,   69,   70,    6,   41,   59,   60,   61,   62,
   44,   41,   45,   59,   60,   61,   62,   41,   44,   43,
  256,   45,   89,   46,   41,   59,   43,   42,   45,   59,
  266,   59,   47,   59,   40,   59,   60,   61,   62,   45,
   44,   40,   59,   60,   61,   62,   45,   42,   43,   45,
   45,  264,   47,   59,   43,   59,   45,  256,   91,  272,
  259,  267,   45,    4,   93,   60,   61,   62,   91,   40,
  271,   60,   61,   62,   45,   45,   44,    3,   45,   45,
  265,   22,   45,   44,   39,   43,  256,   45,   14,   45,
   93,   59,   45,   41,   20,   45,   45,  272,   59,   45,
   43,   59,   45,   58,  274,  275,  276,  256,   91,   41,
   44,   44,   93,   67,   68,  264,   59,   42,   43,   74,
   45,  256,   47,  272,   41,   59,   59,   41,   54,  264,
   85,  256,   86,   88,   59,   61,  256,  257,  256,  264,
  260,  261,  262,  263,  256,  265,  264,  272,   40,   75,
  256,  261,  262,  273,  272,  265,   48,   59,   42,  256,
  256,  257,  272,   47,  260,  261,  262,  263,  256,  265,
  256,   41,  256,  256,  256,  258,  259,   93,  256,  256,
  256,  256,  264,  256,  256,  272,  268,  269,  270,  264,
  272,  256,  264,  268,  269,  270,  256,  272,  256,  264,
  272,  271,   93,  256,  264,   93,  264,  272,  256,  256,
  256,  264,  272,  267,  272,  268,  269,  270,  264,  272,
  256,   40,  268,  269,  270,   59,  272,  266,  256,   43,
  256,   45,  256,   17,  267,   19,   59,  271,   59,  256,
  264,   59,  265,   59,  268,  269,  270,  264,  272,   41,
  256,  268,  269,  270,   41,  272,   41,  256,   41,  265,
  256,  267,  259,   59,   59,  271,  265,  256,  267,  265,
  266,  267,  271,  268,  269,  270,  258,  259,   59,  268,
  269,  270,  265,  259,  267,  256,  256,    0,  256,  256,
  256,    6,   22,  256,  265,  265,  267,  267,  265,  265,
  267,  267,  265,  256,  267,    9,  256,  256,   33,  265,
  256,  267,  265,  256,  267,  265,  265,  267,  267,  265,
   44,  267,  256,  256,   -1,   42,   43,   42,   45,   -1,
   47,   -1,   -1,   -1,   49,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   82,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   95,   96,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  122,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  133,  134,
};
}
final static short YYFINAL=5;
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
"lista_sentencias_declarativas : lista_sentencias_declarativas sentencia_declarativa",
"lista_sentencias_declarativas : sentencia_declarativa",
"bloque_sentencias_ejecutables : BEGIN lista_sentencias_ejecutables END ';'",
"bloque_sentencias_ejecutables : BEGIN lista_sentencias_ejecutables error ';'",
"bloque_sentencias_ejecutables : BEGIN lista_sentencias_ejecutables END error",
"lista_sentencias_ejecutables : lista_sentencias_ejecutables sentencia_ejecutable",
"lista_sentencias_ejecutables : sentencia_ejecutable",
"sentencia_declarativa : tipo lista_variables ';'",
"sentencia_declarativa : lista_variables ';'",
"sentencia_declarativa : tipo lista_variables error",
"tipo : INT",
"tipo : ULONG",
"lista_variables : lista_variables ',' ID",
"lista_variables : lista_variables ',' coleccion",
"lista_variables : ID",
"lista_variables : coleccion",
"coleccion : ID '[' cte ']'",
"coleccion : ID '[' cte",
"coleccion : ID cte ']'",
"sentencia_ejecutable : sentencia_asignacion",
"sentencia_ejecutable : sentencia_seleccion",
"sentencia_ejecutable : sentencia_control",
"sentencia_ejecutable : sentencia_declarativa",
"sentencia_ejecutable : sentencia_impresion",
"sentencia_impresion : PRINT '(' CADENA ')' ';'",
"sentencia_impresion : error '(' CADENA ')' ';'",
"sentencia_impresion : PRINT CADENA ')' ';'",
"sentencia_impresion : PRINT '(' CADENA ';'",
"sentencia_impresion : PRINT '(' CADENA ')' error",
"sentencia_impresion : PRINT '(' error ')' ';'",
"sentencia_control : WHILE condicion DO bloque_sentencias_ejecutables ';'",
"sentencia_control : error condicion DO bloque_sentencias_ejecutables ';'",
"sentencia_control : WHILE condicion error bloque_sentencias_ejecutables ';'",
"sentencia_control : WHILE condicion DO bloque_sentencias_ejecutables error",
"sentencia_seleccion : IF condicion bloque_sentencias_ejecutables END_IF ';'",
"sentencia_seleccion : IF condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF ';'",
"sentencia_seleccion : error condicion bloque_sentencias_ejecutables END_IF ';'",
"sentencia_seleccion : IF condicion bloque_sentencias_ejecutables error ';'",
"sentencia_seleccion : IF condicion bloque_sentencias_ejecutables END_IF error",
"sentencia_seleccion : error condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF ';'",
"sentencia_seleccion : IF condicion bloque_sentencias_ejecutables error bloque_sentencias_ejecutables END_IF ';'",
"sentencia_seleccion : IF condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables error ';'",
"sentencia_seleccion : IF condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF error",
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
"sentencia_asignacion : error ASIGN expresion ';'",
"sentencia_asignacion : id ASIGN error ';'",
"sentencia_asignacion : id ASIGN expresion error",
"expresion : expresion '+' termino",
"expresion : error '+' termino",
"expresion : expresion '+' error",
"expresion : error '-' termino",
"expresion : expresion '-' error",
"expresion : termino",
"termino : termino '*' factor",
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
"id : ID ID ']'",
"id : ID '[' ID error",
"id : ID '[' cte ']'",
"id : ID '[' error ']'",
"cte : CTE",
"cte : '-' CTE",
};

//#line 174 "gramatica.y"

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
//#line 456 "Parser.java"
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
case 6:
//#line 37 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Sentencias ejecutables OK"), Color.YELLOW);}
break;
case 7:
//#line 38 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: END"), Color.RED);}
break;
case 8:
//#line 39 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 12:
//#line 47 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Type not declared"), Color.RED);}
break;
case 13:
//#line 48 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 21:
//#line 63 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ["), Color.RED);}
break;
case 22:
//#line 64 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ]"), Color.RED);}
break;
case 29:
//#line 75 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: PRINT"), Color.RED);}
break;
case 30:
//#line 76 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 31:
//#line 77 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 32:
//#line 78 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 33:
//#line 79 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Character chain not declared."), Color.RED);}
break;
case 35:
//#line 83 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: WHILE"), Color.RED);}
break;
case 36:
//#line 84 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: DO"), Color.RED);}
break;
case 37:
//#line 85 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 38:
//#line 88 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio un IF OK"), Color.YELLOW);}
break;
case 40:
//#line 90 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: IF"), Color.RED);}
break;
case 41:
//#line 91 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: END_IF"), Color.RED);}
break;
case 42:
//#line 92 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 43:
//#line 93 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: IF"), Color.RED);}
break;
case 44:
//#line 94 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: ELSE"), Color.RED);}
break;
case 45:
//#line 95 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: END_IF"), Color.RED);}
break;
case 46:
//#line 96 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 48:
//#line 100 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 49:
//#line 101 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 50:
//#line 102 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing parenthesis in condition"), Color.RED);}
break;
case 51:
//#line 103 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Condition not declared."), Color.RED);}
break;
case 53:
//#line 107 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term in operation."), Color.RED);}
break;
case 54:
//#line 108 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing comparator in operation."), Color.RED);}
break;
case 55:
//#line 109 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term in operation."), Color.RED);}
break;
case 62:
//#line 120 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio una asignacion OK"), Color.YELLOW);}
break;
case 63:
//#line 121 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing ID on assign."), Color.RED);}
break;
case 64:
//#line 123 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR right-hand term on assign."), Color.RED);}
break;
case 65:
//#line 124 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 67:
//#line 128 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '+' operation."), Color.RED);}
break;
case 68:
//#line 129 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '+' operation."), Color.RED);}
break;
case 69:
//#line 130 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '-' operation."), Color.RED);}
break;
case 70:
//#line 131 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '-' operation."), Color.RED);}
break;
case 73:
//#line 136 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '*' operation."), Color.RED);}
break;
case 74:
//#line 137 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '*' operation."), Color.RED);}
break;
case 75:
//#line 138 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '/' operation."), Color.RED);}
break;
case 76:
//#line 139 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '/' operation."), Color.RED);}
break;
case 82:
//#line 149 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 83:
//#line 150 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 85:
//#line 152 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 86:
//#line 153 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 88:
//#line 155 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 89:
//#line 156 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 90:
//#line 157 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Unknown function."), Color.RED);}
break;
case 93:
//#line 162 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ["), Color.RED);}
break;
case 94:
//#line 163 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ]"), Color.RED);}
break;
case 96:
//#line 165 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing index on array"), Color.RED);}
break;
//#line 813 "Parser.java"
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
{
  this.al = al;
  yydebug=true;
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
