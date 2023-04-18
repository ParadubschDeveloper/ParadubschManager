package de.craftery.craftinglib.util.features;

import de.craftery.craftinglib.CraftingLib;

public class HeadDatabaseFeature implements Feature {
    @Override
    public boolean isAvailable() {
        return CraftingLib.getInstance().getHeadDatabase() != null;
    }

    @Override
    public String getFeatureName() {
        return "HeadDatabase";
    }
}
