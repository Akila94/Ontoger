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

    /**
     * This method determines whether a given string is a single word or a multi word
     * The separators considered are _, -, and +.
     * CamelCase words are also determined.
     *
     * @param s The string to be determined
     * @return Returns true if the string is multi word
     */
    public static boolean isMultiWord(String s) {
        boolean returnValue;
        if (!s.contains("_") && !s.contains("-") && !s.contains("+")) {
            returnValue = false;
        } else {
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * Determines a string is multi worded of not
     *
     * @param s String to be determined
     * @return True if multiword, false if single word
     */
    public static boolean isMultiWordString(String s) {
        //Detect camel case strings and split into words
        String words = s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
        if (words.contains(" "))
            return true;
        else
            return false;
    }

    public static String getShortName(String s) {
        return s.substring(s.lastIndexOf("#") + 1);
    }
}
