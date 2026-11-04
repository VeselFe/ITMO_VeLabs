	.text
.org 0x88

_start:
	lit 0x00
	a!
init_loop:		\ Инииализация символами '_'
	lit 0x5f
	!+

	a
	lit 0x20 inv 1 + +
	-if init_AB
	
	init_loop ;

init_AB:	 
	lit 0x00 
	a!
	
	lit 0x80
	b!

waitting:
	@b	
	
	check_endOfLine

	dup
	lit 'z'	1 +	 	\ ('z'+1)
	inv	1 + +
	-if continue 	\ x >= ('z'+1)
	
	check_sm_A
	-if continue 	\ ('A'-1) >= x
	
	dup
	lit 'a' inv 1 + + 			\ x - 'a'
	-if read_symb				\ если 'a' <= x <= 'z' то точно символ -> переходим в состояния нахождения в слове
	
	dup
	lit 'Z' 1 + inv 1 + + 	\ x - ('Z'+1)
	-if continue			\ 'Z' < x < 'a' то точно не символ

	read_symb ;				\ переходим в состояния нахождения в слове
	continue:				 
		dup
		!+					\ сохранили в буфер
						
    	check_endBuf 		\ Проверили что не заккончилось место   	
		
		waitting ;			\ оставляем то же состояние

check_endOfLine:
	dup
	lit 10 		 	\ проверка на выход по переносу строки 
	inv 1 + +
	if finish
	;

check_sm_A:	\ Проверка, строго меньше ли 'A'
	dup

	lit 'A' 
	lit 0 inv +	 	\ 'A'-1
	
	inv 1 + 		\ -('A'-1)
	+ 				\ b - ('A'-1)
	
	inv 1 + 		\ ('A'-1) - b
	;

finish:
	lit 0x5F5F5F00
	!+
	
	lit 0x00 	\ Возвращаем указатель в начало буфера
	a!
	
	print_loop:	\ Печатаем символы первой "строки" до \0
		@+         
		
		dup
		lit 0xff
		and				\ Делаем маску для проверки и печати только 1го символа
		dup
		if prlp_end 	\ Если \0 то закончили
		!p 0x84     	\ Иначе печатаем
		
		print_loop ;
	prlp_end:
		halt

err_buf:	\ В случае переполнения буфера печатаем код ошибки
	lit 0x84 b!
	lit 0xCCCCCCCC !b
	halt

check_endBuf:
	a 
	lit 0x20 inv 1 + +  	\ A - 0x20
    if err_buf 
	;

read_symb:	\ Состояние нахождения в слове
	dup
	lit 'Z' inv 1 + +		\ x - 'Z'
	inv 1 +					\ 'Z' - x
	-if preLoop 			\ 'Z' >= x >= 'A' 

	lit 0x20 inv 1 + +		\ x - 0x20 = X
preLoop:
	dup
	!+						\ Сохраняем в буфер заглавную букву слова
						
    check_endBuf   	

	loop:
		@b	
		
		check_endOfLine
	
		dup
		lit 'a' inv 1 + +		\ x - 'a'
		-if check_end_of_word 	\ Если x >= 'a' то смотрим не закончилось ли слово

		check_sm_A				\ Инчае смотрим если меньше чем 'A' то тоже ждем нового слова				
		-if goToWaitting 		\ x < 'A'

		dup
		lit 'Z' 1 + inv 1 + + 	\ x - ('Z'+1)
		-if goToWaitting		\ 'Z' < x < 'a' то точно не символ

		lit 0x20 +				\ X + 0x20 = x

		dup
		!+
		
    	check_endBuf  	\ A - 0x20: Проверка на размер буфера    	

		loop ;
		
 	check_end_of_word:
		dup

		lit 'z' 1 + inv 1 + +	\ x - ('z'+1)
		-if goToWaitting 		\ Если x >= 'z'+1 то это не буква и переходим к ожиданию нового слова
		dup
		!+						\ Иначе идем дальше по слову - это обычная буква
    	
		check_endBuf 	\ A - 0x20: проверка на размер буфера    	

		loop ;

	goToWaitting: 
		check_endOfLine
	
		dup
		!+

		check_endBuf \ A - 0x20: проверка на размер буфера

		waitting ;	\ Возвращаемся к состоянию ожидания нового слова