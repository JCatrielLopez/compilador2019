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

  final static short[] yylhs = {-1,
          0, 0, 0, 0, 1, 1, 4, 4, 2, 2,
          2, 2, 2, 6, 6, 3, 3, 3, 3, 3,
          7, 7, 8, 8, 8, 8, 9, 9, 9, 5,
          5, 5, 5, 14, 14, 14, 14, 14, 14, 14,
          14, 13, 13, 13, 13, 13, 13, 12, 12, 12,
          12, 12, 12, 12, 12, 12, 12, 12, 12, 12,
          15, 15, 15, 15, 15, 16, 16, 16, 16, 18,
          18, 18, 18, 18, 18, 11, 11, 11, 11, 17,
          17, 17, 17, 17, 20, 20, 20, 20, 20, 20,
          20, 21, 21, 21, 22, 22, 22, 22, 22, 22,
          22, 22, 19, 19, 19, 19, 10, 10,
  };
  final static short[] yylen = {2,
          1, 1, 2, 1, 2, 1, 1, 1, 4, 4,
          4, 4, 4, 2, 1, 3, 2, 3, 3, 2,
          1, 1, 3, 3, 1, 1, 4, 3, 4, 1,
          1, 1, 1, 5, 5, 4, 4, 4, 5, 4,
          3, 5, 5, 5, 5, 5, 3, 5, 7, 5,
          5, 5, 5, 7, 7, 7, 7, 7, 7, 3,
          3, 2, 2, 1, 2, 3, 3, 3, 3, 1,
          1, 1, 1, 1, 1, 4, 4, 3, 4, 3,
          3, 1, 3, 3, 3, 3, 1, 3, 3, 3,
          3, 1, 1, 3, 3, 2, 3, 2, 3, 2,
          3, 1, 1, 4, 4, 4, 1, 2,
  };
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
  final static short[] yydefred = {0,
          0, 21, 22, 0, 0, 0, 0, 2, 6, 0,
          0, 26, 0, 0, 0, 0, 0, 20, 15, 0,
          0, 30, 31, 32, 33, 0, 0, 0, 0, 0,
          0, 3, 5, 0, 0, 17, 0, 0, 0, 107,
          0, 0, 0, 93, 0, 0, 0, 92, 0, 87,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 14, 18, 0, 0, 28, 0, 0, 0, 0,
          0, 19, 16, 0, 24, 74, 73, 75, 0, 0,
          70, 71, 72, 0, 0, 0, 0, 108, 0, 65,
          0, 0, 0, 7, 0, 8, 62, 0, 0, 0,
          0, 0, 0, 60, 0, 0, 0, 41, 0, 0,
          0, 0, 0, 47, 0, 0, 0, 0, 0, 12,
          10, 0, 78, 0, 29, 27, 13, 0, 0, 11,
          9, 88, 90, 0, 0, 0, 0, 0, 94, 77,
          0, 61, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 89, 85, 91, 86, 0, 0,
          0, 0, 0, 0, 0, 36, 40, 0, 37, 0,
          38, 0, 0, 0, 0, 0, 104, 0, 79, 76,
          106, 105, 0, 96, 0, 98, 0, 100, 0, 35,
          43, 0, 50, 0, 53, 0, 52, 51, 0, 0,
          0, 48, 39, 34, 45, 44, 46, 42, 101, 95,
          97, 99, 0, 0, 0, 0, 0, 0, 0, 54,
          59, 57, 55, 58, 56, 49,
  };
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
final static short[] yydgoto = {6,
        7, 94, 9, 95, 96, 20, 10, 11, 12, 44,
        22, 23, 24, 25, 45, 46, 47, 84, 48, 49,
        50, 139,
};
  final static short[] yysindex = {-148,
          262, 0, 0, -82, 414, 0, -75, 0, 0, -87,
          36, 0, 44, 22, -11, 66, -70, 0, 0, -125,
          95, 0, 0, 0, 0, -233, 115, -40, -46, -81,
          262, 0, 0, 5, 102, 0, -196, 454, -26, 0,
          -44, -185, 92, 0, 322, 61, 413, 0, 167, 0,
          384, 150, 292, 53, 89, 11, 451, -213, -43, 4,
          75, 0, 0, 70, 103, 0, 107, 152, 267, 26,
          156, 0, 0, -82, 0, 0, 0, 0, 3, 3,
          0, 0, 0, -44, -183, 205, 29, 0, 176, 0,
          178, 165, 344, 0, 46, 0, 0, -44, 268, 273,
          274, 289, 295, 0, 97, 128, 85, 0, 190, -35,
          1, 206, 344, 0, 344, 373, 160, 163, 181, 0,
          0, -19, 0, 108, 0, 0, 0, 199, 203, 0,
          0, 0, 0, 73, 243, -36, -30, -10, 0, 0,
          231, 0, 110, -233, 240, 344, 255, 73, 205, 167,
          205, 167, 205, 73, 0, 0, 0, 0, 344, 277,
          344, 290, 311, 403, 294, 0, 0, 306, 0, 324,
          0, 330, 331, 140, 335, 0, 0, 0, 0, 0,
          0, 0, 286, 0, 351, 0, 357, 0, 360, 0,
          0, 143, 0, 145, 0, 149, 0, 0, 155, 153,
          -248, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 364, 368, 370, 376, 379, 380, 388, 0,
          0, 0, 0, 0, 0, 0,
  };
  final static short[] yyrindex = {0,
          419, 0, 0, 118, 0, 0, 448, 0, 0, 0,
          0, 0, 0, 0, 0, 0, -41, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 179, 0,
          0, 0, 0, 0, 0, 0, 0, 0, -28, 0,
          0, 0, 0, 0, 0, 333, 0, 0, 16, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 144, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          355, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 177, -6, 0, 0, 0, 0, 0,
          0, 0, 0, 444, 0, 0, 0, 197, 38, 60,
          82, 104, 207, 220, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, -37, 0, -32, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 0, 0,
  };
  final static short[] yygindex = {0,
          0, 194, 445, 43, 516, 446, 0, 15, 418, -1,
          0, 0, 0, 0, 112, 131, 6, 412, 572, 263,
          7, 0,
  };
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
  final static int YYTABLESIZE = 772;
  final static short YYFINAL = 6;
  final static short YYMAXTOKEN = 276;
  final static String[] yyname = {
          "end-of-file", null, null, null, null, null, null, null, null, null, null, null, null, null,
          null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
          null, null, null, null, null, null, null, null, null, null, "'('", "')'", "'*'", "'+'", "','",
          "'-'", "'.'", "'/'", null, null, null, null, null, null, null, null, null, null, null, "';'",
          "'<'", "'='", "'>'", null, null, null, null, null, null, null, null, null, null, null, null,
          null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
          "'['", null, "']'", null, null, null, null, null, null, null, null, null, null, null, null,
          null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
          null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
          null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
          null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
          null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
          null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
          null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
          null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
          null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
          null, null, null, null, null, null, null, "IF", "ELSE", "END_IF", "PRINT", "INT", "ULONG",
          "WHILE", "DO", "ID", "CADENA", "CTE", "MAYOR_IGUAL", "MENOR_IGUAL", "DISTINTO", "ASIGN",
          "BEGIN", "END", "FIRST", "LAST", "LENGTH",
  };
  final static String[] yyrule = {
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
          "bloque_sentencias_ejecutables : error lista_sentencias_ejecutables error ';'",
          "bloque_sentencias_ejecutables : BEGIN error END ';'",
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
          "sentencia_impresion : error '(' CADENA ')' ';'",
          "sentencia_impresion : PRINT CADENA ')' ';'",
          "sentencia_impresion : PRINT '(' CADENA ';'",
          "sentencia_impresion : PRINT '(' ')' ';'",
          "sentencia_impresion : PRINT '(' error ')' ';'",
          "sentencia_impresion : PRINT '(' error ';'",
          "sentencia_impresion : PRINT error ';'",
          "sentencia_control : WHILE condicion DO bloque_sentencias ';'",
          "sentencia_control : error condicion DO bloque_sentencias ';'",
          "sentencia_control : WHILE condicion error bloque_sentencias ';'",
          "sentencia_control : WHILE error DO bloque_sentencias ';'",
          "sentencia_control : WHILE condicion DO error ';'",
          "sentencia_control : WHILE error ';'",
          "sentencia_seleccion : IF condicion bloque_sentencias END_IF ';'",
          "sentencia_seleccion : IF condicion bloque_sentencias ELSE bloque_sentencias END_IF ';'",
          "sentencia_seleccion : error condicion bloque_sentencias END_IF ';'",
          "sentencia_seleccion : IF condicion bloque_sentencias error ';'",
          "sentencia_seleccion : IF condicion error END_IF ';'",
          "sentencia_seleccion : IF error bloque_sentencias END_IF ';'",
          "sentencia_seleccion : error condicion bloque_sentencias ELSE bloque_sentencias END_IF ';'",
          "sentencia_seleccion : IF condicion bloque_sentencias error bloque_sentencias END_IF ';'",
          "sentencia_seleccion : IF condicion bloque_sentencias ELSE bloque_sentencias error ';'",
          "sentencia_seleccion : IF condicion error ELSE bloque_sentencias END_IF ';'",
          "sentencia_seleccion : IF condicion bloque_sentencias ELSE error END_IF ';'",
          "sentencia_seleccion : IF error bloque_sentencias ELSE bloque_sentencias END_IF ';'",
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
  static short[] yytable;
  static short[] yycheck;

  static {
    yytable();
  }

  static {
    yycheck();
  }

  boolean yydebug;        //do I want debug output?
  int[] statestk = new int[YYSTACKSIZE]; //state stack
  String yytext;//user variable to return contextual strings
  ParserVal yyval; //used to return semantic vals from action routines
  ParserVal yylval;//the 'lval' (result) I got from yylex()
  ParserVal[] valstk;
  //The following are now global, to aid in error reporting
  int yyn;       //next next thing to do
  int yym;       //
  int yystate;   //current parsing state from state table

//#line 186 "src/main/resources/gramatica.y"

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

  String yys;    //current token string
  private boolean verbose = false;

  /**
   * Default constructor.  Turn off with -Jnoconstruct .
   */
  public Parser() {
    //nothing to do
  }


  public Parser(Lexer al, boolean verbose) {
    this.al = al;
    this.verbose = verbose;
  }

  /**
   * Create a parser, setting the debug to true or false.
   *
   * @param debugMe true for debugging, false for no debug.
   */
  public Parser(boolean debugMe) {
    yydebug = debugMe;
  }

  static void yytable() {
    yytable = new short[]{43,
            42, 42, 25, 185, 42, 168, 29, 218, 27, 187,
            219, 27, 103, 103, 103, 21, 103, 25, 103, 85,
            59, 29, 79, 167, 35, 67, 27, 80, 56, 189,
            103, 103, 103, 103, 102, 102, 102, 64, 102, 179,
            102, 170, 115, 43, 69, 21, 87, 42, 42, 66,
            116, 112, 102, 102, 102, 102, 82, 119, 82, 169,
            82, 52, 120, 72, 69, 43, 42, 129, 74, 124,
            42, 99, 135, 100, 82, 82, 82, 82, 83, 37,
            83, 88, 83, 43, 130, 132, 133, 140, 42, 134,
            136, 137, 138, 105, 36, 107, 83, 83, 83, 83,
            80, 97, 80, 148, 80, 52, 154, 1, 156, 158,
            42, 108, 2, 3, 42, 99, 4, 100, 80, 80,
            80, 80, 84, 5, 84, 53, 84, 58, 123, 109,
            60, 14, 90, 121, 15, 145, 42, 16, 37, 29,
            84, 84, 84, 84, 81, 37, 81, 61, 81, 43,
            99, 79, 100, 63, 42, 172, 80, 173, 175, 42,
            73, 25, 81, 81, 81, 81, 180, 43, 34, 81,
            83, 82, 42, 91, 70, 14, 25, 4, 15, 43,
            31, 16, 91, 29, 42, 2, 3, 23, 192, 4,
            90, 71, 43, 8, 42, 125, 5, 42, 207, 126,
            32, 194, 23, 196, 43, 199, 201, 66, 102, 42,
            127, 86, 117, 103, 131, 38, 141, 67, 142, 184,
            39, 118, 40, 40, 39, 186, 40, 103, 103, 103,
            41, 103, 68, 106, 103, 103, 103, 68, 105, 103,
            103, 103, 103, 103, 54, 188, 79, 69, 166, 102,
            102, 80, 176, 102, 55, 177, 102, 102, 102, 38,
            66, 102, 102, 102, 171, 102, 110, 39, 39, 40,
            40, 82, 82, 178, 41, 82, 111, 51, 82, 82,
            82, 38, 183, 82, 82, 82, 39, 82, 40, 190,
            39, 181, 40, 83, 83, 182, 41, 83, 191, 38,
            83, 83, 83, 146, 147, 83, 83, 83, 39, 83,
            40, 42, 42, 193, 41, 80, 80, 42, 42, 80,
            18, 57, 80, 80, 80, 122, 209, 80, 80, 80,
            39, 80, 40, 42, 39, 195, 40, 84, 84, 42,
            163, 84, 164, 165, 84, 84, 84, 38, 197, 84,
            84, 84, 202, 84, 159, 160, 39, 89, 40, 81,
            81, 150, 152, 81, 203, 38, 81, 81, 81, 198,
            65, 81, 81, 81, 39, 81, 40, 76, 77, 78,
            41, 40, 204, 143, 14, 161, 162, 15, 205, 206,
            16, 210, 39, 208, 40, 143, 14, 211, 41, 15,
            212, 213, 16, 214, 39, 38, 40, 215, 143, 14,
            41, 217, 15, 216, 39, 16, 40, 39, 4, 40,
            143, 14, 220, 41, 15, 79, 221, 16, 222, 39,
            80, 40, 67, 67, 223, 41, 67, 224, 225, 67,
            67, 67, 104, 81, 83, 82, 226, 1, 67, 103,
            30, 33, 68, 68, 75, 99, 68, 100, 101, 68,
            68, 68, 69, 69, 0, 0, 69, 0, 68, 69,
            69, 69, 81, 83, 82, 66, 66, 0, 69, 66,
            0, 0, 66, 66, 66, 92, 92, 0, 92, 0,
            92, 66, 79, 0, 0, 79, 0, 80, 0, 0,
            80, 0, 0, 92, 92, 92, 0, 0, 0, 114,
            81, 83, 82, 81, 83, 82, 19, 13, 14, 0,
            19, 15, 128, 149, 16, 0, 17, 0, 151, 153,
            0, 118, 39, 40, 40, 62, 0, 39, 39, 40,
            40, 0, 0, 0, 155, 62, 19, 106, 14, 0,
            157, 15, 0, 39, 16, 40, 29, 0, 0, 39,
            0, 40, 0, 5, 0, 0, 92, 14, 0, 0,
            15, 0, 26, 16, 0, 29, 26, 92, 14, 0,
            0, 15, 5, 0, 16, 93, 29, 0, 64, 64,
            0, 26, 64, 5, 0, 64, 64, 64, 0, 92,
            14, 26, 26, 15, 64, 0, 16, 19, 29, 0,
            63, 63, 0, 0, 63, 5, 26, 63, 63, 63,
            0, 19, 26, 0, 26, 0, 63, 0, 174, 14,
            0, 0, 15, 0, 0, 16, 0, 29, 0, 92,
            14, 0, 0, 15, 5, 0, 16, 0, 29, 0,
            0, 76, 77, 78, 0, 5, 0, 0, 200, 14,
            0, 0, 15, 144, 26, 16, 0, 29, 98, 28,
            14, 0, 0, 15, 5, 0, 16, 144, 29, 0,
            76, 77, 78, 0, 26, 0, 26, 26, 0, 19,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 92,
    0,    0,    0,    0,    0,    0, 0, 0, 0, 0,
            0, 92, 92, 92, 113, 19, 0, 26, 76, 77,
            78, 76, 77, 78, 0, 0, 0, 0, 0, 0,
            26, 0, 26, 0, 26, 26, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 144,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, 0, 0, 0, 0,
            0, 144,
    };
  }

  static void yycheck() {
    yycheck = new short[]{40,
            45, 45, 44, 40, 45, 41, 44, 256, 91, 40,
            259, 44, 41, 42, 43, 1, 45, 59, 47, 46,
            91, 59, 42, 59, 10, 27, 59, 47, 40, 40,
            59, 60, 61, 62, 41, 42, 43, 271, 45, 59,
            47, 41, 256, 40, 91, 31, 41, 45, 45, 93,
            264, 41, 59, 60, 61, 62, 41, 59, 43, 59,
            45, 40, 59, 59, 91, 40, 45, 69, 265, 64,
            45, 43, 256, 45, 59, 60, 61, 62, 41, 44,
            43, 267, 45, 40, 59, 79, 80, 59, 45, 84,
            274, 275, 276, 51, 59, 53, 59, 60, 61, 62,
            41, 41, 43, 98, 45, 40, 101, 256, 102, 103,
            45, 59, 261, 262, 45, 43, 265, 45, 59, 60,
            61, 62, 41, 272, 43, 14, 45, 16, 59, 41,
            256, 257, 41, 59, 260, 93, 45, 263, 44, 265,
            59, 60, 61, 62, 41, 44, 43, 273, 45, 40,
            43, 42, 45, 59, 45, 113, 47, 115, 116, 45,
            59, 44, 59, 60, 61, 62, 59, 40, 256, 60,
            61, 62, 45, 43, 256, 257, 59, 265, 260, 40,
            256, 263, 52, 265, 45, 261, 262, 44, 146, 265,
            41, 273, 40, 0, 45, 93, 272, 45, 59, 93,
            7, 159, 59, 161, 40, 163, 164, 93, 42, 45,
            59, 256, 256, 47, 59, 256, 41, 41, 41, 256,
            265, 265, 267, 267, 265, 256, 267, 256, 257, 271,
            271, 260, 273, 271, 263, 264, 265, 41, 271, 268,
            269, 270, 271, 272, 256, 256, 42, 41, 59, 256,
            257, 47, 93, 260, 266, 93, 263, 264, 265, 256,
            41, 268, 269, 270, 59, 272, 256, 265, 265, 267,
            267, 256, 257, 93, 271, 260, 266, 256, 263, 264,
            265, 256, 40, 268, 269, 270, 265, 272, 267, 59,
            265, 93, 267, 256, 257, 93, 271, 260, 59, 256,
            263, 264, 265, 258, 259, 268, 269, 270, 265, 272,
            267, 45, 45, 59, 271, 256, 257, 45, 45, 260,
            59, 256, 263, 264, 265, 256, 41, 268, 269, 270,
            265, 272, 267, 45, 265, 59, 267, 256, 257, 45,
            256, 260, 258, 259, 263, 264, 265, 256, 59, 268,
            269, 270, 59, 272, 258, 259, 265, 266, 267, 256,
            257, 99, 100, 260, 59, 256, 263, 264, 265, 59,
            256, 268, 269, 270, 265, 272, 267, 268, 269, 270,
            271, 267, 59, 256, 257, 258, 259, 260, 59, 59,
            263, 41, 265, 59, 267, 256, 257, 41, 271, 260,
            41, 259, 263, 259, 265, 256, 267, 259, 256, 257,
            271, 259, 260, 259, 265, 263, 267, 265, 0, 267,
            256, 257, 59, 271, 260, 42, 59, 263, 59, 265,
            47, 267, 256, 257, 59, 271, 260, 59, 59, 263,
            264, 265, 59, 60, 61, 62, 59, 0, 272, 271,
            5, 7, 256, 257, 37, 43, 260, 45, 47, 263,
            264, 265, 256, 257, -1, -1, 260, -1, 272, 263,
            264, 265, 60, 61, 62, 256, 257, -1, 272, 260,
            -1, -1, 263, 264, 265, 42, 43, -1, 45, -1,
            47, 272, 42, -1, -1, 42, -1, 47, -1, -1,
            47, -1, -1, 60, 61, 62, -1, -1, -1, 59,
            60, 61, 62, 60, 61, 62, 1, 256, 257, -1,
            5, 260, 256, 256, 263, -1, 265, -1, 256, 256,
            -1, 265, 265, 267, 267, 20, -1, 265, 265, 267,
            267, -1, -1, -1, 256, 30, 31, 256, 257, -1,
            256, 260, -1, 265, 263, 267, 265, -1, -1, 265,
            -1, 267, -1, 272, -1, -1, 256, 257, -1, -1,
            260, -1, 1, 263, -1, 265, 5, 256, 257, -1,
            -1, 260, 272, -1, 263, 264, 265, -1, 256, 257,
            -1, 20, 260, 272, -1, 263, 264, 265, -1, 256,
            257, 30, 31, 260, 272, -1, 263, 92, 265, -1,
            256, 257, -1, -1, 260, 272, 45, 263, 264, 265,
            -1, 106, 51, -1, 53, -1, 272, -1, 256, 257,
            -1, -1, 260, -1, -1, 263, -1, 265, -1, 256,
            257, -1, -1, 260, 272, -1, 263, -1, 265, -1,
            -1, 268, 269, 270, -1, 272, -1, -1, 256, 257,
            -1, -1, 260, 92, 93, 263, -1, 265, 256, 256,
            257, -1, -1, 260, 272, -1, 263, 106, 265, -1,
            268, 269, 270, -1, 113, -1, 115, 116, -1, 174,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, 256,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, 268, 269, 270, 264, 200, -1, 146, 268, 269,
            270, 268, 269, 270, -1, -1, -1, -1, -1, -1,
            159, -1, 161, -1, 163, 164, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, 174, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1,   -1,   -1,
   -1,  200,
};
}

public void yyerror(String s) {

  Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "P", "|",
          s), Color.RED);
}
//## end of method parse() ######################################


//## run() --- for Thread #######################################

  public void check_range(String cte) {

    int new_cte;
    if (Long.parseLong(cte) < Math.pow(2, 15)) {
      new_cte = Integer.valueOf(cte);
    } else {
      Printer.print(String.format("%5s %s %3s %s %s", " - ", "|", "AL", "|",
              "WARNING CTE fuera de rango, se le asigna el valor minimo."), Color.YELLOW);
      new_cte = (int) Math.pow(2, 15) - 1;
    }

    String new_lex = "-" + new_cte;
    Token old_token = SymbolTable.getLex(cte);

    if (!SymbolTable.contains(new_lex)) {
      Token t = new Token(old_token.getID(), new_lex, "CTE NEG");
      SymbolTable.add(t);

      if (t.getAttr("contador") == null) {
        t.addAttr("contador", "1");
      } else {
        int contador = Integer.parseInt(t.getAttr("contador")) + 1 ;
        t.addAttr("contador", String.valueOf(contador));
      }
    }

    int contador = Integer.parseInt(old_token.getAttr("contador")) - 1;
    if( contador == 0) {
      SymbolTable.remove(old_token.getLex());
    } else {
      old_token.addAttr("contador", String.valueOf(contador));
    }
  }
//## end of method run() ########################################


//## Constructors ###############################################

  //#line 580 "Parser.java"
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
    doaction = true;
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
//#line 28 "src/main/resources/gramatica.y"
        {
          if (this.verbose)
            Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Programa OK"), Color.RESET);
        }
        break;
        case 2:
//#line 29 "src/main/resources/gramatica.y"
        {
          if (this.verbose)
            Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Programa OK (E)"), Color.RESET);
        }
        break;
        case 3:
//#line 30 "src/main/resources/gramatica.y"
        {
          if (this.verbose)
            Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Programa OK (D y E)"), Color.RESET);
        }
        break;
        case 4:
//#line 31 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en programa"), Color.RED);
        }
        break;
        case 10:
//#line 43 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave BEGIN."), Color.RED);
        }
        break;
        case 11:
//#line 44 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave END."), Color.RED);
        }
        break;
        case 12:
//#line 45 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan los delimitadores de bloque."), Color.RED);
        }
        break;
        case 13:
//#line 46 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque de ejecucion."), Color.RED);
        }
        break;
        case 17:
//#line 54 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Tipo no declarado."), Color.RED);
        }
        break;
        case 18:
//#line 55 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Tipo no reconocido."), Color.RED);
        }
        break;
        case 19:
//#line 56 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en lista de variables."), Color.RED);
        }
        break;
        case 20:
//#line 57 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia declarativa."), Color.RED);
        }
        break;
        case 28:
//#line 71 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la declaracion de la coleccion."), Color.RED);
        }
        break;
        case 29:
//#line 72 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la declaracion de la coleccion."), Color.RED);}
break;
case 34:
//#line 81 "src/main/resources/gramatica.y"
{
  if (this.verbose)
    Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia PRINT OK."), Color.RESET);
}
break;
        case 35:
//#line 82 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave PRINT."), Color.RED);
        }
        break;
        case 36:
//#line 83 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal '('."), Color.RED);
        }
        break;
        case 37:
//#line 84 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ')'."), Color.RED);
        }
        break;
        case 38:
//#line 85 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la cadena a imprimir."), Color.RED);
        }
        break;
        case 39:
//#line 86 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la cadena a imprimir."), Color.RED);
        }
        break;
        case 40:
//#line 87 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la cadena a imprimir."), Color.RED);
        }
        break;
        case 41:
//#line 88 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia PRINT."), Color.RED);
        }
        break;
        case 42:
//#line 91 "src/main/resources/gramatica.y"
        {
          if (this.verbose)
            Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia WHILE OK"), Color.RESET);
        }
        break;
        case 43:
//#line 92 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave WHILE."), Color.RED);
        }
        break;
        case 44:
//#line 93 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave DO."), Color.RED);
        }
        break;
        case 45:
//#line 94 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de WHILE."), Color.RED);
        }
        break;
        case 46:
//#line 95 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia WHILE."), Color.RED);
        }
        break;
        case 47:
//#line 96 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia WHILE."), Color.RED);
        }
        break;
        case 48:
//#line 99 "src/main/resources/gramatica.y"
        {
          if (this.verbose)
            Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia IF OK."), Color.RESET);
        }
        break;
        case 49:
//#line 100 "src/main/resources/gramatica.y"
        {
          if (this.verbose)
            Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia IF-ELSE OK."), Color.RESET);
        }
        break;
        case 50:
//#line 101 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en sentencia IF."), Color.RED);}
break;
case 51:
//#line 102 "src/main/resources/gramatica.y"
{
  Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en sentencia IF."), Color.RED);}
break;
case 52:
//#line 103 "src/main/resources/gramatica.y"
{
  Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque IF."), Color.RED);}
break;
case 53:
//#line 104 "src/main/resources/gramatica.y"
{
  Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de IF."), Color.RED);}
break;
case 54:
//#line 105 "src/main/resources/gramatica.y"
{
  Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave IF."), Color.RED);
}
break;
        case 55:
//#line 106 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave ELSE."), Color.RED);
        }
        break;
        case 56:
//#line 107 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave END_IF."), Color.RED);
        }
        break;
        case 57:
//#line 108 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque IF."), Color.RED);}
break;
case 58:
//#line 109 "src/main/resources/gramatica.y"
{
  Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque ELSE."), Color.RED);}
break;
case 59:
//#line 110 "src/main/resources/gramatica.y"
{
  Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion."), Color.RED);}
break;
case 60:
//#line 111 "src/main/resources/gramatica.y"
{
  Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en sentencia IF."), Color.RED);
}
break;
        case 62:
//#line 115 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el caracter ("), Color.RED);
        }
        break;
        case 63:
//#line 116 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el caracter )"), Color.RED);
        }
        break;
        case 64:
//#line 117 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR faltan parentesis en la condicion"), Color.RED);
        }
        break;
        case 65:
//#line 118 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion."), Color.RED);
        }
        break;
        case 67:
//#line 122 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la comparacion."), Color.RED);
        }
        break;
        case 68:
//#line 123 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la comparacion."), Color.RED);
        }
        break;
        case 69:
//#line 124 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la comparacion."), Color.RED);
        }
        break;
        case 76:
//#line 135 "src/main/resources/gramatica.y"
        {
          if (this.verbose)
            Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia ASIGN OK."), Color.RESET);
        }
        break;
        case 77:
//#line 136 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el ID de la asignacion."), Color.RED);
        }
        break;
        case 78:
//#line 137 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el lado derecho de la asignacion."), Color.RED);
        }
        break;
        case 79:
//#line 138 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el lado derecho de la asignacion."), Color.RED);
        }
        break;
        case 83:
//#line 145 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '+'"), Color.RED);
        }
        break;
        case 84:
//#line 146 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '-'"), Color.RED);}
break;
case 88:
//#line 152 "src/main/resources/gramatica.y"
{
  Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '*'"), Color.RED);
}
break;
        case 89:
//#line 153 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '*"), Color.RED);}
break;
case 90:
//#line 154 "src/main/resources/gramatica.y"
{
  Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '/'"), Color.RED);}
break;
case 91:
//#line 155 "src/main/resources/gramatica.y"
{
  Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '/'"), Color.RED);
}
break;
        case 96:
//#line 164 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion FIRST."), Color.RED);
        }
        break;
        case 98:
//#line 166 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion LAST."), Color.RED);
        }
        break;
        case 100:
//#line 168 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion LENGTH."), Color.RED);
        }
        break;
        case 101:
//#line 169 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Funcion desconocida."), Color.RED);
        }
        break;
        case 102:
//#line 170 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Funcion desconocida."), Color.RED);
        }
        break;
        case 106:
//#line 176 "src/main/resources/gramatica.y"
        {
          Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el subindice de la coleccion."), Color.RED);
        }
        break;
        case 108:
//#line 180 "src/main/resources/gramatica.y"
        {
          String cte = val_peek(0).sval;
          check_range(val_peek(0).sval);
        }
        break;
//#line 990 "Parser.java"
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

  /**
   * A default run method, used for operating this parser
   * object in the background.  It is intended for extending Thread
   * or implementing Runnable.  Turn off with -Jnorun .
   */
  public void run()
{
  yyparse();
}
//###############################################################



}
//################### END OF CLASS ##############################
