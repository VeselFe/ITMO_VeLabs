	.text

_start:
	addi sp, zero, 0x188
	addi s1, zero, 0x80		; инициализируем порты для чтения
	addi s2, zero, 0x84
	
	lw s3, 0(s1)			; загружаем наше интовое число
	
	mv a0, s3
	jal ra, count_zero
	mv s4, a0

	finish:
		sw s4, 0(s2)
		halt
	
count_zero:				; На вход будем получать a0=оставшиеся биты числа, на выход отдаем количество нулей
	beqz a0, int_is_zero

	addi sp, sp, -4		; сохраняем адрес возврата 
	sw ra, 0(sp)

	slti t0, a0, 0 		; n < 0 ? 1 : 0
	bnez t0, end_rec	; если первый бит не 0 то закончили ( <=> число < 0 )
	
	slli a0, a0, 1

	jal ra, count_zero	; и считаем дальше
	addi a0, a0, 1		; если 0, то +1
	j return

	end_rec:
		mv a0, zero
	return:
		lw ra, 0(sp)	; восстранавливаем код возврата и переходим по нему
		addi sp, sp, 4 
		jalr zero, ra, 0
	int_is_zero:
		addi a0, zero, 32
		jalr zero, ra, 0
