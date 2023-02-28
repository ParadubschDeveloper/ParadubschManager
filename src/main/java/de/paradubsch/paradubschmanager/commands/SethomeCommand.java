package de.paradubsch.paradubschmanager.commands;

import de.craftery.ErrorOr;
import de.craftery.InternalMessages;
import de.paradubsch.paradubschmanager.models.Home;
import de.paradubsch.paradubschmanager.models.PlayerData;
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

public class SethomeCommand implements CommandExecutor, TabCompleter {
    public static final String HOME_EXISTING_ERROR = "Home already exists";
    public static final String HOME_LIMIT_REACHED_ERROR = "Home limit reached";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) {
            MessageAdapter.sendMessage(sender, InternalMessages.CMD_ONLY_FOR_PLAYERS);
            return true;
        }
        Player player = (Player) sender;

        String homeName;
        if (args.length > 0) {
            homeName = args[0];
        } else {
            homeName = "default";
        }

        boolean overwrite = args.length > 1 && args[1].equalsIgnoreCase("confirm");

        setHome(player, homeName, overwrite);
        return true;
    }

    public static ErrorOr<Void> setHome(Player player, String homeName, boolean overwrite) {
        List<Home> homes = Home.getByPlayer(player);
        Optional<Home> predicate = homes.stream().filter(home_ -> home_.getName().equals(homeName)).findFirst();

        if (predicate.isPresent() && overwrite) {
            Home home = predicate.get();

            home.setX((long) player.getLocation().getX());
            home.setY(player.getLocation().getBlockY());
            home.setZ((long) player.getLocation().getZ());
            home.setWorld(player.getLocation().getWorld().getName());

            home.saveOrUpdate();
            MessageAdapter.sendMessage(player, Message.Info.CMD_HOME_SET, homeName);
            return ErrorOr.release(null);
        }

        if (predicate.isPresent()) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_SETHOME_ALREADY_EXISTING, homeName);
            MessageAdapter.sendMessage(player, Message.Info.CMD_SETHOME_OVERRIDE_EXISTING_HOME, homeName);
            return new ErrorOr<>(HOME_EXISTING_ERROR);
        }

        PlayerData playerData = PlayerData.getByPlayer(player);
        if (homes.size() >= playerData.getMaxHomes()) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_SETHOME_NOT_ENOUGH_HOMES);
            MessageAdapter.sendMessage(player, Message.Info.CMD_SETHOME_BUYHOME);
            return new ErrorOr<>(HOME_LIMIT_REACHED_ERROR);
        }

        Home home = new Home();
        home.setName(homeName);
        home.setX(player.getLocation().getBlockX());
        home.setY(player.getLocation().getBlockY());
        home.setZ(player.getLocation().getBlockZ());
        home.setWorld(player.getLocation().getWorld().getName());
        home.setPlayerRef(player.getUniqueId().toString());

        home.save();
        MessageAdapter.sendMessage(player, Message.Info.CMD_HOME_SET, homeName);
        return ErrorOr.release(null);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
