package atomspace.performance.storage;

import atomspace.performance.DBAtom;
import atomspace.performance.DBAtomSpace;
import atomspace.performance.DBLink;
import atomspace.performance.DBNode;
import atomspace.performance.triple.Triple;

public interface TripleAtomModel extends TripleModel {

    String TYPE_EVALUATION = "EvaluationLink";
    String TYPE_LIST = "ListLink";
    String TYPE_PREDICATE = "PredicateNode";
    String TYPE_CONCEPT = "ConceptNode";
    String TYPE_SUBJECT = "SubjectNode";
    String TYPE_OBJECT = "ObjectNode";

    static DBAtom toAtomPredicate(DBAtomSpace atomspace, Triple triple) {
        return atomspace.getLink(toLinkLabel(triple.predicate),
                atomspace.getNode(TYPE_SUBJECT, triple.subject),
                atomspace.getNode(TYPE_OBJECT, triple.object));
    }


    static DBAtom toAtomEvaluation(DBAtomSpace atomspace, Triple triple) {
        return atomspace.getLink(TYPE_EVALUATION,
                atomspace.getNode(TYPE_PREDICATE, triple.predicate),
                atomspace.getLink(TYPE_LIST,
                        atomspace.getNode(TYPE_CONCEPT, triple.subject),
                        atomspace.getNode(TYPE_CONCEPT, triple.object)));
    }

    static void handleAtom(DBAtom atom, AtomHandler handler) {
        if (atom instanceof DBNode) {
            handler.handleNode((DBNode) atom);
        } else if (atom instanceof DBLink) {
            DBLink link = (DBLink) atom;
            handler.handleLinkBegin(link);
            for (DBAtom a : link.atoms) {
                handleAtom(a, handler);
            }
            handler.handleLinkEnd(link);
        }
    }

    static String toLinkLabel(String name) {
        StringBuilder builder = new StringBuilder();
        builder.append(name.substring(0, 1).toUpperCase());
        for (int i = 1; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '-') {
            } else if (Character.isDigit(c)) {
                builder.append((char) ('a' + (int) (c - '0')));
            } else {
                builder.append(c);
            }
        }
        builder.append("Link");
        return builder.toString();
    }

    interface AtomHandler {

        void handleNode(DBNode node);

        void handleLinkBegin(DBLink link);

        void handleLinkEnd(DBLink link);
    }
}
