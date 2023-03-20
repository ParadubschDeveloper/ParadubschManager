package de.paradubsch.easterevent;

import de.craftery.CraftingLib;
import de.craftery.CraftingPlugin;

public class EasterEvent extends CraftingPlugin {
    private static final String EASTER_EVENT_NAME = "easter_event_2023";

    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Enabling EasterEvent plugin...");
        CraftingLib.registerCollectableType(EASTER_EVENT_NAME);
    }
}
