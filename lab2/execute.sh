#!/bin/bash

mkdir -p out
mkdir -p results

javac -d out assembler/DeBruijnAssembler.java assembler/graph/*.java assembler/io/*.java || exit 1

java -cp out assembler.DeBruijnAssembler "$@"