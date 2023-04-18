package de.craftery.craftinglib.util;

import de.craftery.craftinglib.CraftingLib;
import de.craftery.craftinglib.PlayerData;
import de.craftery.craftinglib.InternalMessages;
import de.craftery.craftinglib.messaging.lang.Language;
import de.craftery.craftinglib.messaging.lang.BaseMessageType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageAdapter {
    public static void sendConsoleError (Exception ex) {
        CraftingLib.getInstance().getLogger().warning(ex.getMessage());
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

        Component infoText = CraftingLib.getInstance().getLanguageManager().get(template, playerLang, args);
        return CraftingLib.getInstance().getLanguageManager().get(InternalMessages.CHAT_PREFIX, playerLang).append(infoText);
    }

    public static void sendMessage(CommandSender cs, BaseMessageType template, String... args) {
        Bukkit.getScheduler().runTask(CraftingLib.getInstance(), () -> {
            Component message = getSendableMessage(cs, template, args);
            cs.sendMessage(message);
        });
    }

    public static void sendUnprefixedMessage(CommandSender cs, BaseMessageType constant, String... args) {
        Bukkit.getScheduler().runTask(CraftingLib.getInstance(), () -> {
            Language playerLang = getSenderLang(cs);

            Component constantText = CraftingLib.getInstance().getLanguageManager().get(constant, playerLang, args);
            cs.sendMessage(constantText);
        });
    }

    public static void broadcastUnprefixedMessage(BaseMessageType template, String... args) {
        Bukkit.getScheduler().runTask(CraftingLib.getInstance(), () -> {
            Language lang = Language.getDefaultLanguage();

            Component constantText = CraftingLib.getInstance().getLanguageManager().get(template, lang, args);
            Bukkit.getServer().sendMessage(constantText);
        });
    }
}
