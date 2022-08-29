package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.GuiItem;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.gui.window.BazaarCollectGui;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BazaarCollectButton extends GuiItem {
    @Override
    public void onClick(Player p) {
        GuiManager.navigate(BazaarCollectGui.class, p);
    }

    @Override
    public void build() {
        this.setItemMaterial(Material.BUCKET);
        this.setDisplayName(Message.Gui.BAZAAR_COLLECT_TITLE);
    }
}
