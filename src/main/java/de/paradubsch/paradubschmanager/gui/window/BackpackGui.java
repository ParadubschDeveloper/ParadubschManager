package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.util.gui.BaseGui;
import de.paradubsch.paradubschmanager.gui.items.*;
import de.paradubsch.paradubschmanager.models.Backpack;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BackpackGui extends BaseGui {

    @Override
    public void init (Language lang){
        instantiate(lang, Message.Gui.BUY_ORDER_TITLE, 3);
    }

    @Override
    public void build () {
        Integer page = (Integer) this.getKvStore().get("backpackPage");
        if (page == null) {
            page = 1;
            this.getKvStore().set("backpackPage", page);
        }

        Backpack backpack = Backpack.getByPlayer(this.player);
        List<ItemStack> items = backpack.getItems();
        this.getKvStore().set("backpack", backpack);

        int maxPage = (int) Math.ceil(items.size() / 45.0);

        int itemIndex = (int) Math.min(items.size(), Math.floor(page * 45.0D));
        int pageIndex = (int) Math.max(Math.min(Math.ceil(items.size() / 45), page), 1);
        for (int i = itemIndex; i < Math.min(items.size(), pageIndex * 45); i++) {
            int row = (int) Math.ceil(itemIndex / 9.0D);
            int col = (int) ((itemIndex+1) % 9);
            this.addAbstractItem(BackpackItemButton.class, row, col, itemIndex, items.get(itemIndex));
            itemIndex++;
        }

        this.addItem(CancelButton.class, 6, 9);

        if (page > 1) {
            this.addItem(BackpackBackPagingButton.class, 6, 4);
        }

        if (page < maxPage) {
            this.addItem(BackpackNextPagingButton.class, 6, 6);
        }

        this.addItem(CancelButton.class, 6, 9);
    }

}