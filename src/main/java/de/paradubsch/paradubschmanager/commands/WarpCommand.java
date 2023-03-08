package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.Warp;
import de.craftery.util.Expect;
import de.craftery.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.minArgs(1, args)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_WARP_NOT_PROVIDED);
            return true;
        }

        switch (args[0]) {
            case "create": {
                createWarp(sender, args);
                break;
            }
            case "delete": {
                deleteWarp(sender, args);
                break;
            }
            default: {
                warp(sender, args[0]);
                break;
            }
        }

        return true;
    }

    private void createWarp(CommandSender sender, String[] name) {
        if (!Expect.minArgs(2, name)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_WARP_NOT_PROVIDED);
            return;
        }
        createWarp(sender, name[1]);
    }

    private void createWarp(CommandSender sender, String name) {
        if (!sender.hasPermission("paradubsch.warp.manage")) {
            MessageAdapter.sendMessage(sender, Message.Error.NO_PERMISSION);
            return;
        }


        if (!Expect.playerSender(sender)) {
            return;
        }

        Location loc = ((Player) sender).getLocation();
        Warp warp = new Warp();
        warp.setWorld(loc.getWorld().getName());
        warp.setX(loc.getX());
        warp.setY(loc.getY());
        warp.setZ(loc.getZ());
        warp.setYaw(loc.getYaw());
        warp.setPitch(loc.getPitch());
        warp.setName(name);
        warp.setCreationTimestamp(Timestamp.from(Instant.now()));

        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), warp::saveOrUpdate);

        MessageAdapter.sendMessage(sender, Message.Info.CMD_WARP_CREATED, name);
    }

    private void warp(CommandSender sender, String arg) {
        if (!Expect.playerSender(sender)) {
            return;
        }
        warp((Player) sender, arg);
    }

    public static void warp (Player player, String warpName) {
        Warp warp = Warp.getById(warpName);

        if (warp == null) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_WARP_NOT_FOUND, warpName);
            return;
        }

        Location loc = new Location(Bukkit.getWorld(warp.getWorld()), warp.getX(), warp.getY(), warp.getZ(), warp.getYaw(), warp.getPitch());
        player.teleport(loc);
        MessageAdapter.sendMessage(player, Message.Info.CMD_WARP_TELEPORTED, warpName);
    }

    private void deleteWarp(CommandSender sender, String[] args) {
        if (!Expect.minArgs(2, args)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_WARP_NOT_PROVIDED);
            return;
        }
        Warp warp = Warp.getById(args[1]);

        if (warp == null) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_WARP_NOT_FOUND, args[1]);
            return;
        }

        warp.delete();

        MessageAdapter.sendMessage(sender, Message.Info.CMD_WARP_DELETED, args[1]);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1 && sender.hasPermission("paradubsch.warp.manage")) {
            l.add("create");
            l.add("delete");
            return l;
        }
        return l;
    }
}
