package com.ontoger.matchers;

import com.ontoger.core.constants.CommonConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.semanticweb.owlapi.model.OWLClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ConceptMatcher {

    Logger log = LoggerFactory.getLogger(ConceptMatcher.class);

    public boolean isSimilarBySynonymy(OWLClass concept1, OWLClass concept2) {
        String nameOfConcept1 = concept1.getIRI().getShortForm();
        String nameOfConcept2 = concept2.getIRI().getShortForm();
        try {
            final Document document = Jsoup.connect(CommonConstants.WORDNET_URL + nameOfConcept1).get();
            List<String> synSet = document.select("div.container.main")
                                                .select("li.syn")
                                                .select("a[href]")
                                                .eachText();
            if (synSet.stream().anyMatch(x -> x.equalsIgnoreCase(nameOfConcept2))) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            log.error("I/O Exception\n", e);
        }
        return true;
    }

    public boolean isSimilarByWord(OWLClass concept1, OWLClass concept2) {
        if (concept1.getIRI().toString().equalsIgnoreCase(concept2.getIRI().toString())) {
            return true;
        } else {
            return false;
        }
    }
}
