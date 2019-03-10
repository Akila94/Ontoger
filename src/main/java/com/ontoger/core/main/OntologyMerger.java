package com.ontoger.core.main;

import com.ontoger.core.constants.CommonConstants;
import org.apache.commons.io.FileUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OntologyMerger {

    public static void main(String[] args) throws OWLOntologyCreationException {

        OntologyMerger merger = new OntologyMerger();

        OWLOntology ontology1 = merger.getOntology("testOntology1.owl");
        OWLOntology ontology2 = merger.getOntology("testOntology2.owl");

        merger.readSubClasses(ontology1);

    }

    //Import ontology to program
    public OWLOntology getOntology(String ontologyName) throws OWLOntologyCreationException {
        //Get both ontologies to the program.
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(FileUtils.getFile(CommonConstants
                .ONTOLOGY_FILE_PATH + ontologyName));

        return ontology;
    }

    //reading sub classes of a super class
    public void readSubClasses(OWLOntology ontology) {

        for (final OWLSubClassOfAxiom subClassOfAxiom : ontology.getAxioms(AxiomType.SUBCLASS_OF)) {
            if (subClassOfAxiom.getSuperClass() instanceof OWLClass && subClassOfAxiom.getSubClass() instanceof OWLClass) {
                System.out.println(((OWLClass) subClassOfAxiom.getSubClass()).getIRI().getShortForm() +
                        " extends " + ((OWLClass) subClassOfAxiom.getSuperClass()).getIRI().getShortForm());
            }
        }
    }

    //Decide what is the source ontology
    public List<OWLOntology> decideSourceOntology(OWLOntology ontology1, OWLOntology ontology2) {
        OWLOntology sourceOntology;
        OWLOntology destOntology;

        Set<OWLClass> classesOfOntology1 = ontology1.getClassesInSignature();
        Set<OWLClass> classesOfOntology2 = ontology2.getClassesInSignature();
        if (classesOfOntology1.size() > classesOfOntology2.size()) {
            sourceOntology = ontology2;
            destOntology = ontology1;
        } else {
            sourceOntology = ontology1;
            destOntology = ontology2;
        }

        List<OWLOntology> ontologies = new ArrayList<>();
        ontologies.add(sourceOntology);
        ontologies.add(destOntology);
        return ontologies;
    }

}
