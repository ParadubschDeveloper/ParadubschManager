package de.paradubsch.quizevent;

import de.craftery.craftinglib.CraftingPlugin;
import de.paradubsch.quizevent.commands.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class QuizEvent extends CraftingPlugin implements Listener {
    public static final List<QuizTeam> teams = new ArrayList<>();

    public static boolean buzzerEnabled = true;

    @Override
    public void onEnable() {
        super.onEnable();
        Bukkit.getLogger().info("ParadubschQuiz loaded.");

        teams.add(new QuizTeam("Hellblau", ChatColor.AQUA));
        teams.add(new QuizTeam("Schwarz", ChatColor.BLACK));
        teams.add(new QuizTeam("Blau", ChatColor.BLUE));
        teams.add(new QuizTeam("Türkis", ChatColor.DARK_AQUA));
        teams.add(new QuizTeam("Dunkelblau", ChatColor.DARK_BLUE));
        teams.add(new QuizTeam("Dunkelgrau", ChatColor.DARK_GRAY));
        teams.add(new QuizTeam("Dunkelgrün", ChatColor.DARK_GREEN));
        teams.add(new QuizTeam("Dunkellila", ChatColor.DARK_PURPLE));
        teams.add(new QuizTeam("Dunkelrot", ChatColor.DARK_RED));
        teams.add(new QuizTeam("Gold", ChatColor.GOLD));
        teams.add(new QuizTeam("Grau", ChatColor.GRAY));
        teams.add(new QuizTeam("Grün", ChatColor.GREEN));
        teams.add(new QuizTeam("Helllila", ChatColor.LIGHT_PURPLE));
        teams.add(new QuizTeam("Rot", ChatColor.RED));
        teams.add(new QuizTeam("Weiß", ChatColor.WHITE));
        teams.add(new QuizTeam("Gelb", ChatColor.YELLOW));

        this.registerLegacyCommand("team", new TeamCommand());
        this.registerLegacyCommand("pult", new PultCommand());
        this.registerLegacyCommand("buzzer", new BuzzerCommand());
        this.registerLegacyCommand("punkte", new PunkteCommand());
        this.registerLegacyCommand("punktestand", new PunktestandCommand());

        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        super.onDisable();

        Bukkit.getLogger().info("ParadubschQuiz unloaded.");
    }
    @EventHandler
    public void playerQuitEvent(PlayerQuitEvent event) {
        for (QuizTeam team : teams) {
            if (team.getPlayers().contains(event.getPlayer())) {
                team.getPlayers().remove(event.getPlayer());
                Bukkit.broadcast(Component.text(team.getTeamColor() + event.getPlayer().getName() + ChatColor.GRAY + " hat das Team " + team.getTeamColor() + team.getName() + ChatColor.GRAY + " verlassen."));
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clicked = event.getClickedBlock();
            if (clicked != null && clicked.getType().name().endsWith("_BUTTON")) {
                QuizEvent.teams.forEach(team -> {
                    if (team.getBuzzerLocation() != null &&
                            team.getBuzzerLocation().getBlockX() == clicked.getX() &&
                            team.getBuzzerLocation().getBlockY() == clicked.getY() &&
                            team.getBuzzerLocation().getBlockZ() == clicked.getZ() &&
                            team.getPlayers().contains(event.getPlayer())
                    ) {
                        if (buzzerEnabled) {
                            buzzerEnabled = false;
                            Bukkit.getServer().broadcast(Component.text(
                                    ChatColor.GREEN + event.getPlayer().getName() +
                                            "(" + team.getTeamColor() + team.getName() + ChatColor.GREEN + ") hat gebuzzert!"));
                            buzzerEnabled = false;
                        } else {
                            event.getPlayer().sendMessage("Der Buzzer ist gerade deaktiviert.");
                        }
                    }
                });
            }
        }
    }

}
