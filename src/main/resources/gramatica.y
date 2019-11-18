%{
package parser;
import globals.*;
import globals.Error;
import assembler.*;
import lexer.Lexer;
import lexer.Token;
import java.util.ArrayList;
import java.util.Stack;
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


programa 	                : bloque_declarativo {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se definio bien el programa."));}
			        | bloque_ejecutable {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se definio bien el programa con sent. ejecutables"));}
			        | bloque_declarativo bloque_ejecutable {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se definio bien el programa con sent. ejecutables y declarativas."));}
                                | error  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en programa"));}
;

bloque_declarativo		: bloque_declarativo sentencia_declarativa
				| sentencia_declarativa
;

bloque_ejecutable		: BEGIN lista_sentencias_ejecutables END ';'
                                | error lista_sentencias_ejecutables END ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave BEGIN."));}
                                | BEGIN lista_sentencias_ejecutables error ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave END."));}
                                | BEGIN error END ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque de ejecucion."));}
                                | BEGIN lista_sentencias_ejecutables END error {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta literal ';'."));}
                                | BEGIN lista_sentencias_ejecutables 'EOF' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave 'END;'."));}
;

bloque_sentencias               : bloque_ejecutable
                                | sentencia_ejecutable
;

lista_sentencias_ejecutables	: lista_sentencias_ejecutables sentencia_ejecutable
				| sentencia_ejecutable
;

sentencia_declarativa 		: tipo lista_variables ';'
                                | lista_variables ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Tipo no declarado."));}
                                | error lista_variables ';'{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Tipo no reconocido."));}
                                | tipo error ';'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en lista de variables."));}
                                | error ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia declarativa."));}
;

tipo				: INT {type = "INT";}
				| ULONG {type = "ULONG";}
;

lista_variables			: lista_variables ',' ID {addVariable($3.sval, "VARIABLE");}
				| lista_variables ',' coleccion {addVariable($3.sval, "COLECCION");}
				| lista_variables  ID {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR falta ',' entre variables."));}
				| lista_variables  coleccion {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR falta ',' entre variables."));}
				| ID {addVariable($1.sval, "VARIABLE");}
				| coleccion {addVariable($1.sval, "COLECCION");}
;

coleccion			: ID '[' cte ']' {
							Token coleccion = SymbolTable.getLex($1.sval);
							if (coleccion.getAttr("use") == null){
							    coleccion.addAttr("size", $3.sval);
							    coleccion.addAttr("Elements", new ArrayList<>());
							}
							else
							    Error.add(
								    String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR La variable " + $1.sval + " ya se encuentra declarada.")
							    );
						}
                                |   ID '[' ']'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el tamaño de la declaracion."));}
                                |   ID '[' error ']'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la declaracion del tamaño de la coleccion."));}
;


sentencia_ejecutable 		: sentencia_impresion
				| sentencia_control
				| sentencia_seleccion
				| sentencia_asignacion
;

sentencia_impresion		: PRINT '(' CADENA ')' ';' {AdminTercetos.add(new Terceto("PRINT", $3.sval)); if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia Print."));}
                                | PRINT '(' ID ')' ';' {AdminTercetos.add(new Terceto("PRINT", $3.sval));}
				| PRINT '(' cte ')' ';' {AdminTercetos.add(new Terceto("PRINT", $3.sval));}
                                | error '(' CADENA ')' ';'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave PRINT."));}
                                | PRINT  CADENA ')' ';'   {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal '('."));}
                                | PRINT '(' CADENA  ';'   {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ')'."));}
                                | PRINT '(' ')' ';'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la cadena a imprimir."));}
                                | PRINT '(' error ')' ';'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la cadena a imprimir."));}
                                | PRINT '(' error ';'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la cadena a imprimir."));}
				| PRINT CADENA ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR faltan los parentesis."));}
                                | PRINT error ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia PRINT."));}
				| PRINT '(' CADENA ')' error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ';'"));}
;

sentencia_control		: WHILE condicion_while DO bloque_sentencias ';' {
                                            String terceto_inc = AdminTercetos.pop();
											AdminTercetos.get(terceto_inc).completar(String.valueOf(AdminTercetos.cantTercetos() + 1));
											terceto_inc = AdminTercetos.pop();
											AdminTercetos.add(new Terceto("BI", "["+terceto_inc+"]"));
											if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia While."));
										}
                                | error condicion_while DO bloque_sentencias ';'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave WHILE."));}
                                | WHILE condicion_while error bloque_sentencias ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave DO."));}
                                | WHILE error DO bloque_sentencias ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de WHILE."));}
                                | WHILE condicion_while DO error ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el bloque de sentencias WHILE."));}
                                | WHILE error ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia WHILE."));}
				| WHILE condicion_while DO bloque_sentencias error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ';'."));}
;

condicion_while			: condicion {
                                         AdminTercetos.push(AdminTercetos.last().getId());
                                         AdminTercetos.add(new Terceto("BF", $1.sval));
                                         AdminTercetos.push(AdminTercetos.last().getId());
                                    }

;

sentencia_seleccion		: IF condicion_if bloque_then END_IF ';' {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia If.."));}
                                | IF condicion_if bloque_then ELSE bloque_else END_IF ';' {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia If-Else."));}
                                | error condicion_if bloque_then END_IF ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR al comienzo de la sentencia If."));}
                                | IF condicion_if bloque_then error ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR al final de la sentencia If."));}
                                | IF condicion_if error END_IF ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el bloque de sentencias IF."));}
                                | IF error bloque_then END_IF ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de IF."));}
                                | IF condicion_if bloque_then END_IF error{Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ';'."));}
                                | error condicion_if bloque_then ELSE bloque_else END_IF ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave IF."));}
                                | IF condicion_if bloque_then error bloque_else END_IF ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave ELSE."));}
                                | IF condicion_if bloque_then ELSE bloque_else error ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave END_IF."));}
                                | IF condicion_if error ELSE bloque_else END_IF ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque THEN de la sentencia IF."));}
                                | IF condicion_if bloque_then ELSE error END_IF ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque ELSE de la sentencia IF."));}
                                | IF error bloque_then ELSE bloque_else END_IF ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de la sentencia IF-ELSE."));}
                                | IF condicion_if bloque_then ELSE bloque_else END_IF error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ';'."));}
                                | IF error ';' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en sentencia IF."));}
;

condicion_if			: condicion {AdminTercetos.add(new Terceto("BF", $1.sval)); AdminTercetos.push(AdminTercetos.last().getId());}
;

bloque_then			: bloque_sentencias {	String terceto_inc = AdminTercetos.pop();
							AdminTercetos.get(terceto_inc).completar(String.valueOf(AdminTercetos.cantTercetos() + 2));
							AdminTercetos.add(new Terceto("BI"));
							AdminTercetos.push(AdminTercetos.last().getId());
						    }
;

bloque_else			: bloque_sentencias {	String terceto_inc = AdminTercetos.pop();
							AdminTercetos.get(terceto_inc).completar(String.valueOf(AdminTercetos.cantTercetos() + 1));
						    }
;

condicion			: '(' comparacion ')'          {$$ = $1;}
                                |  comparacion ')' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el caracter ("));}
                                | '(' comparacion  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el caracter )"));}
                                | comparacion {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR faltan ambos parentesis en la condicion"));}
                                | '(' ')' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion."));}
;

comparacion			: expresion comparador expresion {
                                                        Terceto t = crearTercetoOperacion($2.sval, $1.sval, $3.sval);
                                                        t.setType(tipos.pop());
                                                        $$ = new ParserVal(t);
                                                     }

                                | error comparador expresion  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el lado izquierdo de la comparacion."));}
                                | expresion error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta comparador."));}
                                | expresion comparador error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el lado derecho de la comparacion."));}
;

comparador		        : '<' { $$ = new ParserVal("<");}
				| '>' { $$ = new ParserVal(">");}
				| '=' { $$ = new ParserVal("=");}
				| MENOR_IGUAL { $$ = $1;}
				| MAYOR_IGUAL { $$ = $1;}
				| DISTINTO { $$ = $1;}
;

sentencia_asignacion 		: id ASIGN expresion ';' {
							    String tipo_id = SymbolTable.getLex($1.sval).getAttr("type");
							    boolean conversion = false;
							    String tipo_exp="";
							    if (!tipos.isEmpty())
							    	tipo_exp = tipos.pop();
							    if(tipo_id == "INT"){
							    	if(tipo_exp == "ULONG"){
							    		Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR tipos incompatibles."));
							    	}
							    }else{

								if (tipo_exp == "INT") {
									conversion = true;
									Terceto t = new Terceto("_CONV", $3.sval);
									t.setType("ULONG"); //TODO Verificar si es necesario setear el tipo aca.
									AdminTercetos.add(t);
							 	}
							    }
							    Terceto t = new Terceto(":=", $1.sval);
							    if(conversion)
							        t.completar(AdminTercetos.last().getId());
							    else
                                    t.completar($3.sval);

                                t.setType(tipo_id);
                                AdminTercetos.add(t);


							    if (this.verbose)
								Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Se encontro una sentencia Asign."));
                                    			}
                                | error ASIGN expresion ';'  {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el ID de la ."));}
                                | id ASIGN ';'    {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el lado derecho de la asignacion."));}
                                | id ASIGN error ';'    {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el lado derecho de la asignacion."));}

;

expresion			                : expresion '+' termino { $$ = new ParserVal(crearTercetoOperacion("+", $1.sval, $3.sval)); }
                                | expresion '-' termino { $$ = new ParserVal(crearTercetoOperacion("-", $1.sval, $3.sval)); }
				                | termino
                                | expresion '+' error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '+'"));}
                                | expresion '-' error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '-'"));}
;

termino				: termino '*' factor { $$ = new ParserVal(crearTercetoOperacion("*", $1.sval, $3.sval)); }
                                | termino '/' factor { $$ = new ParserVal(crearTercetoOperacion("/", $1.sval, $3.sval)); }
				| factor
                                | error '*' factor {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '*'"));}
                                | termino '*' error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '*"));}
                                | error '/' factor {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '/'"));}
                                | termino '/' error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '/'"));}
;

factor 				: id
				| cte
				| ID '.' funcion {	if(SymbolTable.getLex($1.sval).getAttr("use") == "COLECCION"){
								if($3.sval == "_length"){
									tipos.push("INT");
								}else{
									tipos.push(SymbolTable.getLex($1.sval).getAttr("type"));
								}
								AdminTercetos.add(new Terceto("call", $3.sval, $1.sval));
							}else{
								Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR " + $1.sval + " no es una coleccion."));
							}
						 }
;

funcion				: FIRST '(' ')' {$$ = new ParserVal("_first");}
                                | FIRST error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion FIRST."));}
                                | LAST '(' ')' {$$ = new ParserVal("_last");}
                                | LAST error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion LAST."));}
                                | LENGTH '(' ')' {$$ = new ParserVal("_length");}
                                | LENGTH error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion LENGTH."));}
                                | error '(' ')' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Funcion desconocida."));}
                                | error {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Funcion desconocida."));}
;

id 				: ID {
                                            if(SymbolTable.getLex($1.sval).getAttr("use") != null){
                                            	$$ = $1;
                                                tipos.push(SymbolTable.getLex($1.sval).getAttr("type"));
                                            }
                                            else
                                               Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + $1.sval + " no declarada."));
                                       }
				| ID '[' ID ']'{
						    Token coleccion = SymbolTable.getLex($1.sval);
						    Token tamaño = SymbolTable.getLex($3.sval);
						     if(coleccion.getAttr("use") != null){
							if(coleccion.getAttr("use") == "COLECCION"){
								if (tamaño.getAttr("use") != null){
								    if (tamaño.getAttr("type").equals("INT")) {
									$$ = new ParserVal($1.sval+"["+$3.sval+"]");
									tipos.push(coleccion.getAttr("type"));
								    }
								    else
									Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + $3.sval + " no es de tipo INT."));
								}
								else
								    Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + $3.sval + " no declarada."));
						     	}
						     	else
								Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR " + $1.sval + " no es una coleccion."));
						     }
						     else
                                                        Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + $1.sval + " no declarada."));
                                                }
				| ID '[' cte ']'{
							Token coleccion = SymbolTable.getLex($1.sval);
					    		Token tamaño = SymbolTable.getLex($3.sval);
					       		if(coleccion.getAttr("use") != null){
					       		  if(coleccion.getAttr("use") == "COLECCION"){
								if (tamaño.getAttr("use") != null){
							      		if (tamaño.getAttr("type").equals("INT")){
								  		$$ = new ParserVal($1.sval+"["+$3.sval+"]");
								  		tipos.pop(); //elimino el tipo de indice
							  			tipos.push(coleccion.getAttr("type"));
							      		} else
								  		Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + $3.sval + " no es de tipo INT."));
							  	} else
							      		Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + $3.sval + " no declarada."));
					       		  } else {
					       		  	Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR " + $1.sval + " no es una coleccion."));
					       		  }
						       }
						       else
							  Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Variable " + $1.sval + " no declarada."));
						  }
                                | ID '[' error ']' {Error.add(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el subindice de la coleccion. Se esperaba un INT."));}
;

cte 				: CTE {
                                            String cte = $1.sval;
                                            check_range(cte, false);
                                            $$ = $1;
                                        }
				| '-' CTE {
						String cte = $2.sval;
						check_range(cte, true);
						$$ = new ParserVal("-" + cte);
                                           }
;


%%

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
	String tipo = tipos.pop();

	if (conv == 1)
		arg1 = AdminTercetos.last().getId();
	if (conv == 2)
		arg2 = AdminTercetos.last().getId();
	Terceto terceto = new Terceto(op, arg1, arg2);
	terceto.setType(tipo);

	tipos.push(tipo);
	AdminTercetos.add(terceto);
	return terceto.getId();
}