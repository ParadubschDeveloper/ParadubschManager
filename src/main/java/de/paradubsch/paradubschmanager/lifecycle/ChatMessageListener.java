package de.paradubsch.paradubschmanager.lifecycle;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
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

        String message = ((TextComponent) e.message()).content();

        PlayerData pd = PlayerData.getByPlayer(e.getPlayer());

        MessageAdapter.broadcastMessage(
                Message.Constant.CHAT_MESSAGE_TEMPLATE,
                pd.getChatPrefix(),
                pd.getNameColor(),
                e.getPlayer().getName(),
                pd.getDefaultChatColor(),
                message
        );
    }
}
