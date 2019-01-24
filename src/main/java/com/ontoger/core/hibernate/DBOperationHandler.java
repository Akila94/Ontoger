package com.ontoger.core.hibernate;

import org.hibernate.SessionFactory;

public class DBOperationHandler {

    SessionFactory factory = SessionInitializer.getSessionFactoryForClass(ConceptLevelsTable.class);

    public void writeClassesWithLevelsToDB() {

    }

}
