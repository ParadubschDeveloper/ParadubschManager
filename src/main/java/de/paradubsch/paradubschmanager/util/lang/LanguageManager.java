package de.paradubsch.paradubschmanager.util.lang;

import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        FileConfiguration langConf = languageFiles.get(lang.getShortName());
        String translation = langConf.getString( msg.getConfigPrefix() + "." + msg.getKey());
        if (translation == null) {
            translation = msg.getDefault();
        }
        for (int i = 1; i <= args.length; i++) {
            translation = translation.replace("%" + i, args[i-1]);
        }
        return Component.text(ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', translation));
    }

    public Component get (BaseMessageType msg, Language lang, TextComponent... args) {
        FileConfiguration langConf = languageFiles.get(lang.getShortName());
        String translation = langConf.getString( msg.getConfigPrefix() + "." + msg.getKey());
        if (translation == null) {
            translation = msg.getDefault();
        }

        Component component = Component.text(ChatColor.GRAY + "");
        String[] parts = translation.split("(?=%[0-" + args.length +"])|(?<=%[0-" + args.length + "])");

        for (String s : parts) {
            boolean isComponent = false;
            for (int i = 1; i <= args.length; i++) {
                if (s.equals("%" + i)) {
                    component = component.append(args[i-1]);
                    isComponent = true;
                }
            }
            if (!isComponent) {
                component = component.append(Component.text(ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', s)));
            }
        }

        return component;
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
