package de.paradubsch.paradubschmanager.lifecycle;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.GsBanMember;
import de.paradubsch.paradubschmanager.models.GsWhitelistEnabled;
import de.paradubsch.paradubschmanager.models.GsWhitelistMember;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.raidstone.wgevents.events.RegionEnteredEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GsRegionListener implements Listener {
    public GsRegionListener() {
        ParadubschManager.getInstance().getServer().getPluginManager().registerEvents(this, ParadubschManager.getInstance());
    }

    @EventHandler
    public void onRegionEntered(RegionEnteredEvent event) {
        if (event.getPlayer() == null) return;
        if (event.getPlayer().hasPermission("paradubsch.gs.bypass")) return;

        boolean canceled = GsWhitelistEnabled.check(event.getRegion().getId()) &&
                !GsWhitelistMember.canJoin(event.getRegion().getId(), event.getPlayer()) &&
                !event.getRegion().getOwners().contains(event.getPlayer().getUniqueId());
        if (canceled) {
            event.setCancelled(true);
            MessageAdapter.sendMessage(event.getPlayer(), Message.Error.GS_NOT_WHITELISTED);
            return;
        }
        canceled = !GsBanMember.canJoin(event.getRegion().getId(), event.getPlayer()) &&
                !event.getRegion().getOwners().contains(event.getPlayer().getUniqueId());
        if (canceled) {
            event.setCancelled(true);
            MessageAdapter.sendMessage(event.getPlayer(), Message.Error.GS_BANNED);
        }
    }
}
