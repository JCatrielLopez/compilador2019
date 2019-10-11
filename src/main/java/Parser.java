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






//#line 1 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
//#line 19 "Parser.java"




public class Parser
{

boolean yydebug = true;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

  public Parser(Lexer al) {
    this.al = al;
  }

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
    0,    0,    0,    0,    2,    2,    2,    2,    2,    2,
    2,    3,    3,    3,    3,    3,    3,    3,    4,    4,
    4,    6,    6,    6,    6,    6,    6,    6,    6,    6,
    6,    1,    1,    1,    1,    1,    1,    5,    5,
};
final static short yylen[] = {                            2,
    4,    4,    4,    4,    3,    3,    3,    3,    3,    3,
    1,    3,    3,    3,    3,    3,    3,    1,    1,    1,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    1,    4,    3,    4,    4,    4,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   38,    0,   19,    0,    0,   18,   20,   34,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   39,    0,
    0,    2,    0,    0,   37,   35,   33,   36,    3,    4,
    1,    0,    0,    0,   13,   16,    0,    0,    0,    0,
   21,    0,    0,    0,    0,   14,   12,   17,   15,    0,
    0,    0,    0,    0,    0,    0,   31,   23,   24,   22,
   26,   27,   25,   29,   30,   28,
};
final static short yydgoto[] = {                          3,
   13,   14,   15,   16,   17,   51,
};
final static short yysindex[] = {                      -249,
 -254,  -88,    0, -238,  -45,  -52,  -44,  -41,  -32,  -34,
    0, -224,    0,   21,  -22,    0,    0,    0,  -43,  -87,
  -33,   29,  -19,  -40,  -40,  -31,  -31, -247,    0,  -37,
  -27,    0,  -24,  -23,    0,    0,    0,    0,    0,    0,
    0,   26,  -22,  -22,    0,    0,    9,  -38,  -21,  -17,
    0,   26,  -22,   26,  -22,    0,    0,    0,    0,   18,
   34,  -11,   40,  -10,   41,   -9,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0, -204,    0,    0,    0,    0,    0,    0,    0,   -8,
    0,    0,    0,    0,   -7,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   -1,    2,    0,    0,    0,    0,    0,    0,
    0,    3,   10,   11,   20,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   83,   81,   62,   51,   84,    0,
};
final static int YYTABLESIZE=276;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         12,
   12,   62,    7,   12,   12,   37,    1,   12,   47,   26,
   24,   28,   25,   12,   27,    2,    5,   12,   64,   33,
   12,   12,   66,   30,   34,   31,   48,   49,   50,   70,
   73,   76,    8,   32,   32,   11,   32,   11,   32,   41,
   18,    6,   29,    6,    9,    7,    9,    7,   60,   35,
   32,   11,    5,   10,    5,   10,    7,    6,   67,   38,
    9,    7,    8,   30,    8,   31,   32,   26,    5,   10,
   26,   24,   27,   25,   68,   27,   45,   46,    8,   32,
   71,   74,    4,   57,   59,   43,   44,   39,   23,    0,
   21,   53,   55,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   36,    0,
    0,    0,    0,    0,    0,    0,    6,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    9,   19,    0,    0,   22,   42,    0,   61,   52,   10,
   20,   11,   11,   10,   10,   11,   11,   10,   54,   11,
    6,   56,   58,   10,   63,   11,   40,   10,   65,   11,
   10,   10,   11,   11,   69,   72,   75,   32,   11,    0,
    0,    0,    0,    0,    6,    0,    0,    9,    7,    0,
    0,    0,    0,    0,    0,    5,   10,    0,    0,    0,
    0,    0,    0,    0,    0,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         45,
   45,   40,   91,   45,   45,   93,  256,   45,  256,   42,
   43,   46,   45,   45,   47,  265,  271,   45,   40,   42,
   45,   45,   40,   43,   47,   45,  274,  275,  276,   41,
   41,   41,  271,   42,   43,   43,   45,   45,   47,   59,
   93,   43,  267,   45,   43,   43,   45,   45,   40,   93,
   59,   59,   43,   43,   45,   45,   91,   59,   41,   93,
   59,   59,   43,   43,   45,   45,  271,   42,   59,   59,
   42,   43,   47,   45,   41,   47,   26,   27,   59,   59,
   41,   41,    0,   33,   34,   24,   25,   59,    8,   -1,
    7,   30,   31,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  265,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  256,  256,   -1,   -1,  256,  256,   -1,  256,  256,  265,
  265,  267,  267,  265,  265,  267,  267,  265,  256,  267,
  265,  256,  256,  265,  256,  267,  256,  265,  256,  267,
  265,  265,  267,  267,  256,  256,  256,  256,  256,   -1,
   -1,   -1,   -1,   -1,  256,   -1,   -1,  256,  256,   -1,
   -1,   -1,   -1,   -1,   -1,  256,  256,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  256,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=276;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'",null,
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
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
"$accept : sentencia_asignacion",
"sentencia_asignacion : id ASIGN expresion ';'",
"sentencia_asignacion : error ASIGN expresion ';'",
"sentencia_asignacion : id ASIGN error ';'",
"sentencia_asignacion : id ASIGN expresion error",
"expresion : expresion '+' termino",
"expresion : error '+' termino",
"expresion : expresion '+' error",
"expresion : expresion '-' termino",
"expresion : error '-' termino",
"expresion : expresion '-' error",
"expresion : termino",
"termino : termino '*' factor",
"termino : error '*' factor",
"termino : termino '*' error",
"termino : termino '/' factor",
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

//#line 178 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"

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
	//System.out.println("Linea " + al.getNroLinea() + ": (Parser) " + s);
}
//#line 315 "Parser.java"
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
//#line 122 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio una asignacion OK"), Color.YELLOW);}
break;
case 2:
//#line 123 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing ID on assign."), Color.RED);}
break;
case 3:
//#line 125 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR right-hand term on assign."), Color.RED);}
break;
case 4:
//#line 126 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
break;
case 5:
//#line 129 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio una suma!"), Color.YELLOW);}
break;
case 6:
//#line 130 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '+' operation."), Color.RED);}
break;
case 7:
//#line 131 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '+' operation."), Color.RED);}
break;
case 8:
//#line 132 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio una resta!"), Color.YELLOW);}
break;
case 9:
//#line 133 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '-' operation."), Color.RED);}
break;
case 10:
//#line 134 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '-' operation."), Color.RED);}
break;
case 12:
//#line 138 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio una multiplicacion!"), Color.YELLOW);}
break;
case 13:
//#line 139 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '*' operation."), Color.RED);}
break;
case 14:
//#line 140 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '*' operation."), Color.RED);}
break;
case 15:
//#line 141 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio una division!"), Color.YELLOW);}
break;
case 16:
//#line 142 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '/' operation."), Color.RED);}
break;
case 17:
//#line 143 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '/' operation."), Color.RED);}
break;
case 23:
//#line 153 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 24:
//#line 154 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 26:
//#line 156 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 27:
//#line 157 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 29:
//#line 159 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
break;
case 30:
//#line 160 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
break;
case 31:
//#line 161 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Unknown function."), Color.RED);}
break;
case 34:
//#line 166 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ["), Color.RED);}
break;
case 35:
//#line 167 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ]"), Color.RED);}
break;
case 37:
//#line 169 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing index on array"), Color.RED);}
break;
case 38:
//#line 172 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio una constante!"), Color.YELLOW);}
break;
case 39:
//#line 173 "/home/catriel/IdeaProjects/compilador2019/src/main/resources/s.y"
{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio una -constante!"), Color.YELLOW);}
break;
//#line 576 "Parser.java"
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
