package de.paradubsch.paradubschmanager.lifecycle.playtime;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.util.Hibernate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PlaytimeManager implements Listener {
    private final Map<Player, PlaytimeInstance> cachedData = new HashMap<>();

    public PlaytimeManager() {
        ParadubschManager.getInstance().getServer().getPluginManager().registerEvents(this, ParadubschManager.getInstance());
        Bukkit.getOnlinePlayers().forEach(player -> {
            CompletableFuture.supplyAsync(() -> Hibernate.getPlayerData(player))
                    .thenAccept(pd -> {
                        PlaytimeInstance pi = new PlaytimeInstance();
                        pi.setPlaytime(pd.getPlaytime());
                        pi.setLastRecordTime(System.currentTimeMillis());
                        cachedData.put(player, pi);
                    });
        });
        enableScheduler();
    }

    public long getPlaytime(Player player) {
        PlaytimeInstance pi = cachedData.get(player);
        if (pi == null) throw new RuntimeException("Player is not Cached.- This must be an error!");

        long newPlaytime = pi.getPlaytime() + System.currentTimeMillis() - pi.getLastRecordTime();
        pi.setPlaytime(newPlaytime);
        pi.setLastRecordTime(System.currentTimeMillis());
        cachedData.put(player, pi);
        return newPlaytime;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        CompletableFuture.supplyAsync(() -> Hibernate.getPlayerData(player))
                .thenAccept(pd -> {
                   PlaytimeInstance pi = new PlaytimeInstance();
                   pi.setPlaytime(pd.getPlaytime());
                   pi.setLastRecordTime(System.currentTimeMillis());
                   cachedData.put(player, pi);
                });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PlaytimeInstance pi = cachedData.get(player);
        if (pi == null) {
            return;
        }

        long newPlaytime = pi.getPlaytime() + System.currentTimeMillis() - pi.getLastRecordTime();

        pi.setPlaytime(newPlaytime);
        pi.setLastRecordTime(System.currentTimeMillis());
        cachedData.remove(player);

        CompletableFuture.supplyAsync(() -> Hibernate.getPlayerData(player))
                .thenApply(pd -> {
                    pd.setPlaytime(newPlaytime);
                    return pd;
                }).thenAcceptAsync(Hibernate::save);
    }

    private void enableScheduler () {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(ParadubschManager.getInstance(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    PlaytimeInstance pi = cachedData.get(player);
                    if (pi == null) {
                        return;
                    }

                    long newPlaytime = pi.getPlaytime() + System.currentTimeMillis() - pi.getLastRecordTime();

                    pi.setPlaytime(newPlaytime);
                    pi.setLastRecordTime(System.currentTimeMillis());
                    cachedData.put(player, pi);

                    CompletableFuture.supplyAsync(() -> Hibernate.getPlayerData(player))
                            .thenApply(pd -> {
                                pd.setPlaytime(newPlaytime);
                                return pd;
                            }).thenAcceptAsync(Hibernate::save);

                });
            }
        }, 20*60*5, 20*60*5);

    }


}
