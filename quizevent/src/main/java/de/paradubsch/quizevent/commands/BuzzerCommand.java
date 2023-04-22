package de.paradubsch.quizevent.commands;


import de.paradubsch.quizevent.QuizEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BuzzerCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können mit dem Pult interagieren!");
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Du musst eine Aktion eingeben (set/reset)");
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (args.length != 5) {
                player.sendMessage("Falscher Syntax! /buzzer set <team> <x> <y> <z>");
                return true;
            }
            QuizEvent.teams.stream().filter(team -> team.getName().equalsIgnoreCase(args[1])).findFirst().ifPresent(team -> {
                try {
                    int x = Integer.parseInt(args[2]);
                    int y = Integer.parseInt(args[3]);
                    int z = Integer.parseInt(args[4]);
                    Location loc = new Location(player.getWorld(), x, y, z);
                    team.setBuzzerLocation(loc);
                    player.sendMessage(ChatColor.GREEN + "Du hast den Buzzer des Teams " + team.getTeamColor() + team.getName() + ChatColor.GREEN + " auf " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + " gesetzt!");
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Die Koordinaten müssen Zahlen sein!");
                }
            });

        } else if (args[0].equalsIgnoreCase("reset")) {
            QuizEvent.buzzerEnabled = true;
            player.sendMessage(ChatColor.GREEN + "Du hast den Buzzer zurückgesetzt!");
        } else {
            player.sendMessage("Du musst eine Aktion eingeben (set/reset)");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            l.add("set");
            l.add("reset");
            return l;
        }

        if (args.length == 2) {
            l.addAll(QuizEvent.teams.stream().map(team -> team.getName().toLowerCase()).toList());
        }

        return l;
    }
}
