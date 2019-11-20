.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib

.data
cte1 dw 1
_a dw ?
cte2 dw 2
_b dw ?
_c dw ?
_d dw ?
cte3 dw 5
_z dw ?
.code
start:
MOV AX, _a
IMUL AX, cte2
ADD AX, cte1 //TODO esto no esta andando revisar suma
MOV AX, _b //TODO esto no esta andando revisar suma
IMUL AX, cte3
ADD AX, AX
MOV _z, AX
invoke ExitProcess, 0
end start
