package de.paradubsch.paradubschmanager.config;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class ConfigurationManager {
    public static void copyDefaultConfiguration() {
        ParadubschManager instance = ParadubschManager.getInstance();
        FileConfiguration config = instance.getConfig();

        config.addDefault("chatPrefix", "&8&l[&6&lParadubsch&8&l] &r");
        config.addDefault("homePrice", 150);
        config.addDefault("http.server", "http://localhost:8090");
        config.addDefault("http.port", 8090);
        config.addDefault("http.maxThreads", 5);
        config.addDefault("http.minThreads", 2);
        config.addDefault("http.idleTimeout", 120);

        config.addDefault("tabprefix.default.prefix", "&7Spieler");
        config.addDefault("tabprefix.default.team", "999Spieler");
        config.addDefault("tabprefix.default.color", "&7");

        config.addDefault("bazaar.1.item", "OAK_LOG");
        config.addDefault("bazaar.1.qty", 16);
        config.addDefault("bazaar.1.offer", 25);
        config.addDefault("bazaar.1.buy", 5);

        config.addDefault("chatprefix.default.prefix", "&7Spieler");
        config.addDefault("chatprefix.default.namecolor", "&7");
        config.addDefault("chatprefix.default.chatcolor", "&7");

        config.addDefault("hibernate.driver", "com.mysql.cj.jdbc.Driver");
        config.addDefault("hibernate.url", "jdbc:mysql://localhost:3306/test?useSSL=false");
        config.addDefault("hibernate.user", "testuser");
        config.addDefault("hibernate.pass", "testpassword");
        config.addDefault("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        config.addDefault("hibernate.showSql", "true");
        config.addDefault("hibernate.currentSessionContextClass", "thread");
        config.addDefault("hibernate.hmb2ddlAuto", "create-drop");

        config.addDefault("hibernate.cachingEnabled", "false");

        config.options().copyDefaults(true);
        instance.saveConfig();
    }

    public static @NotNull String getString(String path) {
        String value = ParadubschManager.getInstance().getConfig().getString(path);
        if (value == null) {
            ParadubschManager.getInstance().getLogger().warning("The path " + path + " is not set in the config.yml");
            return "";
        }
        return value;
    }

    public static int getInt(String path) {
        return ParadubschManager.getInstance().getConfig().getInt(path);
    }

    public static FileConfiguration getConfig() {
        return ParadubschManager.getInstance().getConfig();
    }

    public static FileConfiguration getCustomConfig(String filename) {
        ParadubschManager instance = ParadubschManager.getInstance();

        if (!instance.getDataFolder().exists()) {
            try {
                instance.getDataFolder().mkdir();
            } catch (Exception e) {
                MessageAdapter.sendConsoleError(e);
            }
        }

        File file = new File(instance.getDataFolder(), filename);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                MessageAdapter.sendConsoleError(e);
            }

        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public static void saveCustomConfig (FileConfiguration config, String filename) {
        ParadubschManager instance = ParadubschManager.getInstance();

        if (!instance.getDataFolder().exists()) {
            try {
                instance.getDataFolder().mkdir();
            } catch (Exception e) {
                MessageAdapter.sendConsoleError(e);
            }
        }

        File file = new File(instance.getDataFolder(), filename);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                MessageAdapter.sendConsoleError(e);
            }

        }

        try {
            config.save(file);
        } catch (IOException e) {
            MessageAdapter.sendConsoleError(e);
        }
    }
}
