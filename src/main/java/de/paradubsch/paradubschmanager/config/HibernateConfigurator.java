package de.paradubsch.paradubschmanager.config;

import de.paradubsch.paradubschmanager.models.*;
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
                String dbUrl = ConfigurationManager.getEnvironmentVariable("DB_URL") != null ?
                        ConfigurationManager.getEnvironmentVariable("DB_URL") :
                        ConfigurationManager.getString("hibernate.url");
                settings.put(Environment.URL, dbUrl);
                settings.put(Environment.USER, ConfigurationManager.getString("hibernate.user"));

                String dbPass = ConfigurationManager.getEnvironmentVariable("MYSQL_ROOT_PASSWORD") != null ?
                        ConfigurationManager.getEnvironmentVariable("MYSQL_ROOT_PASSWORD") :
                        ConfigurationManager.getString("hibernate.pass");
                settings.put(Environment.PASS, dbPass);
                settings.put(Environment.DIALECT, ConfigurationManager.getString("hibernate.dialect"));

                settings.put(Environment.CONNECTION_PROVIDER, "org.hibernate.c3p0.internal.C3P0ConnectionProvider");

                settings.put(Environment.SHOW_SQL, ConfigurationManager.getString("hibernate.showSql"));

                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, ConfigurationManager.getString("hibernate.currentSessionContextClass"));

                settings.put(Environment.HBM2DDL_AUTO, ConfigurationManager.getString("hibernate.hmb2ddlAuto"));

                if (ConfigurationManager.getString("hibernate.cachingEnabled").equals("true")) {
                    settings.put(Environment.USE_SECOND_LEVEL_CACHE, true);
                    settings.put(Environment.CACHE_REGION_FACTORY, "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
                    settings.put("hibernate.cache.ehcache.missing_cache_strategy", "create");

                    settings.put("hibernate.javax.cache.uri", "/ehcache.xml");
                }

                configuration.setProperties(settings);

                // Register Database Models
                configuration.addAnnotatedClass(PlayerData.class);
                configuration.addAnnotatedClass(Home.class);
                configuration.addAnnotatedClass(SaveRequest.class);
                configuration.addAnnotatedClass(PunishmentHolder.class);
                configuration.addAnnotatedClass(WarnPunishment.class);
                configuration.addAnnotatedClass(BanPunishment.class);
                configuration.addAnnotatedClass(MutePunishment.class);
                configuration.addAnnotatedClass(PunishmentUpdate.class);
                configuration.addAnnotatedClass(Warp.class);
                configuration.addAnnotatedClass(WorkerPlayer.class);
                configuration.addAnnotatedClass(BazaarOrder.class);
                configuration.addAnnotatedClass(BazaarCollectable.class);
                configuration.addAnnotatedClass(Backpack.class);
                configuration.addAnnotatedClass(StorageSlot.class);
                configuration.addAnnotatedClass(ItemData.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory == null) return;

        if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
            sessionFactory.getCurrentSession().flush();
        }

        sessionFactory.getCurrentSession().close();
        sessionFactory.getCurrentSession().disconnect();
        sessionFactory.close();

        sessionFactory = null;

    }
}
