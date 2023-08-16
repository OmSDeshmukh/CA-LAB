    .data
a: 
    13
    .text
main:
    load %x0, $a, %x3
    addi %x0, 1, %x5
    divi %x3, 2, %x4
    beq %x31, %x5, od
    subi %x0, 1, %x10
    end
od:
    addi %x0, 1, %x10
    end