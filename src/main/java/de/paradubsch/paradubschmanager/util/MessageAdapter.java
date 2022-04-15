package de.paradubsch.paradubschmanager.util;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.lang.BaseMessageType;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageAdapter {
    public static void sendConsoleError (Exception ex) {
        ParadubschManager.getInstance().getLogger().warning(ex.getMessage());
    }

    public static void sendMessage(CommandSender cs, BaseMessageType template, String... args) {
        new Thread(() -> {
            Language playerLang;
            if (cs instanceof Player) {
                Player player = (Player) cs;
                PlayerData playerData = Hibernate.getPlayerData(player);
                playerLang = Language.getLanguageByShortName(playerData.getLanguage());
            } else {
                playerLang = Language.getDefaultLanguage();
            }

            Component infoText = ParadubschManager.getInstance().getLanguageManager().get(template, playerLang, args);
            Component message = Component.text(ChatColor.translateAlternateColorCodes('&', ConfigurationManager.getString("chatPrefix")))
                    .append(infoText);
            cs.sendMessage(message);
        }).start();
    }

    public static void sendUnprefixedMessage(CommandSender cs, BaseMessageType constant, String... args) {
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

    public static void broadcastMessage(BaseMessageType template, String... args) {
        new Thread(() -> {
            Language lang = Language.getDefaultLanguage();

            Component constantText = ParadubschManager.getInstance().getLanguageManager().get(template, lang, args);
            Bukkit.getServer().sendMessage(constantText);
        }).start();
    }

}
