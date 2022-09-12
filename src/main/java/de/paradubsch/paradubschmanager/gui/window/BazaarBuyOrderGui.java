package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.util.gui.BaseGui;
import de.paradubsch.paradubschmanager.gui.items.*;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.BazaarItemData;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;

public class BazaarBuyOrderGui extends BaseGui {
    @Override
    public void init(Language lang) {
        instantiate(lang, Message.Gui.BUY_ORDER_TITLE, 3);
    }

    @Override
    public void build() {
        BazaarItemData data = (BazaarItemData) this.args.get(0);
        this.addAbstractItem(BazaarAmountSelectorButton.class, 2, 2, data.getMaterial() + "btn", data);
        this.addAbstractItem(BazaarBuyPriceSelectorButton.class, 2, 4, data.getMaterial() + "btn2", data);
        this.addAbstractItem(BazaarPlaceBuyOrderButton.class, 2, 8, data.getMaterial() + "buyBtn", data);

        this.addItem(BackButton.class, 3, 8);
        this.addItem(CancelButton.class, 3, 9);
    }
}
