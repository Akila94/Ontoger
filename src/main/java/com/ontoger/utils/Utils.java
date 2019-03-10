package com.ontoger.utils;

import com.google.common.collect.HashMultimap;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class Utils {

    static Logger log = Logger.getLogger(Utils.class.getName());

    public static ArrayList<String> getFilteredClasses(int level, OWLOntology ontology, HashMultimap<Integer,
            String> classes) {
        String ontologyIRI = ontology.getOntologyID().getOntologyIRI().toString();
        ArrayList<String> list = new ArrayList<>();
        Collection<String> classesOfLevel = null;
        try {
            for (Entry<Integer, Collection<String>> entry : classes.asMap().entrySet()) {
                if (entry.getKey().equals(level)) {
                    classesOfLevel = entry.getValue();
                }
            }
            for (String s : classesOfLevel) {
                if (s.contains(ontologyIRI)) {
                    list.add(s);
                }
            }
        } catch (NullPointerException e) {
            log.warning("No classes exist in level " + level + "in ontology " + ontologyIRI + ".");
            throw new NullPointerException("No Classes exist for level " + level + "in ontology " + ontologyIRI + ".");
        }
        return list;
    }

    public static int getNumberOfLevelsInOntology(HashMultimap<Integer, String> multimap, OWLOntology ontology) {
        String ontologyIRI = ontology.getOntologyID().getOntologyIRI().toString();
        ArrayList<Integer> list = new ArrayList<>();
        for (Entry<Integer, Collection<String>> entry : multimap.asMap().entrySet()) {
            Collection c = entry.getValue();
            for (Object s : c) {
                if (s.toString().contains(ontologyIRI)) {
                    list.add(entry.getKey());
                }
            }
        }
        return Collections.max(list);
    }
}
