package de.paradubsch.quizevent;

import de.craftery.craftinglib.CraftingPlugin;
import org.bukkit.event.Listener;


public class QuizEvent extends CraftingPlugin implements Listener {
    @Override
    public void onEnable() {
        super.onEnable();
        System.out.println("Enabling QuizEvent plugin...");
        getServer().getPluginManager().registerEvents(this, this);
    }
}
