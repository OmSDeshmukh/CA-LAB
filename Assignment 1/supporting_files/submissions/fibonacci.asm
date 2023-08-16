	.data
n:
	10
	.text
main:
    addi %x3, 65535, %x3
    store %x0, $n, %x3
    addi %x0, 1, %x4
    subi %x3, 1, %x3
    store %x4, $n, %x3
    addi %x0, 1, %x5
    load %x0, $n, %x6
    jmp loops
loops:
    addi %x5, 1, %x5
    beq %x5, %x6, final
    load %x3, $n, %x7
    addi %x3, 1, %x3
    load %x3, $n, %x8
    subi %x3, 2, %x3
    add %x7, %x8, %x9
    store %x9, $n, %x3
    jmp loops
final:
    end