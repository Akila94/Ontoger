package com.ontoger.core.main;

import com.google.common.collect.HashMultimap;
import com.ontoger.core.constants.CommonConstants;
import com.ontoger.matchers.DataMuseAPI;
import com.ontoger.utils.Utils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class AlgorithmTester {

    private static HashMultimap<Integer, String> classesNLevels;
    private static OWLOntology sourceOntology;
    private static OWLOntology destOntology;
    private static Set<OWLClass> sourceClasses;
    private static Set<OWLClass> destClasses;

    public static void main(String[] args) throws IOException {
//        int noOfClassesInSource;
//        int noOfClassesInDest;
        int levelsOfSource;
        int levelsOfDest;
//
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OntologyMerger merger = new OntologyMerger();
        DataMuseAPI dataMuseAPI = new DataMuseAPI();
        ClassLevelExtractor extractor = new ClassLevelExtractor();
//
        try {
            sourceOntology = merger.getOntology(CommonConstants.SOURCE_ONTOLOGY_FILE_NAME);
            destOntology = merger.getOntology(CommonConstants.DEST_ONTOLOGY_FILE_NAME);
//
            List<OWLOntology> ontologies = merger.decideSourceOntology(sourceOntology, destOntology);
//
            sourceOntology = ontologies.get(0);
            destOntology = ontologies.get(1);

            classesNLevels = extractor.getClassesWithLevels(CommonConstants.SOURCE_ONTOLOGY_FILE_NAME,
                    CommonConstants.DEST_ONTOLOGY_FILE_NAME);

            levelsOfSource = Utils.getNumberOfLevelsInOntology(classesNLevels, sourceOntology) + 1;
            levelsOfDest = Utils.getNumberOfLevelsInOntology(classesNLevels, destOntology) + 1;
            sourceClasses = sourceOntology.getClassesInSignature();
            destClasses = destOntology.getClassesInSignature();
//            noOfClassesInSource = sourceClasses.size();
//            noOfClassesInDest = destClasses.size();

            List<String> classesOfLevelsOfO1;
            List<String> classesOfLevelsOfO2;

            for (int i = 0; i < levelsOfSource; i++) {
                classesOfLevelsOfO1 = Utils.getFilteredClasses(i, sourceOntology, classesNLevels);
                outerLoop:
                for (String s : classesOfLevelsOfO1) {
                    for (int j = 0; j < levelsOfDest; j++) {
                        classesOfLevelsOfO2 = Utils.getFilteredClasses(j, destOntology, classesNLevels);
                        innerLoop:
                        for (String c : classesOfLevelsOfO2) {
//                            System.out.println("Class : " + s + " is compared with class : " + c);
                            String s1 = Utils.getShortName(s);
                            String s2 = Utils.getShortName(c);
                            if (!Utils.isMultiWord(s1) && !Utils.isMultiWord(s2)) {
                                if (s1.equalsIgnoreCase(s2)) {
                                    //Add relationship and add to triple
                                    System.out.println(s1 + " Similar to " + s2);
                                    continue outerLoop;
                                } else if (dataMuseAPI.isRelated(s1, s2, CommonConstants.DATAMUSE_SYNONYM)) {
                                    //Add relationship and add to triple
                                    System.out.println("Synonym");
                                    continue outerLoop;
                                } else if (dataMuseAPI.isRelated(s1, s2, CommonConstants.DATAMUSE_HYPERNYM)) {
                                    //Add relationship and add to triple
                                    System.out.println("Hypernym");
                                    continue outerLoop;
                                } else if (dataMuseAPI.isRelated(s1, s2, CommonConstants.DATAMUSE_HYPONYM)) {
                                    //Add relationship and add to triple
                                    System.out.println("Hyponym");
                                    continue outerLoop;
                                } else if (dataMuseAPI.isRelated(s1, s2, CommonConstants.DATAMUSE_COMPRISE)) {
                                    //Add relationship and add to triple
                                    System.out.println("Comprise");
                                    continue outerLoop;
                                } else if (dataMuseAPI.isRelated(s1, s2, CommonConstants.DATAMUSE_PART_OF)) {
                                    //Add relationship and add to triple
                                    System.out.println("Part Of");
                                    continue outerLoop;
                                }
                            }
                        }
                        classesOfLevelsOfO2.clear();
                    }
                }
                classesOfLevelsOfO1.clear();
            }
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
    }

}
