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
    2,    4,    4,    3,    3,    6,    6,    7,    7,    7,
    7,    8,    5,    5,    5,    5,    5,   13,   12,   11,
   11,   14,   14,   14,   14,   14,   15,   15,   15,   15,
   17,   17,   17,   17,   17,   17,   10,   10,   10,   10,
   10,   16,   16,   19,   19,   20,   20,   20,   21,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   18,   18,
   18,   18,    9,    9,
};
final static short yylen[] = {                            2,
    1,    1,    2,    1,    2,    1,    4,    4,    3,    4,
    2,    2,    1,    3,    2,    1,    1,    3,    3,    1,
    1,    4,    1,    1,    1,    1,    1,    5,    5,    5,
    7,    3,    2,    2,    1,    1,    3,    3,    3,    3,
    1,    1,    1,    1,    1,    1,    4,    3,    3,    4,
    3,    3,    1,    3,    1,    1,    1,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    1,    4,
    4,    4,    1,    2,
};
final static short yydefred[] = {                         0,
    0,   16,   17,    0,    0,    0,    0,    2,    6,    0,
    0,   21,    0,    0,    0,    0,    0,   11,   26,    0,
   13,   23,   24,   25,   27,    0,    0,    0,    0,    3,
    5,    0,   15,    0,    0,    0,   73,    0,    0,   57,
    0,    0,    0,   56,    0,   55,    0,    0,    0,    0,
    0,   12,    0,    0,    0,    9,    0,   14,    0,   19,
   45,   44,   46,   41,   42,   43,    0,    0,    0,   74,
    0,    0,    0,   33,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   48,    8,    0,    0,   49,   22,   10,
    7,    0,    0,    0,    0,    0,    0,   58,   32,    0,
    0,    0,    0,   40,    0,   54,    0,    0,   72,   70,
    0,   50,   47,   71,    0,    0,    0,    0,    0,    0,
    0,    0,   30,   28,   29,   68,   60,   61,   59,   63,
   64,   62,   66,   67,   65,    0,   31,
};
final static short yydgoto[] = {                          6,
    7,    8,   19,   20,   21,   10,   11,   12,   40,   22,
   23,   24,   25,   41,   42,   43,   67,   44,   45,   46,
   98,
};
final static short yysindex[] = {                      -112,
  -54,    0,    0,  -82,  -91,    0, -104,    0,    0, -237,
  -36,    0,   21,   -8,   21,   -4,   31,    0,    0, -125,
    0,    0,    0,    0,    0,   22,  -31, -150,  -54,    0,
    0,   49,    0, -210,   40,  -40,    0, -208,   34,    0,
 -246,   47,   13,    0,   48,    0, -185, -173,   35,  -26,
   57,    0,   38,   -6,    2,    0,  -55,    0,  -82,    0,
    0,    0,    0,    0,    0,    0,   31,   35, -171,    0,
   40,   56, -201,    0,   31,   31,   39,   31,   76, -246,
    5,   26,   27,    0,    0,   63,   11,    0,    0,    0,
    0,  102,   61,  116,   -9,   28,   29,    0,    0, -246,
  103,  102,   48,    0,  102,    0,  105,  106,    0,    0,
    0,    0,    0,    0,  126,  132,   36,  134,   41,  135,
   51,  -81,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  118,    0,
};
final static short yyrindex[] = {                         0,
  179,    0,    0,   55,    0,    0,  181,    0,    0,    0,
    0,    0,    0,    0,    0,  -32,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -138,  -41,    0,    0,    0,    0,
    0, -113,    0,    0,  -19,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   83,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -109,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -132,    0,    0,    0,
    0,  -38,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -16,    3,    0,    6,    0,    0,    0,    0,    0,
  -10,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    9,   71,  177,   32,    0,  173,  150,  -20,    0,
    0,    0,    0,  170,  147,   19,  144,   10,  112,  111,
    0,
};
final static int YYTABLESIZE=310;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         69,
   69,   69,   38,   91,   18,   69,   55,   34,   27,   29,
   26,   20,   69,   38,   26,   30,   76,   69,   69,   69,
   69,   53,   33,   53,   39,    5,   20,    4,   83,   26,
  117,   47,   84,   22,   71,   50,   76,   26,   26,   53,
   53,   53,   53,   52,   54,   52,   37,   93,   22,   73,
   68,   52,   88,   76,   59,   76,  100,  101,   70,   52,
   39,   52,   52,   52,   52,   38,   38,  119,  121,  113,
    9,   87,   64,   66,   65,   38,  129,   31,   38,   38,
   79,  132,   38,   38,   94,   92,   49,   74,  108,   78,
   80,  135,   34,  102,   89,  105,   99,  109,   20,   64,
   66,   65,   95,   96,   97,   56,   13,   58,  122,   14,
    2,    3,   15,   20,   16,   85,  107,   36,  110,  111,
   17,  112,   57,   51,   51,   36,   18,   51,   51,   51,
   51,   13,   51,   36,   14,    2,    3,   15,   51,   16,
   51,   18,   35,    1,   76,   17,   34,   51,    2,    3,
   35,   29,    4,  114,   34,  115,    2,    3,   35,    5,
    4,  123,   34,  124,  125,   13,  126,    5,   14,    2,
    3,   15,  127,   16,  130,  133,  137,  136,    4,   17,
    1,   28,   32,   60,   48,   72,   77,  103,  106,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   90,    0,   13,    0,    0,   14,    2,    3,   15,    0,
   16,    0,    0,    0,   69,   69,   17,   38,   69,   69,
   69,   69,   69,   69,    0,   38,   69,   69,   69,   69,
   69,   69,   69,   38,   69,   37,   53,   53,   69,   39,
   53,   53,   53,   53,   53,   53,  116,   39,   53,   53,
   53,   53,   53,   53,   71,   39,   71,    0,   52,   52,
   71,   37,   52,   52,   52,   52,   52,   52,   75,   37,
   52,   52,   52,   52,   52,   52,   35,   37,    0,    0,
   61,   62,   63,  118,  120,   36,   36,   37,   37,   71,
   81,  128,   53,   86,  104,   36,  131,   37,   36,   82,
   37,   37,   36,   36,   37,   37,  134,   61,   62,   63,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   41,   59,   59,   46,   27,   44,   91,  256,
    1,   44,   45,   45,    5,    7,   43,   59,   60,   61,
   62,   41,   59,   43,   41,  272,   59,  265,   49,   20,
   40,   40,   59,   44,   45,   17,   43,   28,   29,   59,
   60,   61,   62,   41,   26,   43,   41,   68,   59,   41,
   91,   20,   59,   43,  265,   43,  258,  259,  267,   28,
   40,   59,   60,   61,   62,   45,   45,   40,   40,   59,
    0,   53,   60,   61,   62,   45,   41,    7,   45,   45,
  266,   41,   45,   45,  256,   67,   91,   41,   80,   42,
  264,   41,   44,   75,   93,   77,   41,   93,   44,   60,
   61,   62,  274,  275,  276,  256,  257,   59,  100,  260,
  261,  262,  263,   59,  265,   59,   41,  256,   93,   93,
  271,   59,  273,  256,  257,  264,   44,  260,  261,  262,
  263,  257,  265,  272,  260,  261,  262,  263,  271,  265,
  273,   59,  256,  256,   43,  271,  256,  273,  261,  262,
  264,  256,  265,   93,  264,   40,  261,  262,  272,  272,
  265,   59,  272,   59,   59,  257,   41,  272,  260,  261,
  262,  263,   41,  265,   41,   41,   59,  259,    0,  271,
    0,    5,   10,   34,   15,   39,   43,   76,   78,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  256,   -1,  257,   -1,   -1,  260,  261,  262,  263,   -1,
  265,   -1,   -1,   -1,  256,  257,  271,  256,  260,  261,
  262,  263,  264,  265,   -1,  264,  268,  269,  270,  271,
  272,  273,  265,  272,  267,  267,  256,  257,  271,  256,
  260,  261,  262,  263,  264,  265,  256,  264,  268,  269,
  270,  271,  272,  273,  265,  272,  267,   -1,  256,  257,
  271,  256,  260,  261,  262,  263,  264,  265,  256,  264,
  268,  269,  270,  271,  272,  273,  256,  272,   -1,   -1,
  268,  269,  270,  256,  256,  265,  265,  267,  267,  256,
  256,  256,  271,  256,  256,  265,  256,  267,  265,  265,
  267,  267,  265,  265,  267,  267,  256,  268,  269,  270,
};
}
final static short YYFINAL=6;
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
"bloque_sentencias_ejecutables : error lista_sentencias_ejecutables END ';'",
"bloque_sentencias_ejecutables : BEGIN lista_sentencias_ejecutables error",
"bloque_sentencias_ejecutables : BEGIN lista_sentencias_ejecutables END error",
"bloque_sentencias_ejecutables : error ';'",
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
"sentencia_ejecutable : sentencia_declarativa",
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
"sentencia_asignacion : id ASIGN error ';'",
"sentencia_asignacion : id ASIGN expresion",
"expresion : expresion '+' termino",
"expresion : termino",
"termino : termino '*' factor",
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

//#line 176 "gramatica.y"

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
//#line 383 "Parser.java"
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
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: END"), Color.RED);}
break;
case 10:
//#line 41 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 11:
//#line 42 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: asd"), Color.RED);}
break;
case 15:
//#line 51 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Type not declared"), Color.RED);}
break;
case 28:
//#line 78 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio una impresion OK"), Color.YELLOW);}
break;
case 30:
//#line 92 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio un IF OK"), Color.YELLOW);}
break;
case 33:
//#line 104 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 34:
//#line 105 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 35:
//#line 106 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing parenthesis in condition"), Color.RED);}
break;
case 36:
//#line 107 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Condition not declared."), Color.RED);}
break;
case 38:
//#line 111 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term in operation."), Color.RED);}
break;
case 39:
//#line 112 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing comparator in operation."), Color.RED);}
break;
case 40:
//#line 113 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term in operation."), Color.RED);}
break;
case 47:
//#line 124 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio una asignacion    OK"), Color.YELLOW);}
break;
case 48:
//#line 125 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing ID on assign."), Color.RED);}
break;
case 49:
//#line 126 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing := on assign."), Color.RED);}
break;
case 50:
//#line 127 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR right-hand term on assign."), Color.RED);}
break;
case 51:
//#line 128 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 60:
//#line 153 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 61:
//#line 154 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 63:
//#line 156 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 64:
//#line 157 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 66:
//#line 159 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 67:
//#line 160 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 68:
//#line 161 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Unknown function."), Color.RED);}
break;
case 72:
//#line 167 "gramatica.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing index on array"), Color.RED);}
break;
//#line 656 "Parser.java"
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
