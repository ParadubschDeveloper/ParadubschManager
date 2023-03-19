package de.craftery.util;

import org.hibernate.Session;

public class TestDatabaseConnection {
    public TestDatabaseConnection() {
        Session session = HibernateConfigurator.getSessionFactory().openSession();
        session.close();
        HibernateConfigurator.shutdown();
    }
}
