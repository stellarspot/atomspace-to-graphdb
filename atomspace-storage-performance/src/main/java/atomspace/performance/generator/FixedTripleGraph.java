package atomspace.performance.generator;

public class FixedTripleGraph extends TripleGraph {

    public FixedTripleGraph(Triple... triples) {
        for (Triple triple : triples) {
            addTriple(triple);
        }
    }
}
