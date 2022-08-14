package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.AbstractGuiItem;
import de.paradubsch.paradubschmanager.commands.RtpCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class RtpButton extends AbstractGuiItem {
    @Override
    public void onClick(Player p) {
        p.closeInventory();
        World world = Bukkit.getWorld((String) this.getIdentifier());
        RtpCommand.rtp(p, world, 1);
    }

    @Override
    public void build() {
        Material mat = (Material) this.itemArgs.get(0);
        this.setItemMaterial(mat);
        if (this.getIdentifier() == "world_the_end") {
            this.setDisplayName("§7End");
        } else if (this.getIdentifier() == "world_nether") {
            this.setDisplayName("§7Nether");
        } else {
            this.setDisplayName("§7" + this.getIdentifier());
        }
    }
}
