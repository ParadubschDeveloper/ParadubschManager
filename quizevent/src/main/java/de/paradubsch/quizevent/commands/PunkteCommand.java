package de.paradubsch.quizevent.commands;

import de.paradubsch.quizevent.QuizEvent;
import de.paradubsch.quizevent.QuizTeam;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PunkteCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) {
            sender.sendMessage("Falscher Syntax! /punkte <Team> <Punkte>");
            return true;
        }
        QuizEvent.teams.stream().filter(team -> team.getName().equalsIgnoreCase(args[0])).findFirst().ifPresent(team -> {
            try {
                int points = Integer.parseInt(args[1]);
                team.addPoints(points);
                if (points == 1) {
                    Bukkit.getServer().broadcast(Component.text("Das Team " + team.getTeamColor() + team.getName() + ChatColor.RESET + " hat " + points + " Punkt bekommen!"));
                } else {
                    Bukkit.getServer().broadcast(Component.text("Das Team " + team.getTeamColor() + team.getName() + ChatColor.RESET + " hat " + points + " Punkte bekommen!"));
                }
            } catch (NumberFormatException e) {
                sender.sendMessage("Die Punkte müssen eine Zahl sein!");
            }
        });

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();

        if (args.length == 1) {
            l.addAll(QuizEvent.teams.stream().map(QuizTeam::getName).toList());
        }
        return l;
    }
}
