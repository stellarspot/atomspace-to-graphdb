package atomspace.performance.storage.neo4j;

import atomspace.performance.DBAtom;
import atomspace.performance.generator.DBAtomsTriplesGenerator;
import atomspace.performance.generator.Triple;
import atomspace.performance.generator.RandomTripleGraph;
import atomspace.performance.generator.TripleGraph;

import java.util.List;
import java.util.Set;

public class Neo4jTriplesQuery {


    public static void main(String[] args) throws Exception {

        try (DBNeo4jStorage storage = new DBNeo4jStorage()) {

            storage.clearDB();

            TripleGraph tripleGraph = new RandomTripleGraph(3, 2, 5, 3);
            DBAtomsTriplesGenerator generator = new DBAtomsTriplesGenerator(tripleGraph);

            for (Triple triple : tripleGraph.getTriples()) {
                System.out.println(triple);
            }

            storeAndQueryTriples(storage, generator);
            storeAndQueryAtoms(storage, generator);
        }
    }

    private static void storeAndQueryTriples(DBNeo4jStorage storage, DBAtomsTriplesGenerator generator) {
        TripleGraph tripleGraph = generator.tripleGraph;
        Set<Triple> triples = generator.getTripleGraph().getTriples();
        System.out.printf("triple graph %s%n", tripleGraph.getStatistics());

        for (Triple triple : triples) {
            System.out.println(triple);
        }

        storage.putTriples(triples);

        int iterations = 2;
        Triple[] array = triples.toArray(new Triple[]{});

        List<String> queryObjects = storage.queryTripleObject(iterations, array);
        System.out.printf("objects found: %d%n", queryObjects.size());
    }

    private static void storeAndQueryAtoms(DBNeo4jStorage storage, DBAtomsTriplesGenerator generator) {

        Set<Triple> triples = generator.getTripleGraph().getTriples();
        List<DBAtom> atoms = generator.getAtoms();

        for (DBAtom atom : atoms) {
            storage.putAtoms(atoms);
        }
    }
}
