package com.ontoger.core.main;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLEntityRenamer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ApplicationExecuter {

    public static void main(String[] args) {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OntologyCorrespondenceFinder correspondenceFinder = new OntologyCorrespondenceFinder(manager);

        List<OWLOntology> ontologyList = correspondenceFinder.loadOWLOntologies("testOntology1.owl", "testOntology2" +
                ".owl");
//        correspondenceFinder.decideSourceOntology(ontologyList);
//        correspondenceFinder.matchSimilarityByName(ontologyList.get(0), ontologyList.get(1));

        //Testing the relationship between two concepts of two ontologies
        OWLOntology ontology = ontologyList.get(0);
        Set<OWLObjectProperty> ops = ontology.getObjectPropertiesInSignature();
        List<OWLObjectPropertyExpression> opl = new ArrayList<OWLObjectPropertyExpression>(ops);
        Set<OWLObjectPropertyRangeAxiom> rangeAxiom = ontology.getObjectPropertyRangeAxioms(opl.get(0));
        Set<OWLObjectPropertyDomainAxiom> domainAxiom = ontology.getObjectPropertyDomainAxioms(opl.get(0));
        List<OWLObjectPropertyDomainAxiom> opda = new ArrayList<>(domainAxiom);
        List<OWLObjectPropertyRangeAxiom> opra = new ArrayList<>(rangeAxiom);
        OWLClassExpression ce = opra.get(0).getRange();
        OWLClassExpression ce1 = opda.get(0).getDomain();
//        System.out.println("Now, the domain of this op:" + opl.get(0) + " is : " + ce1);
        System.out.println("Now, the range of this op:" + opl.get(0) + " is : " + ce);

        IRI ontology1IRI = ontology.getOntologyID().getOntologyIRI();

        //Lets place this op's range in a concept in the other ontology
        OWLOntology ontology2 = ontologyList.get(1);
        manager.addAxiom(ontology2, opra.get(0));

        IRI ontology2IRI = ontology2.getOntologyID().getOntologyIRI();

        Set<OWLObjectProperty> ops1 = ontology2.getObjectPropertiesInSignature();
        List<OWLObjectPropertyExpression> opl1 = new ArrayList<OWLObjectPropertyExpression>(ops1);
        Set<OWLObjectPropertyRangeAxiom> domainAxiom1 = ontology.getObjectPropertyRangeAxioms(opl1.get(0));
//        System.out.println(domainAxiom.toString());

        //Renaming the IRI of this domain axiom
        OWLEntityRenamer renamer = new OWLEntityRenamer(manager, manager.getOntologies());
        Set<OWLClass> c = ontology.getClassesInSignature();
        List<OWLClass> l = new ArrayList<>(c);
        OWLClass cls = l.get(2);
        System.out.println(cls.getIRI().getShortForm());
        List<OWLOntologyChange> changes = renamer.changeIRI(cls, ontology2IRI);
        manager.applyChanges(changes);


        List<OWLObjectPropertyRangeAxiom> opda1 = new ArrayList<>(domainAxiom1);
        OWLClassExpression ce2 = opda1.get(0).getRange();
        System.out.println("Range of op " + opl1.get(0) + "Now : " + ce2);


    }
}
