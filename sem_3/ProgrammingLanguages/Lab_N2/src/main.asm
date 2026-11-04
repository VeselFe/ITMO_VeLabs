section .data
%include "src/words.inc"
bfov: db "error: buffer overflow", 10, 0
wnf: db "word was not found", 10, 0
input_str: db "Input string: ", 0


section .bss
; память под считываемую строку
str_addr: resb 256

section .text
%include "src/lib.inc"
%include "src/dict.inc"

global _start

_start:
	mov rdi, input_str
	call print_string
	mov rdi, str_addr
	mov rsi, 256
	call read_line

	test rax, rax
	je buffer_overflow
	mov rsi, indicator
	mov rdi, str_addr
	call find_word
	test rax, rax
	je was_not_found
	
	add rax, 8
	sub rsp, 8
	mov rdi, rax
	push rax
	call string_length
	pop rdi
	add rsp, 8
	add rdi, rax
	inc rdi
	call print_string
	call print_newline
	
	mov rdi, 0
	jmp exit
	buffer_overflow:
		mov rdi, bfov
		call print_err
		mov rdi, 1
		call exit
	was_not_found:
		mov rdi, wnf
		call print_err
		mov rdi, 0
		jmp exit
