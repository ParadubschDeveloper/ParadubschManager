package de.paradubsch.paradubschmanager.commands;

import de.craftery.ErrorOr;
import de.paradubsch.paradubschmanager.models.Home;
import de.craftery.util.Expect;
import de.craftery.util.MessageAdapter;
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
    public static final String HOME_NOT_FOUND_ERROR = "Home not found";
    public static final String HOME_NOT_FOUND_BUT_ALTERNATIVE_ERROR = "Home not found, but alternative is existing";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;

        Player player = (Player) sender;

        if (!Expect.minArgs(1, args)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_HOMENAME_NOT_PROVIDED);
            return true;
        }
        String homeName = args[0];

        if (!Expect.minArgs(2, args)) {
            MessageAdapter.sendMessage(sender, Message.Info.CMD_DELHOME_CONFIRM, homeName);
            return true;
        }

        if (args[1].equals("confirm")) {
            deleteHome(player, homeName);
        } else {
            MessageAdapter.sendMessage(sender, Message.Info.CMD_DELHOME_CONFIRM, args[0]);
        }
        return true;
    }

    public static ErrorOr<Void> deleteHome(Player player, String homeName) {
        List<Home> homes = Home.getByPlayer(player);

        Optional<Home> predicate = homes.stream().filter(home_ -> home_.getName().equals(homeName)).findFirst();
        Optional<Home> alternative = homes.stream().filter(home_ -> home_.getName().equalsIgnoreCase(homeName)).findFirst();
        if (predicate.isPresent()) {
            Home home = predicate.get();
            home.delete();

            MessageAdapter.sendMessage(player, Message.Info.CMD_DELHOME_DONE, homeName);
            return ErrorOr.release(null);
        } else if (alternative.isPresent()) {
            Home home = alternative.get();
            MessageAdapter.sendMessage(player, Message.Info.CMD_DELHOME_MAYBE_WRONG_NAME, homeName, home.getName());
            return new ErrorOr<>(HOME_NOT_FOUND_BUT_ALTERNATIVE_ERROR);
        } else {
            MessageAdapter.sendMessage(player, Message.Error.CMD_VIEWHOME_HOME_NOT_FOUND, homeName);
            return new ErrorOr<>(HOME_NOT_FOUND_ERROR);
        }
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
