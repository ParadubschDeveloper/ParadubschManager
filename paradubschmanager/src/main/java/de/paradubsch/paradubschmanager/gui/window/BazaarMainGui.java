package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.craftinglib.util.gui.BaseGui;
import de.paradubsch.paradubschmanager.gui.items.*;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.Bazaar;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.BazaarItemData;
import de.craftery.craftinglib.messaging.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;

import java.util.List;

public class BazaarMainGui extends BaseGui {
    @Override
    public void init(Language lang) {
        instantiate(lang, Message.Gui.BAZAAR_MAIN_TITLE, 6);
    }

    @Override
    public void build() {
        Integer page = (Integer) this.getKvStore().get("bazaarPage");
        if (page == null) {
            page = 1;
            this.getKvStore().set("bazaarPage", page);
        }

        List<BazaarItemData> items = Bazaar.getBazaarConfigItems();

        int maxPage = (int) Math.ceil(items.size() / 45.0);

        for (BazaarItemData item : items) {
            int keyIndex;
            try {
                keyIndex = Integer.parseInt(item.getIndexKey());
            } catch (NumberFormatException e) {
                continue;
            }

            keyIndex = keyIndex - ((page - 1) * 45);

            if (keyIndex > 45) continue;
            if (keyIndex < 1) continue;

            int row = (int) Math.ceil(keyIndex / 9f);
            int col = keyIndex % 9;
            if (col == 0) col = 9;
            this.addAbstractItem(BazaarItemButton.class, row, col, item.getIndexKey(), item);
        }

        this.addItem(BazaarCollectButton.class, 6, 1);

        if (page > 1) {
            this.addItem(BazaarBackPagingButton.class, 6, 4);
        }

        if (page < maxPage) {
            this.addItem(BazaarNextPagingButton.class, 6, 6);
        }

        this.addItem(CancelButton.class, 6, 9);
    }
}
