package com.ontoger.utils;

import com.ontoger.exceptions.NoSuchOWLClassFoundException;
import org.semanticweb.owlapi.model.OWLClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class OWLUtils {

    static Logger log = LoggerFactory.getLogger(OWLUtils.class);

    //This method returns the relevant owl class when the name provided
    public static OWLClass getOWLConceptFromName(String name, Set<OWLClass> classes) {
        OWLClass owlClass = null;
        for(OWLClass c : classes) {
            if (c.getIRI().toString().equals(name)) {
                owlClass = c;
            } else {
                continue;
            }
        }
        if (owlClass == null) {
            log.error("The specified OWL class doesn't exist in this ontology.");
            throw new NoSuchOWLClassFoundException("No classs found in thie ontology.");
        }
        return owlClass;
    }

}
