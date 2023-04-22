package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.craftinglib.util.gui.BaseGui;
import de.paradubsch.paradubschmanager.gui.items.*;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.BazaarItemData;
import de.craftery.craftinglib.messaging.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;

public class BazaarSellOrderGui extends BaseGui {
    @Override
    public void init(Language lang) {
        instantiate(lang, Message.Gui.SELL_ORDER_TITLE, 3);
    }

    @Override
    public void build() {
        BazaarItemData data = (BazaarItemData) this.args.get(0);
        this.addAbstractItem(BazaarSellPriceSelectorButton.class, 2, 3, data.getMaterial() + "priceBtn", data);
        this.addAbstractItem(BazaarPlaceSellOrderButton.class, 2, 8, data.getMaterial() + "sellBtn", data);

        this.addItem(BackButton.class, 3, 8);
        this.addItem(CancelButton.class, 3, 9);
    }
}
