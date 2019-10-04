%{package analizadorSintactico;
  import analizadorLexico.*;
  import globales.*;
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

programa 	:	lista_sentencias_declarativas
			| 	bloque_sentencias_ejecutables
			| 	lista_sentencias_declarativas bloque_sentencias_ejecutables
;

lista_sentencias_declarativas	:	lista_sentencias_declarativas sentencia_declarativa
								|	sentencia_declarativa
;

bloque_sentencias_ejecutables	:	BEGIN lista_sentencias_ejecutables END ';'
;

lista_sentencias_ejecutables	:	lista_sentencias_ejecutables sentencia_ejecutable
								|	sentencia_ejecutable
;

sentencia_declarativa 			:	tipo lista_variables ';'

tipo							: 	INT
								|	ULONG
;

lista_variables					:	lista_variables ',' ID
								|	lista_variables ',' coleccion
								|	ID
								|	coleccion
;

coleccion						:	ID '[' cte ']'
;

sentencia_ejecutable 			:	sentencia_asignacion
								| 	sentencia_seleccion
								|	sentencia_control
								|	sentencia_impresion
;

sentencia_impresion				:	PRINT '(' CADENA ')' ';'
;

sentencia_control				:	WHILE condicion DO bloque_sentencias_ejecutables ';'
;

sentencia_seleccion				:	IF condicion bloque_sentencias_ejecutables END_IF ';'
								|	IF condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF ';'
;

condicion						:	'(' comparacion ')'
;

comparacion						:	expresion comparador expresion
;

comparador		            	: '<'
							  	| '>'
							  	| '='
							  	| MENOR_IGUAL
							  	| MAYOR_IGUAL
							  	| DISTINTO
;

sentencia_asignacion 			:	id ASIGN expresion ';'
;

expresion						:	expresion '+' termino
								|	expresion '-' termino
								| 	termino
;

termino							:	termino '*' factor
								|	termino '/' factor
								| 	factor
;

factor 							: 	id
								| 	cte
								|	ID '.' funcion
;

funcion							:	FIRST '(' ')'
								|	LAST '(' ')'
								|	LENGTH '(' ')'

id 								:	ID
								|	ID '[' ID ']'
								|	ID '[' cte ']'
;

cte 							:	CTE
								| 	'-' CTE
;


%%