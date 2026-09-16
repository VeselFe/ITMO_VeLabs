org 0x360
myWord: word 0x6900
iter: word 0x8
zero_adress: word 0x05DD
one_adress: word 0x05E0

org 0x383	    
VU_mask: word 0x40
mask: word 0x00FF
str: word 0x05DD
stop: word 0x00FF
temp: word 0x0000

START: CLA
    VU8:  
        IN 0x19
        AND VU_mask
        BEQ VU8
    IN 0x18
    SWAB
    ST myWord

while: CLA
    LD myWord  
    ROL
    ST myWord
    BCS one
    JUMP zero
    continue:
        LOOP iter
        JUMP while
    HLT

zero:
    LD zero_adress
    ST str
    JUMP VU6
one:
    LD one_adress
    ST str
    JUMP VU6

VU6:  
    IN 0x11
    AND VU_mask
    BEQ VU6
LD (str)+
ST temp
SWAB
AND mask
CMP stop
BEQ continue
OUT 0x10

LD temp
AND mask
CMP stop
BEQ continue
OUT 0x10
JUMP VU6


; Код для вывода 0
org 0x5DD 
word 0x3C42
org 0x5DE 
word 0x423C
org 0x5DF 
word 0x00FF

; Код для вывода 1
org 0x5E0 
word 0x1020
org 0x5E1 
word 0x7E00
org 0x5E2 
word 0xFFFF