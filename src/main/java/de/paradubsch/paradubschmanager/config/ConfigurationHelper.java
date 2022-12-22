package de.paradubsch.paradubschmanager.config;

import de.craftery.util.ConfigurationManager;

public class ConfigurationHelper {
    public static void addSpecificConfurations() {
        ConfigurationManager.addDefault("homePrice", 150);

        ConfigurationManager.addDefault("http.server", "http://localhost:8090");
        ConfigurationManager.addDefault("http.port", 8090);
        ConfigurationManager.addDefault("http.maxThreads", 5);
        ConfigurationManager.addDefault("http.minThreads", 2);
        ConfigurationManager.addDefault("http.idleTimeout", 120);

        ConfigurationManager.addDefault("tabprefix.default.prefix", "&7Spieler");
        ConfigurationManager.addDefault("tabprefix.default.team", "999Spieler");
        ConfigurationManager.addDefault("tabprefix.default.color", "&7");

        ConfigurationManager.addDefault("bazaar.1.item", "OAK_LOG");
        ConfigurationManager.addDefault("bazaar.1.qty", 16);
        ConfigurationManager.addDefault("bazaar.1.offer", 25);
        ConfigurationManager.addDefault("bazaar.1.buy", 5);

        ConfigurationManager.addDefault("chatprefix.default.prefix", "&7Spieler");
        ConfigurationManager.addDefault("chatprefix.default.namecolor", "&7");
        ConfigurationManager.addDefault("chatprefix.default.chatcolor", "&7");


        ConfigurationManager.addDefault("timevote.createCooldown", 1800000L);
    }
}
