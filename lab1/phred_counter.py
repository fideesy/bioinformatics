total = 0
count = 0

with open("reads.fastq.txt") as f:
    for i, line in enumerate(f):

        if i % 4 == 3 and len(line.strip()) >= 10:
            
            q = ord(line[9]) - 33
            total += q
            count += 1

print(round(total / count))
