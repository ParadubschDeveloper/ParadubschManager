package de.paradubsch.paradubschmanager.lifecycle;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.commands.InvseeCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InvseeInventoryGuard implements Listener {
    public InvseeInventoryGuard() {
        ParadubschManager.getInstance().getServer().getPluginManager().registerEvents(this, ParadubschManager.getInstance());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().title().equals(InvseeCommand.INVSEE_SECURED_TITLE)) {
            event.setCancelled(true);
        }
    }
}
