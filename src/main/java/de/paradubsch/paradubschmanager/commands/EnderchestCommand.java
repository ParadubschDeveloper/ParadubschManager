package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
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

public class EnderchestCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;

        Player playerSender = (Player) sender;

        Player target;
        if (args.length == 1) {
            if (playerSender.hasPermission("paradubsch.enderchest.others")) {
                target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    MessageAdapter.sendMessage(playerSender, Message.Error.CMD_PLAYER_NOT_ONLINE, args[0]);
                    return true;
                }
            } else {
                MessageAdapter.sendMessage(playerSender, Message.Error.NO_PERMISSION);
                return true;
            }
        } else {
            target = playerSender;
        }

        playerSender.openInventory(target.getEnderChest());

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && sender.hasPermission("paradubsch.enderchest.others")) return null;

        return new ArrayList<>();
    }
}
