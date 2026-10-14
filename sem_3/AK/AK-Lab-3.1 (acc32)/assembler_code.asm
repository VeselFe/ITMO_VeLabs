.text

_start:
    load_addr input
    load_acc
    beqz end            ; если 0 чисел суммируем, то сразу заканчиваем
    store_addr quantity ; устанавливаем количество чисел для суммироавния

loop:
    load_addr input     ; загружаем новое число и сохраняем для проверки знака потом
    load_acc
    store_addr cur

    add sum_s           ; обновляем сумму
    store_addr sum_s

    bcc check_sign      ; если нет переноса в старший разряд, то име дальше
    load_addr sum_b     ; иначе делаем +1 к старшему слову
    add CONST_1
    store_addr sum_b

check_sign:
    load_addr cur
    bgez continue       ; если прибавляемое число положительное, то идем дальше

    load_addr sum_b     ; иначе вычитаем 1 из старшего слова
    sub CONST_1
    store_addr sum_b

continue:
    load_addr quantity  ; обновляем счетчик чисел
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

.data
.org 0x88

input:      .word 0x80
output:     .word 0x84
quantity:   .word 0
sum_s:      .word 0
sum_b:      .word 0
cur:        .word 0
CONST_1:    .word 1