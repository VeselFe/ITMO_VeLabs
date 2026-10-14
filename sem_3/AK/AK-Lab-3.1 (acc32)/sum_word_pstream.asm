	.data

input:		.word 0x80
output: 	.word 0x84
quantity: 	.word 0
sum_s:		.word 0
sum_b: 		.word 0
CONST_1: 	.word 1
CONST_4: 	.word 4
	
	.text

_start:
	load_addr input 	; загружаем первичное значение в acc
	load_acc
	beqz end			; n = 0
	store_addr quantity
	loop:
		load_addr input
		load_acc
		add sum_s
		store_addr sum_s
		bcc continue
		load_addr sum_b
		add CONST_1
		store_addr sum_b
	continue:
		load_addr quantity		
		sub CONST_1
		store_addr quantity
		beqz end
		
		jmp loop
	end:
		load_addr sum_b
		store_ind output
		
		load_addr sum_s
		store_ind output
		
		halt		
	err:	; в случае некорректных вводных данных возвращаем -1
		load_imm -1
		store_addr output
		halt
