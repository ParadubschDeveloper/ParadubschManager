package de.paradubsch.paradubschmanager.lifecycle;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.Hibernate;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public class TabDecorationManager implements Listener {
    private final JavaPlugin plugin;

    public TabDecorationManager(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getScheduler().runTaskLater(plugin, this::broadcastTabDecorations, 20*5);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        broadcastTabDecorations();
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

    public static void displayTabDecorations(Player p) {
        PacketContainer packet = ParadubschManager.getInstance().getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
        PlayerData playerData = Hibernate.getPlayerData(p);
        Language playerLang = Language.getLanguageByShortName(playerData.getLanguage());
        String header = ParadubschManager.getInstance().getLanguageManager().getString(Message.Info.TAB_HEADER, playerLang, Bukkit.getOnlinePlayers().size() + "");
        String footer = ParadubschManager.getInstance().getLanguageManager().getString(Message.Info.TAB_FOOTER, playerLang);
        packet.getChatComponents().write(0, WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', header)));
        packet.getChatComponents().write(1, WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', footer)));
        try {
            ParadubschManager.getInstance().getProtocolManager().sendServerPacket(p, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
