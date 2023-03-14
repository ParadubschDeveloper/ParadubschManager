package de.craftery.util;


public class TestDatabaseConnection {
    public TestDatabaseConnection() {
        HibernateConfigurator.getSessionFactory().openSession();
        HibernateConfigurator.shutdown();
    }
}
