
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

programa 	                    :	lista_sentencias_declarativas {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Programa OK"), Color.RESET);}
			                    | 	bloque_sentencias_ejecutables {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Programa OK (E)"), Color.RESET);}
			                    | 	lista_sentencias_declarativas bloque_sentencias_ejecutables {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Programa OK (D y E)"), Color.RESET);}
                                |   error  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en programa"), Color.RED);}
;

lista_sentencias_declarativas	:	lista_sentencias_declarativas sentencia_declarativa
								|	sentencia_declarativa
;

bloque_sentencias               :   bloque_sentencias_ejecutables
                                |   sentencia_ejecutable
;

bloque_sentencias_ejecutables	:	BEGIN lista_sentencias_ejecutables END ';'
                                |   error lista_sentencias_ejecutables END ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave BEGIN."), Color.RED);}
                                |   BEGIN lista_sentencias_ejecutables error ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave END."), Color.RED);}
                                |   error lista_sentencias_ejecutables error  ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan los delimitadores de bloque."), Color.RED);}
                                |   BEGIN error END ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque de ejecucion."), Color.RED);}
;

lista_sentencias_ejecutables	:	lista_sentencias_ejecutables sentencia_ejecutable
								|	sentencia_ejecutable
;

sentencia_declarativa 			:	tipo lista_variables ';'
                                |   lista_variables ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Tipo no declarado."), Color.RED);}
                                |   error lista_variables ';'{Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Tipo no reconocido."), Color.RED);}
                                |   tipo error ';'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en lista de variables."), Color.RED);}
                                |   error ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia declarativa."), Color.RED);}
;

tipo							: 	INT
								|	ULONG
;

lista_variables					:	lista_variables ',' ID
								|	lista_variables ',' coleccion
								|	ID
								|	coleccion
;

coleccion						:	ID '[' cte ']'
                                |   ID '[' ']'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la declaracion de la coleccion."), Color.RED);}
                                |   ID '[' error ']'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la declaracion de la coleccion."), Color.RED);}
;

sentencia_ejecutable 			:	sentencia_asignacion
								| 	sentencia_seleccion
								|	sentencia_control
								|	sentencia_impresion
;

sentencia_impresion				:   PRINT '(' CADENA ')' ';' {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia PRINT OK."), Color.RESET);}
                                |   error '(' CADENA ')' ';'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave PRINT."), Color.RED);}
                                |   PRINT  CADENA ')' ';'   {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal '('."), Color.RED);}
                                |   PRINT '(' CADENA  ';'   {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el literal ')'."), Color.RED);}
                                |   PRINT '(' ')' ';'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la cadena a imprimir."), Color.RED);}
                                |   PRINT '(' error ')' ';'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la cadena a imprimir."), Color.RED);}
                                |   PRINT '(' error ';'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la cadena a imprimir."), Color.RED);}
                                |   PRINT error ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia PRINT."), Color.RED);}
;

sentencia_control				:	WHILE condicion DO bloque_sentencias ';' {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia WHILE OK"), Color.RESET);}
                                |   error condicion DO bloque_sentencias ';'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave WHILE."), Color.RED);}
                                |   WHILE condicion error bloque_sentencias ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave DO."), Color.RED);}
                                |   WHILE error DO bloque_sentencias ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de WHILE."), Color.RED);}
                                |   WHILE condicion DO error ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia WHILE."), Color.RED);}
                                |   WHILE error ';' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la sentencia WHILE."), Color.RED);}
;

sentencia_seleccion				:	IF condicion bloque_sentencias END_IF ';' {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia IF OK."), Color.RESET);}
                                |   IF condicion bloque_sentencias ELSE bloque_sentencias END_IF ';' {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia IF-ELSE OK."), Color.RESET);}
                                |   error condicion bloque_sentencias END_IF ';'    {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en sentencia IF."), Color.RED);}
                                |   IF condicion bloque_sentencias error ';'        {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en sentencia IF."), Color.RED);}
                                |   IF condicion error END_IF ';'       {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque IF."), Color.RED);}
                                |   IF error bloque_sentencias END_IF ';'       {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion de IF."), Color.RED);}
                                |   error condicion bloque_sentencias ELSE bloque_sentencias END_IF ';'   {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave IF."), Color.RED);}
                                |   IF condicion bloque_sentencias error bloque_sentencias END_IF ';'     {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave ELSE."), Color.RED);}
                                |   IF condicion bloque_sentencias ELSE bloque_sentencias error ';'       {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta la palabra clave END_IF."), Color.RED);}
                                |   IF condicion error ELSE bloque_sentencias END_IF ';'    {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque IF."), Color.RED);}
                                |   IF condicion bloque_sentencias ELSE error END_IF ';'    {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en bloque ELSE."), Color.RED);}
                                |   IF error bloque_sentencias ELSE bloque_sentencias END_IF ';'    {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion."), Color.RED);}
                                |   IF error ';'    {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en sentencia IF."), Color.RED);}
;

condicion						:	'(' comparacion ')'
                                |    comparacion ')' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el caracter ("), Color.RED);}
                                |   '(' comparacion  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el caracter )"), Color.RED);}
                                |   comparacion {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR faltan parentesis en la condicion"), Color.RED);}
                                |   '(' ')' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en la condicion."), Color.RED);}
;

comparacion						:	expresion comparador expresion
                                |   error comparador expresion    {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la comparacion."), Color.RED);}
                                |   expresion error expresion  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la comparacion."), Color.RED);}
                                |   expresion comparador error     {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la comparacion."), Color.RED);}
;

comparador		            	:   '<'
							  	|   '>'
							  	|   '='
							  	|   MENOR_IGUAL
							  	|   MAYOR_IGUAL
							  	|   DISTINTO
;

sentencia_asignacion 			:	id ASIGN expresion ';' {if (this.verbose) Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "Sentencia ASIGN OK."), Color.RESET);}
                                |   error ASIGN expresion ';'  {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el ID de la asignacion."), Color.RED);}
                                |   id ASIGN ';'    {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta el lado derecho de la asignacion."), Color.RED);}
                                |   id ASIGN error ';'    {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el lado derecho de la asignacion."), Color.RED);}

;

expresion						:	expresion '+' termino
                                |   expresion '-' termino
								| 	termino
                                |   expresion '+' error {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '+'"), Color.RED);}
                                |   expresion '-' error {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '-'"), Color.RED);}
;

termino							:	termino '*' factor
                                |   termino '/' factor
								| 	factor
                                |   error '*' factor {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '*'"), Color.RED);}
                                |   termino '*' error {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '*"), Color.RED);}
                                |   error '/' factor {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '/'"), Color.RED);}
                                |   termino '/' error {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Falta un termino en la operacion '/'"), Color.RED);}
;

factor 							: 	id
								| 	cte
								|	ID '.' funcion
;

funcion							:	FIRST '(' ')'
                                |   FIRST error {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion FIRST."), Color.RED);}
                                |   LAST '(' ')'
                                |   LAST error {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion LAST."), Color.RED);}
                                |	LENGTH '(' ')'
                                |   LENGTH error {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Faltan parentesis en funcion LENGTH."), Color.RED);}
                                |   error '(' ')' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Funcion desconocida."), Color.RED);}
                                |   error {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR Funcion desconocida."), Color.RED);}
;

id 								:	ID
								|	ID '[' ID ']'
								|	ID '[' cte ']'
                                |   ID '[' error ']' {Printer.print(String.format("%5s %s %3s %s %s", al.getLineNumber(), "|", "AS", "|", "ERROR en el subindice de la coleccion."), Color.RED);}
;

cte 							:	CTE
								| 	'-' CTE {String cte = $2.sval;
								             check_range($2.sval);}
;


%%

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