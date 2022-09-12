package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.GuiItem;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.gui.window.BazaarSellOrderGui;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BazaarSellOrderButton extends GuiItem {
    @Override
    public void onClick(Player p) {
        GuiManager.navigate(BazaarSellOrderGui.class, p, this.itemArgs.get(0));
    }

    @Override
    public void build() {
        this.setItemMaterial(Material.DIAMOND_HORSE_ARMOR);
        this.setDisplayName(Message.Gui.PLACE_SELL_ORDER);
    }
}
