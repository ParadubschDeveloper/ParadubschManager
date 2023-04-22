package de.paradubsch.paradubschmanager.lifecycle;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.craftery.craftinglib.PlayerData;
import de.paradubsch.paradubschmanager.models.PunishmentHolder;
import de.paradubsch.paradubschmanager.models.logging.ChatMessageLog;
import de.craftery.craftinglib.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.TimeCalculations;
import de.paradubsch.paradubschmanager.util.lang.Message;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.Timestamp;
import java.time.Instant;

public class ChatMessageListener implements Listener {
    public ChatMessageListener() {
        ParadubschManager.getInstance().getServer().getPluginManager().registerEvents(this, ParadubschManager.getInstance());
    }

    @EventHandler
    public void onChatMessage(AsyncChatEvent e) {
        e.setCancelled(true);

        String message = ((TextComponent) e.message()).content();
        PlayerData pd = PlayerData.getByPlayer(e.getPlayer());

        PunishmentHolder ph = PunishmentHolder.getByPlayerDataOrCreate(pd);

        if (ph.getActiveMuteExpiration() != null &&
                ph.getActiveMuteExpiration().getTime() > Timestamp.from(Instant.now()).getTime()) {
            if (ph.isActiveMute() && ph.isPermaMuted()) {
                MessageAdapter.sendMessage(e.getPlayer(), Message.Info.PERMA_MUTED);
                return;
            } else if (ph.isActiveMute()) {
                String expirationString = TimeCalculations.timeStampToExpiration(ph.getActiveMuteExpiration(), MessageAdapter.getSenderLang(e.getPlayer()));
                MessageAdapter.sendMessage(e.getPlayer(), Message.Info.MUTED_HEADER, expirationString);
                MessageAdapter.sendMessage(e.getPlayer(), Message.Info.MUTED_BODY, ph.getActiveMuteReason());
                return;
            }
        } else if (ph.getActiveMuteReason() != null) {
            ph.setActiveMute(false);
            ph.setActiveMuteExpiration(Timestamp.from(Instant.now()));
            ph.setActiveMuteId(0);
            ph.setActiveMuteReason(null);
            ph.saveOrUpdate();
        }

        ChatMessageLog.logMessage(e.getPlayer(), message);

        MessageAdapter.broadcastUnprefixedMessage(
                Message.Constant.CHAT_MESSAGE_TEMPLATE,
                pd.getChatPrefix(),
                pd.getNameColor(),
                e.getPlayer().getName(),
                pd.getDefaultChatColor(),
                message
        );
    }
}
