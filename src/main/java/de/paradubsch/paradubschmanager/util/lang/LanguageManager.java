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

            for (Class<?> clazz : Message.class.getClasses()) {
                if (clazz.isEnum() && clazz.getEnumConstants().length > 0 && clazz.getEnumConstants()[0] instanceof BaseMessageType) {
                    for (Object constant : clazz.getEnumConstants()) {
                        BaseMessageType msg = (BaseMessageType) constant;
                        conf.addDefault(msg.getConfigPrefix() + "." + msg.getKey(), msg.getDefault());
                    }
                }
            }

            conf.options().copyDefaults(true);
            ConfigurationManager.saveCustomConfig(conf, shortName + ".lang.yml");
            languageFiles.put(shortName, conf);
        }
    }

    public Component get (BaseMessageType msg, Language lang, String... args) {
        return ChatComponentFactory.assemble(getString(msg, lang, args));
    }

    public String getString (BaseMessageType constant, Language lang, String... args) {
        FileConfiguration langConf = languageFiles.get(lang.getShortName());
        String translation = langConf.getString(constant.getConfigPrefix() + "." + constant.getKey());
        if (translation == null) {
            translation = constant.getDefault();
        }
        for (int i = 1; i <= args.length; i++) {
            translation = translation.replace("%" + i, args[i-1]);
        }
        return translation;
    }
}
