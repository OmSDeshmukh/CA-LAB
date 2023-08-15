	.data
a:
	1
	.text
main:
    load %x0, $a, %x3
    load %x0, $a, %x4
    addi %x0, 0, %x5
    jmp wloop
wloop:
    beq %x0, %x4, final
    divi %x4, 10, %x6
    muli %x5, 10, %x5
    add %x5, %x31, %x5
    divi %x4, 10, %x4
    jmp wloop
final:
    beq %x5, %x3, yup
    subi %x0, 1, %x10
    end
yup:
    addi %x0, 1, %x10
    end