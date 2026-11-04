ASM=nasm
ASMFLAGS=-f elf64
LD=ld

all: program

src/%.o: src/%.asm
	$(ASM) $(ASMFLAGS) -o $@ $<

src/dict.o: src/dict.asm src/lib.inc

src/main.o: src/main.asm src/dict.inc src/colon.inc src/words.inc src/lib.inc

program: src/main.o src/dict.o src/lib.o
	$(LD) -o program $^

clean:
	rm -f src/*.o *.o

test: program
	python3 tests/tests.py

.PHONY: all clean test
