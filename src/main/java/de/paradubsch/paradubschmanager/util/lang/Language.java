package de.paradubsch.paradubschmanager.util.lang;

import java.util.Arrays;

public enum Language {
    GERMAN("Deutsch", "de");

    private final String name;
    private final String shortName;

    Language(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public static String[] getShortNames() {
        return Arrays.stream(Language.values()).map(Language::getShortName).toArray(String[]::new);
    }

    public static String[] getLanguages() {
        return Arrays.stream(Language.values()).map(Language::getName).toArray(String[]::new);
    }

    public static Boolean isLanguage(String language) {
        return Arrays.stream(Language.values()).anyMatch(l -> l.getName().equals(language));
    }

    public static Language getLanguageByShortName (String language) {
        return Arrays.stream(Language.values()).filter(l -> l.getShortName().equals(language)).findFirst().orElse(Language.GERMAN);
    }

    public static Language getLanguageByName (String language) {
        return Arrays.stream(Language.values()).filter(l -> l.getName().equals(language)).findFirst().orElse(Language.GERMAN);
    }

    public static Language getDefaultLanguage() {
        return Language.GERMAN;
    }
}
