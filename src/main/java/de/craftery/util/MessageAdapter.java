package de.craftery.util;

import de.craftery.CraftPlugin;
import de.craftery.InternalMessages;
import de.craftery.util.lang.Language;
import de.craftery.util.lang.BaseMessageType;
import de.paradubsch.paradubschmanager.models.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageAdapter {
    public static void sendConsoleError (Exception ex) {
        CraftPlugin.getInstance().getLogger().warning(ex.getMessage());
    }

    public static Language getSenderLang(CommandSender sender) {
        Language language;
        if (sender instanceof Player player) {
            PlayerData playerData = PlayerData.getByPlayer(player);
            language = Language.getLanguageByShortName(playerData.getLanguage());
        } else {
            language = Language.getDefaultLanguage();
        }
        return language;
    }

    public static Component getSendableMessage(CommandSender cs, BaseMessageType template, String... args) {
        Language playerLang = getSenderLang(cs);

        Component infoText = CraftPlugin.getInstance().getLanguageManager().get(template, playerLang, args);
        return CraftPlugin.getInstance().getLanguageManager().get(InternalMessages.CHAT_PREFIX, playerLang).append(infoText);
    }

    public static void sendMessage(CommandSender cs, BaseMessageType template, String... args) {
        Bukkit.getScheduler().runTask(CraftPlugin.getInstance(), () -> {
            Component message = getSendableMessage(cs, template, args);
            cs.sendMessage(message);
        });
    }

    public static void sendUnprefixedMessage(CommandSender cs, BaseMessageType constant, String... args) {
        Bukkit.getScheduler().runTask(CraftPlugin.getInstance(), () -> {
            Language playerLang = getSenderLang(cs);

            Component constantText = CraftPlugin.getInstance().getLanguageManager().get(constant, playerLang, args);
            cs.sendMessage(constantText);
        });
    }

    public static void broadcastUnprefixedMessage(BaseMessageType template, String... args) {
        Bukkit.getScheduler().runTask(CraftPlugin.getInstance(), () -> {
            Language lang = Language.getDefaultLanguage();

            Component constantText = CraftPlugin.getInstance().getLanguageManager().get(template, lang, args);
            Bukkit.getServer().sendMessage(constantText);
        });
    }
}
