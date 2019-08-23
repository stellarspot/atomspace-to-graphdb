package atomspace.performance.storage.neo4j;

import atomspace.performance.generator.DBAtomsTriplesGenerator;
import atomspace.performance.generator.Triple;
import atomspace.performance.generator.TripleGraph;

import java.util.Set;

public class Neo4jTriplesQuery {


    public static void main(String[] args) throws Exception {


        try (DBNeo4jStorage storage = new DBNeo4jStorage()) {

            storage.clearDB();

            TripleGraph tripleGraph = new TripleGraph(3, 2, 5, 3);
            DBAtomsTriplesGenerator generator = new DBAtomsTriplesGenerator(tripleGraph);

            Set<Triple> triples = generator.getTripleGraph().getTriples();

            System.out.printf("subjects: %d, predicates: %d, objects: %d\n",
                    tripleGraph.subjectsNumber, tripleGraph.predicatesNumber, tripleGraph.objectsNumber);
            for (Triple triple : triples) {
                System.out.println(triple);
            }

            storage.putTriples(triples);
        }
    }
}
