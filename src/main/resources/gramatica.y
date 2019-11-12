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


programa 	                    :	lista_sentencias_declarativas {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se definio bien el programa."));}
			                    | 	bloque_sentencias_ejecutables {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se definio bien el programa con sent. ejecutables"));}
			                    | 	lista_sentencias_declarativas bloque_sentencias_ejecutables {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se definio bien el programa con sent. ejecutables y declarativas."));}
                                |   error  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en programa"));}
;

lista_sentencias_declarativas	:	lista_sentencias_declarativas sentencia_declarativa
								|	sentencia_declarativa
;

bloque_sentencias               :   bloque_sentencias_ejecutables
                                |   sentencia_ejecutable
;

bloque_sentencias_ejecutables	:	BEGIN lista_sentencias_ejecutables END ';'
                                |   error lista_sentencias_ejecutables END ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave BEGIN."));}
                                |   BEGIN lista_sentencias_ejecutables error ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave END."));}
                                |   error lista_sentencias_ejecutables error  ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan los delimitadores de bloque."));}
                                |   BEGIN error END ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque de ejecucion."));}
;

lista_sentencias_ejecutables	:	lista_sentencias_ejecutables sentencia_ejecutable
								|	sentencia_ejecutable
;

sentencia_declarativa 			:	tipo lista_variables ';'
                                |   lista_variables ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Tipo no declarado."));}
                                |   error lista_variables ';'{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Tipo no reconocido."));}
                                |   tipo error ';'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en lista de variables."));}
                                |   error ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia declarativa."));}
;

tipo							: 	INT     {type = "INT";}
								|	ULONG   {type = "LONG INT";}
;

lista_variables					:	lista_variables ',' ID          {addVariable($3.sval);}
								|	lista_variables ',' coleccion   {addVariable($3.sval);}
                                |	ID                              {addVariable($1.sval);}
								|	coleccion                       {addVariable($1.sval);}
;

//TODO Verificar que esto este bien. El id seria el de cte? SymbolTable.getID("id") o SymbolTable.getID("coleccion")?
coleccion						:	ID '[' cte ']' {
                                                        if (!SymbolTable.contains($1.sval){
                                                            Token t = new Token(SymbolTable.getID("id"), $1.sval, "coleccion");
                                                            t.addAttr("size", $3.sval);
                                                            t.addAttr("Use", "VARIABLE");
                                                            t.addAttr("Type", type);
                                                            SymbolTable.add(t);
                                                        }
                                                        else
                                                            Error.add(
                                                                    String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR La variable " + lex + " ya se encuentra declarada.")
                                                            )

                                                    }
}
                                |   ID '[' ']'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el tamaño de la declaracion."));}
                                |   ID '[' error ']'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la declaracion del tamaño de la coleccion."));}
;

//TODO Recuerden agregar el error de que falta “;” al final de una sentencia.
sentencia_ejecutable 			:	sentencia_asignacion
								| 	sentencia_seleccion
								|	sentencia_control
								|	sentencia_impresion
;

//TODO   Recuerden agregar el print para id y constante.
sentencia_impresion				:   PRINT '(' CADENA ')' ';' {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia Print."));}
                                |   error '(' CADENA ')' ';'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave PRINT."));}
                                |   PRINT  CADENA ')' ';'   {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal '('."));}
                                |   PRINT '(' CADENA  ';'   {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ')'."));}
                                |   PRINT '(' ')' ';'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la cadena a imprimir."));}
                                |   PRINT '(' error ')' ';'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la cadena a imprimir."));}
                               // |   PRINT '(' error ';'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la cadena a imprimir."));}
                                |   PRINT error ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia PRINT."));}
;

//TODO Generar tercetos para sentencia WHILE
sentencia_control				:	WHILE condicion DO bloque_sentencias ';' {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia While."));}
                                |   error condicion DO bloque_sentencias ';'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave WHILE."));}
                                |   WHILE condicion error bloque_sentencias ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave DO."));}
                                |   WHILE error DO bloque_sentencias ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de WHILE."));}
                                |   WHILE condicion DO error ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el bloque de sentencias WHILE."));}
                                |   WHILE error ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia WHILE."));}
;
//TODO Generar tercetos para sentencia IF
sentencia_seleccion				:	IF condicion bloque_sentencias END_IF ';' {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia If.."));}
                                |   IF condicion bloque_sentencias ELSE bloque_sentencias END_IF ';' {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia If-Else."));}
                                |   error condicion bloque_sentencias END_IF ';'    {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR al comienzo de la sentencia If."));}
                                |   IF condicion bloque_sentencias error ';'        {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR al final de la sentencia If."));}
                                |   IF condicion error END_IF ';'       {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el bloque de sentencias IF."));}
                                |   IF error bloque_sentencias END_IF ';'       {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de IF."));}
                                |   error condicion bloque_sentencias ELSE bloque_sentencias END_IF ';'   {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave IF."));}
                                |   IF condicion bloque_sentencias error bloque_sentencias END_IF ';'     {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave ELSE."));}
                                |   IF condicion bloque_sentencias ELSE bloque_sentencias error ';'       {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave END_IF."));}
                                |   IF condicion error ELSE bloque_sentencias END_IF ';'    {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque THEN de la sentencia IF."));}
                                |   IF condicion bloque_sentencias ELSE error END_IF ';'    {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque ELSE de la sentencia IF."));}
                                |   IF error bloque_sentencias ELSE bloque_sentencias END_IF ';'    {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de la sentencia IF-ELSE."));}
                                |   IF error ';'    {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en sentencia IF."));}
;


condicion						:	'(' comparacion ')'
                                |    comparacion ')' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el caracter ("));}
                                |   '(' comparacion  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el caracter )"));}
                                |   comparacion {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR faltan ambos parentesis en la condicion"));}
                                |   '(' ')' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion."));}
;

//TODO Crear terceto para la comparacion.
comparacion						:	expresion comparador expresion
                                |   error comparador expresion    {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el lado izquierdo de la comparacion."));}
                                |   expresion error expresion  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el comparador de la comparacion."));}
                                |   expresion comparador error     {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el lado derecho de la comparacion."));}
;

comparador		            	:   '<' { $$ = new ParserVal("<");}
							  	|   '>' { $$ = new ParserVal(">");}
							  	|   '=' { $$ = new ParserVal("=");}
							  	|   MENOR_IGUAL
							  	|   MAYOR_IGUAL
							  	|   DISTINTO
;

//TODO Rowing para las colecciones.
//TODO Habria que hacer $$ = $2.sval?
sentencia_asignacion 			:	id ASIGN expresion ';' {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia Asign."));}
                                |   error ASIGN expresion ';'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el ID de la asignacion."));}
                                |   id ASIGN ';'    {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el lado derecho de la asignacion."));}
                                |   id ASIGN error ';'    {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el lado derecho de la asignacion."));}

;


expresion						:	expresion '+' termino { $$ = new ParserVal(crearTercetoOperacion("+", $1.sval, $3.sval)); }
                                |   expresion '-' termino { $$ = new ParserVal(crearTercetoOperacion("-", $1.sval, $3.sval)); }
								| 	termino
                                |   expresion '+' error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '+'"));}
                                |   expresion '-' error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '-'"));}
;

termino							:	termino '*' factor { $$ = new ParserVal(crearTercetoOperacion("*", $1.sval, $3.sval)); }
                                |   termino '/' factor { $$ = new ParserVal(crearTercetoOperacion("/", $1.sval, $3.sval)); }
								| 	factor
                                |   error '*' factor {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '*'"));}
                                |   termino '*' error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '*"));}
                                |   error '/' factor {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '/'"));}
                                |   termino '/' error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '/'"));}
;

factor 							: 	id
								| 	cte
								|	ID '.' funcion
;

//TODO Generar codigo para las funciones.
funcion							:	FIRST '(' ')'
                                |   FIRST error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion FIRST."));}
                                |   LAST '(' ')'
                                |   LAST error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion LAST."));}
                                |	LENGTH '(' ')'
                                |   LENGTH error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion LENGTH."));}
                                |   error '(' ')' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Funcion desconocida."));}
                                |   error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Funcion desconocida."));}
;

//TODO Como acceder a un elemento de una coleccion?
id 								:	ID {
                                            if(SymbolTable.contains($1.sval)
                                                $$ = $1.sval;
                                            else
                                               Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + $1.sval + " no declarada."));
                                       }
}
								|	ID '[' ID ']'{
                                                     if(SymbolTable.contains($1.sval){
                                                        if (SymbolTable.contains($3.sval){
                                                            Token t = SymbolTable.getLex($3.sval)
                                                            if (t.getAttr("type", "INT")
                                                                //TODO Acceder al elemento.
                                                            else
                                                                Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + $3.sval + " no es de tipo INT."));
                                                        }
                                                        else
                                                            Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + $3.sval + " no declarada."));
                                                     }
                                                     else
                                                        Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + $1.sval + " no declarada."));
                                                }
								|	ID '[' cte ']'{
                                                       if(SymbolTable.contains($1.sval){
                                                          if (SymbolTable.contains($3.sval){
                                                              Token t = SymbolTable.getLex($3.sval)
                                                              if (t.getAttr("type", "INT")
                                                                  //TODO Acceder al elemento.
                                                              else
                                                                  Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + $3.sval + " no es de tipo INT."));
                                                          }
                                                          else
                                                              Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + $3.sval + " no declarada."));
                                                       }
                                                       else
                                                          Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + $1.sval + " no declarada."));
                                                  }
                                |   ID '[' error ']' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el subindice de la coleccion. Se esperaba un INT."));}
;

cte 							:	CTE {
                                            String cte = $1.sval;
                                            check_range(cte);
                                            $$ = $1;
                                            tipos.push(type);
                                        }
}
								| 	'-' CTE {
                                                String cte = $2.sval;
								                check_range(cte);
								                $$ = new ParserVal("-" + cte);
                                                tipos.push(type);
                                             }
;


%%

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
        )
    }
}

public Integer checkTypes(String exp1, String exp2) {
        String tipo1 = "", tipo2 = "";
        if (!tiposArrojados.isEmpty())
            tipo1 = tipos.pop();
        if (!tiposArrojados.isEmpty())
            tipo2 = tipos.pop();

        if (tipo1 != tipo2)
        {
            tipos.push("ULONG");

            if (tipo1 == "INT") { //TODO _CONV no es un nombre muy descriptivo. Cambiarlo? INT -> ULONG es la unica conversion posible.
                AdministradorTerceto.crearTerceto("_CONV", exp2, "null");
                return 2; //Indice del argumento a convertir.
            }
            else {
                AdministradorTerceto.crearTerceto("_CONV", exp1, "null");
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
			arg1 = AdministradorTerceto.get(AdministradorTerceto.getUltimoTerceto()).getId();
		if (conv == 2)
			arg2 = AdministradorTerceto.get(AdministradorTerceto.getUltimoTerceto()).getId();

		tipos.push(t);
		return AdministradorTerceto.crearTerceto(op,arg1, arg2,t);
}