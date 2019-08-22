package sample;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.File;

/**
 * Proved path to the ontology file as the first argument.
 */
public class OntologyParserSample {

    public static void main(String[] args) throws Exception {

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        createOntology(manager);

        if (args.length == 0) {
            System.err.println("Pass path to the ontology file as the first argument!");
            System.exit(-1);
        }

        String ontologyFile = args[0];
        loadOntology(manager, ontologyFile);
    }

    private static void createOntology(OWLOntologyManager manager) throws Exception {

        OWLOntology ontology = manager.createOntology();
        ontology.saveOntology(new FunctionalSyntaxDocumentFormat(), System.out);

    }

    private static void loadOntology(OWLOntologyManager manager, String ontologyFile) throws Exception {
        System.out.printf("Ontology file: %s%n", ontologyFile);
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(ontologyFile));
        System.out.printf("ontology size: %d%n", manager.ontologies().count());
    }
}
