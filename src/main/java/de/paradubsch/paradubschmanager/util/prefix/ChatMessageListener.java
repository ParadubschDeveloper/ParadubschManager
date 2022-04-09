package de.paradubsch.paradubschmanager.util.prefix;

import de.paradubsch.paradubschmanager.ParadubschManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatMessageListener implements Listener {
    public ChatMessageListener() {
        ParadubschManager.getInstance().getServer().getPluginManager().registerEvents(this, ParadubschManager.getInstance());
    }

    @EventHandler
    public void onChatMessage(AsyncChatEvent e) {
        // chatban? #TODO

        e.setCancelled(true);


    }
}
