#!/bin/bash

awk 'NR % 4 == 2 {
    seq = toupper($0)
    total += length(seq)
    gc += gsub(/[GC]/, "", seq)
}
END {
    printf "%.2f\n", (gc / total) * 100
}' reads.fastq.txt
