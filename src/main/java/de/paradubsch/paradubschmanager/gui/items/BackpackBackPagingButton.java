package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.GuiItem;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.models.Backpack;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BackpackBackPagingButton extends GuiItem {

    @Override
    public void onClick(Player p) {
        // Disable this feature for now
        if (true)
            return;
        Integer page = (Integer) this.getKvStore(p).get("backpackPage");
        if (page == null) {
            page = 1;
            this.getKvStore(p).set("backpackPage", page);
        } else {
            if (page > 1)
                page--;
            this.getKvStore(p).set("backpackPage", page);
        }
        Backpack backpack = (Backpack) this.getKvStore(p).get("backpack");
        if (backpack == null)
            return;
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
        this.setItemHead("7789");
        this.setDisplayName(Message.Gui.BACK_PAGE);
        this.addLore("§8(" + (page - 1 > 0 ? page - 1 : "§c/") + "§8)");
    }

}
