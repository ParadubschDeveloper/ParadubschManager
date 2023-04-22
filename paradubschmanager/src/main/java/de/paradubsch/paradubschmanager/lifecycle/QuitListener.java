package de.paradubsch.paradubschmanager.lifecycle;

import de.craftery.craftinglib.util.CachingManager;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.craftery.craftinglib.util.HibernateConfigurator;
import de.craftery.craftinglib.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
    public QuitListener() {
        ParadubschManager.getInstance().getServer().getPluginManager().registerEvents(this, ParadubschManager.getInstance());
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onQuitEvent(PlayerQuitEvent e) {
        e.setQuitMessage("");
        ParadubschManager.getInstance().getVanishedPlayers().remove(e.getPlayer().getUniqueId());
        HibernateConfigurator.getSessionFactory().getCache()
                .evictEntityData(PlayerData.class, e.getPlayer().getUniqueId().toString());

        CachingManager cm = ParadubschManager.getInstance().getCachingManager();
        cm.deleteEntity(PlayerData.class, e.getPlayer().getUniqueId().toString());
    }
}
