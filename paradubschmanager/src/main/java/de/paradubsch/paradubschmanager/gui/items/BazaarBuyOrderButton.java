package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.craftinglib.util.gui.GuiItem;
import de.craftery.craftinglib.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.gui.window.BazaarBuyOrderGui;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BazaarBuyOrderButton extends GuiItem {
    @Override
    public void onClick(Player p) {
        GuiManager.navigate(BazaarBuyOrderGui.class, p, this.itemArgs.get(0));
    }

    @Override
    public void build() {
        this.setItemMaterial(Material.HOPPER);
        this.setDisplayName(Message.Gui.PLACE_BUY_ORDER);
    }
}
