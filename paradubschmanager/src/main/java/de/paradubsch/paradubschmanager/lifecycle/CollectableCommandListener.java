package de.paradubsch.paradubschmanager.lifecycle;

import de.craftery.util.MessageAdapter;
import de.craftery.util.collectables.Collectable;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.commands.CollectablesCommand;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CollectableCommandListener implements Listener {
    public CollectableCommandListener() {
        ParadubschManager.getInstance().getServer().getPluginManager().registerEvents(this, ParadubschManager.getInstance());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        String possibleIngameEventType = CollectablesCommand.playersInAddMode.get(e.getPlayer().getUniqueId());
        if (possibleIngameEventType == null) return;
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = e.getClickedBlock();
            if (clickedBlock == null) return;

            if (Collectable.exists(clickedBlock.getLocation())) {
                MessageAdapter.sendMessage(e.getPlayer(), Message.Error.BLOCK_IS_ALREADY_COLLECTABLE);
                return;
            }
            Collectable.addCollectable(clickedBlock.getLocation(), possibleIngameEventType);
            MessageAdapter.sendMessage(e.getPlayer(), Message.Info.COLLECTABLE_ADDED, possibleIngameEventType);
            CollectablesCommand.playersInAddMode.remove(e.getPlayer().getUniqueId());
        }
    }
}
