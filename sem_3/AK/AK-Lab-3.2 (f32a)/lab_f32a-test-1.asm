.text

_start:
	@p newStr
	a!
loop:
	a
	@p input  \ считываем символ на стек данных
	a!
	@

	b!	\ копируем в B
	a!

	b
	@p code_end
	a!
	@
	
	inv 
	@p 1 
	+ 
	+
	-if symbol
	@p 0
	!+
	halt
	symbol:
		b
		@p code_z_p1 	\ дальше проверяем если код символа больше 'z' то иедм дальше 
		a!
		@

		inv 
		+ 
		@p 1 
		+
		-if notSymb
		b	\ возвращаем значение назад
	 	@p code_A_m1
		a!
		@

		inv 
		+ 
		@p 1 
		+
		inv 
		@p 1 
		+		\ получили -(x - ('A'-1))
		-if notSymb
		b
		
		@p code_a
		a!
		@		

		inv 
		+ 
		@p 1 
		+
		-if check_LC
		chek_UC:
			@p bWord
			a!
			@			
			
			@p 0	
			inv 
			+
			if write_sym
			
		chek_LC:
			@p bWord
			a!
			@
			
			if write_sym 
			b
			@p 0x20
			inv
			@p 1
			+
			+
			@p 0
			!p bWord \ меняем флаг что нужно перевести в верний регистр
			write_sym;
	write_sym:
		!+
		loop;
	notSymb:
		@p 1
		!p bWord
		loop;
.data

bWord: 1
newStr: 0x00
input: 0x80
output: 0x00
code_end: 0x0A
code_A: 0x41
code_A_m1: 0x40
code_a: 0x61
code_z: 0x7A	
code_z_p1: 0x7B
