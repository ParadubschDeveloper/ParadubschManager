package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.AbstractGuiItem;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.BazaarItemData;
import org.bukkit.entity.Player;

public class BazaarItemButtom extends AbstractGuiItem {
    @Override
    public void onClick(Player p) {

    }

    @Override
    public void build() {
        BazaarItemData item = (BazaarItemData) this.itemArgs.get(0);
        this.setItemMaterial(item.getMaterial());
    }
}
