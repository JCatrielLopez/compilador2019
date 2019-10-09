%{package compilador2019;
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

programa 	                    :	lista_sentencias_declarativas
			                    | 	bloque_sentencias_ejecutables
			                    | 	lista_sentencias_declarativas bloque_sentencias_ejecutables
;

lista_sentencias_declarativas	:	lista_sentencias_declarativas sentencia_declarativa
								|	sentencia_declarativa
;

bloque_sentencias_ejecutables	:	BEGIN lista_sentencias_ejecutables END ';'
                                |   BEGIN lista_sentencias_ejecutables error ';'  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: END"), Color.RED);}
                                |   BEGIN lista_sentencias_ejecutables END error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
;

lista_sentencias_ejecutables	:	lista_sentencias_ejecutables sentencia_ejecutable
								|	sentencia_ejecutable
;

sentencia_declarativa 			:	tipo lista_variables ';'
                                |   lista_variables ';' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Type not declared"), Color.RED);}
                                |   tipo lista_variables error  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
;

tipo							: 	INT
								|	ULONG
								//|   error   {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Unknown type declared."), Color.RED);}
;

lista_variables					:	lista_variables ',' ID
								|	lista_variables ',' coleccion
								|	ID
								|	coleccion
;

coleccion						:	ID '[' cte ']'
                                |   ID '[' cte   {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ["), Color.RED);}
                                |   ID cte ']'   {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ]"), Color.RED);}
;

sentencia_ejecutable 			:	sentencia_asignacion
								| 	sentencia_seleccion
								|	sentencia_control
								|	sentencia_impresion
;

sentencia_impresion				:   PRINT '(' CADENA ')' ';'
                                |   error '(' CADENA ')' ';'  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: PRINT"), Color.RED);}
                                |   PRINT  CADENA ')' ';'   {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
                                |   PRINT '(' CADENA  ';'   {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
                                |   PRINT '(' CADENA ')' error  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
                                |   PRINT '(' error ')' ';'  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Character chain not declared."), Color.RED);}
;

sentencia_control				:	WHILE condicion DO bloque_sentencias_ejecutables ';'
                                |   error condicion DO bloque_sentencias_ejecutables ';'  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: WHILE"), Color.RED);}
                                |   WHILE condicion error bloque_sentencias_ejecutables ';' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: DO"), Color.RED);}
                                |   WHILE condicion DO bloque_sentencias_ejecutables error    {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
;

sentencia_seleccion				:	IF condicion bloque_sentencias_ejecutables END_IF ';'
                                |   IF condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF ';'
                                |   error condicion bloque_sentencias_ejecutables END_IF ';'    {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: IF"), Color.RED);}
                                |   IF condicion bloque_sentencias_ejecutables error ';'        {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: END_IF"), Color.RED);}
                                |   IF condicion bloque_sentencias_ejecutables END_IF error     {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
                                |   error condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF ';' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: IF"), Color.RED);}
                                |   IF condicion bloque_sentencias_ejecutables error bloque_sentencias_ejecutables END_IF ';'   {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: ELSE"), Color.RED);}
                                |   IF condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables error ';'     {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: END_IF"), Color.RED);}
                                |   IF condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF error  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
;

condicion						:	'(' comparacion ')'
                                |    comparacion ')' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
                                |   '(' comparacion  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
                                |   comparacion {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing parenthesis in condition"), Color.RED);}
                                |   error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Condition not declared."), Color.RED);}
;

comparacion						:	expresion comparador expresion
                                |   error comparador expresion    {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term in operation."), Color.RED);}
                                |   expresion error expresion  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing comparator in operation."), Color.RED);}
                                |   expresion comparador error     {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term in operation."), Color.RED);}
;

comparador		            	:   '<'
							  	|   '>'
							  	|   '='
							  	|   MENOR_IGUAL
							  	|   MAYOR_IGUAL
							  	|   DISTINTO
;

sentencia_asignacion 			:	id ASIGN expresion ';'
                                |   error ASIGN expresion ';'  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing ID on assign."), Color.RED);}
                                //|   id expresion ';' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: :="), Color.RED);} //TODO Por que esta comentada esta regla?
                                |   id ASIGN error ';'    {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR right-hand term on assign."), Color.RED);}
                                |   id ASIGN expresion  error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
;

expresion						:	expresion '+' termino
                                |   error '+' termino {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '+' operation."), Color.RED);}
                                |   expresion '+' error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '+' operation."), Color.RED);}
                                |   error '-' termino {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '-' operation."), Color.RED);}
                                |   expresion '-' error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '-' operation."), Color.RED);}
								| 	termino
;

termino							:	termino '*' factor
                                |   error '*' factor {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '*' operation."), Color.RED);}
                                |   termino '*' error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '*' operation."), Color.RED);}
                                |   error '/' factor {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '/' operation."), Color.RED);}
                                |   termino '/' error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '/' operation."), Color.RED);}
								| 	factor
;

factor 							: 	id
								| 	cte
								|	ID '.' funcion
;

funcion							:	FIRST '(' ')'
                                |   FIRST error ')' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
                                |   FIRST '(' error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
                                |   LAST '(' ')'
                                |   LAST error ')' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
                                |   LAST '(' error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
                                |	LENGTH '(' ')'
                                |   LENGTH error ')' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
                                |   LENGTH '(' error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
                                |   error '(' ')' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Unknown function."), Color.RED);}
;

id 								:	ID
								|	ID '[' ID ']'
                                |	ID ID ']'   {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ["), Color.RED);}
                                |	ID '[' ID error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ]"), Color.RED);}
								|	ID '[' cte ']'
                                |   ID '[' error ']' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing index on array"), Color.RED);}
;

cte 							:	CTE
								| 	'-' CTE
;


%%

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