package de.craftery.craftinglib.messaging.lang;

import de.craftery.craftinglib.messaging.parser.ChatComponentFactory;
import de.craftery.craftinglib.util.ConfigurationManager;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class LanguageManager {

    private final Map<String, FileConfiguration> languageFiles = new HashMap<>();

    public void registerMessageEnum(Class<? extends BaseMessageType> messageEnum) {
        for (Language lang : getLanguages()){
            String shortName = lang.getShortName();
            FileConfiguration conf = ConfigurationManager.getCustomConfig(shortName + ".lang.yml");

            for (BaseMessageType constant : messageEnum.getEnumConstants()) {
                conf.addDefault(constant.getConfigPrefix() + "." + constant.getKey(), constant.getDefault());
            }
            conf.options().copyDefaults(true);
            ConfigurationManager.saveCustomConfig(conf, shortName + ".lang.yml");
            languageFiles.put(shortName, conf);
        }
    }

    public static List<Language> getLanguages() {
        ConfigurationSection section = ConfigurationManager.getConfig().getConfigurationSection("languages");
        List<Language> languages = new ArrayList<>();
        if (section == null) return languages;

        for (String key : section.getKeys(false)) {
            languages.add(new Language(key, section.getString(key)));
        }

        return languages;
    }

    public Component get(BaseMessageType msg, Language lang, String... args) {
        return ChatComponentFactory.assemble(getString(msg, lang, args));
    }

    public String getString(BaseMessageType constant, Language lang, String... args) {
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
