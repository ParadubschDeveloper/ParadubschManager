package de.paradubsch.paradubschmanager.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
            // #TODO customizable message
            sender.sendMessage("§cDieser Befehl kann nur ingame ausgeführt werden!");
        }

        return sender instanceof Player;
    }
}
