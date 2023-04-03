package de.paradubsch.easterevent;

import de.craftery.util.lang.BaseMessageType;

public enum EasterEventMessage implements BaseMessageType {
    COLLECTED_EASTER_EGG("collected_easter_egg", "&aDu hast %1/%2 Ostereier gefunden!"),
    REGULAR_GOT_MONEY("regular_got_money", "&aDafür bekommst du &625&7€&a!"),
    LAST_EASTER_EGG("last_easter_egg", "&aDu hast alle Ostereier gefunden! &7+&6100&7€&a!");

    private final String key;
    private final String def;

    EasterEventMessage(String key, String def) {
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
        return EasterEvent.EASTER_EVENT_NAME;
    }
}
