package atomspace.performance.storage.neo4j;

import atomspace.performance.DBAtom;
import atomspace.performance.generator.DBAtomsTriplesGenerator;
import atomspace.performance.generator.Triple;
import atomspace.performance.generator.TripleGraph;

import java.util.List;
import java.util.Set;

public class Neo4jTriplesQuery {


    public static void main(String[] args) throws Exception {


        try (DBNeo4jStorage storage = new DBNeo4jStorage()) {

            storage.clearDB();

            TripleGraph tripleGraph = new TripleGraph(3, 2, 5, 3);
            DBAtomsTriplesGenerator generator = new DBAtomsTriplesGenerator(tripleGraph);

            storeAndQueryTriples(storage, generator);
            storeAndQueryAtoms(storage, generator);

        }
    }

    private static void storeAndQueryTriples(DBNeo4jStorage storage, DBAtomsTriplesGenerator generator) {
        TripleGraph tripleGraph = generator.tripleGraph;
        Set<Triple> triples = generator.getTripleGraph().getTriples();
        System.out.printf("subjects: %d, predicates: %d, objects: %d\n",
                tripleGraph.subjectsNumber, tripleGraph.predicatesNumber, tripleGraph.objectsNumber);

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
            System.out.println(atom);
        }
    }
}
