package de.paradubsch.paradubschmanager.util;

import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Expect {
    public static Boolean playerString(@Nullable String player) {
        if (player == null) return false;
        return player.matches("[a-zA-Z0-9_]{1,16}");
    }

    public static Boolean argLen(Integer len, String[] args) {
        return args.length == len;
    }

    public static Boolean minArgs(Integer len, String[] args) {
        return args.length >= len;
    }

    public static Boolean playerSender (CommandSender sender) {
        if (!(sender instanceof Player)) {
            MessageAdapter.sendPlayerError(sender, Message.Error.CMD_ONLY_FOR_PLAYERS);
        }

        return sender instanceof Player;
    }

    public static Boolean cachedPlayer (@NotNull String player) {
        return Hibernate.getPlayerData(player) != null;
    }
}
