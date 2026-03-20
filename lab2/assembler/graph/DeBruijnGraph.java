package assembler.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DeBruijnGraph {
    private final int k;
    private final Map<String, List<DeBruijnEdge>> out = new HashMap<>();
    private final Map<String, List<DeBruijnEdge>> in = new HashMap<>();

    public DeBruijnGraph(int k) {
        this.k = k;
    }

    public int getK() {
        return k;
    }

    public void addNode(String v) {
        out.computeIfAbsent(v, _ -> new ArrayList<>());
        in.computeIfAbsent(v, _ -> new ArrayList<>());
    }

    public void addEdge(String from, String to, String sequence, int coverage) {
        addNode(from);
        addNode(to);
        DeBruijnEdge e = new DeBruijnEdge(from, to, sequence, coverage);
        out.get(from).add(e);
        in.get(to).add(e);
    }

    public int inDegree(String v) {
        return in.getOrDefault(v, List.of()).size();
    }

    public int outDegree(String v) {
        return out.getOrDefault(v, List.of()).size();
    }

    public Set<String> nodes() {
        Set<String> set = new HashSet<>();
        set.addAll(out.keySet());
        set.addAll(in.keySet());
        return set;
    }

    public List<DeBruijnEdge> allEdges() {
        List<DeBruijnEdge> edges = new ArrayList<>();
        for (List<DeBruijnEdge> list : out.values()) {
            edges.addAll(list);
        }
        return edges;
    }

    public List<DeBruijnEdge> getOutgoing(String v) {
        return out.getOrDefault(v, List.of());
    }

    public void rebuildFromEdges(List<DeBruijnEdge> edges) {
        out.clear();
        in.clear();
        for (DeBruijnEdge e : edges) {
            addEdge(e.from, e.to, e.sequence, e.coverage);
        }
    }
}