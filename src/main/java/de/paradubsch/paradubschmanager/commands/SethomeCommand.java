package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.persistance.model.Home;
import de.paradubsch.paradubschmanager.persistance.model.PlayerData;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.Hibernate;
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

public class SethomeCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_ONLY_FOR_PLAYERS);
            return true;
        }
        Player player = (Player) sender;


        String homeName;
        if (args.length > 0) {
            homeName = args[0];
        } else {
            homeName = "default";
        }

        if (args.length > 1 && args[1].equalsIgnoreCase("confirm")) {
            Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
                PlayerData playerData = Hibernate.getPlayerData(player);
                List<Home> homes = Hibernate.getHomes(player);

                if (homes.stream().anyMatch(home -> home.getName().equals(homeName))) {
                    Home home = homes.stream().filter(home1 -> home1.getName().equals(homeName)).findFirst().get();
                    homes.remove(home);
                    home.setX((long) player.getLocation().getX());
                    home.setY(player.getLocation().getBlockY());
                    home.setZ((long) player.getLocation().getZ());
                    home.setWorld(player.getLocation().getWorld().getName());

                    homes.add(home);
                    playerData.setHomes(homes);

                    MessageAdapter.sendMessage(player, Message.Info.CMD_HOME_SET, homeName);
                    Hibernate.save(playerData);
                    return;
                }

                if (homes.size() >= playerData.getMaxHomes()) {
                    MessageAdapter.sendMessage(sender, Message.Error.CMD_SETHOME_NOT_ENOUGH_HOMES);
                    Bukkit.getScheduler().runTaskLater(ParadubschManager.getInstance(), () ->
                            MessageAdapter.sendMessage(sender, Message.Info.CMD_SETHOME_BUYHOME), 1L);
                    return;
                }

                Home home = new Home();
                home.setName(homeName);
                home.setX(player.getLocation().getBlockX());
                home.setY(player.getLocation().getBlockY());
                home.setZ(player.getLocation().getBlockZ());
                home.setWorld(player.getLocation().getWorld().getName());
                home.setPlayerRef(playerData);

                homes.add(home);


                playerData.setHomes(homes);

                MessageAdapter.sendMessage(player, Message.Info.CMD_HOME_SET, homeName);
                Hibernate.save(playerData);
            });
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            PlayerData playerData = Hibernate.getPlayerData(player);
            List<Home> homes = Hibernate.getHomes(player);

            if (homes.stream().anyMatch(home -> home.getName().equals(homeName))) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_SETHOME_ALREADY_EXISTING, homeName);
                Bukkit.getScheduler().runTaskLater(ParadubschManager.getInstance(), () ->
                        MessageAdapter.sendMessage(sender, Message.Info.CMD_SETHOME_OVERRIDE_EXISTING_HOME, homeName), 1L);
                Hibernate.save(playerData);
                return;
            }

            if (homes.size() >= playerData.getMaxHomes()) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_SETHOME_NOT_ENOUGH_HOMES);
                Bukkit.getScheduler().runTaskLater(ParadubschManager.getInstance(), () ->
                        MessageAdapter.sendMessage(sender, Message.Info.CMD_SETHOME_BUYHOME), 1L);
                Hibernate.save(playerData);
                return;
            }

            Home home = new Home();
            home.setName(homeName);
            home.setX(player.getLocation().getBlockX());
            home.setY(player.getLocation().getBlockY());
            home.setZ(player.getLocation().getBlockZ());
            home.setWorld(player.getLocation().getWorld().getName());
            home.setPlayerRef(playerData);

            homes.add(home);


            playerData.setHomes(homes);

            MessageAdapter.sendMessage(player, Message.Info.CMD_HOME_SET, homeName);
            Hibernate.save(playerData);
        });
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
