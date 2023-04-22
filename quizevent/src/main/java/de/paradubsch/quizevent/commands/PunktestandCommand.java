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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PunktestandCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Bukkit.broadcast(Component.text("Punktestand:"));

        List<QuizTeam> teams = new ArrayList<>(QuizEvent.teams);
        teams.sort(Comparator.comparingInt(QuizTeam::getPoints));
        for (QuizTeam team : teams) {
            if (team.getPlayers().size() > 0) {
                String players = team.getPlayers().stream().map(Player::getName).collect(Collectors.joining(", "));
                Bukkit.broadcast(Component.text(
                        ChatColor.GRAY + "Team " + team.getTeamColor() + team.getName() +
                                ChatColor.GRAY + " (" + players + "): "+
                                ChatColor.GREEN + team.getPoints()));
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
