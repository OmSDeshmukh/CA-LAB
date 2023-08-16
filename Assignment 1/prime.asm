	.data
a:
	110
	.text
main:
    load %x0, $a, %x3
    addi %x0, 0, %x4
    addi %x0, 0, %x5
    jmp wloop
wloop:
    addi %x5, 1, %x5
    bgt %x5, %x3, finall
    div %x3, %x5, %x6
    beq %x31, %x0, uc
    jmp wloop
uc:
    addi %x4, 1, %x4
    jmp wloop
finall:
    addi %x0, 2, %x8
    beq %x8, %x4, yup
    subi %x0, 1, %x10
    end
yup:
    addi %x0, 1, %x10
    end