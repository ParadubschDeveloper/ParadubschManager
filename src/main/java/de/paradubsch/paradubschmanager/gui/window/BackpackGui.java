package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.util.gui.BaseGui;
import de.craftery.util.gui.GuiItem;
import de.craftery.util.gui.GuiManager;
import de.craftery.util.gui.KVStore;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.gui.items.*;
import de.paradubsch.paradubschmanager.models.Backpack;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BackpackGui extends BaseGui {

    private final int maxSlot = 3 * 9 - 1;

    @Override
    public void init(Language lang) {
        instantiate(lang, Message.Gui.BACKPACK_TITLE, 4);
    }

    @Override
    public void build() {
        Integer page = (Integer) this.getKvStore().get("backpackPage");
        if (page == null) {
            page = 1;
            this.getKvStore().set("backpackPage", page);
        }

        Backpack backpack = Backpack.getByPlayer(this.player);
        List<ItemStack> items = backpack.getItems();
        this.getKvStore().set("backpack", backpack);

        int columns = 3 * 9;
        int maxPage = (int) Math.ceil(items.size() / columns);
        /*
        int itemIndex = (int) Math.min(items.size(), Math.floor(page * columns));
        int pageIndex = (int) Math.max(Math.min(Math.ceil(items.size() / columns), page), 1);
        for (int i = itemIndex; i < Math.min(items.size(), pageIndex * columns); i++) {
            int row = (int) Math.ceil(itemIndex / 9.0D);
            int col = (int) ((itemIndex + 1) % 9);
            this.addAbstractItem(BackpackItemButton.class, row, col, itemIndex, items.get(itemIndex));
            itemIndex++;
        }*/

        for (ItemStack itemStack : items) {
            inv.addItem(itemStack);
        }


        for (int i = 1; i < 10; i++) {
            if (i != 4 && i != 6 && i != 9) {
                this.addItem(PlaceholderButton.class, 4, i, Material.GRAY_STAINED_GLASS_PANE);
            }
        }
        this.addItem(CancelButton.class, 4, 9);

        this.addItem(BackpackBackPagingButton.class, 4, 4);
        this.addItem(BackpackNextPagingButton.class, 4, 6, maxPage);

        this.addItem(CancelButton.class, 4, 9);
    }

    /**
     * @param player      Player
     * @param event       InventoryClickEvent
     * @param handledItem GuiItem
     *                    <br>
     *                    1. ablage in sperr region <br>
     *                    (2.) Shift click im unteren inv wenn inventar voll
     */
    @Override
    public void onClick(Player player, InventoryClickEvent event, GuiItem handledItem) {
        if (handledItem == null) {
            event.setCancelled(false);
            /*
            if (event.getClickedInventory() != null &&
                    event.getClickedInventory().equals(event.getView().getTopInventory()) &&
                    event.getSlot() > maxSlot) {
                event.setCancelled(true);
            } else if (event.getClickedInventory() != null && (event.getCurrentItem() != null || event.getCursor() != null)) {
                Inventory inv = event.getView().getTopInventory();
                if ((event.getClickedInventory().equals(event.getView().getBottomInventory()) &&
                        event.isShiftClick()) || event.getClickedInventory().equals(event.getView().getTopInventory()))
                    Bukkit.getScheduler().runTaskLater(ParadubschManager.getInstance(), () -> {
                        Backpack backpack = (Backpack) this.getKvStore(player).get("backpack");
                        List<ItemStack> items = new ArrayList<>();
                        for (int i = 0; i < inv.getSize(); i++) {
                            if (i <= maxSlot) {
                                if (inv.getItem(i) != null) {
                                    items.add(inv.getItem(i));
                                }
                            }
                        }
                        backpack.setItems(items);
                        Backpack.storeByPlayer(player, backpack);
                        GuiManager.rebuild(player);
                    }, 1L);
            }
            */
        }
    }

    @Override
    public void onClose(Player player, InventoryCloseEvent event) {
        Backpack backpack = (Backpack) this.getKvStore().get("backpack");
        Inventory inv = player.getOpenInventory().getTopInventory();
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < inv.getSize(); i++) {
            if (i <= 3*9-1) {
                if (inv.getItem(i) != null) {
                    items.add(inv.getItem(i));
                }
            }
        }
        backpack.setItems(items);
        Backpack.storeByPlayer(player, backpack);
    }
}