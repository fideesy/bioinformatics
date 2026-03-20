#для референсного генома
./execute.sh --input resources/ecoli_1k.fna.txt \
-k 15 \
--out-fasta results/ref_k21.fasta \
--out-gfa results/ref_k21.gfa

#для прочтений без очистки
./execute.sh --input resources/ecoli_reads.fastq.txt \
-k 21 \
--out-fasta results/reads_k21.fasta \
--out-gfa results/reads_k21.gfa

#для прочтений с очисткой
./execute.sh --input resources/ecoli_reads.fastq.txt \
-k 21 \
--clean \
--min-coverage 2 \
--max-tip-length 2 \
--out-fasta results/reads_k21_clean.fasta \
--out-gfa results/reads_k21_clean.gfa