    .data
a:
    7
    6
    5
    4
    3
    2
    1
    .text
main:
    addi %x5, 100, %x5
    addi %x6, 300, %x6
    addi %x9, 120, %x9
fori:
    store %x9, $a, %x5
    addi %x5, 1, %x5
    blt %x5, %x6, fori
    end