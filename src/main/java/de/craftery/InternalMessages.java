package de.craftery;

import de.craftery.util.lang.BaseMessageType;

public enum InternalMessages implements BaseMessageType {
    INSERT_NUMBER("internalConstantInsertNumber", "Zahl eingeben");
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
