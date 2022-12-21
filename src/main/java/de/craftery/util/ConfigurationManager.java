package de.craftery.util;

import de.craftery.CraftPlugin;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class ConfigurationManager {
    public static void copyDefaultConfiguration() {
        JavaPlugin instance = CraftPlugin.getInstance();
        FileConfiguration config = instance.getConfig();

        ConfigurationManager.addDefault("chatPrefix", "&8&l[&6&lServer&8&l] &r");

        config.addDefault("hibernate.driver", "com.mysql.cj.jdbc.Driver");
        config.addDefault("hibernate.url", "jdbc:mysql://localhost:3306/test?useSSL=false");
        config.addDefault("hibernate.user", "root");
        config.addDefault("hibernate.pass", "");
        config.addDefault("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        config.addDefault("hibernate.showSql", "true");
        config.addDefault("hibernate.currentSessionContextClass", "thread");
        config.addDefault("hibernate.hmb2ddlAuto", "create-drop");

        config.addDefault("hibernate.cachingEnabled", "false");


        config.addDefault("languages.de", "Deutsch");

        config.options().copyDefaults(true);
        instance.saveConfig();
    }

    public static void addDefault(@NotNull String key, @NotNull Object value) {
        JavaPlugin instance = CraftPlugin.getInstance();
        FileConfiguration config = instance.getConfig();
        config.addDefault(key, value);
        config.options().copyDefaults(true);
        instance.saveConfig();
    }

    public static String getEnvironmentVariable(String name) {
        return System.getenv(name);
    }

    public static @NotNull String getString(String path) {
        String value = CraftPlugin.getInstance().getConfig().getString(path);
        if (value == null) {
            CraftPlugin.getInstance().getLogger().warning("The path " + path + " is not set in the config.yml");
            return "";
        }
        return value;
    }

    public static int getInt(String path) {
        return CraftPlugin.getInstance().getConfig().getInt(path);
    }

    public static FileConfiguration getConfig() {
        return CraftPlugin.getInstance().getConfig();
    }

    public static FileConfiguration getCustomConfig(String filename) {
        CraftPlugin instance = CraftPlugin.getInstance();

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
        CraftPlugin instance = CraftPlugin.getInstance();

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
