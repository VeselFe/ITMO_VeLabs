	.data
input: .word 0x80
output: .word 0x84
buffer: .word 0x22A

	.text
.org 0x88
_start:
	movea.l 0x1000, A7	; Инициализация стека
	movea.l input, A0	; Инициализируем потоки и буффер
	movea.l (A0), A0
	movea.l output, A1
	movea.l (A1), A1
	movea.l buffer, A2
	movea.l (A2), A2

	move.l (A0), D0		; читаем количество символов
	move.l D0, D4		; копируем для записывающего прохода
	cmp.l 0, D0
	beq zero_finish		
	bmi input_error

	loop:
		move.l (A0), D1	; stdin -> D1
		
		clr.l D5		; Заводи D5 для посимвольного чтения D1, для корректной записи в буффер
		jsr read_one_byte
		jsr read_one_byte
		jsr read_one_byte

		move.b D1, D3	; Последний символ читаем без сдвига D5
		and.l 0xFF, D3
		add.l D3, D5

		move.l D5, (A2)+	; Пишем в буффер

		sub.l 4, D0			
		cmp.l 0, D0
		bgt loop			; если счетчик > 0 считываем дальше
		ble compressing 	; eсли осталось обработать <= 0 символов, то выходим
    zero_finish:
		move.l 0, (A1)
		halt
	input_error:
		move.l -1, (A1)
		halt
	read_one_byte:
		move.b D1, D3		; копируем 1 символ
		and.l 0xFF, D3		; маска чтобы убрать расширение знака
		add.l D3, D5		; добавляем в накапливающую переменную
		asr.l 8, D1			; делаем сдвиги на на символ
		asl.l 8, D5
		rts
		
	compressing:
		movea.l A2, A3
		movea.l A3, A4		; Копируем адрес буфера сжатых значений
		movea.l buffer, A2	
		movea.l (A2), A2
		clr.l D1	; переменная под текущий символ
		clr.l D5	; количество символов после компресии

		compress_letter:
			move.b (A2)+, D1
			clr.b D3			; счетчик для символа D1
			add.b 1, D3			; D3 = 1
			sub.l 1, D4 		; первый символ уже считан

			same_letter:
				cmp.l 0, D4
				beq continue

				cmp.b (A2), D1	; если это уже другой символ, то идем дальше
				bne continue
				
				cmp.b 0xFF, D3	; Проверка на < 255
				beq continue 

				move.b (A2)+, D2	; A2++ (просто копируем в безопасную неиспользуемую переменную для увеличения A2)
				sub.l 1, D4 		; D4--
				add.l 1, D3			; D3++
				
				jmp same_letter
			continue:
				move.b D3, (A3)+	; Записываем число повторений символа D1				
				move.b D1, (A3)+	; ЗАписываем сам символш
				add.l 1, D5
				cmp.l 0, D4
				ble write_compressed_word
				jmp compress_letter

	 write_compressed_word:
		clr.b D2
	 	
		movea.l A4, A3	; Адрес буфера сжатых значений
		asl.l 1, D5		; Количество байт для печти
		move.l D5, (A1)

		print_loop:	
			cmp.l 0, D5
			ble finish
			cmp.l 4, D5
			blt print_remains	; Если у нас меньше 4 символов в слове, то печатаем только 1ую пару, остальное нули

			jsr read_from_buf_word ; после этого в D2 4 байта на печать
			move.l D2, (A1)

			sub.l 4, D5
			jmp print_loop
		print_remains:
			clr.l D2

			jsr read_from_buf_byte
			jsr read_from_buf_byte

			asl.l 8, D2
			
			move.l D2, (A1)
		finish:
			halt
		
		read_from_buf_word:
			clr.l D1
			clr.l D2

			jsr read_from_buf_byte
			jsr read_from_buf_byte
			jsr read_from_buf_byte

			move.b (A3)+, D1
			and.l 0xFF, D1
			add.l D1, D2

			rts
		read_from_buf_byte:
			move.b (A3)+, D1
			and.l 0xFF, D1
			add.l D1, D2
			asl.l 8, D2
			rts