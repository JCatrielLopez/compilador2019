.386
include \masm32\include\masm32rt.inc
dll_dllcrt0 PROTO C
printf PROTO C :VARARG

.data

;Datos del programa.
cte1 dw 1
_a dw 3, 10, 20, 30
_b dd 4, -100, -200, -300, -400
_c dw ?
cte2 dw 5

;Variables predefinidas.
@resultado_16 dw ?
@resultado_32 dd ?
@coleccion dd ?
@indice dw ?
@tipo dd ?
@offset dd ?
@valor_asignacion_16 dw ?
@valor_asignacion_32 dd ?
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
MOV @tempEBX, EBX
MOV EAX, @coleccion
ADD EAX, @tipo
MOV EBX, [EAX]
MOV @resultado_16, BX
MOV @resultado_32, EBX
MOV EAX, @tempEAX
MOV EBX, @tempEBX
RET

_last:
MOV @tempEAX, EAX
MOV @tempEBX, EBX
MOV EAX, @coleccion
MOV EBX, 0              ;pongo 0 por si habia algo
MOV BX, [EAX]           ;length
IMUL EBX, @tipo         ;calculo el offset
ADD EAX, EBX
MOV EBX, [EAX]
MOV @resultado_16, BX
MOV @resultado_32, EBX
MOV EAX, @tempEAX
MOV EBX, @tempEBX
RET

_element:
MOV @tempEAX, EAX
MOV @tempEBX, EBX
CALL _offset
MOV EAX, @offset
MOV EBX, [EAX]
MOV @resultado_16, BX
MOV @resultado_32, EBX
MOV EAX, @tempEAX
MOV EBX, @tempEBX
RET

_offset:
MOV @tempEAX, EAX
MOV @tempEBX, EBX
MOV @tempEDX, EDX
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

_rowing:
MOV @tempEAX, EAX
MOV @tempEBX, EBX
MOV @tempECX, ECX
CALL _length
MOV AX, @resultado_16
SUB AX, 1
MOV EBX, @coleccion
MOV @indice, AX
r_while:
CMP @indice, 0
JL r_end
CALL _offset
MOV ECX, @offset
CMP @tipo, 2
JNE R_asignacion32
MOV AX, @valor_asignacion_16
MOV [ECX], AX
JMP R_out
R_asignacion32:
MOV EAX, @valor_asignacion_32
MOV [ECX], EAX
R_out:
SUB @indice, 1
JMP r_while
r_end:
MOV EAX, @tempEAX
MOV EBX, @tempEBX
MOV ECX, @tempECX
RET

start:

;Seteamos los tamaños de las colecciones.
;MOV [_a], 5
;Fin de seteo de tamaños.

LEA EAX, [_a]
MOV @coleccion, EAX
MOV @tipo, 2
MOV @indice, 2
;CALL _length
;invoke printf, cfm$("%. %d\n"), @resultado_16
;CALL _first
;invoke printf, cfm$("%. %d\n"), @resultado_16
;invoke printf, cfm$("%. %d\n"), @resultado_32
;CALL _last
;invoke printf, cfm$("%. %d\n"), @resultado_16
;invoke printf, cfm$("%. %d\n"), @resultado_32
;CALL _element
;invoke printf, cfm$("%. %d\n"), @resultado_16
;invoke printf, cfm$("%. %d\n"), @resultado_32

;CALL _offset
;MOV AX, 99
;MOV EDX, @offset
;MOV [EDX], AX
;CALL _element
;invoke printf, cfm$("%. %d\n"), @resultado_16


;MOV @valor_asignacion_32, -1
;CALL _rowing

;MOV @indice, 0
;CALL _element
;invoke printf, cfm$("%. %d\n"), @resultado_32

;MOV @indice, 1
;CALL _element
;invoke printf, cfm$("%. %d\n"), @resultado_32

;MOV @indice, 2
;CALL _element
;invoke printf, cfm$("%. %d\n"), @resultado_32

;MOV @indice, 3
;CALL _element
;invoke printf, cfm$("%. %d\n"), @resultado_32



invoke ExitProcess, 0

end start