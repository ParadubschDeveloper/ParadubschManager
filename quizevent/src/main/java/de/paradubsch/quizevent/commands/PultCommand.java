package de.paradubsch.quizevent.commands;

import de.paradubsch.quizevent.QuizEvent;
import de.paradubsch.quizevent.QuizTeam;
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
import java.util.Optional;

public class PultCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können mit dem Pult interagieren!");
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            Optional<QuizTeam> opTeam = QuizEvent.teams.stream().filter(team -> team.getPlayers().contains(player)).findFirst();
            if (opTeam.isEmpty()) {
                player.sendMessage(ChatColor.RED + "Du bist in keinem Team!");
                return true;
            }

            opTeam.ifPresent(team -> {
                Location loc = team.getPultLocation();
                if (loc == null) {
                    player.sendMessage(ChatColor.RED + "Dein Team hat noch kein Pult gesetzt!");
                } else {
                    player.teleport(loc);
                    player.sendMessage(ChatColor.GREEN + "Du wurdest zum Pult des Teams " + team.getTeamColor() + team.getName() + ChatColor.GREEN + " teleportiert!");
                }
            });
            return true;
        }
        switch (args[0]) {
            case "tp": {
                if (args.length > 1) {
                    QuizEvent.teams.stream().filter(team -> team.getName().equalsIgnoreCase(args[1])).findFirst().ifPresent(team -> {
                        Location loc = team.getPultLocation();
                        if (loc == null) {
                            player.sendMessage(ChatColor.RED + "Das Team hat noch kein Pult gesetzt!");
                        } else {
                            player.teleport(loc);
                            player.sendMessage(ChatColor.GREEN + "Du wurdest zum Pult des Teams " + team.getTeamColor() + team.getName() + ChatColor.GREEN + " teleportiert!");
                        }
                    });
                } else {
                    player.sendMessage(ChatColor.RED + "Du musst ein Team angeben!");
                }

                break;
            }

            case "set": {
                if (!player.hasPermission("paradubschquiz.pult.set")) {
                    player.sendMessage(ChatColor.RED + "Du hast keine Berechtigung, das Pult zu setzen!");
                    return true;
                }
                if (args.length > 1) {
                    QuizEvent.teams.stream().filter(team -> team.getName().equalsIgnoreCase(args[1])).findFirst().ifPresent(team -> {
                        team.setPultLocation(player.getLocation());
                        player.sendMessage(ChatColor.GREEN + "Du hast das Pult des Teams " + team.getTeamColor() + team.getName() + ChatColor.GREEN + " gesetzt!");
                    });
                } else {
                    player.sendMessage(ChatColor.RED + "Du musst ein Team angeben!");
                }
                break;
            }

            default: {
                QuizEvent.teams.stream().filter(team -> team.getName().equalsIgnoreCase(args[0])).findFirst().ifPresent(team -> {
                    Location loc = team.getPultLocation();
                    if (loc == null) {
                        player.sendMessage(ChatColor.RED + "Das Team hat noch kein Pult gesetzt!");
                    } else {
                        player.teleport(loc);
                        player.sendMessage(ChatColor.GREEN + "Du wurdest zum Pult des Teams " + team.getTeamColor() + team.getName() + ChatColor.GREEN + " teleportiert!");
                    }
                });
            }
        }


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();

        if (args.length == 1) {
            l.addAll(QuizEvent.teams.stream().map(team -> team.getName().toLowerCase()).toList());
            l.add("tp");
            l.add("set");
            return l;
        }
        if (args.length == 2) {
            l.addAll(QuizEvent.teams.stream().map(team -> team.getName().toLowerCase()).toList());
        }
        return l;
    }
}
