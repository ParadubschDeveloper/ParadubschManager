package de.paradubsch.paradubschmanager.util.lang;

public class Message {
    public enum Error implements BaseMessageType {
        CMD_LANGUAGE_NOT_PROVIDED("cmdLanguageNotProvided", "&cDu musst eine Sprache angeben!"),
        CMD_LANGUAGE_NOT_FOUND("cmdLanguageNotFound", "Die Sprache &d%1 &7konnte &cnicht &7gefunden werden!"),
        CMD_RECEIVER_NOT_PROVIDED("cmdReceiverNotProvided", "&cDu musst einen Empfänger angeben!"),
        CMD_RECEIVER_NOT_PLAYER("cmdReceiverNotPlayer", "Der Empfänger &d%1 &7ist kein Spieler!"),
        CMD_MESSAGE_NOT_PROVIDED("cmdMessageNotProvided", "&cDu musst eine Nachricht angeben!"),
        CMD_RECEIVER_NOT_ONLINE("cmdReceiverNotOnline", "Der Spieler &d%1 &7ist &cnicht &7online!"),
        CMD_ONLY_FOR_PLAYERS("cmdOnlyForPlayers", "&cDieser Befehl kann nur von Spielern ausgeführt werden!"),
        CMD_PLAYER_NOT_PROVIDED("cmdPlayerNotProvided", "&cDu musst einen Spieler angeben!"),
        CMD_PREFIX_NOT_PROVIDED("cmdPrefixNotProvided", "&cDu musst einen Prefix angeben!"),
        CMD_PLAYER_NEVER_ONLINE("cmdPlayerNeverOnline", "Der Spieler &d%1 &7war noch &cnie &7online!"),
        CMD_COLOR_NOT_PROVIDED("cmdColorNotProvided", "&cDu musst eine Farbe angeben!"),
        NO_PERMISSION("noPermission", "&cDu hast keine Berechtigung!"),
        CMD_MONEY_UNKNOWN_SUBCOMMAND("cmdMoneyUnknownSubcommand", "Der Unterbefehl &d%1 &7konnte &cnicht &7gefunden werden!"),
        CMD_MONEY_AMOUNT_NOT_PROVIDED("cmdMoneyAmountNotProvided", "&cDu musst einen Betrag angeben, den du überweisen willst!"),
        CMD_MONEY_AMOUNT_NOT_VALID("cmdMoneyAmountNotValid", "&7Der Betrag &d%1 &7ist nicht gültig. Bitte wähle eine ganze Zahl!"),
        CMD_MONEY_PAY_NOT_ENOUGH_MONEY("cmdMoneyPayNotEnoughMoney", "&cDu hast nicht genug Geld um &a%1 &e€ &czu überweisen!"),
        CMD_MONEY_PAY_SELF("cmdMoneyPaySelf", "&cDu kannst dir nicht selbst Geld überweisen!");

        private final String key;
        private final String def;

        Error(String key, String def) {
            this.key = key;
            this.def = def;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String getDefault() {
            return this.def;
        }

        @Override
        public String getConfigPrefix() {
            return "error";
        }
    }
    public enum Info implements BaseMessageType {
        CMD_LANGUAGE_SET("cmdLanguageSet", "Du hast deine Sprache &aerfolgreich &7zu &d%1 &7geändert."),
        CMD_PREFIX_SET("cmdPrefixSet", "Du hast den Prefix von &e%1 &aerfolgreich &7zu &d%2 &7geändert."),
        CMD_NAME_COLOR_SET("cmdNameColorSet", "Du hast die Namensfarbe von &e%1 &aerfolgreich &7zu %2%3 &7geändert."),
        CMD_DEFAULT_CHAT_COLOR_SET("cmdDefaultChatColorSet", "Du hast die Standard-Chatfarbe von &e%1 &aerfolgreich &7zu %2Farbe &7geändert."),
        CMD_YOUR_PLAYTIME("cmdYourPlaytime", "Deine Spielzeit: &a%1 &7Tage, &a%2 &7Stunden, &a%3 &7Minuten, &a%4 &7Sekunden"),
        CMD_OTHER_PLAYTIME("cmdOtherPlaytime", "Die Spielzeit von &e%1 &7beträgt &a%2 &7Tage, &a%3 &7Stunden, &a%4 &7Minuten, &a%5 &7Sekunden."),
        CMD_MONEY_DISPLAY_SELF("cmdMoneyDisplaySelf", "Dein Geld: &a%1 &e€"),
        CMD_MONEY_PAYED("cmdMoneyPayed", "Du hast &a%1 &e€ &7an &d%2 &7überwiesen."),
        CMD_MONEY_RECEIVED("cmdMoneyReceived", "Du hast &a%1 &e€ &7von &e%2 &7erhalten.");

        private final String key;
        private final String def;

        Info(String key, String def) {
            this.key = key;
            this.def = def;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String getDefault() {
            return this.def;
        }

        @Override
        public String getConfigPrefix() {
            return "info";
        }
    }

    public enum Constant implements BaseMessageType {
        SERVER_CONSOLE("constantServerConsole", "&4&lServer Konsole"),
        MSG_TEMPLATE("constantMsgTemplate", "&8[&a%1 &7» &a%2&8] &d%3"),
        FROM_YOU("constantFromYou", "Du"),
        TO_YOU("constantToYou", "Dir"),
        CHAT_MESSAGE_TEMPLATE("constantChatMessageTemplate", "%1 &8| %2%3&r &8» %4%5");

        private final String key;
        private final String def;

        Constant(String key, String def) {
            this.key = key;
            this.def = def;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String getDefault() {
            return this.def;
        }

        @Override
        public String getConfigPrefix() {
            return "constant";
        }
    }
}
