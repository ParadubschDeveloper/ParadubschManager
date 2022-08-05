package de.paradubsch.paradubschmanager.lifecycle;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.commands.GmCommand;
import de.paradubsch.paradubschmanager.persistance.model.PlayerData;
import de.paradubsch.paradubschmanager.persistance.model.PunishmentHolder;
import de.paradubsch.paradubschmanager.util.Hibernate;
import de.paradubsch.paradubschmanager.util.TimeCalculations;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

import java.sql.Timestamp;
import java.time.Instant;

public class PlayerJoinPrecedure implements Listener {
    public PlayerJoinPrecedure() {
        ParadubschManager.getInstance().getServer().getPluginManager().registerEvents(this, ParadubschManager.getInstance());
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            PunishmentHolder ph = Hibernate.getPunishmentHolder(player);

            if (!ph.isActiveBan()) return;

            if (ph.getActiveBanExpiration().getTime() > Timestamp.from(Instant.now()).getTime()) {
                Bukkit.getScheduler().runTask(ParadubschManager.getInstance(), () -> {
                    PlayerData target = Hibernate.getPlayerData(player);
                    Language lang = Language.getLanguageByName(target.getLanguage());
                    String expirationString = TimeCalculations.timeStampToExpiration(ph.getActiveBanExpiration(), lang);
                    Component msg = ParadubschManager.getInstance().getLanguageManager().get(Message.Info.CMD_BAN_KICK_MESSAGE, lang, ph.getActiveBanReason(), expirationString, "#b-" + ph.getActiveBanId());
                    player.kick(msg);
                });
            } else {
                ph.setActiveBan(false);
                ph.setActiveBanExpiration(Timestamp.from(Instant.now()));
                ph.setActiveBanId(0);
                ph.setActiveBanReason(null);
                Hibernate.save(ph);
            }
        });
        Bukkit.getScheduler().runTaskLater(ParadubschManager.getInstance(), () -> {
            if (player.getPersistentDataContainer().has(GmCommand.GM_KEY, PersistentDataType.INTEGER)) {
                Integer gm = player.getPersistentDataContainer().get(GmCommand.GM_KEY, PersistentDataType.INTEGER);
                if (gm != null) {
                    if (gm == 0) {
                        player.setGameMode(GameMode.SURVIVAL);
                    } else if (gm == 1) {
                        player.setGameMode(GameMode.CREATIVE);
                    } else if(gm == 2) {
                        player.setGameMode(GameMode.ADVENTURE);
                    } else if(gm == 3) {
                        player.setGameMode(GameMode.SPECTATOR);
                    }
                }
            }
        }, 2L);
    }
}
