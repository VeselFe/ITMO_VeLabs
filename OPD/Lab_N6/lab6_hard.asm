org 0x0
V0: word $DEFAULT, 0x180
V1: word $DEFAULT, 0x180 
V2: word $DEFAULT, 0x180
V3: word $INT3, 0x180
V4: word $DEFAULT, 0x180
V5: word $INT5, 0x180
V6: word $INT8, 0x180
V7: word $DEFAULT, 0x180
DEFAULT: PUSH    
         CLA  
         OUT 1
         OUT 3   
         OUT 5   
         OUT 7   
         OUT 0xB
         OUT 0xE
         OUT 0x12
         OUT 0x16
         ;OUT 0x1A
         ;OUT 0x1E
         POP
         IRET

org 0x030
START:  DI
        CLA
        OUT 1
        OUT 3
        OUT 5

        LD #0xB ; 1000|0011 = 1011 = B
        OUT 7

        CLA
        OUT 0xB
        
        LD #0xD ; 1000|0101 = 1101 = D ВУ-5
        OUT 0xE
        LD #0xE ; 1000|0110 = 1110 = E ВУ-8
        OUT 0x1A

        CLA
        OUT 0x12
        OUT 0x16
        OUT 0x1E
        JUMP $MAIN_WHILE

org 0x050
READER: word 0x200
CURLINE: word 0x200 ;Проверять, что не меньше и не больше
ENDP: word 0x2FF
DATA: word 0x300


MAIN_WHILE: EI
            LD DATA
            INC
            ST DATA
            JUMP MAIN_WHILE

INT3:   PUSH
        LD $READER
        BEQ ZERO
        OUT 6
        LD READER
        INC 
        ST READER
        ZERO: LD #0x00FF
              OUT 6
              POP
              IRET

INT5:   PUSH
        ;LD (READER)+
        LD #0x30
        OUT 0xC
        ;BEQ NULL
        ;OUT 0xC
        ;LD READER
        ;INC
        ;ST READER
        NULL: POP
              IRET
        

INT8:   PUSH
        IN 0x18
        ST $CURLINE
        LD CURLINE
        INC
        ST CURLINE
        POP ;надо ли
        IRET