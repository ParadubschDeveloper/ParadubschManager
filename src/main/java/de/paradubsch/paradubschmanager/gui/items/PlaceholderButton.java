package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlaceholderButton extends GuiItem {
    @Override
    public void onClick(Player p) {
        // Do nothing
    }

    @Override
    public void build() {
        Material mat = (Material) this.itemArgs.get(0);
        this.setItemMaterial(mat);
        this.setDisplayName("");
    }
}
