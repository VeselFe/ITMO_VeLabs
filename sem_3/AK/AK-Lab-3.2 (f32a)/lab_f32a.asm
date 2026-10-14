	.data
.org 0x00

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

	@p 'z'
	@p 1
	+		\ ('z'+1)
	inv
	@p 1
	+ +
	-if continue \ x >= \('z'+1)
	
	check_sm_A
	-if continue \ ('A'-1) >= b
	read_symb ;
	continue:
		!+
		waitting ;

check_end:
	dup

	@p 10 \ проверка на выход 
	inv
	@p 1
	+
	+
	;	

bufOF:
    a  
    @p 0x20 
    inv
    @p 1
    + +              \ A - 0x20
    -if finish     	
    ;

check_sm_A:
	dup

	@p 'A'
	@p 1
	inv
	@p 1
	+
	+
	\ 'A'-1
	
	inv
	@p 1
	+
	\ -('A'-1)

	+ \ b - ('A'-1)
	
	inv
	@p 1
	+
	\ ('A'-1) - b
	;


finish:
	@p 0
	!+
	halt

read_symb:
	@p 'A'
	inv
	@p 1
	+ +
	-if preLoop \ x >= A 

	@p 0x20
	+			\ x + 0x20 = X
preLoop:
	!+
	loop:
		bufOF
		@b	
		check_end
		if finish
	
		dup

		@p 'a'
		inv
		@p 1
		+ +
		-if check_end_of_word 	\ если x >= 'a' то смотрим не закончилось ли слово
		
		check_sm_A				\ инчае смотрим если меньше чем 'A' то тоже ждем нового слова				
		-if goToWaitting \ x < 'A'
		@p 0x20
		inv
		@p 1
		+ +
		!+
		waitting ;
		
 	check_end_of_word:
		dup

		@p 'z'
		@p 1
		+
		inv
		@p 1
		+ +
		-if goToWaitting \ если x >= 'z'+1 то это не буква и переходим к ожиданию нового слова
		!+		\ иначе идем дальше по слову - это обычная буква
		loop ;
	goToWaitting: 
		!+
		waitting ;
