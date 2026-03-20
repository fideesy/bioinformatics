package assembler.io;

import assembler.graph.DeBruijnEdge;
import assembler.graph.DeBruijnGraph;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GraphWriter {
    public static void writeContigsFasta(DeBruijnGraph g, String path) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(Path.of(path))) {
            int id = 1;
            for (DeBruijnEdge e : g.allEdges()) {
                bw.write(">contig_" + id + " len=" + e.sequence.length() + " cov=" + e.coverage);
                bw.newLine();
                bw.write(e.sequence);
                bw.newLine();
                id++;
            }
        }
    }

    public static void writeGfa(DeBruijnGraph g, String path) throws IOException {
        Map<String, String> names = new HashMap<>();
        int id = 1;

        for (String node : g.nodes()) {
            names.put(node, "S" + id);
            id++;
        }

        try (BufferedWriter bw = Files.newBufferedWriter(Path.of(path))) {
            bw.write("H\tVN:Z:1.0");
            bw.newLine();

            for (String node : g.nodes()) {
                bw.write("S\t" + names.get(node) + "\t" + node);
                bw.newLine();
            }

            int edgeId = 1;
            for (DeBruijnEdge e : g.allEdges()) {
                bw.write("L\t" + names.get(e.from) + "\t+\t" + names.get(e.to) + "\t+\t" + (g.getK() - 1) + "M");
                bw.newLine();
                bw.write("# edge_" + edgeId + " seq=" + e.sequence + " cov=" + e.coverage);
                bw.newLine();
                edgeId++;
            }
        }
    }
}