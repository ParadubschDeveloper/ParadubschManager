package de.paradubsch.paradubschmanager.commands;

import de.craftery.ErrorOr;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.Home;
import de.craftery.util.Expect;
import de.craftery.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Location;
import org.bukkit.World;
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

public class HomeCommand implements CommandExecutor, TabCompleter {
    public static final String HOME_NOT_FOUND_ERROR = "Home not found";
    public static final String HOME_NOT_FOUND_BUT_ALTERNATIVE_ERROR = "Home not found, but alternative is existing";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;
        Player player = (Player) sender;

        String homeName;
        if (args.length == 0) {
            homeName = "default";
        } else {
            homeName = args[0];
        }

        teleportHome(player, homeName);
        return true;
    }

    public static ErrorOr<Void> teleportHome(Player player, String homeName) {
        List<Home> homes = Home.getByPlayer(player);

        Optional<Home> predicate = homes.stream().filter(home_ -> home_.getName().equals(homeName)).findFirst();
        Optional<Home> alternative = homes.stream().filter(home_ -> home_.getName().equalsIgnoreCase(homeName)).findFirst();
        if (predicate.isPresent()) {
            Home home = predicate.get();

            double x = home.getX() >= 0 ? home.getX() + 0.5 : home.getX() - 0.5;
            double z = home.getZ() >= 0 ? home.getZ() + 0.5 : home.getZ() - 0.5;

            World world = ParadubschManager.getInstance().getServer().getWorld(home.getWorld());
            Location loc = new Location(world, x, home.getY(), z);

            player.teleport(loc);

            MessageAdapter.sendMessage(player, Message.Info.CMD_HOME_TELEPORT, homeName);
            return ErrorOr.release(null);
        } else if (alternative.isPresent()) {
            Home home = alternative.get();
            MessageAdapter.sendMessage(player, Message.Info.CMD_HOME_MAYBE_WRONG_NAME, homeName, home.getName());
            return new ErrorOr<>(HOME_NOT_FOUND_BUT_ALTERNATIVE_ERROR);
        } else {
            MessageAdapter.sendMessage(player,Message.Error.CMD_HOME_NOT_FOUND, homeName);
            return new ErrorOr<>(HOME_NOT_FOUND_ERROR);
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
