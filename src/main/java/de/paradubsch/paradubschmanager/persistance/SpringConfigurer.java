package de.paradubsch.paradubschmanager.persistance;

import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ResourceLoader;

import java.util.Properties;

public class SpringConfigurer {
    private static Properties getConfiguration() {
        Properties props = new Properties();
        props.put("spring.datasource.driver-class-name", ConfigurationManager.getString("hibernate.driver"));
        props.put("spring.datasource.username", ConfigurationManager.getString("hibernate.user"));
        props.put("spring.datasource.password", ConfigurationManager.getString("hibernate.pass"));
        props.put("spring.datasource.url", ConfigurationManager.getString("hibernate.url"));
        props.put("spring.jpa.show-sql", ConfigurationManager.getString("hibernate.showSql"));
        props.put("spring.jpa.hibernate.ddl-auto", ConfigurationManager.getString("hibernate.hmb2ddlAuto"));
        props.put("spring.jpa.properties.hibernate.dialect", ConfigurationManager.getString("hibernate.dialect"));
        props.put("spring.flyway.enabled", "false");
        props.put("logging.level.org.springframework", "DEBUG");
        if (ConfigurationManager.getString("hibernate.cachingEnabled").equals("true")) {
            props.put("spring.jpa.properties.javax.persistence.sharedCache.mode", "ENABLE_SELECTIVE");
            props.put("spring.jpa.properties.hibernate.cache.use_second_level_cache", "true");
            props.put("spring.jpa.properties.hibernate.cache.use_query_cache", "true");
            props.put("spring.jpa.properties.hibernate.cache.region.factory_class", "org.hibernate.cache.jcache.JCacheRegionFactory");
            /*settings.put("hibernate.cache.ehcache.missing_cache_strategy", "create");
            settings.put("hibernate.javax.cache.uri", "/ehcache.xml");*/
        }
        return props;
    }

    public static ConfigurableApplicationContext initializeSpringApplication(ResourceLoader loader) {
        return new SpringApplicationBuilder(SpringApplication.class).properties(getConfiguration()).run();
    }
}
