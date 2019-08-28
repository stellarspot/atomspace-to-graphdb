package atomspace.performance.triple;

public class FixedTripleGraph extends TripleGraph {

    public FixedTripleGraph(Triple... triples) {
        for (Triple triple : triples) {
            addTriple(triple);
        }
    }
}
