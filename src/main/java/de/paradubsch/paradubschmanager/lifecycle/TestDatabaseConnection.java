package de.paradubsch.paradubschmanager.lifecycle;

import de.paradubsch.paradubschmanager.config.HibernateConfigurator;

public class TestDatabaseConnection {
    public TestDatabaseConnection() {
        HibernateConfigurator.getSessionFactory().openSession();
        HibernateConfigurator.shutdown();
    }
}
