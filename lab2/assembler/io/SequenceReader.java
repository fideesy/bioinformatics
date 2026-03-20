package assembler.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SequenceReader {
    static List<String> readFasta(String path) throws IOException {
        List<String> sequences = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        try (BufferedReader br = Files.newBufferedReader(Path.of(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith(">")) {
                    if (!current.isEmpty()) {
                        sequences.add(current.toString());
                        current.setLength(0);
                    }
                } else {
                    current.append(line);
                }
            }
        }
        if (!current.isEmpty()) {
            sequences.add(current.toString());
        }
        return sequences;
    }

    static List<String> readFastq(String path) throws IOException {
        List<String> sequences = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Path.of(path))) {
            while (true) {
                String h = br.readLine();
                if (h == null) break;
                String seq = br.readLine();
                String plus = br.readLine();
                String qual = br.readLine();
                if (seq == null || plus == null || qual == null) {
                    break;
                }
                sequences.add(seq.trim());
            }
        }
        return sequences;
    }

    public static List<String> readSequences(String path) throws IOException {
        String lower = path.toLowerCase();
        if (lower.endsWith(".fa") || lower.endsWith(".fasta") || lower.endsWith(".fna")
                || lower.endsWith(".fa.txt") || lower.endsWith(".fasta.txt") || lower.endsWith(".fna.txt")) {
            return readFasta(path);
        }
        if (lower.endsWith(".fq") || lower.endsWith(".fastq")
                || lower.endsWith(".fq.txt") || lower.endsWith(".fastq.txt")) {
            return readFastq(path);
        }
        throw new IllegalArgumentException("Unknown file format: " + path);
    }
}
