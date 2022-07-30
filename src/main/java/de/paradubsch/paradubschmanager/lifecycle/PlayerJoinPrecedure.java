package de.paradubsch.paradubschmanager.lifecycle;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.commands.GmCommand;
import de.paradubsch.paradubschmanager.util.Hibernate;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

public class PlayerJoinPrecedure implements Listener {
    public PlayerJoinPrecedure() {
        ParadubschManager.getInstance().getServer().getPluginManager().registerEvents(this, ParadubschManager.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(ParadubschManager.getInstance(), () -> {
            if (player.getPersistentDataContainer().has(GmCommand.GM_KEY, PersistentDataType.INTEGER)) {
                Integer gm = player.getPersistentDataContainer().get(GmCommand.GM_KEY, PersistentDataType.INTEGER);
                System.out.println(gm);
                if (gm != null) {
                    if (gm == 0) {
                        System.out.println("SET GM 0");
                        player.setGameMode(GameMode.SURVIVAL);
                    } else if (gm == 1) {
                        System.out.println("SET GM 1");
                        player.setGameMode(GameMode.CREATIVE);
                    } else if(gm == 2) {
                        System.out.println("SET GM 2");
                        player.setGameMode(GameMode.ADVENTURE);
                    } else if(gm == 3) {
                        System.out.println("SET GM 3");
                        player.setGameMode(GameMode.SPECTATOR);
                    }
                }
            }
        }, 2L);
        Hibernate.cachePlayerName(player);
    }
}
