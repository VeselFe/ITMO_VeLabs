%include "src/lib.inc"

section .text

global find_word

; rdi - указатель на строку
; rsi - указатель на начало словаря
find_word:
	push rbx
	push rdi
	loop:
		test rsi, rsi
		je finish
		mov rbx, rsi		; Сохраняем указатель на элемент словаря
		add rsi, 8			; Берем слово, так как оно идет за ключем
		mov rdi, [rsp]		; Копируем со стека rdi
		sub rsp, 8
		call string_equals	; Сравниваем строки
		add rsp, 8
		cmp rax, 1			
		je found			; Слово найдено
		mov rsi, [rbx] 		; Переходим к следующему слову
		jmp loop
	finish:
		xor rax, rax
		pop rdi
		pop rbx
		ret
	found:
		mov rax, rbx
		pop rdi
		pop rbx
		ret
