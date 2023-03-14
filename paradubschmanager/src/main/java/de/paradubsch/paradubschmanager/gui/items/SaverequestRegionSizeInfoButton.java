package de.paradubsch.paradubschmanager.gui.items;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import de.craftery.util.gui.GuiItem;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SaverequestRegionSizeInfoButton extends GuiItem {
    @Override
    public void onClick(Player p) {

    }

    @Override
    public void build() {
        ProtectedCuboidRegion region = (ProtectedCuboidRegion) this.windowArgs.get(0);
        this.setItemMaterial(Material.SCAFFOLDING);
        this.setDisplayName(Message.Gui.PLOT_SIZE);

        int minx = region.getMinimumPoint().getX();
        int minz = region.getMinimumPoint().getZ();
        int maxx = region.getMaximumPoint().getX();
        int maxz = region.getMaximumPoint().getZ();
        int xDiff;
        if (minx > maxx) {
            xDiff = minx - maxx;
        } else {
            xDiff = maxx - minx;
        }

        int zDiff;
        if (minz > maxz) {
            zDiff = minz - maxz;
        } else {
            zDiff = maxz - minz;
        }
        xDiff++;
        zDiff++;
        this.addLore(Component.text("§a" + xDiff + "x" + zDiff));
    }
}
