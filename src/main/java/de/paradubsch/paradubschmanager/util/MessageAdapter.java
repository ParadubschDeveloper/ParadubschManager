package de.paradubsch.paradubschmanager.util;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageAdapter {
    public void sendChatMessage(Player player, String msg) {
        Component message = getPlayerPrefix(player)
                .append(Component.text(" " + ChatColor.DARK_GRAY + "|"))
                .append(Component.text(" " + ChatColor.GRAY + msg));
        Bukkit.getServer().sendMessage(message);
    }

    public TextComponent getPlayerPrefix(Player player) {
        return Component.text(ChatColor.GRAY + "Player");
    }

    public static void sendConsoleError (String msg) {
        ParadubschManager.getInstance().getLogger().warning(msg);
    }

    public static void sendConsoleError (Exception ex) {
        ParadubschManager.getInstance().getLogger().warning(ex.getMessage());
    }

    public static void sendPlayerError (Player player, Message.Error error, String... args) {
        new Thread(() -> {
            PlayerData playerData = Hibernate.getPlayerData(player);
            Language playerLang = Language.getLanguageByShortName(playerData.getLanguage());
            Component errorText = ParadubschManager.getInstance().getLanguageManager().get(error, playerLang, args);
            Component message = Component.text(ChatColor.translateAlternateColorCodes('&', ConfigurationManager.getString("chatPrefix")))
                    .append(errorText);
            player.sendMessage(message);
        }).start();
    }

    public static void sendPlayerInfo (Player player, Message.Info info, String... args) {
        new Thread(() -> {
            PlayerData playerData = Hibernate.getPlayerData(player);
            Language playerLang = Language.getLanguageByShortName(playerData.getLanguage());
            Component infoText = ParadubschManager.getInstance().getLanguageManager().get(info, playerLang, args);
            Component message = Component.text(ChatColor.translateAlternateColorCodes('&', ConfigurationManager.getString("chatPrefix")))
                    .append(infoText);
            player.sendMessage(message);
        }).start();
    }
}
