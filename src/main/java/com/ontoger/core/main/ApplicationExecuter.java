package com.ontoger.core.main;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.util.List;

public class ApplicationExecuter {

    public static void main(String[] args) {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OntologyCorrespondenceFinder correspondenceFinder = new OntologyCorrespondenceFinder(manager);

        List<OWLOntology> ontologyList = correspondenceFinder.loadOWLOntologies("RiceVarietyOntology.owl", "weed.owl");
        correspondenceFinder.decideSourceOntology(ontologyList);
        correspondenceFinder.matchSimilarityByName(ontologyList.get(0), ontologyList.get(1));
    }

}
