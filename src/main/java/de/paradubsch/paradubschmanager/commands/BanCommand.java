package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BanCommand implements TabCompleter, CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.minArgs(1, args)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_PROVIDED);
            return true;
        }

        switch (args[0]) {
            case "list": {
                break;
            }
            case "edit": {
                break;
            }
            case "delete": {
                break;
            }
            default: banPlayer(sender, args);
        }

        return true;
    }

    private void banPlayer(CommandSender sender, String[] args) {
        //ban player duration reason
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            Bukkit.getOnlinePlayers().forEach(x -> l.add(x.getName()));
            l.add("list");
            l.add("edit");
            l.add("delete");
            return l;
        }
        if (args.length == 2) {
            if (args[0].equals("list") || args[0].equals("edit") ||args[0].equals("delete")) {
                return null;
            }
        }
        return l;
    }
}
