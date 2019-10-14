
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

programa 	                    :	lista_sentencias_declarativas {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "programa OK (delcarativas solas)"), Color.YELLOW);}
			                    | 	bloque_sentencias_ejecutables {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "programa OK (bloque ejecutables)"), Color.YELLOW);}
			                    | 	lista_sentencias_declarativas bloque_sentencias_ejecutables {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "programa OK (declarativas y ejecutables)"), Color.YELLOW);}
                                |   error  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "programa error"), Color.YELLOW);}
;

lista_sentencias_declarativas	:	lista_sentencias_declarativas sentencia_declarativa
								|	sentencia_declarativa
;

bloque_sentencias_ejecutables	:	BEGIN lista_sentencias_ejecutables END ';'
                                |   lista_sentencias_ejecutables END ';' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: BEGIN"), Color.RED);}
                                |   error lista_sentencias_ejecutables END ';' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: BEGIN"), Color.RED);}
                                |   BEGIN lista_sentencias_ejecutables END error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
                                //|   lista_sentencias_ejecutables {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Falta delimitadores del bloque (begin/end)"), Color.RED);}
                                
;

lista_sentencias_ejecutables	:	lista_sentencias_ejecutables sentencia_ejecutable
								|	sentencia_ejecutable
;

sentencia_declarativa 			:	tipo lista_variables ';'
                                |   lista_variables ';' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Type not declared"), Color.RED);}
                                |   error lista_variables ';'{Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Unknown type declared."), Color.RED);}
                                |   tipo lista_variables error  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
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
                                |   ID '[' ']'  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing colection size"), Color.RED);}
                                |   ID '[' error ']'  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in colection size"), Color.RED);}
;

sentencia_ejecutable 			:	sentencia_asignacion
								| 	sentencia_seleccion
								|	sentencia_control
								|	sentencia_impresion
;

sentencia_impresion				:   PRINT '(' CADENA ')' ';' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio una impresion OK"), Color.YELLOW);}
                                |   error '(' CADENA ')' ';'  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: PRINT"), Color.RED);}
                                |   PRINT  CADENA ')' ';'   {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
                                |   PRINT '(' CADENA  ';'   {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
                                |   PRINT '(' CADENA ')' error  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
                                |   PRINT '(' ')' ';'  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing chain."), Color.RED);}
                                |   PRINT error  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in argument."), Color.RED);}
                                |   PRINT '(' error  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in argument."), Color.RED);}
                                |   PRINT '(' error ')' ';'  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Character chain not declared."), Color.RED);}
;

sentencia_control				:	WHILE condicion DO bloque_sentencias_ejecutables ';' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio un WHILE OK"), Color.YELLOW);}
                                |   error condicion DO bloque_sentencias_ejecutables ';'  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: WHILE"), Color.RED);}
                                |   WHILE condicion error bloque_sentencias_ejecutables ';' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: DO"), Color.RED);}
                                |   WHILE condicion DO bloque_sentencias_ejecutables error    {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
                                |   WHILE error DO bloque_sentencias_ejecutables ';' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in condition"), Color.RED);}
                                |   WHILE condicion DO error ';' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in while"), Color.RED);}
;

sentencia_seleccion				:	IF condicion bloque_sentencias_ejecutables END_IF ';' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio un IF OK"), Color.YELLOW);}
                                |   IF condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF ';' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio un IF OK"), Color.YELLOW);}
                                |   error condicion bloque_sentencias_ejecutables END_IF ';'    {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: IF"), Color.RED);}
                                |   IF condicion bloque_sentencias_ejecutables error ';'        {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: END_IF"), Color.RED);}
                                |   IF condicion bloque_sentencias_ejecutables END_IF error     {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
                                |   IF condicion error END_IF ';'     {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in bloque IF"), Color.RED);}
                                |   IF error bloque_sentencias_ejecutables END_IF ';'     {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in condition"), Color.RED);}
                                |   error condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF ';' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: IF"), Color.RED);}
                                |   IF condicion bloque_sentencias_ejecutables error bloque_sentencias_ejecutables END_IF ';'   {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: ELSE"), Color.RED);}
                                |   IF condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables error ';'     {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing keyword: END_IF"), Color.RED);}
                                |   IF condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF error  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
                                |   IF condicion error ELSE bloque_sentencias_ejecutables END_IF ';'  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in bloque IF"), Color.RED);}
                                |   IF condicion bloque_sentencias_ejecutables ELSE error END_IF ';'  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in bloque ELSE"), Color.RED);}
                                |   IF condicion error ELSE error END_IF ';'  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in bloque IF and ELSE"), Color.RED);}
                                |   IF error bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF ';'  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in condition"), Color.RED);}
;

condicion						:	'(' comparacion ')'
                                |    comparacion ')' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ("), Color.RED);}
                                |   '(' comparacion  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: )"), Color.RED);}
                                |   comparacion {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing parenthesis in condition"), Color.RED);}
                                |   '(' ')' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR in condition."), Color.RED);}
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

sentencia_asignacion 			:	id ASIGN expresion ';' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "Se reconocio una asignacion    OK"), Color.YELLOW);}
                                |   ASIGN expresion ';'  {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing ID on assign."), Color.RED);}
                                |   id ASIGN ';'    {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing right-hand term on assign."), Color.RED);}
                                
                                |   id ASIGN expresion error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing character: ;"), Color.RED);}
                                |   id ASIGN error ';'    {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR right-hand term on assign."), Color.RED);}
                                
;

expresion						:	expresion '+' termino
                                |   expresion '-' termino
								| 	termino
                                |   expresion '+' error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '+' operation."), Color.RED);}
                                |   expresion '-' error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '-' operation."), Color.RED);}
;

termino							:	termino '*' factor
                                |   termino '/' factor
								| 	factor
                                |   error '*' factor {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '*' operation."), Color.RED);}
                                |   termino '*' error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '*' operation."), Color.RED);}
                                |   error '/' factor {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '/' operation."), Color.RED);}
                                |   termino '/' error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing term on '/' operation."), Color.RED);}
;

factor 							: 	id
								| 	cte
								|	ID '.' funcion
;

funcion							:	FIRST '(' ')'
                                |   FIRST error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing characters: ( or ))"), Color.RED);}
                                |   LAST '(' ')'
                                |   LAST error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing characters: ( or ))"), Color.RED);}
                                |	LENGTH '(' ')'
                                |   LENGTH error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Missing characters: ( or ))"), Color.RED);}
                                |   error '(' ')' {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Unknown function."), Color.RED);}
                                |   error {Printer.print(String.format("%5s %s %s", al.getLineNumber(), "|", "ERROR Unknown function."), Color.RED);}
;

id 								:	ID
								|	ID '[' ID ']'
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
    System.out.println("Linea " + al.getLineNumber() + ": (Parser) " + s);
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