org 0x0
V0: word $DEFAULT, 0x180
V1: word $DEFAULT, 0x180 
V2: word $DEFAULT, 0x180
V3: word $DEFAULT, 0x180
V4: word $DEFAULT, 0x180
V5: word $DEFAULT, 0x180
V6: word $INT8, 0x180
V7: word $DEFAULT, 0x180;word $INT5, 0x180
DEFAULT: PUSH    
         CLA  
         OUT 1
         OUT 3   
         OUT 5   
         OUT 7   
         OUT 0xB
         OUT 0xE;
         OUT 0x12
         OUT 0x16
         ;OUT 0x1A
         OUT 0x1E
         POP
         IRET
org 0x030
START:  DI
        CLA
        OUT 1
        OUT 3
        OUT 5
        OUT 7
        OUT 0xB
        ;LD #0xF  ; 1000 | 0111 = 1111 = F
        OUT 0xD
        OUT 0xE
        LD #0xE  ; 1000 | 0110 = 1110 = E
        OUT 0x19
        OUT 0x1A
        CLA
        OUT 0x12
        OUT 0x16
        OUT 0x1E
        JUMP $MAIN_WHILE
org 0x050
BEGIN: word 0x200       ; Начало кода на брэйнфаке
READER: word 0x200      ; Указательна текущую строку код для печати кодов программы
PROGSTRING: word 0x200     ; Указатель на текущую строку кода
ENDP: word 0x2FF        ; Конец кода на брэйнфаке
IFSTART: word 0x0
firstDB: word 0x300
MAIN_WHILE: DI
            LD IFSTART
            BNE PROGRAMM
            NOP
            EI
            JUMP MAIN_WHILE
org 0x70
wordToStartProg: word 0x60   ; символ '`' для перехода к выполнению программы
INT8:   PUSH
        LD PROGSTRING
        CMP firstDB
        BEQ EXIT
        LD #0
        IN 0x18
        CMP wordToStartProg
        BEQ STARTPROG
        ST (PROGSTRING)+
        JUMP EXIT
        STARTPROG:  ST $PROGSTRING
                    LD #1
                    ST  IFSTART
                    JUMP EXIT
org 0x80
INT5:   PUSH
        LD READER
        CMP ENDP
        BGE EXIT ; Проверяем, что не конец памяти под комманды
        LD (READER)+
        BEQ ZERO ; Проверяем что есть что выводить
        OUT 0xC
        JUMP EXIT
        ZERO:   LD -(READER)
                LD #0x30
                OUT 0xC 
                JUMP EXIT
EXIT:   POP
        IRET
CURLINE: word 0x200
PROGRAMM:       DI
                LD BEGIN
                ST CURLINE
                JUMP ENTER
ENTER:          CLA
                IN 0xD
                AND 0x40
                BEQ ENTER
                LD #0x0A
                OUT 0xC
                ;HLT
                JUMP PROGRAMM_WHILE
PROGRAMM_WHILE: LD CURLINE
                CMP DB  ; Проверка на конец программы
                BEQ FINISH
                LD (CURLINE)+
                BEQ FINISH
                CMP BIGGER
                BEQ BIGGER_FUNC
                CMP SMALLER
                BEQ SMALLER_FUNC
                CMP PLUS
                BEQ PLUS_FUNC
                CMP MINUS
                BEQ MINUS_FUNC
                CMP DOT
                BEQ DOT_FUNC
                CMP COMMA
                BEQ COMMA_FUNC
                CMP LEFT_BRACKET
                BEQ LEFT_BRACKET_FUNC
                CMP RIGHT_BRACKET
                BEQ RIGHT_BRACKET_FUNC
                JUMP PROGRAMM_WHILE
        FINISH: HLT
CHECK_END:      word 0x5FF
BIGGER_FUNC:    LD DATA
                CMP CHECK_END
                BEQ ERROR
                INC
                ST DATA
                JUMP PROGRAMM_WHILE
SMALLER_FUNC:   LD DATA
                CMP DB
                BEQ ERROR
                DEC
                ST DATA
                JUMP PROGRAMM_WHILE
PLUS_FUNC:      LD (DATA)
                INC
                ST (DATA)
                JUMP PROGRAMM_WHILE
MINUS_FUNC:     LD (DATA)
                DEC
                ST (DATA)
                JUMP PROGRAMM_WHILE
DOT_FUNC:       CLA
                IN 0xD
                AND #0x40
                BEQ DOT_FUNC
                LD (DATA)
                ADD #0x30 ; ДЛЯ ОТЛАДКИ И ВЫВОДА ЧИСЕЛ ПОСЛЕ +-
                OUT 0xC
                JUMP PROGRAMM_WHILE
COMMA_FUNC:     CLA
                IN 0x19
                AND #0x40
                BEQ COMMA_FUNC
                IN 0x18
                ST (DATA)
                JUMP PROGRAMM_WHILE
LEFT_BRACKET_FUNC:      LD (DATA)
                        BNE PROGRAMM_WHILE
                        LD #1
                        ST LEVEL
        SEARCH_END:     LD CURLINE
                        INC
                        ST CURLINE
                        CMP END_POGRAMM
                        BEQ ERROR
                        LD (CURLINE)
                        CMP LEFT_BRACKET
                        BNE CHECK_RIGHT
                        LD LEVEL
                        INC
                        ST LEVEL
                        JUMP SEARCH_END
        CHECK_RIGHT:    CMP RIGHT_BRACKET
                        BNE SEARCH_END
                        LD LEVEL
                        DEC
                        ST LEVEL
                        BNE SEARCH_END
                        JUMP PROGRAMM_WHILE
LOCAL_BEGIN: word 0x200
RIGHT_BRACKET_FUNC:     ;HLT
                        LD (DATA)
                        BEQ PROGRAMM_WHILE
                        LD #1
                        ST LEVEL
                        LD CURLINE
                        DEC
                        ST CURLINE
        SEARCH_BEGIN:   LD CURLINE
                        DEC
                        ST CURLINE
                        CMP LOCAL_BEGIN
                        BEQ ERROR
                        LD (CURLINE)
                        AND #0xFF
                        CMP RIGHT_BRACKET
                        BNE CHECK_LEFT
                        LD LEVEL
                        INC
                        ST LEVEL
                        JUMP SEARCH_BEGIN
        CHECK_LEFT:     CMP LEFT_BRACKET
                        BNE SEARCH_BEGIN
                        LD LEVEL
                        DEC
                        ST LEVEL
                        BNE SEARCH_BEGIN
                        JUMP PROGRAMM_WHILE
ERROR:  IN 0xD
        AND #0x40
        BEQ ERROR
        LD #0x21
        OUT 0xC
        HLT
LEVEL:          word ?
BIGGER:         word 0x3E ; >
SMALLER:        word 0x3C ; <
PLUS:           word 0x2B ; +
MINUS:          word 0x2D ; -
DOT:            word 0x2E ; .
COMMA:          word 0x2C ; ,
LEFT_BRACKET:   word 0x5B ; [
RIGHT_BRACKET:  word 0x5D ; ]
DB: word 0x300          ; Константа начала данных
DATA: word 0x300        ; Указатель на текущую ячейку данных
END_POGRAMM: word 0x2FF 

; [ + + ] + + .
; + + + [ > + < - ] > . < .
; + + [ > + + [ > + < - ] < - ] > > .