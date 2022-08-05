package de.paradubsch.paradubschmanager.lifecycle;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import de.paradubsch.paradubschmanager.persistance.model.PlayerData;
import de.paradubsch.paradubschmanager.util.Hibernate;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TabDecorationManager implements Listener {
    private final JavaPlugin plugin;

    private static Scoreboard sb;

    public TabDecorationManager(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getScheduler().runTaskLater(plugin, this::broadcastTabDecorations, 20*5);
        broadcastScoreboardTeams();
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        broadcastTabDecorations();
        broadcastScoreboardTeams();
    }

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event) {
        broadcastTabDecorations();
    }

    private void broadcastTabDecorations() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                TabDecorationManager.displayTabDecorations(p);
            }
        });
    }

    public static void broadcastScoreboardTeams() {
        Bukkit.getScheduler().runTask(ParadubschManager.getInstance(), () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                setScoreboardTeam(p);
            }
        });
    }

    public static void displayTabDecorations(Player p) {
        ProtocolManager pm = ParadubschManager.getProtocolManager();
        if (pm == null) return;
        PacketContainer packet = pm.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
        PlayerData playerData = Hibernate.getPlayerData(p);
        Language playerLang = Language.getLanguageByShortName(playerData.getLanguage());
        String header = ParadubschManager.getInstance().getLanguageManager().getString(Message.Info.TAB_HEADER, playerLang, Bukkit.getOnlinePlayers().size() + "");
        String footer = ParadubschManager.getInstance().getLanguageManager().getString(Message.Info.TAB_FOOTER, playerLang);
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', header)));
        packet.getChatComponents().write(1, WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', footer)));
        try {
            pm.sendServerPacket(p, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void setScoreboardTeam(Player p) {
        ConfigurationSection configurationSection = ConfigurationManager.getConfig().getConfigurationSection("tabprefix");
        if (configurationSection == null) return;
        Set<String> keys = configurationSection.getKeys(false);
        String rankForPlayer = null;
        List<String> ranks = new ArrayList<>();
        keys.stream().sorted((str1, str2) -> {
            String s1 = ConfigurationManager.getConfig().getString("tabprefix." + str1 + ".team");
            String s2 = ConfigurationManager.getConfig().getString("tabprefix." + str2 + ".team");
            if (s1 == null || s2 == null) {
                return 0;
            }
            return s2.compareTo(s1);
        }).forEach(ranks::add);
        for (String rank : ranks) {
            if (p.hasPermission("group." + rank)) rankForPlayer = rank;
        }
        applyScoreboard(p, rankForPlayer);
    }

    private static void applyScoreboard(Player p, String groupName) {
        String team = ConfigurationManager.getConfig().getString("tabprefix." + groupName + ".team");
        String prefix = ConfigurationManager.getConfig().getString("tabprefix." + groupName + ".prefix");
        String color = ConfigurationManager.getConfig().getString("tabprefix." + groupName + ".color", "&7");
        if (team == null || prefix == null) return;

        if (TabDecorationManager.sb == null) {
            TabDecorationManager.sb = Bukkit.getScoreboardManager().getNewScoreboard();
        }

        Scoreboard scoreboard = TabDecorationManager.sb;
        Team scoreboardTeam = scoreboard.getTeam(team);
        if (scoreboardTeam == null) {
            scoreboardTeam = scoreboard.registerNewTeam(team);
        }
        scoreboardTeam.prefix(Component.text(ChatColor.translateAlternateColorCodes('&', prefix + " &7| ")));

        color = color.replaceAll("&", "");
        color = color.replaceAll("[^0-9a-f]", "");
        ChatColor parsedChatColor = ChatColor.getByChar(color.charAt(0));
        if (parsedChatColor == null) return;

        scoreboardTeam.color(translateCharCode(parsedChatColor.getChar()));
        scoreboardTeam.addEntry(p.getName());
        p.setScoreboard(scoreboard);
    }

    public static NamedTextColor translateCharCode (char name) {
        switch (name) {
            case '0': return NamedTextColor.BLACK;
            case '1': return NamedTextColor.DARK_BLUE;
            case '2': return NamedTextColor.DARK_GREEN;
            case '3': return NamedTextColor.DARK_AQUA;
            case '4': return NamedTextColor.DARK_RED;
            case '5': return NamedTextColor.DARK_PURPLE;
            case '6': return NamedTextColor.GOLD;
            case '8': return NamedTextColor.DARK_GRAY;
            case '9': return NamedTextColor.BLUE;
            case 'a': return NamedTextColor.GREEN;
            case 'b': return NamedTextColor.AQUA;
            case 'c': return NamedTextColor.RED;
            case 'd': return NamedTextColor.LIGHT_PURPLE;
            case 'e': return NamedTextColor.YELLOW;
            case 'f': return NamedTextColor.WHITE;
            default: return NamedTextColor.GRAY;
        }
    }
}
