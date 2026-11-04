section .text
 
global exit
global string_length
global print_string
global print_char
global print_newline
global print_uint
global print_int
global string_equals
global read_char
global read_word
global parse_uint 
global parse_int
global string_copy
global print_err
global read_line

; Принимает код возврата и завершает текущий процесс
exit: 
	mov rax, 60
	syscall

; Принимает указатель на нуль-терминированную строку, возвращает её длину
string_length:
    xor rax, rax
	.loop:
		cmp byte [rdi+rax], 0
		je .end
		inc rax
		jmp .loop
	.end:
    	ret

; Принимает указатель на нуль-терминированную строку, выводит её в stdout
print_string:
	push rdi
	call string_length
	pop rsi 	; адрес строки из стека -> rsi
	mov rdx, rax
	mov rax, 1 	; write  -> rax
	mov rdi, 1 	; stdout -> rdi 
	syscall
    ret

print_err:
	push rdi
	call string_length
	pop rsi 	; адрес строки из стека -> rsi
	mov rdx, rax
	mov rax, 1 	; write  -> rax
	mov rdi, 2 	; stderr -> rdi 
	syscall
    ret

; Принимает код символа и выводит его в stdout
print_char:
    push rdi
	mov rsi, rsp	; адрес стека -> rsi 
	mov rdx, 1		; длина = 1
	mov rax, 1 		; write  -> rax
	mov rdi, 1 		; stdout -> rdi 
	syscall	
	pop rdi
	ret

; Переводит строку (выводит символ с кодом 0xA)
print_newline:
    mov rdi, 0xA
	jmp print_char

; Выводит беззнаковое 8-байтовое число в десятичном формате 
; Совет: выделите место в стеке и храните там результаты деления
; Не забудьте перевести цифры в их ASCII коды.
print_uint:
    mov rax, rdi	; наше число -> rax
	sub rsp, 32
	xor rcx, rcx
	.division:
		mov r8, 10 ; делитель
		xor rdx, rdx	
		div r8 	; делим с остатком: rax := rax/rbx, rdx := rax % rbx
		add rdx, 0x30 ; приводим в аскиль
		mov [rsp+rcx], dl
		inc rcx
		cmp rax, 0
		jnz .division
	.loop:
		dec rcx
		movzx rdi, byte [rsp+rcx]
		push rcx
		call print_char
		pop rcx
		cmp rcx, 0
		je .end
		jmp .loop	
	.end:
		add rsp, 32
		ret

; Выводит знаковое 8-байтовое число в десятичном формате 
print_int:
	test rdi, rdi 	; выставляем флаг SF
	jns print_uint	; если число положительное печатаем как беззнак
	push rdi
	mov rdi, 45		; печатаем '-'	
	call print_char	
	pop rdi	
	neg rdi			; получаем допкод
	jmp print_uint  ; печатаем как беззнак
	ret

; Принимает два указателя на нуль-терминированные строки, возвращает 1 если они равны, 0 иначе
string_equals:
	.loop:
		mov al, byte [rdi] 	; сравниваем значения
		cmp al, byte [rsi]
		jne .false			; если неравны то сразу выход с нулем
		cmp al, 0			; если оба равны нулю то выходим с 1
		je .true	
		inc rdi
		inc rsi
		jmp .loop
	.false:
		xor rax, rax
		ret
	.true:
		mov rax, 1
		ret

; Читает один символ из stdin и возвращает его. Возвращает 0 если достигнут конец потока
read_char:
    sub rsp, 8		; выравниваем стек
    push 0
	xor rax, rax	; read -> rax
    xor rdi, rdi	; stdin -> rdi
    mov rdx, 1		; 1 символ читаем
    mov rsi, rsp	; пишем в стек
    syscall
    pop rax			; возвращаем в rax
    add rsp, 8
    ret

; Принимает: адрес начала буфера, размер буфера
; Читает в буфер слово из stdin, пропуская пробельные символы в начале, .
; Пробельные символы это пробел 0x20, табуляция 0x9 и перевод строки 0xA.
; Останавливается и возвращает 0 если слово слишком большое для буфера
; При успехе возвращает адрес буфера в rax, длину слова в rdx.
; При неудаче возвращает 0 в rax
; Эта функция должна дописывать к слову нуль-терминатор
read_word:
	; аргументы rdi, rsi
	push r12
	push r13
	push r14
	cmp rsi, 0		; если сразу конец
	je .false
	dec rsi			; размер буфера на 1 меньше из-за нуля который в конец ставим
	mov r12, rdi
	mov r13, rsi
	xor r14, r14
	.skip_spaces:	
		call read_char
		cmp rax, 0x20
		je .skip_spaces
		cmp rax, 0x9 
		je .skip_spaces
		cmp rax, 0xA
		je .skip_spaces	
	cmp rax, 0	; если только пробелы
	je .false
	.loop:
		cmp rax, 0	; конец ввода
		je .true
		cmp rax, 0x20
		je .true
		cmp rax, 0x9
		je .true
		cmp rax, 0xA
		je .true
		cmp r14, r13 
		je .false
		mov [r12+r14], al ; сохраняем считанный символ
		inc r14
		call read_char
		jmp .loop
   	.true:
		mov byte [r12+r14], 0
		mov rax, r12
   		mov rdx, r14
		pop r14
		pop r13
		pop r12
		ret
	.false:
		xor rax, rax
		xor rdx, rdx
		pop r14
		pop r13
		pop r12
		ret

read_line: 
    push r12 
    push r13 
    push r14 
    mov r12, rdi ; r12 = адрес буфера 
    mov r13, rsi ; r13 = размер буфера 
    dec r13      ; r13 = учитываем \0
    xor r14, r14 ; r14 = счетчик прочитанных символов 
    .loop: 
        call read_char 
        test rax, rax
        je .end 
        cmp rax, 0xA ; \n
        je .end 
        cmp r14, r13 ; Проверка на переполнение буфера 
        jge .overflow 
        mov [r12 + r14], al ; Сохраняем символ
        inc r14 
        jmp .loop 
    .end: 
        mov byte [r12 + r14], 0 ; \0 в конец строки 
        mov rax, r12 ; Возвращаем адрес буфера
        mov rdx, r14 ; Возвращаем длину строки
        pop r14 
        pop r13 
        pop r12 
        ret 
    .overflow: 
        xor rax, rax ; При переполнении возвращаем 0 
        pop r14 
        pop r13 
        pop r12 
        ret

; Принимает указатель на строку, пытается
; прочитать из её начала беззнаковое число.
; Возвращает в rax: число, rdx : его длину в символах
; rdx = 0 если число прочитать не удалось
parse_uint:
	xor rsi, rsi
	xor rax, rax
	xor rcx, rcx
	.loop:
		mov al, byte [rdi]	; Если не цифра -> конец 
		cmp al, 0x30
		jb .finish
		cmp al, 0x39
		ja .finish
		sub al, 0x30	; оставляем само значение
		imul rsi, 10	; копим значение A_n = 10*A_n-1 + наша новая цифра
		add rsi, rax
		inc rdi	
		inc rcx
		jmp .loop
	.finish:
		mov rax, rsi
		mov rdx, rcx
		ret


; Принимает указатель на строку, пытается
; прочитать из её начала знаковое число.
; Если есть знак, пробелы между ним и числом не разрешены.
; Возвращает в rax: число, rdx : его длину в символах (включая знак, если он был) 
; rdx = 0 если число прочитать не удалось
parse_int:
	xor rax, rax
	mov al, byte [rdi]
	cmp al, '-' ; отдельно обрабатываем случаи когда число начинается с -/+
	je .minus
	cmp al, '+'
	je .plus
	jmp parse_uint
	.minus:
		mov r8, 1	; флаг на взятие обратного числа
		jmp .continue
	.plus:
		xor r8, r8
	.continue:
		inc rdi
		sub rsp, 8
		call parse_uint
		add rsp, 8
		cmp rdx, 0
		je .err
		cmp r8, 0
		je .end
    	neg rax
	.end:
		inc rdx
		ret 
	.err:
		xor rax, rax
		xor rdx, rdx
		ret

; Принимает указатель на строку, указатель на буфер и длину буфера
; Копирует строку в буфер
; Возвращает длину строки если она умещается в буфер, иначе 0
string_copy:
	push rdi
	push rsi
	push rdx
	call string_length	; длина слова без \0
	pop rdx
	pop rsi
	pop rdi
	inc rax			; добавляем 1 учитывая \0
	cmp rax, rdx	; если буфер меньше то сразу переходим к ошибке
	ja .err
   	xor rcx, rcx
 	.loop:
    	mov r8b, byte [rdi + rcx]
    	mov byte [rsi + rcx], r8b  
    	cmp r8b, 0
    	je .finish          
    	inc rcx
    	jmp .loop
	.finish:
		dec rax	; возвращаем длимну строки без \0
		ret
	.err:
		xor rax, rax
		xor rdx, rdx
		ret
