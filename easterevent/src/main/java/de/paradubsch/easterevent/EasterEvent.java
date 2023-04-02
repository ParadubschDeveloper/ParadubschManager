package de.paradubsch.easterevent;

import de.craftery.CraftingLib;
import de.craftery.CraftingPlugin;
import de.craftery.util.collectables.CollectableCollectedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EasterEvent extends CraftingPlugin implements Listener {
    private static final String EASTER_EVENT_NAME = "easter_event_2023";

    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Enabling EasterEvent plugin...");
        CraftingLib.registerCollectableType(EASTER_EVENT_NAME);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onCollectableCollected(CollectableCollectedEvent e) {
        if (e.getCollectable().getType().equals(EASTER_EVENT_NAME)) {
            e.getPlayer().sendMessage("You collected an Easter Egg!");
            // TODO:
            // check amount of eggs already collected
            // check if the playes gets the 25€ for a single agg
            // check if the player got the last egg and gets the 100€
            // show action bar message
            // handle event
        }
    }
}
