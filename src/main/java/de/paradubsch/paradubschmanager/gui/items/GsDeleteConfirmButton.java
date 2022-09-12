package de.paradubsch.paradubschmanager.gui.items;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.craftery.util.gui.GuiItem;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GsDeleteConfirmButton extends GuiItem {
    @Override
    public void onClick(Player p) {
        ProtectedRegion region = (ProtectedRegion) this.windowArgs.get(0);

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        if (container == null) return;
        RegionManager manager = container.get(BukkitAdapter.adapt(p.getWorld()));
        if (manager == null) return;

        manager.removeRegion(region.getId());

        p.closeInventory();
        MessageAdapter.sendMessage(p, Message.Info.GS_DELETE_SUCCESSFUL);
    }

    @Override
    public void build() {
        this.setItemMaterial(Material.LIME_STAINED_GLASS_PANE);
        this.setDisplayName(Message.Gui.GS_DELETE_BUTTON_TITLE);
        this.addLore(Message.Gui.GS_DELETE_BUTTON_LORE);
    }
}
