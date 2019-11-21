.386
include \masm32\include\masm32rt.inc
dll_dllcrt0 PROTO C
printf PROTO C :VARARG

.data

;Datos del programa.
cte1 dw 1
_a dw 3, 10, 20, 30
_b dd 4, 100, 200, 300, 400
_c dw ?
cte2 dw 5

;Variables predefinidas.
@resultado_16 dw ?
@resultado_32 dd ?
@coleccion dd ?
@indice dw ?
@tipo dd ?
@offset dd ?
ConvError db "Error, perdida de informacion en conversion.", 0
IndiceError db "Error, indice fuera de los limites de la coleccion.", 0
@tempEAX dd ?
@tempEBX dd ?
@tempECX dd ?
@tempEDX dd ?
.code

error_negativo:
invoke MessageBox, NULL, addr ConvError, addr ConvError, MB_OK
invoke ExitProcess, 0

error_indice:
invoke MessageBox, NULL, addr IndiceError, addr IndiceError, MB_OK
invoke ExitProcess, 0

_length:
MOV @tempEAX, EAX
MOV @tempEBX, EBX
MOV EAX, @coleccion
MOV BX, [EAX]
MOV @resultado_16, BX
MOV EAX, @tempEAX
MOV EBX, @tempEBX
RET

_first:
MOV @tempEAX, EAX
MOV EAX, @coleccion
ADD EAX, @tipo
MOV @offset, EAX
CMP @tipo, 2
JNE F_Resultado32
CALL _setResult16
JMP F_end
F_Resultado32:
CALL _setResult32
MOV EAX, @tempEAX
F_end:
RET

_last:
MOV @tempEAX, EAX
MOV @tempEBX, EBX
MOV EAX, @coleccion
MOV EBX, 0              ;pongo 0 por si habia algo
MOV BX, [EAX]           ;length
IMUL EBX, @tipo         ;calculo el offset
ADD EAX, EBX
MOV @offset, EAX
CMP @tipo, 2
JNE L_Resultado32
CALL _setResult16
JMP L_end
L_Resultado32:
CALL _setResult32
MOV EAX, @tempEAX
MOV EBX, @tempEBX
L_end:
RET

_element:
CALL _offset
CMP @tipo, 2
JNE E_Resultado32
CALL _setResult16
JMP E_end
E_Resultado32:
CALL _setResult32
E_end:
RET

_offset:
MOV @tempEAX, EAX
MOV @tempEBX, EBX
MOV @tempEBX, EDX
MOV EAX, @coleccion
MOV EDX, 0
MOV DX, @indice
CMP DX, 0               ;controlo q indice no sea negativo
JL error_indice
ADD DX, 1               ;agrego +1 al indice
MOV EBX, 0
MOV BX, [EAX]           ;length
CMP DX, BX              ;controlo q sea <=
JG error_indice
IMUL EDX, @tipo         ;calculo el offset
ADD EAX, EDX
MOV @offset, EAX
MOV EAX, @tempEAX
MOV EBX, @tempEBX
MOV EDX, @tempEDX
RET

_setResult16:
MOV @tempEAX, EAX
MOV @tempEAX, EBX
MOV EAX, @offset
MOV BX, [EAX]
MOV @resultado_16, BX
MOV EAX, @tempEAX
MOV EAX, @tempEBX
RET

_setResult32:
MOV @tempEAX, EAX
MOV @tempEAX, EBX
MOV EAX, @offset
MOV EBX, [EAX]
MOV @resultado_32, EBX
MOV EAX, @tempEAX
MOV EAX, @tempEBX
RET

_rowing:
MOV @tempEAX, EAX             ;Posicion del indice donde voy a asignar el @valor_asignacion
MOV @tempEBX, EBX             ;Contador de posiciones. Puede que este de mas, no se si puedo usar una variable en el .while
MOV EAX, @indice
MOV EBX, @coleccion
MOV @indice, [EBX]

r_while:
CMP @indice, 0
JL r_end
_call offset
MOV ECX, @offset
MOV [ECX], @valor_asignacion
SUB @indice, 1
JUMP r_while

r_end:
RET

start:

;Seteamos los tamaños de las colecciones.
;MOV [_a], 5
;Fin de seteo de tamaños.

LEA EAX, [_a]
MOV @coleccion, EAX
MOV @tipo, 2
MOV @indice, 1
;CALL _length
;invoke printf, cfm$("%. %d\n"), @resultado_16
CALL _element
invoke printf, cfm$("%. %d\n"), @resultado_16
;invoke printf, cfm$("%. %d\n"), @resultado_32
;CALL _last
;invoke printf, cfm$("%. %d\n"), @resultado_16
;CALL _element
;invoke printf, cfm$("%. %d\n"), @resultado_16

;CALL _offset
;MOV AX, 99
;MOV EDX, @offset
;MOV [EDX], AX
;CALL _element
;invoke printf, cfm$("%. %d\n"), @resultado_16

invoke ExitProcess, 0

end start