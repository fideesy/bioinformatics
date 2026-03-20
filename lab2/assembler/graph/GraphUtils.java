package assembler.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphUtils {
    public static DeBruijnGraph buildDeBruijnGraph(List<String> sequences, int k) {
        DeBruijnGraph g = new DeBruijnGraph(k);
        Map<String, Integer> counts = new HashMap<>();

        for (String seq : sequences) {
            if (seq.length() < k + 1) {
                continue;
            }
            for (int i = 0; i + k < seq.length(); i++) {
                String edgeSeq = seq.substring(i, i + k + 1);
                counts.put(edgeSeq, counts.getOrDefault(edgeSeq, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            String edgeSeq = entry.getKey();
            int coverage = entry.getValue();
            String from = edgeSeq.substring(0, k);
            String to = edgeSeq.substring(1);
            g.addEdge(from, to, edgeSeq, coverage);
        }

        return g;
    }

    public static DeBruijnGraph compressGraph(DeBruijnGraph g) {
        DeBruijnGraph compressed = new DeBruijnGraph(g.getK());
        Set<DeBruijnEdge> used = new HashSet<>();

        for (String node : g.nodes()) {
            if (g.outDegree(node) == 0) {
                continue;
            }

            boolean startHere = g.inDegree(node) != 1 || g.outDegree(node) != 1;
            if (!startHere) {
                continue;
            }

            for (DeBruijnEdge first : g.getOutgoing(node)) {
                if (used.contains(first)) {
                    continue;
                }

                String start = first.from;
                String end = first.to;
                StringBuilder sequence = new StringBuilder(first.sequence);
                int totalCoverage = first.coverage;
                int totalKmers = 1;
                used.add(first);

                while (g.inDegree(end) == 1 && g.outDegree(end) == 1) {
                    DeBruijnEdge next = g.getOutgoing(end).getFirst();
                    if (used.contains(next)) {
                        break;
                    }
                    sequence.append(next.sequence.charAt(next.sequence.length() - 1));
                    totalCoverage += next.coverage;
                    totalKmers += 1;
                    used.add(next);
                    end = next.to;
                }

                int avgCoverage = Math.max(1, Math.round((float) totalCoverage / totalKmers));
                compressed.addEdge(start, end, sequence.toString(), avgCoverage);
            }
        }

        for (DeBruijnEdge e : g.allEdges()) {
            if (!used.contains(e)) {
                compressed.addEdge(e.from, e.to, e.sequence, e.coverage);
            }
        }

        return compressed;
    }

    public static void removeLowCoverageEdges(DeBruijnGraph g, int minCoverage) {
        List<DeBruijnEdge> kept = new ArrayList<>();
        for (DeBruijnEdge e : g.allEdges()) {
            if (e.coverage >= minCoverage) {
                kept.add(e);
            }
        }
        g.rebuildFromEdges(kept);
    }

    public static void removeTips(DeBruijnGraph g, int maxTipLength) {
        List<DeBruijnEdge> kept = new ArrayList<>();
        for (DeBruijnEdge e : g.allEdges()) {
            boolean sourceTip = g.inDegree(e.from) == 0 && e.kmersCount(g.getK()) <= maxTipLength;
            boolean sinkTip = g.outDegree(e.to) == 0 && e.kmersCount(g.getK()) <= maxTipLength;

            if (!sourceTip && !sinkTip) {
                kept.add(e);
            }
        }
        g.rebuildFromEdges(kept);
    }
}