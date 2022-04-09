package de.paradubsch.paradubschmanager.config;

import de.paradubsch.paradubschmanager.models.PlayerData;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateConfigurator {
    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                Properties settings = new Properties();
                settings.put(Environment.DRIVER, ConfigurationManager.getString("hibernate.driver"));
                settings.put(Environment.URL, ConfigurationManager.getString("hibernate.url"));
                settings.put(Environment.USER, ConfigurationManager.getString("hibernate.user"));
                settings.put(Environment.PASS, ConfigurationManager.getString("hibernate.pass"));
                settings.put(Environment.DIALECT, ConfigurationManager.getString("hibernate.dialect"));

                settings.put(Environment.SHOW_SQL, ConfigurationManager.getString("hibernate.showSql"));

                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, ConfigurationManager.getString("hibernate.currentSessionContextClass"));

                settings.put(Environment.HBM2DDL_AUTO, ConfigurationManager.getString("hibernate.hmb2ddlAuto"));

                configuration.setProperties(settings);

                // Register Database Models
                configuration.addAnnotatedClass(PlayerData.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
