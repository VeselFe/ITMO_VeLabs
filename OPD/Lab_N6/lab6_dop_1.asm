org 0x0
V0: word $INT7, 0x180
V1: word $DEFAULT, 0x180 
V2: word $DEFAULT, 0x180
V3: word $DEFAULT, 0x180
V4: word $DEFAULT, 0x180
V5: word $DEFAULT, 0x180
V6: word $DEFAULT, 0x180
V7: word $DEFAULT, 0x180
DEFAULT: IRET 
        PUSH    
         CLA  
         OUT 1
         OUT 3   
         OUT 5   
         OUT 7   
         OUT 0xB
         ;OUT 0xE
         OUT 0x12
         OUT 0x16
         OUT 0x1A
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
        
        LD #8  ; 1000|0111 = 1111
        OUT 0xD
        OUT 0xE
        
        CLA
        OUT 0x1A
        OUT 0x12
        OUT 0x16
        OUT 0x1E
        JUMP $MAIN_WHILE

org 0x050
BEGIN: word 0x200
READER: word 0x200
CURLINE: word 0x200 ; Проверять, что не меньше и не больше
ENDP: word 0x2FF
DB: word 0x300
DATA: word 0x300
IFSTART: word 0

MAIN_WHILE: DI
            LD IFSTART
            DEC
            BEQ  PROGRAMM
            EI
            INC 
            ST IFSTART
            JUMP MAIN_WHILE

org 0x90
INT7:   PUSH
        LD #0x30
        OUT 0xC
        POP
        IRET

;org 0x090
;PROGRAMM: ; Начало нашей введенной программы
;        LD BEGIN
;        ST READER
;    PROG:
;        LD (READER)+
;        BEQ FINISH
;        CMP 0x1 ; '>'
;        BEQ NEXT      
;
;        FINISH: 
;            HLT
;
;org 0x0C0
;VU_mask: word 0x40
;
;NEXT: 
;    LD #0x3E
;    LD #0x0C1
;    #ST BACK
;    JUMP PRINT
;    JUMP PROG
;
;PRINT:    
;    VU5: IN 0xD
;         AND VU_mask
;         BEQ VU5
;    OUT 0xC
;    JUMP BACK
    