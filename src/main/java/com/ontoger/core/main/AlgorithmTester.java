package com.ontoger.core.main;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.util.List;
import java.util.Set;

public class AlgorithmTester {

    public static void main(String[] args) {

        OWLOntology sourceOntology;
        OWLOntology destOntology;
        int noOfClassesInSource;
        int noOfClassesInDest;

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OntologyMerger merger = new OntologyMerger();
        OntologyCorrespondenceFinder matcher = new OntologyCorrespondenceFinder(manager);

        try {
            sourceOntology = merger.getOntology("TestOntology1.owl");
            destOntology = merger.getOntology("TestOntology2.owl");

            List<OWLOntology> ontologies = merger.decideSourceOntology(sourceOntology, destOntology);

            sourceOntology = ontologies.get(0);
            destOntology = ontologies.get(1);

            Set<OWLClass> sClasses = sourceOntology.getClassesInSignature();
            Set<OWLClass> dClasses = destOntology.getClassesInSignature();

            noOfClassesInSource = sClasses.size();
            noOfClassesInDest = dClasses.size();

            for (int sourceClass = 0; sourceClass < noOfClassesInSource; sourceClass++) {
                for (int destClass = 0; destClass < noOfClassesInDest; destClass++) {

                }
            }

        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
    }

}
