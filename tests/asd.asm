.386
include \masm32\include\masm32rt.inc
dll_dllcrt0 PROTO C
printf PROTO C :VARARG

.data

;Datos del programa. 
_a dw 6 dup (?)
_c dd 4 dup (?)
cte1 dw 0
cte2 dw 1
cte3 dw 2
cte4 dw 3
cte5 dw 5
_x dw ?
_y dw ?
_z dw ?
cte6 dw 30
cte7 dw 20
cte8 dw 10

;Variables predefinidas. 
@resultado_16 dw ?
@resultado_32 dd ?
@coleccion dd ?
@indice dw ?
@tipo dd ?
@offset dd ?
@valor_asignacion_16 dw ?
@valor_asignacion_32 dd ?
@tempEAX dd ?
@tempEBX dd ?
@tempECX dd ?
@tempEDX dd ?
ConvError db "Error, perdida de informacion en conversion.", 0
IndiceError db "Error, indice fuera de los limites de la coleccion.", 0

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
MOV EBX, 0
MOV BX, [EAX]
IMUL EBX, @tipo
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
CMP DX, 0  
JL error_indice
ADD DX, 1
MOV EBX, 0
MOV BX, [EAX]
CMP DX, BX
JG error_indice
IMUL EDX, @tipo
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
MOV [_a], 5
MOV [_c], 3
;Fin de seteo de tamaños. 

MOV BX, cte2
LEA EAX, [_a]
MOV @coleccion, EAX
MOV @tipo, 2
CALL _offset 
MOV EAX, @offset
MOV [EAX], BX
MOV BX, cte3
LEA EAX, [_a]
MOV @coleccion, EAX
MOV @tipo, 2
CALL _offset 
MOV EAX, @offset
MOV [EAX], BX
MOV BX, cte4
LEA EAX, [_a]
MOV @coleccion, EAX
MOV @tipo, 2
CALL _offset 
MOV EAX, @offset
MOV [EAX], BX
MOV BX, cte8
CMP BX, 0
JL error_negativo
MOV EAX, 0
MOV AX, BX
LEA EDX, [_c]
MOV @coleccion, EDX
MOV @tipo, 4
CALL _offset 
MOV EDX, @offset
MOV [EDX], EAX
MOV BX, cte7
CMP BX, 0
JL error_negativo
MOV EAX, 0
MOV AX, BX
LEA EDX, [_c]
MOV @coleccion, EDX
MOV @tipo, 4
CALL _offset 
MOV EDX, @offset
MOV [EDX], EAX
MOV BX, cte6
CMP BX, 0
JL error_negativo
MOV EAX, 0
MOV AX, BX
LEA EDX, [_c]
MOV @coleccion, EDX
MOV @tipo, 4
CALL _offset 
MOV EDX, @offset
MOV [EDX], EAX
LEA EAX, [_a]
MOV @coleccion, EAX
MOV @tipo, 2
MOV @indice, 0
CALL _element
MOV AX, @resultado_16
MOV _x, AX
LEA EAX, [_a]
MOV @coleccion, EAX
MOV @tipo, 2
CALL _first
MOV AX, @resultado_16
MOV _x, AX
invoke printf, cfm$("%. %d\n"), _x
LEA EAX, [_c]
MOV @coleccion, EAX
CALL _length
MOV AX, @resultado_16
MOV _x, AX
MOV BX, cte5
CMP BX, 0
JL error_negativo
MOV EAX, 0
MOV AX, BX
LEA EDX, [_c]
MOV @coleccion, EDX
MOV @tipo, 4
CALL _offset 
MOV EDX, @offset
MOV [EDX], EAX
MOV BX, _x
CMP BX, 0
JL error_negativo
MOV EAX, 0
MOV AX, BX
LEA EDX, [_c]
MOV @coleccion, EDX
MOV @tipo, 4
CALL _offset 
MOV EDX, @offset
MOV [EDX], EAX
invoke ExitProcess, 0

end start