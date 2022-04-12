package de.paradubsch.paradubschmanager.util.lang;

import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class LanguageManager {
    private final Map<String, FileConfiguration> languageFiles = new HashMap<>();
    public LanguageManager() {

        for (String shortName : Language.getShortNames()){
            FileConfiguration conf = ConfigurationManager.getCustomConfig(shortName + ".lang.yml");

            for (Message.Error error : Message.Error.values()){
                conf.addDefault("error." + error.getKey(), error.getDefault());
            }

            for (Message.Info info : Message.Info.values()){
                conf.addDefault("info." + info.getKey(), info.getDefault());
            }

            for (Message.Constant info : Message.Constant.values()){
                conf.addDefault("constant." + info.getKey(), info.getDefault());
            }

            conf.options().copyDefaults(true);
            ConfigurationManager.saveCustomConfig(conf, shortName + ".lang.yml");
            languageFiles.put(shortName, conf);
        }
    }

    public Component get (Message.Error msg, Language lang, String... args) {
        FileConfiguration langConf = languageFiles.get(lang.getShortName());
        String translation = langConf.getString("error." + msg.getKey());
        if (translation == null) {
            translation = msg.getDefault();
        }
        for (int i = 1; i <= args.length; i++) {
            translation = translation.replace("%" + i, args[i-1]);
        }
        return Component.text(ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', translation));
    }

    public Component get (Message.Info msg, Language lang, String... args) {
        FileConfiguration langConf = languageFiles.get(lang.getShortName());
        String translation = langConf.getString("info." + msg.getKey());
        if (translation == null) {
            translation = msg.getDefault();
        }
        for (int i = 1; i <= args.length; i++) {
            translation = translation.replace("%" + i, args[i-1]);
        }
        return Component.text(ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', translation));
    }

    public Component get (Message.Constant constant, Language lang, String... args) {
        FileConfiguration langConf = languageFiles.get(lang.getShortName());
        String translation = langConf.getString("constant." + constant.getKey());
        if (translation == null) {
            translation = constant.getDefault();
        }
        for (int i = 1; i <= args.length; i++) {
            translation = translation.replace("%" + i, args[i-1]);
        }
        return Component.text(ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', translation));
    }

    public String getString (Message.Constant constant, Language lang, String... args) {
        FileConfiguration langConf = languageFiles.get(lang.getShortName());
        String translation = langConf.getString("constant." + constant.getKey());
        if (translation == null) {
            translation = constant.getDefault();
        }
        for (int i = 1; i <= args.length; i++) {
            translation = translation.replace("%" + i, args[i-1]);
        }
        return translation;
    }
}
