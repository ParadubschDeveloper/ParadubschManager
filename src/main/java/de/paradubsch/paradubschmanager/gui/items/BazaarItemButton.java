package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.AbstractGuiItem;
import de.craftery.util.gui.GuiManager;
import de.craftery.util.gui.PromptType;
import de.paradubsch.paradubschmanager.gui.window.BazaarItemGui;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.Bazaar;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.BazaarItemData;
import de.paradubsch.paradubschmanager.util.lang.BaseMessageType;
import org.bukkit.entity.Player;

public class BazaarItemButton extends AbstractGuiItem {
    @Override
    public void onClick(Player p) {
        GuiManager.navigate(BazaarItemGui.class, p, this.itemArgs.get(0));
    }

    @Override
    public void build() {
        BazaarItemData item = (BazaarItemData) this.itemArgs.get(0);
        this.setItemMaterial(item.getMaterial());
        BaseMessageType name = Bazaar.translationForMaterial(item.getMaterial());
        this.setDisplayName(name);
    }
}
