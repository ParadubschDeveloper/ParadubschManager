package de.paradubsch.paradubschmanager.util.lang;

public class Message {
    public enum Error {
        CMD_LANGUAGE_NOT_PROVIDED("cmdLanguageNotProvided", "&cDu musst eine Sprache angeben!"),
        CMD_LANGUAGE_NOT_FOUND("cmdLanguageNotFound", "Die Sprache &d%1 &7konnte &cnicht &7gefunden werden!");

        private final String key;
        private final String def;

        Error(String key, String def) {
            this.key = key;
            this.def = def;
        }

        public String getKey() {
            return key;
        }

        public String getDefault() {
            return def;
        }
    }
    public enum Info {
        CMD_LANGUAGE_SET("cmdLanguageSet", "Du hast deine Sprache &aerfolgreich &7zu &d%1 &7geändert.");

        private final String key;
        private final String def;

        Info(String key, String def) {
            this.key = key;
            this.def = def;
        }

        public String getKey() {
            return key;
        }

        public String getDefault() {
            return def;
        }
    }
}
