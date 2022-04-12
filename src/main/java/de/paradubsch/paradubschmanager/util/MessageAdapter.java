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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageAdapter {
    public static void sendChatMessage(Player player, String msg) {
        sendChatConstant(Message.Constant.CHAT_MESSAGE_TEMPLATE, getPlayerPrefix(player), player.getName(), msg);
    }

    public static String getPlayerPrefix(Player player) {
        PlayerData p = Hibernate.getPlayerData(player);
        return p.getChatPrefix();
    }

    public static void sendConsoleError (String msg) {
        ParadubschManager.getInstance().getLogger().warning(msg);
    }

    public static void sendConsoleError (Exception ex) {
        ParadubschManager.getInstance().getLogger().warning(ex.getMessage());
    }

    public static void sendPlayerError (CommandSender cs, Message.Error error, String... args) {
        new Thread(() -> {
            Language playerLang;
            if (cs instanceof Player) {
                Player player = (Player) cs;
                PlayerData playerData = Hibernate.getPlayerData(player);
                playerLang = Language.getLanguageByShortName(playerData.getLanguage());
            } else {
                playerLang = Language.getDefaultLanguage();
            }

            Component errorText = ParadubschManager.getInstance().getLanguageManager().get(error, playerLang, args);
            Component message = Component.text(ChatColor.translateAlternateColorCodes('&', ConfigurationManager.getString("chatPrefix")))
                    .append(errorText);
            cs.sendMessage(message);
        }).start();
    }

    public static void sendPlayerInfo (CommandSender cs, Message.Info info, String... args) {
        new Thread(() -> {
            Language playerLang;
            if (cs instanceof Player) {
                Player player = (Player) cs;
                PlayerData playerData = Hibernate.getPlayerData(player);
                playerLang = Language.getLanguageByShortName(playerData.getLanguage());
            } else {
                playerLang = Language.getDefaultLanguage();
            }

            Component infoText = ParadubschManager.getInstance().getLanguageManager().get(info, playerLang, args);
            Component message = Component.text(ChatColor.translateAlternateColorCodes('&', ConfigurationManager.getString("chatPrefix")))
                    .append(infoText);
            cs.sendMessage(message);
        }).start();
    }

    public static void sendPlayerConstant (CommandSender cs, Message.Constant constant, String... args) {
        new Thread(() -> {
            Language playerLang;
            if (cs instanceof Player) {
                Player player = (Player) cs;
                PlayerData playerData = Hibernate.getPlayerData(player);
                playerLang = Language.getLanguageByShortName(playerData.getLanguage());
            } else {
                playerLang = Language.getDefaultLanguage();
            }

            Component constantText = ParadubschManager.getInstance().getLanguageManager().get(constant, playerLang, args);
            cs.sendMessage(constantText);
        }).start();
    }

    public static void sendChatConstant (Message.Constant constant, String... args) {
        new Thread(() -> {
            Language lang = Language.getDefaultLanguage();

            Component constantText = ParadubschManager.getInstance().getLanguageManager().get(constant, lang, args);
            Bukkit.getServer().sendMessage(constantText);
        }).start();
    }

    public static void setPlayerPrefix(PlayerData p, String prefix) {
        p.setChatPrefix(prefix);
        Hibernate.save(p);
    }
}
