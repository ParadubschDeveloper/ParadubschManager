package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.craftinglib.util.gui.AbstractGuiItem;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.commands.RtpCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class RtpButton extends AbstractGuiItem {
    @Override
    public void onClick(Player p) {
        // 10 Minutes
        ParadubschManager.getInstance().getRtpTimeouts().put(p.getUniqueId(), System.currentTimeMillis() + 30000L);

        p.closeInventory();
        World world = Bukkit.getWorld((String) this.itemArgs.get(2));
        RtpCommand.rtp(p, world, 1);
    }

    @Override
    public void build() {
        String headId = (String) this.itemArgs.get(0);
        this.setItemHead(headId);
        this.setDisplayName("§7" + this.itemArgs.get(1));
    }
}
