package de.craftery;

import de.craftery.util.lang.BaseMessageType;

public enum InternalMessages implements BaseMessageType {
    INSERT_NUMBER("internalConstantInsertNumber", "Zahl eingeben"),
    CHAT_PREFIX("internalConstantChatPrefix", "&8&l[&6&lServer&8&l] &r"),
    CMD_ONLY_FOR_PLAYERS("cmdOnlyForPlayers", "&cDieser Befehl kann nur von Spielern ausgeführt werden!"),
    CMD_ONLY_FOR_CONSOLE("cmdOnlyForConsole", "&cDieser Befehl kann nur von der Konsole ausgeführt werden!"),
    FEATURE_NOT_AVAILABLE("featureNotAvailable", "&cDas Feature &6%1&c ist nicht verfügbar!");
    private final String key;
    private final String def;

    InternalMessages(String key, String def) {
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
        return "internal";
    }
}
