package de.paradubsch.paradubschmanager.util.lang;

public class Message {
    public enum Error {
        CMD_LANGUAGE_NOT_PROVIDED("cmdLanguageNotProvided", "&cDu musst eine Sprache angeben!"),
        CMD_LANGUAGE_NOT_FOUND("cmdLanguageNotFound", "Die Sprache &d%1 &7konnte &cnicht &7gefunden werden!"),
        CMD_RECEIVER_NOT_PROVIDED("cmdReceiverNotProvided", "&cDu musst einen Empfänger angeben!"),
        CMD_RECEIVER_NOT_PLAYER("cmdReceiverNotPlayer", "Der Empfänger &d%1 &7ist kein Spieler!"),
        CMD_MESSAGE_NOT_PROVIDED("cmdMessageNotProvided", "&cDu musst eine Nachricht angeben!"),
        CMD_RECEIVER_NOT_ONLINE("cmdReceiverNotOnline", "Der Spieler &d%1 &7ist &cnicht &7online!");

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

    public enum Constant {
        SERVER_CONSOLE("constantServerConsole", "&4&lServer Konsole"),
        MSG_TEMPLATE("constantMsgTemplate", "&8[&a%1 &7» &a%2&8] &d%3"),
        FROM_YOU("constantFromYou", "Du"),
        TO_YOU("constantToYou", "Dir");

        private final String key;
        private final String def;

        Constant(String key, String def) {
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
