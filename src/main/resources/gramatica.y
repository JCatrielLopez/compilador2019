%{package parser;
  import globals.Printer;
  import globals.SymbolTable;
  import lexer.Lexer;
  import lexer.Token;
  import java.util.Stack;
  import ParserVal;
%}


%token IF
%token ELSE
%token END_IF
%token PRINT
%token INT
%token ULONG
%token WHILE
%token DO
%token ID
%token CADENA
%token CTE
%token MAYOR_IGUAL
%token MENOR_IGUAL
%token DISTINTO
%token ASIGN
%token BEGIN
%token END
%token FIRST
%token LAST
%token LENGTH

%left '+' '-'
%left '*' '/'

%%



programa 	                    :	lista_sentencias_declarativas {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Programa OK"), globals.Color.RESET);}
			                    | 	bloque_sentencias_ejecutables {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Programa OK (E)"), globals.Color.RESET);}
			                    | 	lista_sentencias_declarativas bloque_sentencias_ejecutables {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Programa OK (D y E)"), globals.Color.RESET);}
                                |   error  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en programa"), globals.Color.RED);}
;

lista_sentencias_declarativas	:	lista_sentencias_declarativas sentencia_declarativa
								|	sentencia_declarativa
;

bloque_sentencias               :   bloque_sentencias_ejecutables
                                |   sentencia_ejecutable
;

bloque_sentencias_ejecutables	:	BEGIN lista_sentencias_ejecutables END ';'
                                |   error lista_sentencias_ejecutables END ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave BEGIN."), globals.Color.RED);}
                                |   BEGIN lista_sentencias_ejecutables error ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave END."), globals.Color.RED);}
                                |   error lista_sentencias_ejecutables error  ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan los delimitadores de bloque."), globals.Color.RED);}
                                |   BEGIN error END ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque de ejecucion."), globals.Color.RED);}
;

lista_sentencias_ejecutables	:	lista_sentencias_ejecutables sentencia_ejecutable
								|	sentencia_ejecutable
;

sentencia_declarativa 			:	tipo lista_variables ';'
                                |   lista_variables ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Tipo no declarado."), globals.Color.RED);}
                                |   error lista_variables ';'{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Tipo no reconocido."), globals.Color.RED);}
                                |   tipo error ';'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en lista de variables."), globals.Color.RED);}
                                |   error ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia declarativa."), globals.Color.RED);}
;

tipo							: 	INT     {type = "INT";}
								|	ULONG   {type = "LONG INT";}
;

lista_variables					:	lista_variables ',' ID          {addVariable($3.sval);}
								|	lista_variables ',' coleccion   {addVariable($3.sval);}
                                |	ID                              {addVariable($1.sval);}
								|	coleccion                       {addVariable($1.sval);}
;

//TODO Size de la coleccion en la TS.
coleccion						:	ID '[' cte ']'
                                |   ID '[' ']'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la declaracion de la coleccion."), globals.Color.RED);}
                                |   ID '[' error ']'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la declaracion de la coleccion."), globals.Color.RED);}
;

//TODO Recuerden agregar el error de que falta “;” al final de una sentencia.
sentencia_ejecutable 			:	sentencia_asignacion
								| 	sentencia_seleccion
								|	sentencia_control
								|	sentencia_impresion
;

//TODO   Recuerden agregar el print para id y constante.
sentencia_impresion				:   PRINT '(' CADENA ')' ';' {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia PRINT OK."), globals.Color.RESET);}
                                |   error '(' CADENA ')' ';'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave PRINT."), globals.Color.RED);}
                                |   PRINT  CADENA ')' ';'   {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal '('."), globals.Color.RED);}
                                |   PRINT '(' CADENA  ';'   {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ')'."), globals.Color.RED);}
                                |   PRINT '(' ')' ';'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la cadena a imprimir."), globals.Color.RED);}
                                |   PRINT '(' error ')' ';'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la cadena a imprimir."), globals.Color.RED);}
                                |   PRINT '(' error ';'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la cadena a imprimir."), globals.Color.RED);}
                                |   PRINT error ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia PRINT."), globals.Color.RED);}
;

sentencia_control				:	WHILE condicion DO bloque_sentencias ';' {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia WHILE OK"), globals.Color.RESET);}
                                |   error condicion DO bloque_sentencias ';'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave WHILE."), globals.Color.RED);}
                                |   WHILE condicion error bloque_sentencias ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave DO."), globals.Color.RED);}
                                |   WHILE error DO bloque_sentencias ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de WHILE."), globals.Color.RED);}
                                |   WHILE condicion DO error ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia WHILE."), globals.Color.RED);}
                                |   WHILE error ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia WHILE."), globals.Color.RED);}
;

sentencia_seleccion				:	IF condicion bloque_sentencias END_IF ';' {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia IF OK."), globals.Color.RESET);}
                                |   IF condicion bloque_sentencias ELSE bloque_sentencias END_IF ';' {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia IF-ELSE OK."), globals.Color.RESET);}
                                |   error condicion bloque_sentencias END_IF ';'    {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en sentencia IF."), globals.Color.RED);}
                                |   IF condicion bloque_sentencias error ';'        {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en sentencia IF."), globals.Color.RED);}
                                |   IF condicion error END_IF ';'       {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque IF."), globals.Color.RED);}
                                |   IF error bloque_sentencias END_IF ';'       {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de IF."), globals.Color.RED);}
                                |   error condicion bloque_sentencias ELSE bloque_sentencias END_IF ';'   {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave IF."), globals.Color.RED);}
                                |   IF condicion bloque_sentencias error bloque_sentencias END_IF ';'     {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave ELSE."), globals.Color.RED);}
                                |   IF condicion bloque_sentencias ELSE bloque_sentencias error ';'       {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave END_IF."), globals.Color.RED);}
                                |   IF condicion error ELSE bloque_sentencias END_IF ';'    {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque IF."), globals.Color.RED);}
                                |   IF condicion bloque_sentencias ELSE error END_IF ';'    {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque ELSE."), globals.Color.RED);}
                                |   IF error bloque_sentencias ELSE bloque_sentencias END_IF ';'    {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion."), globals.Color.RED);}
                                |   IF error ';'    {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en sentencia IF."), globals.Color.RED);}
;

condicion						:	'(' comparacion ')'
                                |    comparacion ')' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el caracter ("), globals.Color.RED);}
                                |   '(' comparacion  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el caracter )"), globals.Color.RED);}
                                |   comparacion {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR faltan parentesis en la condicion"), globals.Color.RED);}
                                |   '(' ')' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion."), globals.Color.RED);}
;

comparacion						:	expresion comparador expresion
                                |   error comparador expresion    {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la comparacion."), globals.Color.RED);}
                                |   expresion error expresion  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la comparacion."), globals.Color.RED);}
                                |   expresion comparador error     {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la comparacion."), globals.Color.RED);}
;

comparador		            	:   '<'
							  	|   '>'
							  	|   '='
							  	|   MENOR_IGUAL
							  	|   MAYOR_IGUAL
							  	|   DISTINTO
;

//TODO Rowing para las colecciones.
sentencia_asignacion 			:	id ASIGN expresion ';' {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia ASIGN OK."), globals.Color.RESET);}
                                |   error ASIGN expresion ';'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el ID de la asignacion."), globals.Color.RED);}
                                |   id ASIGN ';'    {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el lado derecho de la asignacion."), globals.Color.RED);}
                                |   id ASIGN error ';'    {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el lado derecho de la asignacion."), globals.Color.RED);}

;

expresion						:	expresion '+' termino
                                |   expresion '-' termino
								| 	termino
                                |   expresion '+' error {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '+'"), globals.Color.RED);}
                                |   expresion '-' error {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '-'"), globals.Color.RED);}
;

termino							:	termino '*' factor
                                |   termino '/' factor
								| 	factor
                                |   error '*' factor {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '*'"), globals.Color.RED);}
                                |   termino '*' error {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '*"), globals.Color.RED);}
                                |   error '/' factor {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '/'"), globals.Color.RED);}
                                |   termino '/' error {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '/'"), globals.Color.RED);}
;

factor 							: 	id
								| 	cte
								|	ID '.' funcion
;

//TODO Generar codigo para las funciones.
funcion							:	FIRST '(' ')'
                                |   FIRST error {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion FIRST."), globals.Color.RED);}
                                |   LAST '(' ')'
                                |   LAST error {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion LAST."), globals.Color.RED);}
                                |	LENGTH '(' ')'
                                |   LENGTH error {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion LENGTH."), globals.Color.RED);}
                                |   error '(' ')' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Funcion desconocida."), globals.Color.RED);}
                                |   error {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Funcion desconocida."), globals.Color.RED);}
;

//TODO Verificar si esta declarada.
//TODO Como acceder a un elemento de una coleccion.
id 								:	ID
								|	ID '[' ID ']' //TODO Verificar que sea de tipo int.
								|	ID '[' cte ']'
                                |   ID '[' error ']' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el subindice de la coleccion."), globals.Color.RED);}
;

cte 							:	CTE
								| 	'-' CTE {String cte = $2.sval;
								             check_range($2.sval);}
;


%%

private Lexer al;
private boolean verbose;

public Parser(Lexer al, boolean as_verbose) {
  this.al = al;
  this.verbose = verbose;
}

// Analisis sintactico
public StringBuffer estructuras_sintacticas = new StringBuffer(); //TODO Para que se usa?
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
      Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "P", "|", s), globals.Color.RED);
}


public void check_range(String cte) {

        int new_cte;
        if (Long.parseLong(cte) <= Math.pow(2, 15)) {
            new_cte = Integer.valueOf(cte);
        } else {
            Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "WARNING Constante fuera de rango: " + cte), globals.Color.YELLOW);
            Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "WARNING Se va a reemplazar por el valor: -" + Math.pow(2, 15)), globals.Color.YELLOW);
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

public void addVariable(String lex){
    if (!SymbolTable.contains(lex)){
        Token token = new Token(SymbolTable.getID("id"), lex, "ID");
        token.addAttr("Use", "VARIABLE");
        token.addAttr("Type", type);
        SymbolTable.add(token);
    }
    else{}
        //TODO Se informa como error
}