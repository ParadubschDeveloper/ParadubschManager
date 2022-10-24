package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.AbstractGuiItem;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.BazaarItemData;
import de.paradubsch.paradubschmanager.models.Backpack;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BackpackItemButton extends AbstractGuiItem {

    @Override
    public void onClick(Player p) {}

    @Override
    public void onClick(Player p, InventoryClickEvent event) {
        boolean shift = event.isShiftClick();
        Backpack backpack = (Backpack) getKvStore().get("backpack");
        if (event.isLeftClick()) {
            ItemStack itemStack = backpack.getItems().get(Integer.parseInt(getIdentifier()));
            if (shift) {
                if (p.getInventory().getSize() > p.getInventory().getContents().length) {
                    p.getInventory().addItem(itemStack);
                    backpack.getItems().remove(Integer.parseInt(getIdentifier()));
                    Backpack.storeByPlayer(p, backpack);
                    GuiManager.rebuild(p);
                }
            } else {
                p.setItemOnCursor(itemStack);
                backpack.getItems().remove(Integer.parseInt(getIdentifier()));
                Backpack.storeByPlayer(p, backpack);
                GuiManager.rebuild(p);
            }
        }
    }

    @Override
    public void build() {
        BazaarItemData item = (BazaarItemData) this.itemArgs.get(0);
        this.setItemMaterial(item.getMaterial());
        this.setDisplayName(Message.Constant.BLANK_TRANSLATABLE, item.getMaterial().name());
    }

}