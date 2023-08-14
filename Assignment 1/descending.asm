	.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
    load %x0, $n, %x3
    subi %x4, 1, %x4
    jmp outerloop
outerloop:
    addi %x4, 1, %x4
    beq %x4, %x3, end
    addi %x4, 0, %x5
    jmp innnerloop
innnerloop:
    addi %x5, 1, %x5
    beq %x5, %x3, outerloop
    load %x4, $a, %x7
    load %x5, $a, %x8
    bgt %x8, %x7, change
change:
    store %x8, $a, %x4
    store %x7, $a, %x5
    jmp innnerloop