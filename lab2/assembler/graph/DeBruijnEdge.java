package assembler.graph;

public class DeBruijnEdge {
    public final String from;
    public final String to;
    public final String sequence;
    public final int coverage;

    public DeBruijnEdge(String from, String to, String sequence, int coverage) {
        this.from = from;
        this.to = to;
        this.sequence = sequence;
        this.coverage = coverage;
    }

    public int kmersCount(int k) {
        return Math.max(1, sequence.length() - k);
    }
}