package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.GuiItem;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.models.Backpack;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BackpackNextPagingButton extends GuiItem {

    @Override
    public void onClick(Player p) {
        Integer page = (Integer) this.getKvStore().get("backpackPage");
        if (page == null) {
            page = 1;
            this.getKvStore().set("backpackPage", page);
        } else {
            if ((int) this.itemArgs.get(0) > page)
                page++;
            this.getKvStore().set("backpackPage", page);
        }
        Backpack backpack = (Backpack) this.getKvStore().get("backpack");
        Inventory inv = p.getOpenInventory().getTopInventory();
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < inv.getSize(); i++) {
            if (i <= 3*9-1) {
                if (inv.getItem(i) != null) {
                    items.add(inv.getItem(i));
                }
            }
        }
        backpack.setItems(items);
        Backpack.storeByPlayer(p, backpack);
        GuiManager.rebuild(p);
    }

    @Override
    public void build() {
        Integer page = (Integer) this.getKvStore().get("backpackPage");
        if (page == null) {
            page = 1;
        }
        this.setItemHead("7786");
        this.setDisplayName(Message.Gui.NEXT_PAGE);
        this.addLore("§8(" + ((int) this.itemArgs.get(0) > page ? page + 1 : "§c/") + "§8)");
    }

}
