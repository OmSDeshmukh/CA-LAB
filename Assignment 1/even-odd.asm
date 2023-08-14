    .data
a: 
    13
    1
    2
    -1
    .text
main:
    load %x0, $a, %x3
    load %x0, 1, %x5
    divi %x3, 2, %x4
    beq %x31, %x5, od
    load %x0, 3, %x10
    end
od:
    load %x0, 1, %x10
    end