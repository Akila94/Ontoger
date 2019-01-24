package com.ontoger.core.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionInitializer {

    public static SessionFactory getSessionFactoryForClass(Class className) {
        return new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(className)
                .buildSessionFactory();
    }
}
