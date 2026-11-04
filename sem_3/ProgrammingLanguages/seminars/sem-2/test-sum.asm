default rel

section .data
    a: dq 30
    b: dq 40

section .text
global _start

sum:
    mov rax, rdi
    add rax, rsi
    ret

_start:
    mov rdi, [a]
    mov rsi, [b]
    call sum            ; rax = 70

    ; --- Завершение программы без вызова SIGSEGV ---
    mov rdi, rax        ; Передаем 70 как код выхода из программы
    mov rax, 60         ; sys_exit
    syscall
