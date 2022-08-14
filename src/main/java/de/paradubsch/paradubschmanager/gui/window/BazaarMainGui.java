package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.util.gui.BaseGui;
import de.paradubsch.paradubschmanager.gui.items.BazaarItemButtom;
import de.paradubsch.paradubschmanager.gui.items.CancelButton;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.Bazaar;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.BazaarItemData;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;

public class BazaarMainGui extends BaseGui {
    @Override
    public void init(Language lang) {
        instantiate(lang, Message.Gui.BAZAAR_MAIN_TITLE, 6);
    }

    @Override
    public void build() {
        for (BazaarItemData item : Bazaar.getBazaarConfigItems()) {
            int keyIndex;
            try {
                keyIndex = Integer.parseInt(item.getIndexKey());
            } catch (NumberFormatException e) {
                continue;
            }
            // TODO: Add paging
            if (keyIndex > 45) continue;

            int row = (int) Math.floor((double) keyIndex / 9);
            int col = keyIndex % 9;
            this.addAbstractItem(BazaarItemButtom.class, row, col, item.getIndexKey(), item);
        }

        this.addItem(CancelButton.class, 6, 9);
    }
}
