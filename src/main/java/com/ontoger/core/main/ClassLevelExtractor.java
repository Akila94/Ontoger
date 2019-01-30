package com.ontoger.core.main;

import com.google.common.collect.HashMultimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.ontoger.core.constants.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class reads the whole ontology with its class levels.
 * The Apache Jena API is used to do this.
 * TESTED AND WORKING FINE
 */
public class ClassLevelExtractor {

    private Logger log = LoggerFactory.getLogger(ClassLevelExtractor.class);
    private List ontClasses;
    private HashMultimap<Integer, String> classesWithLevel = HashMultimap.create();

    //Reading the ontology to the class
    private void readOntology(String file, OntModel model) {
        InputStream in;
        try {
            in = new FileInputStream(file);
            model.read(in, "RDF/XML");
            in.close();
            log.info("Getting all concepts to a list");
            ontClasses = model.listClasses().toList();
            if (ontClasses.isEmpty()) {
                log.warn("Returning as no classes found.");
                return;
            }
        } catch (IOException e) {
            log.error("Couldn't import the ontology." + e.getMessage());
        }
    }

    //Recurse down to subclasses starting from a class
    private void traverse(OntClass oc, List<OntClass> occurs, int depth) {
        if (oc == null) {
            log.warn("Returning null as no ListMultimap.");
            return;
        }
        if (oc.getLocalName() == null || oc.getLocalName().equals("Nothing")) {
            log.warn("Returning null class name is null or class is Nothing.");
            return;
        }

        classesWithLevel.put(depth, oc.toString());

        if (oc.canAs(OntClass.class) && !occurs.contains(oc)) {
            for (Iterator<OntClass> i = oc.listSubClasses(true); i.hasNext();) {
                OntClass subClass = i.next();
                occurs.add(oc);
                traverse(subClass, occurs, depth + 1);
                occurs.remove(oc);
            }
        }
    }

    //Traverse the ontology to find the all given concepts
    private void traverseStart(OntModel model, OntClass ontClass) {
        if (ontClass != null) {
            traverse(ontClass, new ArrayList<OntClass>(), 0);
            return;
        }

        Iterator<OntClass> i = model.listHierarchyRootClasses();

        while (i.hasNext()) {
            OntClass tmp = i.next();
            traverse(tmp, new ArrayList<OntClass>(), 0);
        }
    }

    //All concepts are extracted with their relative level successfully, tested and working fine.
    public JsonObject getClassesWithLevels(String ontologyName) {
        OntModel model = ModelFactory.createOntologyModel();
        readOntology(CommonConstants.ONTOLOGY_FILE_PATH + ontologyName, model);
        traverseStart(model, null);

        //Converting the
        Map classesMap = classesWithLevel.asMap();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gsonObject = gsonBuilder.create();
        String classesJson = gsonObject.toJson(classesMap);

        JsonParser parser = new JsonParser();
        JsonObject classesJsonObj = (JsonObject) parser.parse(classesJson);
        return classesJsonObj;
    }

    public static void main(String[] args) {
        ClassLevelExtractor extractor = new ClassLevelExtractor();
        JsonObject s = extractor.getClassesWithLevels("testOntology1.owl");
        JsonArray a = s.getAsJsonArray(Integer.toString(0));
        System.out.println(a);
    }

}
