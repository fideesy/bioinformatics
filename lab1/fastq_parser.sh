#!/bin/bash

awk 'NR % 4 == 2 {
    len = length($0)
    count++
    sum += len
    if (count == 1 || len < min) min = len
    if (count == 1 || len > max) max = len
}
END {
    printf "Общее число прочтений в файле равно %d.\n", count
    printf "Минимальная длина прочтения равна %d.\n", min
    printf "Средняя длина прочтения равна %d.\n", int(sum / count + 0.5)
    printf "Максимальная длина прочтения равна %d.\n", max
}' reads.fastq.txt
