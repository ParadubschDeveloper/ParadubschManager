package de.paradubsch.paradubschmanager.util;

import org.bukkit.entity.Player;

public class MessageAdapter {
    public void sendChatMessage(Player player, String message) {
        player.sendMessage(message);
    }
}
