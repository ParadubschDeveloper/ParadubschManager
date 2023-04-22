package de.paradubsch.quizevent.commands;

import de.paradubsch.quizevent.QuizEvent;
import de.paradubsch.quizevent.QuizTeam;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeamCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können sich in ein Team begeben");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(Component.text(ChatColor.RED + "Du musst eine Teamfarbe angeben!"));
            return true;
        }

        String targetTeam;
        if (args.length > 1 && args[0].equalsIgnoreCase("join")) {
            targetTeam = args[1];
        } else {
            targetTeam = args[0];
        }

        if (args[0].equalsIgnoreCase("verlassen")) {
            QuizEvent.teams.forEach(t -> t.getPlayers().remove(player));
            player.sendMessage(Component.text(ChatColor.GREEN + "Du hast dein Team verlassen!"));
            return true;
        }

        if (QuizEvent.teams.stream().noneMatch(team -> team.getName().equalsIgnoreCase(targetTeam))) {
            player.sendMessage(Component.text(ChatColor.RED + "Das Team "+ ChatColor.GOLD + targetTeam + ChatColor.RED + " existiert nicht!"));
            return true;
        }

        QuizEvent.teams.forEach(t -> {
            if (t.getName().equalsIgnoreCase(targetTeam)) {
                t.getPlayers().add(player);
                player.sendMessage(Component.text(ChatColor.GREEN + "Du bist nun im Team " + t.getTeamColor() + t.getName() + ChatColor.GREEN + "!"));
            } else {
                t.getPlayers().remove(player);
            }
        });

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            l.addAll(QuizEvent.teams.stream().map(team -> team.getName().toLowerCase()).toList());
            l.add("join");
            l.add("verlassen");
            return l;
        }
        if (args.length == 2) {
            l.addAll(QuizEvent.teams.stream().map(QuizTeam::getName).toList());
        }
        return l;
    }
}
