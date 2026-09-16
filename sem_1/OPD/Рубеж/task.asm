org 0x6BA
LENGTH: word 4
RES_A: word 0x400
RES_B: word 0x401
org 0x6BF
BEGIN:  word 0xFF0A ; 1
        word 0x0001
        word 0x0B0B ; 2
        word 0x0002
        word 0xFF0A ; 3
        word 0x0001
        word 0x0B0B ; 4
        word 0x0002
MAS:    word 0x6BF
ELEMENT:word 0x0
INDEX:  word 0x1
TEMP:   word 0x0
MASK:   word 0xFFFF
START:  LD MASK
        ST (RES_A)
        ST (RES_B)
        CLA
WHILE:  LD INDEX
        ASR
        ASR
        ASL
        ASL
        CMP INDEX
        BNE CONTINUE
        LD ELEMENT
        ASL
        ADD MAS
        ST TEMP
        LD (TEMP)+
        AND (RES_A)
        ST (RES_A)
        LD (TEMP)
        AND (RES_B)
        ST (RES_B)
CONTINUE:       LD INDEX
                INC
                ST INDEX
                LD ELEMENT
                INC
                ST ELEMENT
                LOOP LENGTH
                JUMP WHILE
        HLT