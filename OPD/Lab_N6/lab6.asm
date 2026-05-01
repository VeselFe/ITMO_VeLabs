org 0x0
V0: word $DEFAULT, 0x180
V1: word $DEFAULT, 0x180 
V2: word $INT2, 0x180
V3: word $INT3, 0x180
V4: word $DEFAULT, 0x180
V5: word $DEFAULT, 0x180
V6: word $DEFAULT, 0x180
V7: word $DEFAULT, 0x180
DEFAULT: PUSH    
         CLA  
         OUT 1
         OUT 3   
         ;OUT 5   
         ;OUT 7   
         OUT 0xB
         OUT 0xE
         OUT 0x12
         OUT 0x16
         OUT 0x1E
         POP
         IRET

org 0x030
myX: word 0x0
MIN: word 0xE667
MAX: word 0x199B ; (max)199A + 1

org 0x040
START:  DI
        CLA
        OUT 1
        OUT 3

        LD #0xA ; 1000|0010 = 1010 = A 
        OUT 5
        LD #0xB ; 1000|0011 = 1011 = B
        OUT 7

        CLA
        OUT 0xB
        OUT 0xE
        OUT 0x12
        OUT 0x16
        OUT 0x1E
        JUMP $MAIN_WHILE

org 0x060
MAIN_WHILE: EI
            LD myX
            INC
            DI
            CALL ODZ
            ST myX
            JUMP MAIN_WHILE
    
org 0x70
temp: word 0x0
INT2:   PUSH
        IN 4
        AND myX
        NOT
        CALL ODZ
        ST myX
        POP
        IRET
    

org 0x82
INT3:   PUSH
        LD myX
        ASL
        ASL
        ADD myX
        NEG
        ADD #0x2
        CALL ODZ
        ST myX
        OUT 6
        POP
        IRET

org 0x94
ODZ:    CMP MAX
        BLT NEGOT
        JUMP ERROR
        NEGOT:  CMP MIN
                BLT ERROR
                RET
        ERROR:  LD MIN
                RET