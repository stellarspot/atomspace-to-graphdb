package atomspace.performance.storage.neo4j;

import atomspace.performance.DBAtom;
import atomspace.performance.DBAtomSpace;
import atomspace.performance.DBLink;
import atomspace.performance.DBNode;
import atomspace.performance.triple.Triple;
import atomspace.performance.triple.TripleGraph;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Transaction;

import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

public abstract class TripleAtomNeo4jModel extends TripleNeo4jModel {

    protected final DBAtomSpace atomspace = new DBAtomSpace();

    public TripleAtomNeo4jModel(TripleGraph tripleGraph) {
        super(tripleGraph);
    }

    public TripleAtomNeo4jModel(DBNeo4jStorage storage, TripleGraph tripleGraph) {
        super(storage, tripleGraph);
    }

    protected abstract DBAtom toAtom(Triple triple);

    @Override
    public void storeTriples() {
        try (Session session = storage.driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                for (Triple triple : tripleGraph.getTriples()) {
                    putAtom(tx, toAtom(triple));
                }
                tx.success();
            }
        }

    }

    protected void putAtoms(List<DBAtom> atoms) {
        try (Session session = storage.driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                for (DBAtom atom : atoms) {
                    putAtom(tx, atom);
                }
                tx.success();
            }
        }
    }

    protected void putAtom(Transaction tx, DBAtom atom) {
        if (atom instanceof DBNode) {
            putNode(tx, (DBNode) atom);
        } else if (atom instanceof DBLink) {
            putLink(tx, (DBLink) atom);
        }
    }


    protected void putNode(Transaction tx, DBNode node) {
        tx.run("MERGE (:Atom:Node { id: {id}, type: {type}, value: {value}})  ",
                parameters("id", node.id,
                        "type", node.type,
                        "value", node.value));
    }

    protected void putLink(Transaction tx, DBLink link) {

        // Link(Type, atom1, ..., atomN)
        tx.run("MERGE (:Atom:Link { id: {id}, type: {type}})  ",
                parameters("id", link.id,
                        "type", link.type));

        for (int i = 0; i < link.atoms.length; i++) {
            DBAtom atom = link.atoms[i];
            putAtom(tx, atom);

            tx.run("MATCH (a1:Atom {id: {id1}})," +
                            " (a2:Atom {id: {id2}}) " +
                            " MERGE (a1)-[r:ARG {position: {position}}] ->(a2)",
                    parameters("position", i,
                            "id1", link.id,
                            "id2", atom.id));
        }
    }
}
