package sample;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.LabelFunctionalDocumentFormat;
import org.semanticweb.owlapi.formats.RDFJsonDocumentFormat;
import org.semanticweb.owlapi.model.*;

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

        IRI IOR = IRI.create("http://owl.api.tutorial");

        OWLOntology ontology = manager.createOntology();

        System.out.printf("axioms: %d, format: %s%n", ontology.getAxiomCount(), manager.getOntologyFormat(ontology));

        OWLDataFactory factory = manager.getOWLDataFactory();
        OWLClass person = factory.getOWLClass(IOR + "#Person");
        OWLDeclarationAxiom personAxiom = factory.getOWLDeclarationAxiom(person);

        ontology.add(personAxiom);

        OWLClass woman = factory.getOWLClass(IOR + "#Woman");
        OWLSubClassOfAxiom womanAxiom = factory.getOWLSubClassOfAxiom(woman, person);
        ontology.add(womanAxiom);

        ontology.saveOntology(new RDFJsonDocumentFormat(), System.out);
    }

    private static void loadOntology(OWLOntologyManager manager, String ontologyFile) throws Exception {
        System.out.printf("Ontology file: %s%n", ontologyFile);
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(ontologyFile));
        System.out.printf("ontology size: %d%n", manager.ontologies().count());
        System.out.printf("axioms: %d, format: %s%n", ontology.getAxiomCount(), manager.getOntologyFormat(ontology));
        ontology.saveOntology(new RDFJsonDocumentFormat(), System.out);
    }
}
