package de.paradubsch.paradubschmanager.gui.items;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.craftery.craftinglib.util.gui.GuiItem;
import de.paradubsch.paradubschmanager.models.SaveRequest;
import de.craftery.craftinglib.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SaveConfirmButton extends GuiItem {
    @Override
    public void onClick(Player p) {
        p.closeInventory();

        ProtectedCuboidRegion region1 = (ProtectedCuboidRegion) this.windowArgs.get(0);
        SaveRequest saveRequest = (SaveRequest) this.windowArgs.get(1);

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        if (container == null) return;
        World world = Bukkit.getWorld(saveRequest.getWorld());
        if (world == null) return;
        RegionManager manager = container.get(BukkitAdapter.adapt(world));
        if (manager == null) return;

        manager.addRegion(region1);
        MessageAdapter.sendMessage(p, Message.Info.SAVE_REGION_SUCCESS);
        saveRequest.delete();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equals(saveRequest.getRefName())) {
                MessageAdapter.sendMessage(player, Message.Info.REGION_SAVED_SUCCESSFUL);
            }
        }
    }

    @Override
    public void build() {
        this.setItemMaterial(Material.LIME_STAINED_GLASS_PANE);
        this.setDisplayName(Message.Gui.SAVE_CONFIRM_BUTTON_TITLE);
    }
}
