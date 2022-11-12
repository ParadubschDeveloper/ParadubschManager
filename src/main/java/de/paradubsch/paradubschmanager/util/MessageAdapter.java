package de.paradubsch.paradubschmanager.util;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.lang.BaseMessageType;
import de.paradubsch.paradubschmanager.util.lang.ChatComponentFactory;
import de.paradubsch.paradubschmanager.util.lang.Language;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageAdapter {
    public static void sendConsoleError (Exception ex) {
        ParadubschManager.getInstance().getLogger().warning(ex.getMessage());
    }

    public static Language getSenderLang(CommandSender sender) {
        Language language;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerData playerData = PlayerData.getByPlayer(player);
            language = Language.getLanguageByShortName(playerData.getLanguage());
        } else {
            language = Language.getDefaultLanguage();
        }
        return language;
    }

    public static Component getSendableMessage(CommandSender cs, BaseMessageType template, String... args) {
        Language playerLang = getSenderLang(cs);

        Component infoText = ParadubschManager.getInstance().getLanguageManager().get(template, playerLang, args);
        return ChatComponentFactory.assemble(ConfigurationManager.getString("chatPrefix"))
                .append(infoText);
    }

    public static void sendMessage(CommandSender cs, BaseMessageType template, String... args) {
        Bukkit.getScheduler().runTask(ParadubschManager.getInstance(), () -> {
            Component message = getSendableMessage(cs, template, args);
            cs.sendMessage(message);
        });
    }

    public static void sendUnprefixedMessage(CommandSender cs, BaseMessageType constant, String... args) {
        Bukkit.getScheduler().runTask(ParadubschManager.getInstance(), () -> {
            Language playerLang = getSenderLang(cs);

            Component constantText = ParadubschManager.getInstance().getLanguageManager().get(constant, playerLang, args);
            cs.sendMessage(constantText);
        });
    }

    public static void broadcastMessage(BaseMessageType template, String... args) {
        Bukkit.getScheduler().runTask(ParadubschManager.getInstance(), () -> {
            Language lang = Language.getDefaultLanguage();

            Component constantText = ParadubschManager.getInstance().getLanguageManager().get(template, lang, args);
            Bukkit.getServer().sendMessage(constantText);
        });
    }

}
