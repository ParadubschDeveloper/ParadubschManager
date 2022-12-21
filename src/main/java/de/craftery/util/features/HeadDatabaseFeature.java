package de.craftery.util.features;

import de.craftery.CraftPlugin;

public class HeadDatabaseFeature implements Feature {
    @Override
    public boolean isAvailable() {
        return CraftPlugin.getInstance().getHeadDatabase() != null;
    }

    @Override
    public String getFeatureName() {
        return "HeadDatabase";
    }
}
