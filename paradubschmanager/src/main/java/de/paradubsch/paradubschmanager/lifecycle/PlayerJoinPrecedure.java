package de.paradubsch.paradubschmanager.lifecycle;

import de.craftery.craftinglib.messaging.lang.Language;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.commands.GmCommand;
import de.craftery.craftinglib.PlayerData;
import de.paradubsch.paradubschmanager.models.PunishmentHolder;
import de.paradubsch.paradubschmanager.util.TimeCalculations;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class PlayerJoinPrecedure implements Listener {
    public PlayerJoinPrecedure() {
        ParadubschManager.getInstance().getServer().getPluginManager().registerEvents(this, ParadubschManager.getInstance());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(null);
        Player player = event.getPlayer();

        PlayerData.cachePlayerName(player);
        PunishmentHolder ph = PunishmentHolder.getByPlayerOrCreate(player);

        if (!ph.isActiveBan()) return;

        if (ph.getActiveBanExpiration() != null && ph.getActiveBanExpiration().getTime() > Timestamp.from(Instant.now()).getTime()) {
            PlayerData target = PlayerData.getByPlayer(player);
            Language lang = Language.getLanguageByShortName(target.getLanguage());
            String expirationString = TimeCalculations.timeStampToExpiration(ph.getActiveBanExpiration(), lang);
            Component msg = ParadubschManager.getInstance().getLanguageManager().get(Message.Info.CMD_BAN_KICK_MESSAGE, lang, ph.getActiveBanReason(), expirationString, "#b-" + ph.getActiveBanId());
            player.kick(msg);
        } else {
            ph.setActiveBan(false);
            ph.setActiveBanExpiration(Timestamp.from(Instant.now()));
            ph.setActiveBanId(0);
            ph.setActiveBanReason(null);
            ph.saveOrUpdate();
        }
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
        if (!event.getPlayer().hasPermission("paradubsch.vanish.bypass")) {
            for (UUID pUUID : ParadubschManager.getInstance().getVanishedPlayers()) {
                Player p = Bukkit.getPlayer(pUUID);
                if (p != null) {
                    event.getPlayer().hidePlayer(ParadubschManager.getInstance(), p);
                }
            }
        }
    }
}
