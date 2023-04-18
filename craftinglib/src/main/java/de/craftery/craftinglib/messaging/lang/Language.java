package de.craftery.craftinglib.messaging.lang;

import lombok.Getter;

public class Language {
    @Getter
    private final String shortName;
    @Getter
    private final String name;

    public Language(String shortName, String name) {
        this.shortName = shortName;
        this.name = name;
    }

    public static Language getLanguageByShortName(String shortName) {
        return LanguageManager.getLanguages().stream()
                .filter(l -> l.getShortName().equals(shortName))
                .findFirst().orElse(Language.getDefaultLanguage());
    }
    public static Language getDefaultLanguage() {
        return new Language("de", "Deutsch");
    }
}
