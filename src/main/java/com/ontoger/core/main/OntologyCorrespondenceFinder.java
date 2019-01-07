package com.ontoger.core.main;

import com.ontoger.core.constants.CommonConstants;
import org.apache.commons.io.FileUtils;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OntologyCorrespondenceFinder {

    private OWLOntologyManager ontologyManager;
    private OWLOntology sourceOntology;
    private OWLOntology destOntology;

    public OntologyCorrespondenceFinder(OWLOntologyManager ontologyManager) {
        this.ontologyManager = ontologyManager;
    }

    //Should provide the names of the ontologies to find correspondences.
    public List<OWLOntology> loadOWLOntologies(String sourceOntology,
                                              String destOntology) {
        List<OWLOntology> ontologyList = new ArrayList<>();
        try {
            ontologyList.add(ontologyManager.loadOntologyFromOntologyDocument(FileUtils.getFile(CommonConstants
                    .ONTOLOGY_FILE_PATH + sourceOntology)));
            ontologyList.add(ontologyManager.loadOntologyFromOntologyDocument(FileUtils.getFile(CommonConstants
                    .ONTOLOGY_FILE_PATH + destOntology)));
        } catch (OWLOntologyCreationException e) {
            e.getMessage();
        }
        return ontologyList;
    }

    public void decideSourceOntology(List<OWLOntology> ontologyList) {
        sourceOntology = ontologyList.get(0);
        destOntology = ontologyList.get(1);

        Set<OWLObjectProperty> sourceOPs = sourceOntology.getObjectPropertiesInSignature();
        Set<OWLObjectProperty> destOPs = destOntology.getObjectPropertiesInSignature();

        if (sourceOPs.size() > destOPs.size()) {
            sourceOntology = ontologyList.get(1);
            destOntology = ontologyList.get(0);
        }
    }

    public void matchSimilarityByName(OWLOntology sourceOntology, OWLOntology destOntology) {
        Set<OWLClass> sourceClasses = sourceOntology.getClassesInSignature();
        Set<OWLClass> destClasses = destOntology.getClassesInSignature();

        for (OWLClass sClass : sourceClasses) {
            for (OWLClass dClass : destClasses) {
                if (sClass.getIRI().getShortForm().equalsIgnoreCase(dClass.getIRI().getShortForm())) {
                    System.out.println("Class" + sClass + "is same as " + dClass);
                    //Add code to add these classes to a database
                }
            }
        }
    }

}
