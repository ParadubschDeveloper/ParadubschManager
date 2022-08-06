package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GmCommand implements CommandExecutor, TabCompleter {
    public static NamespacedKey GM_KEY = new NamespacedKey(ParadubschManager.getInstance(), "gamemode");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.minArgs(1, args)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_GM_NO_GAMEMODE_PROVIDED);
            return true;
        }
        Player targetPlayer;
        if (Expect.minArgs(2, args)) {
            if (!sender.hasPermission("paradubsch.gm.other")) {
                MessageAdapter.sendMessage(sender, Message.Error.NO_PERMISSION);
                return true;
            }
            targetPlayer = Bukkit.getPlayer(args[1]);
            if (targetPlayer == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_ONLINE, args[1]);
                return true;
            }
        } else {
            if (!Expect.playerSender(sender)) {
                return true;
            }
            targetPlayer = (Player) sender;
        }
        switch (args[0]) {
            case "s": case "0": case "survival": {
                targetPlayer.setGameMode(GameMode.SURVIVAL);
                targetPlayer.getPersistentDataContainer().set(GM_KEY, PersistentDataType.INTEGER, 0);
                MessageAdapter.sendMessage(sender, Message.Info.CMD_GAMEMODE_CHANGED, targetPlayer.getName(), "Survival");
                break;
            }
            case "c": case "1": case "creative": {
                targetPlayer.setGameMode(GameMode.CREATIVE);
                targetPlayer.getPersistentDataContainer().set(GM_KEY, PersistentDataType.INTEGER, 1);
                MessageAdapter.sendMessage(sender, Message.Info.CMD_GAMEMODE_CHANGED, targetPlayer.getName(), "Creative");
                break;
            }
            case "a": case "2": case "adventure": {
                targetPlayer.setGameMode(GameMode.ADVENTURE);
                targetPlayer.getPersistentDataContainer().set(GM_KEY, PersistentDataType.INTEGER, 2);
                MessageAdapter.sendMessage(sender, Message.Info.CMD_GAMEMODE_CHANGED, targetPlayer.getName(), "Adventure");
                break;
            }
            case "3": case "spectator": {
                targetPlayer.setGameMode(GameMode.SPECTATOR);
                targetPlayer.getPersistentDataContainer().set(GM_KEY, PersistentDataType.INTEGER, 3);
                MessageAdapter.sendMessage(sender, Message.Info.CMD_GAMEMODE_CHANGED, targetPlayer.getName(), "Spectator");
                break;
            }
            default: MessageAdapter.sendMessage(sender, Message.Error.CMD_GM_GAMEMODE_INVALID, args[0]);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            l.add("survival");
            l.add("creative");
            l.add("adventure");
            l.add("spectator");
            return l;
        }
        if (args.length > 2) {
            return l;
        }
        return null;
    }
}
