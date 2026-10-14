.data
	.org 0x00
buffer: .word 0, 0, 0, 0, 0, 0, 0, 0    \ 32 байта для буфера

	.text
	.org 0x100

_start:	
	@p 0x00 
	a!
	
	@p 0x200
	b!

waitting:
	bufOF
	@b	
	check_end
	if finish

	dup
	@p 123              \ 'z' + 1
	inv @p 1 + +
	-if continue_wait

	check_sm_A
	-if continue_wait

	read_symb
	jmp_loop ;          \ Идем в основной цикл обработки слова

continue_wait:
	!+
	waitting ;

jmp_loop:
	loop ;

check_end:
	dup
	@p 10               \ '\n'
	inv @p 1 + +
	;	

bufOF:
	a  
	@p 32
	inv @p 1 + +        \ A - 32
	\ Переход только если A >= 32 (результат A - 32 >= 0)
	-if finish
	;

check_sm_A:
	dup
	@p 64               \ 'A' - 1
	inv @p 1 + +
	;

finish:
	@p 0
	!+
	halt

read_symb:
	dup
	@p 'a'
	inv @p 1 + +
	-if make_cap

	!+
	;

make_cap:
	@p 0x20
	inv @p 1 + +        \ Перевод в заглавную ('a' -> 'A')
	!+
	;

loop:
	bufOF
	@b	
	check_end
	if finish

	dup
	@p 'a'
	inv @p 1 + +
	-if check_end_of_word

	check_sm_A
	-if goToWaitting

	@p 0x20 +           \ Внутри слова заглавную переводим в строчную (+32)
	!+
	loop ;

check_end_of_word:
	dup
	@p 123              \ 'z' + 1
	inv @p 1 + +
	-if goToWaitting
	!+                  \ Обычная строчная буква внутри слова
	loop ;

goToWaitting: 
	!+
	waitting ;
