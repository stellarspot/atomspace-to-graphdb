package atomspace.performance.storage.neo4japi;

import atomspace.performance.DBAtom;
import atomspace.performance.DBAtomSpace;
import atomspace.performance.DBLink;
import atomspace.performance.DBNode;
import atomspace.performance.storage.neo4j.DBNeo4jStorage;
import atomspace.performance.storage.neo4j.TripleNeo4jModel;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.graphdb.*;

import java.util.List;

import static org.neo4j.driver.v1.Values.ofRelationship;
import static org.neo4j.driver.v1.Values.parameters;

public abstract class TripleAtomNeo4jAPIModel extends TripleNeo4jAPIModel {

    protected static final Label ATOM_LABEL = Label.label("Atom");
    protected static final Label NODE_LABEL = Label.label("Node");
    protected static final Label LINK_LABEL = Label.label("Link");
    protected static final String ARG_TYPE = "ARG";

    protected final DBAtomSpace atomspace = new DBAtomSpace();

    public TripleAtomNeo4jAPIModel(TripleGraph tripleGraph) {
        super(tripleGraph);
    }

    public TripleAtomNeo4jAPIModel(DBNeo4jAPIStorage storage, TripleGraph tripleGraph) {
        super(storage, tripleGraph);
    }

    protected abstract DBAtom toAtom(Triple triple);

    @Override
    public void storeTriples() {
        GraphDatabaseService graph = storage.graph;
        try (org.neo4j.graphdb.Transaction tx = graph.beginTx()) {

            for (Triple triple : tripleGraph.getTriples()) {
                putAtom(toAtom(triple));
            }

            tx.success();
        }

    }

    protected Node putAtom(DBAtom atom) {

        if (atom instanceof DBNode) {
            return putNode((DBNode) atom);
        } else if (atom instanceof DBLink) {
            return putLink((DBLink) atom);
        }

        throw new RuntimeException("Unknown aom type!");
    }


    protected Node putNode(DBNode node) {

        GraphDatabaseService graph = storage.graph;
        Label nodeLabel = Label.label(node.type);
        Node n = graph.findNode(nodeLabel, "id", node.id);
        if (n == null) {
            n = graph.createNode();
            n.addLabel(ATOM_LABEL);
            n.addLabel(nodeLabel);
            n.setProperty("id", node.id);
            n.setProperty("value", node.value);
        }

        return n;
    }

    protected Node putLink(DBLink link) {

        GraphDatabaseService graph = storage.graph;

        Label linkLabel = Label.label(link.type);
        Node n = graph.findNode(linkLabel, "id", link.id);

        if (n == null) {
            n = graph.createNode();
            n.addLabel(ATOM_LABEL);
            n.addLabel(linkLabel);
            n.setProperty("id", link.id);
        }


        for (int i = 0; i < link.atoms.length; i++) {
            DBAtom atom = link.atoms[i];
            Node child = putAtom(atom);
            Relationship relationship = n.createRelationshipTo(child, getArgType(i));
        }

        return n;
    }

    protected static RelationshipType getArgType(int position) {
        return RelationshipType.withName(String.format("%s_%d", ARG_TYPE, position));

    }
}
