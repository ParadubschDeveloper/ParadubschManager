package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.persistance.model.Home;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.Hibernate;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
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

public class HomeCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_ONLY_FOR_PLAYERS);
            return true;
        }
        Player player = (Player) sender;

        String homeName;
        if (args.length == 0) {
            homeName = "default";
        } else {
            homeName = args[0];
        }

        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            List<Home> homes = Hibernate.getHomes(player);
            if (homes.stream().anyMatch(home -> home.getName().equals(homeName))) {
                Home home = homes.stream().filter(home1 -> home1.getName().equals(homeName)).findFirst().get();

                double x = home.getX() >= 0 ? home.getX() + 0.5 : home.getX() - 0.5;
                double z = home.getZ() >= 0 ? home.getZ() + 0.5 : home.getZ() - 0.5;

                World world = ParadubschManager.getInstance().getServer().getWorld(home.getWorld());
                Location loc = new Location(world, x, home.getY(), z);
                Bukkit.getScheduler().runTask(ParadubschManager.getInstance(), () -> player.teleport(loc));
                MessageAdapter.sendMessage(player, Message.Info.CMD_HOME_TELEPORT, homeName);
            } else if (homes.stream().anyMatch(home -> home.getName().equalsIgnoreCase(homeName))) {
                Home home = homes.stream().filter(home1 -> home1.getName().equalsIgnoreCase(homeName)).findFirst().get();
                MessageAdapter.sendMessage(player, Message.Info.CMD_HOME_MAYBE_WRONG_NAME, homeName, home.getName());
            } else {
                MessageAdapter.sendMessage(player,Message.Error.CMD_HOME_NOT_FOUND, homeName);
            }
        });
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
