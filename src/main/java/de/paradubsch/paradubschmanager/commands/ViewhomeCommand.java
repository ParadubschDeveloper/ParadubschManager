package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.models.Home;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.Hibernate;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ViewhomeCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_ONLY_FOR_PLAYERS);
            return true;
        }
        Player player = (Player) sender;

        if (!Expect.minArgs(1, args)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_HOMENAME_NOT_PROVIDED);
            return true;
        }

        CompletableFuture.supplyAsync(() -> Hibernate.getHomes(player)).thenAccept(homes -> {
            if (homes.stream().anyMatch(home -> home.getName().equals(args[0]))) {
                Home home = homes.stream().filter(home1 -> home1.getName().equals(args[0])).findFirst().get();
                MessageAdapter.sendMessage(player, Message.Info.CMD_VIEWHOME, home.getName());
            } else if (homes.stream().anyMatch(home -> home.getName().equalsIgnoreCase(args[0]))) {
                Home home = homes.stream().filter(home1 -> home1.getName().equalsIgnoreCase(args[0])).findFirst().get();
                MessageAdapter.sendMessage(player, Message.Info.CMD_VIEWHOME_MAYBE_WRONG_NAME, args[0], home.getName());
            } else {
                MessageAdapter.sendMessage(player, Message.Error.CMD_VIEWHOME_HOME_NOT_FOUND, args[0]);
            }
        });

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
