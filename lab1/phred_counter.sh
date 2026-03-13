#!/bin/bash

awk 'NR % 4 == 0 {print substr($0,10,1)}' reads.fastq.txt | \
python3 -c '
import sys
s=c=0
for ch in sys.stdin.read():
    if ch.strip():
        s+=ord(ch)-33
        c+=1
print(round(s/c))
'
