package com.ontoger.core.hibernate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ontoger.core.main.ClassLevelExtractor;
import com.ontoger.exceptions.NoResultsReturnedException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.logging.Logger;

/**
 * This class handles all the database operations happen in this application.
 * TESTED AND WORKING FINE
 */
public class DBOperationHandler {

    Logger log = Logger.getLogger(DBOperationHandler.class.getName());

    SessionFactory factory = SessionInitializer.getSessionFactoryForClass(ConceptLevelsTable.class);
    ClassLevelExtractor extractor = new ClassLevelExtractor();

//    public void writeClassesWithLevelsToDB() {
//        JsonObject classes = extractor.getClassesWithLevels("testOnto2V1.owl");
//        Session session = factory.getCurrentSession();
//
//        try {
//            log.info("Beginning transaction...");
//            session.beginTransaction();
//            for (int i = 0; i < classes.size(); i++) {
//                JsonArray array = classes.getAsJsonArray(Integer.toString(i));
//                for (int j = 0; j < array.size(); j++) {
//                    ConceptLevelsTable conceptLevelsTable = new ConceptLevelsTable(i, array.get(j).toString());
//                    session.save(conceptLevelsTable);
//                    log.info("Records written to the database!!!");
//                    //Adding code for batch insert in hibernate
//                    if (j % 20 == 0) {
//                        //flush batch inserts and release memory
//                        log.warn("Flushing memory...");
//                        session.flush();
//                        session.clear();
//                    }
//                }
//            }
//            log.info("Committing transaction...");
//            session.getTransaction().commit();
//            session.close();
//            log.info("Transaction committed succssfully!!!");
//        } finally{
//            factory.close();
//        }
//    }

    public List<String> getClassesByLevelFromDB(int level) {
        factory = SessionInitializer.getSessionFactoryForClass(ConceptLevelsTable.class);
        Session session = factory.getCurrentSession();

        try {
            log.info("Beginning transaction...");
            session.beginTransaction();
            String query = "select t.name from ConceptLevelsTable t where t.level=:level";
            List<String> result = session.createQuery(query).setParameter("level", level).list();
            if (result == null) {
                log.severe("Query didn't return any records. Please add some data before querying. Exiting...");
                throw new NoResultsReturnedException("Table is empty or no matches for query.");
            }
            session.getTransaction().commit();
            return result;
        } finally {
            factory.close();
        }
    }

//    public static void main(String[] args) {
//        DBOperationHandler handler = new DBOperationHandler();
//        handler.writeClassesWithLevelsToDB();
//    }
}
