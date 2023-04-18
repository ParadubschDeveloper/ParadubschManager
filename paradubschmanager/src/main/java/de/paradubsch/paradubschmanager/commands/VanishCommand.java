package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.lifecycle.TabDecorationManager;
import de.craftery.craftinglib.util.Expect;
import de.craftery.craftinglib.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VanishCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player targetPlayer;
        if (Expect.minArgs(1, args)) {
            if (!sender.hasPermission("paradubsch.vanish.other")) {
                MessageAdapter.sendMessage(sender, Message.Error.NO_PERMISSION);
                return true;
            }
            targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_ONLINE, args[0]);
                return true;
            }
        } else {
            if (!Expect.playerSender(sender)) {
                return true;
            }
            targetPlayer = (Player) sender;
        }

        List<UUID> vanishPlayers = ParadubschManager.getInstance().getVanishedPlayers();

        if (vanishPlayers.contains(targetPlayer.getUniqueId())) {
            vanishPlayers.remove(targetPlayer.getUniqueId());
            ParadubschManager.getInstance().setVanishedPlayers(vanishPlayers);

            MessageAdapter.sendMessage(sender, Message.Info.CMD_VANISH_DISABLED, targetPlayer.getName());

            for (Player serverPlayer : Bukkit.getOnlinePlayers()) {
                serverPlayer.showPlayer(ParadubschManager.getInstance(), targetPlayer);
            }
            TabDecorationManager.broadcastTabDecorations();
        } else {
            vanishPlayers.add(targetPlayer.getUniqueId());
            ParadubschManager.getInstance().setVanishedPlayers(vanishPlayers);

            MessageAdapter.sendMessage(sender, Message.Info.CMD_VANISH_ENABLED, targetPlayer.getName());

            for (Player serverPlayer : Bukkit.getOnlinePlayers()) {
                if (!serverPlayer.hasPermission("paradubsch.vanish.bypass")) {
                    serverPlayer.hidePlayer(ParadubschManager.getInstance(), targetPlayer);
                }
            }
            TabDecorationManager.broadcastTabDecorations();
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return null;
        }
        return new ArrayList<>();
    }
}
