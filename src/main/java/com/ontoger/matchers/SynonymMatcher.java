//package com.ontoger.matchers;
//
//import com.ontoger.core.constants.CommonConstants;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.semanticweb.owlapi.model.OWLClass;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//
//public class SynonymMatcher {
//
//    Logger log = LoggerFactory.getLogger(SynonymMatcher.class);
//
//    private void isSimilarBySynonymity() {
//        try {
//            final Document document = Jsoup.connect(CommonConstants.WORDNET_URL).get();
//
//        } catch (IOException e) {
//            log.error("I/O Exception\n", e);
//        }
//    }
//
//    public static void main(String[] args) {
//        SynonymMatcher matcher = new SynonymMatcher();
//        matcher.isSimilarBySynonymity();
//    }
//
//}
