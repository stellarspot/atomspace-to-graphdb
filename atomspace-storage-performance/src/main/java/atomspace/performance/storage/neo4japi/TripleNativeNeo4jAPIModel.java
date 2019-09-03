package atomspace.performance.storage.neo4japi;

import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.neo4j.graphdb.*;

import java.util.ArrayList;
import java.util.List;

public class TripleNativeNeo4jAPIModel extends TripleNeo4jAPIModel {

    public TripleNativeNeo4jAPIModel(TripleGraph tripleGraph) {
        super(tripleGraph);
    }

    public TripleNativeNeo4jAPIModel(DBNeo4jAPIStorage storage, TripleGraph tripleGraph) {
        super(storage, tripleGraph);
    }

    @Override
    public String getName() {
        return "Native";
    }

    @Override
    public void storeTriples() {
        System.out.printf("Store triples%n");

        GraphDatabaseService graph = storage.graph;
        try (Transaction tx = graph.beginTx()) {

            for (Triple triple : tripleGraph.getTriples()) {
                Node subjectNode = graph.createNode();
                subjectNode.addLabel(Label.label("SubjectNode"));
                subjectNode.setProperty("value", triple.subject);

                Node objectNode = graph.createNode();
                objectNode.addLabel(Label.label("ObjectNode"));
                objectNode.setProperty("value", triple.object);

                RelationshipType predicateRelation = RelTypes.valueOf(getLinkType(triple.predicate));
                Relationship relationship = subjectNode.createRelationshipTo(objectNode, predicateRelation);
            }

            tx.success();
        }
    }

    @Override
    public List<String> queryObjects(int iterations) {

        List<String> objects = new ArrayList<>();

        return objects;
    }

    private static String getLinkType(String link) {
        return link.replace('-', '_').toUpperCase();
    }

    private enum RelTypes implements RelationshipType {

        LIKES,

        PREDICATE_0,
        PREDICATE_1,
        PREDICATE_10,
        PREDICATE_11,
        PREDICATE_12,
        PREDICATE_13,
        PREDICATE_14,
        PREDICATE_15,
        PREDICATE_16,
        PREDICATE_17,
        PREDICATE_18,
        PREDICATE_19,
        PREDICATE_2,
        PREDICATE_20,
        PREDICATE_21,
        PREDICATE_22,
        PREDICATE_23,
        PREDICATE_24,
        PREDICATE_25,
        PREDICATE_26,
        PREDICATE_27,
        PREDICATE_28,
        PREDICATE_29,
        PREDICATE_3,
        PREDICATE_30,
        PREDICATE_31,
        PREDICATE_32,
        PREDICATE_33,
        PREDICATE_34,
        PREDICATE_35,
        PREDICATE_36,
        PREDICATE_37,
        PREDICATE_38,
        PREDICATE_39,
        PREDICATE_4,
        PREDICATE_5,
        PREDICATE_6,
        PREDICATE_7,
        PREDICATE_8,
        PREDICATE_9,
    }

}
