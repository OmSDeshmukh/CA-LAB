	.data
a:
	113
    1
    0
    2
    -1
	.text
main:
    load %x0, $a, %x3
    load %x0, 2, %x4
    load %x0, 2, %x5
    load %x0, 2, %x7
    jmp wloop
wloop:
    addi %x5, 1, %x5
    bgt %x5, %x3, finall
    div %x3, %x5, %x6
    beq %x31, %x7, uc
    jmp wloop
uc:
    addi %x4, 1, %x4
    jmp wloop
finall:
    load %x0, 3, %x8
    beq %x8, %x4, yup
    load %x0, 4, %x10
    end
yup:
    load %x0, 1, %x10
    end