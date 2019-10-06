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

programa 	                    :	lista_sentencias_declarativas
			                    | 	bloque_sentencias_ejecutables
			                    | 	lista_sentencias_declarativas bloque_sentencias_ejecutables
;

lista_sentencias_declarativas	:	lista_sentencias_declarativas sentencia_declarativa
								|	sentencia_declarativa
;

bloque_sentencias_ejecutables	:	BEGIN lista_sentencias_ejecutables END ';'
                                |   BEGIN lista_sentencias_ejecutables error ';'  //{TODO Falta el END}
                                |   BEGIN lista_sentencias_ejecutables END error //{TODO Falta el ;}
;

lista_sentencias_ejecutables	:	lista_sentencias_ejecutables sentencia_ejecutable
								|	sentencia_ejecutable
;

sentencia_declarativa 			:	tipo lista_variables ';'
                                |   lista_variables ';' //{TODO Falta el tipo}
                                |   tipo lista_variables error  //{TODO Falta el ;}

tipo							: 	INT
								|	ULONG
								|   error   //{TODO Un tipo no definido?}
;

lista_variables					:	lista_variables ',' ID
								|	lista_variables ',' coleccion
								|	ID
								|	coleccion
;

coleccion						:	ID '[' cte ']'
                                |   ID cte ']'  //{TODO Falta el segundo corchete}
                                |   ID '[' cte  //{TODO Falta el primer corchete}
;

sentencia_ejecutable 			:	sentencia_asignacion
								| 	sentencia_seleccion
								|	sentencia_control
								|	sentencia_impresion
;

sentencia_impresion				:   PRINT '(' CADENA ')' ';'
                                |   error '(' CADENA ')' ';' //{TODO Falta la palabra clave PRINT}
                                |   PRINT  CADENA ')' ';'  //{TODO Falta el primer parentesis}
                                |   PRINT '(' error ')' ';' //{TODO Falta se esperaba uan cadena}
                                |   PRINT '(' CADENA  ';'  //{TODO Falta el segundo parentesis}
                                |   PRINT '(' CADENA ')' error //{TODO Falta el ;}
;

sentencia_control				:	WHILE condicion DO bloque_sentencias_ejecutables ';'
                                |   error condicion DO bloque_sentencias_ejecutables ';' //{TODO falta el while}
                                |   WHILE condicion error bloque_sentencias_ejecutables ';' //{TODO Falta el DO}
                                |   WHILE condicion DO bloque_sentencias_ejecutables error    {TODO Falta el ;}
;

sentencia_seleccion				:	IF condicion bloque_sentencias_ejecutables END_IF ';'
                                |   IF condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF ';'
                                |   error condicion bloque_sentencias_ejecutables END_IF ';' //{TODO falta el IF}
                                |   IF condicion bloque_sentencias_ejecutables error ';'  //{TODO Falta el END_IF}
                                |   IF condicion bloque_sentencias_ejecutables END_IF error //{TODO Falta el ;}
                                |   error condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF ';' //{TODO Falta el IF}
                                |   IF condicion bloque_sentencias_ejecutables error bloque_sentencias_ejecutables END_IF ';' //{TODO Falta el ELSE}
                                |   IF condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables error ';' //{TODO Falta el END_IF}
                                |   IF condicion bloque_sentencias_ejecutables ELSE bloque_sentencias_ejecutables END_IF error //{TODO Falta el ;}
;

condicion						:	'(' comparacion ')'
                                |    comparacion ')' //{TODO Falta el primer parentesis}
                                |   '(' comparacion  //{TODO Falta el segundo parentesis}
                                |   comparacion //{TODO faltan ()}
                                |   error //{TODO se esperaba una condicion}
;

comparacion						:	expresion comparador expresion
                                |   error comparador expresion    //{TODO Falta un operando}
                                |   expresion error expresion //{TODO Falta el compradaor}
                                |   expresion comparador error    //{TODO Falta un operando}
;

comparador		            	:   '<'
							  	|   '>'
							  	|   '='
							  	|   MENOR_IGUAL
							  	|   MAYOR_IGUAL
							  	|   DISTINTO
;

sentencia_asignacion 			:	id ASIGN expresion ';'
                                |   error ASIGN expresion ';' //{TODO Falta el id}
                                //|   id expresion ';' //{TODO falta el operador de asignacion}
                                |   id ASIGN error ';'    //{TODO Falta una expresion}  
                                |   id ASIGN expresion  error //{TODO Falta el ;}
;

expresion						:	expresion '+' termino
                                |   error '+' termino //{TODO Falta el primer operando}
                                |   expresion '+' error //{TODO Falta el segundo operando}
                                |   error '-' termino //{TODO Falta el primer operando}
                                |   expresion '-' error //{TODO Falta el segundo operando}
								| 	termino
;

termino							:	termino '*' factor
                                |   error '*' factor //{TODO Falta el primer operando}
                                |   termino '*' error //{TODO Falta el segundo operando}
                                |   error '/' factor //{TODO Falta el primer operando}
                                |   termino '/' error //{TODO Falta el segundo operando}
								| 	factor
;

factor 							: 	id
								| 	cte
								|	ID '.' funcion
;

funcion							:	FIRST '(' ')'
                                |   FIRST error ')' //{TODO Falta el primer parentesis}
                                |   FIRST '(' error //{TODO Falta el segundo parentesis}
                                |   LAST '(' ')'
                                |   LAST error ')' //{TODO Falta el primer parentesis}
                                |   LAST '(' error //{TODO Falta el segundo parentesis}
                                |	LENGTH '(' ')'
                                |   LENGTH error ')' //{TODO Falta el primer parentesis}
                                |   LENGTH '(' error //{TODO Falta el segundo parentesis}
                                |   error '(' ')' //{TODO funcion inexistente}
;

id 								:	ID
								|	ID '[' ID ']'
                                |	ID '[' ID error //{TODO Falta el segundo corchete}
                                |	ID ID ']'   //{TODO Falta el primer corchete}
								|	ID '[' cte ']'
                                |   ID '[' error ']' //{TODO Falta el ID o cte}
;

cte 							:	CTE
								| 	'-' CTE
;


%%