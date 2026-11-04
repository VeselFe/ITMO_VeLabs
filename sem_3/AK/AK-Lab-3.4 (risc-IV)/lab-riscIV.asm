	.text

_start:
	addi s1, zero, 0x80		; инициализируем порты для чтения
	addi s2, zero, 0x84
	
	lw s3, 0(s1)			; загружаем наше интовое число
	mv s4, zero				; счетчик ведущих нулей
	beqz s3, int_eq_zero

	main:
		slti t0, s3, 0 
		bnez t0, finish		; если первый бит не 0 то закончили ( <=> число < 0 )
		addi s4, s4, 1		; считаем очередной ноль
		slli s3, s3, 1		; ставим на первую позицию следующий бит
		j main
	int_eq_zero:
		addi s4, zero, 32	; если наше число 0, то печатаем сразу 32
	finish:
		sw s4, 0(s2)
		halt
