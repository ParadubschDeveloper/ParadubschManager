package de.paradubsch.paradubschmanager.util;

public enum Language {
    GERMAN("Deutsch", "de"),
    ENGLISH("Englisch", "en");

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
}
