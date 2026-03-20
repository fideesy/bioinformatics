package assembler;

import assembler.graph.DeBruijnGraph;
import assembler.graph.GraphUtils;
import assembler.io.GraphWriter;
import assembler.io.SequenceReader;

import java.io.IOException;
import java.util.List;

public class DeBruijnAssembler {
    public static void run(String inputPath,
                           int k,
                           boolean clean,
                           int minCoverage,
                           int maxTipLength,
                           String fastaOut,
                           String gfaOut) throws IOException {

        List<String> sequences = SequenceReader.readSequences(inputPath);

        DeBruijnGraph graph = GraphUtils.buildDeBruijnGraph(sequences, k);
        DeBruijnGraph result = GraphUtils.compressGraph(graph);

        if (clean) {
            GraphUtils.removeLowCoverageEdges(result, minCoverage);
            GraphUtils.removeTips(result, maxTipLength);
            result = GraphUtils.compressGraph(result);
        }

        GraphWriter.writeContigsFasta(result, fastaOut);
        GraphWriter.writeGfa(result, gfaOut);
    }

    public static void main(String[] args) {
        String input = null;
        int k = 21;
        boolean clean = false;
        int minCoverage = 2;
        int maxTipLength = 2;
        String outFasta = "contigs.fasta";
        String outGfa = "graph.gfa";

        try {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--input" -> input = args[++i];
                    case "-k", "--k" -> k = Integer.parseInt(args[++i]);
                    case "--clean" -> clean = true;
                    case "--min-coverage" -> minCoverage = Integer.parseInt(args[++i]);
                    case "--max-tip-length" -> maxTipLength = Integer.parseInt(args[++i]);
                    case "--out-fasta" -> outFasta = args[++i];
                    case "--out-gfa" -> outGfa = args[++i];
                    default -> throw new IllegalArgumentException("Unknown argument: " + args[i]);
                }
            }

            if (input == null) {
                printUsage();
                return;
            }

            run(input, k, clean, minCoverage, maxTipLength, outFasta, outGfa);
            System.out.println("Done.");
            System.out.println("FASTA: " + outFasta);
            System.out.println("GFA: " + outGfa);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("java assembler.DeBruijnAssembler "
                + "--input file.fna|file.fastq "
                + "[-k 21] "
                + "[--clean] "
                + "[--min-coverage 2] "
                + "[--max-tip-length 2] "
                + "[--out-fasta contigs.fasta] "
                + "[--out-gfa graph.gfa]");
    }
}