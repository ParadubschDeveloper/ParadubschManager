package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.models.Home;
import de.paradubsch.paradubschmanager.util.Expect;
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
import java.util.Optional;

public class DelhomeCommand implements CommandExecutor, TabCompleter {
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

        if (!Expect.minArgs(2, args)) {
            MessageAdapter.sendMessage(sender, Message.Info.CMD_DELHOME_CONFIRM, args[0]);
            return true;
        }

        if (args[1].equals("confirm")) {
            List<Home> homes = Home.getByPlayer(player);

            Optional<Home> predicate = homes.stream().filter(home_ -> home_.getName().equals(args[0])).findFirst();
            Optional<Home> alternative = homes.stream().filter(home_ -> home_.getName().equalsIgnoreCase(args[0])).findFirst();
            if (predicate.isPresent()) {
                Home home = predicate.get();
                home.delete();

                MessageAdapter.sendMessage(player, Message.Info.CMD_DELHOME_DONE, args[0]);
            } else if (alternative.isPresent()) {
                Home home = alternative.get();
                MessageAdapter.sendMessage(player, Message.Info.CMD_DELHOME_MAYBE_WRONG_NAME, args[0], home.getName());
            } else {
                MessageAdapter.sendMessage(player, Message.Error.CMD_VIEWHOME_HOME_NOT_FOUND, args[0]);
            }
        } else {
            MessageAdapter.sendMessage(sender, Message.Info.CMD_DELHOME_CONFIRM, args[0]);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            l.add("confirm");
            return l;
        }
        return l;
    }
}
