package com.ontoger.core.hibernate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ontoger.core.main.ClassLevelExtractor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBOperationHandler {

    Logger log = LoggerFactory.getLogger(DBOperationHandler.class);

    SessionFactory factory = SessionInitializer.getSessionFactoryForClass(ConceptLevelsTable.class);
    ClassLevelExtractor extractor = new ClassLevelExtractor();

    public void writeClassesWithLevelsToDB() {
        JsonObject classes = extractor.getClassesWithLevels("weed.owl");
        Session session = factory.getCurrentSession();
        log.info("Beginning transaction...");
        Transaction tx = session.beginTransaction();
        log.info("Transaction began.");

        try {
            for (int i = 0; i < classes.size(); i++) {
                JsonArray array = classes.getAsJsonArray(Integer.toString(i));
                for (int j = 0; j < array.size(); j++) {
                    ConceptLevelsTable conceptLevelsTable = new ConceptLevelsTable(i, array.get(j).toString());
                    session.save(conceptLevelsTable);
                    log.info("Records written to the database!!!");
                    //Adding code for batch insert in hibernate
                    if (j % 20 == 0) {
                        //flush batch inserts and release memory
                        log.warn("Flushing memory...");
                        session.flush();
                        session.clear();
                    }
                }
            }
            log.info("Committing transaction...");
            tx.commit();
            session.close();
            log.info("Transaction committed succssfully!!!");
        } finally{
            factory.close();
        }
    }

    public static void main (String[] args){
        DBOperationHandler handler = new DBOperationHandler();
        handler.writeClassesWithLevelsToDB();
    }
}
