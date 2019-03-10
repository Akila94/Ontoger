package com.ontoger.utils;

import com.ontoger.exceptions.NoSuchOWLClassFoundException;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.Set;
import java.util.logging.Logger;

public class OWLUtils {

    static Logger log = Logger.getLogger(OWLUtils.class.getName());

    //This method returns the relevant owl class when the name provided
    public static OWLClass getOWLConceptFromName(String name, Set<OWLClass> classes) {
        OWLClass owlClass = null;
        for (OWLClass c : classes) {
            if (c.getIRI().toString().equals(name)) {
                owlClass = c;
            } else {
                continue;
            }
        }
        if (owlClass == null) {
            log.severe("The specified OWL class doesn't exist in this ontology.");
            throw new NoSuchOWLClassFoundException("No classs found in this ontology.");
        }
        return owlClass;
    }
}
