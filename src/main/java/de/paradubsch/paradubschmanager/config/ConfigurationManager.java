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

        config.addDefault("consolePrefix", "[ParadubschManager] ");
        config.addDefault("chatPrefix", "&8&l[&6&lParadubsch&8&l] &r");
        config.addDefault("msg.errorPrefix", "&8&l[&6&lParadubsch&8&l] &r&c ");
        config.addDefault("msg.messageFormat", "§6§l%sender% §9§l» §6§l%reciever% §9§l» §r%message%");
        config.addDefault("msg.messageSendMe", "Ich");
        config.addDefault("msg.messageRecieveMe", "Mir");

        config.addDefault("hibernate.driver", "com.mysql.cj.jdbc.Driver");
        config.addDefault("hibernate.url", "jdbc:mysql://localhost:3306/paradubschtest?useSSL=false");
        config.addDefault("hibernate.user", "paradubsch");
        config.addDefault("hibernate.pass", "paradubsch");
        config.addDefault("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        config.addDefault("hibernate.showSql", "true");
        config.addDefault("hibernate.currentSessionContextClass", "thread");
        config.addDefault("hibernate.hmb2ddlAuto", "update");

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
