package de.paradubsch.paradubschmanager.lifecycle;

import de.craftery.craftinglib.CraftingLib;
import de.craftery.craftinglib.util.MessageAdapter;
import de.craftery.craftinglib.util.collectables.Collectable;
import de.craftery.craftinglib.util.collectables.CollectableCollectedEvent;
import de.craftery.craftinglib.util.collectables.CollectedCollectable;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.commands.CollectablesCommand;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CollectableCommandListener implements Listener {
    public CollectableCommandListener() {
        ParadubschManager.getInstance().getServer().getPluginManager().registerEvents(this, ParadubschManager.getInstance());
    }

    private final Map<UUID, Long> lastClick = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (lastClick.get(e.getPlayer().getUniqueId()) != null && lastClick.get(e.getPlayer().getUniqueId()) + 500 > System.currentTimeMillis()) return;
        lastClick.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());

        String possibleIngameEventType = CollectablesCommand.playersInAddMode.get(e.getPlayer().getUniqueId());
        if (possibleIngameEventType != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = e.getClickedBlock();
            if (clickedBlock == null) return;

            if (Collectable.exists(clickedBlock.getLocation())) {
                MessageAdapter.sendMessage(e.getPlayer(), Message.Error.BLOCK_IS_ALREADY_COLLECTABLE);
                return;
            }
            Collectable.addCollectable(clickedBlock.getLocation(), possibleIngameEventType);
            MessageAdapter.sendMessage(e.getPlayer(), Message.Info.COLLECTABLE_ADDED, possibleIngameEventType);
            CollectablesCommand.playersInAddMode.remove(e.getPlayer().getUniqueId());
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = e.getClickedBlock();
            if (clickedBlock == null) return;
            Collectable possibleCollectable = Collectable.getByLocation(clickedBlock.getLocation());
            if (possibleCollectable == null) return;

            if (CollectedCollectable.hasCollected(e.getPlayer(), possibleCollectable)) {
                MessageAdapter.sendMessage(e.getPlayer(), Message.Error.COLLECTABLE_ALREADY_COLLECTED);
                return;
            }

            CollectableCollectedEvent event = new CollectableCollectedEvent(e.getPlayer(), possibleCollectable);

            CraftingLib.getInstance().getServer().getPluginManager().callEvent(event);

            if (event.isHandled()) {
                CollectedCollectable.collect(e.getPlayer(), possibleCollectable);
            } else {
                MessageAdapter.sendMessage(e.getPlayer(), Message.Error.COLLECTABLE_NOT_COLLECTED);
            }
        }
    }
}
