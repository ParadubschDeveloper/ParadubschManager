package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.util.gui.BaseGui;
import de.paradubsch.paradubschmanager.gui.items.BackButton;
import de.paradubsch.paradubschmanager.gui.items.BazaarBuyOrderButton;
import de.paradubsch.paradubschmanager.gui.items.BazaarInstantSellButton;
import de.paradubsch.paradubschmanager.gui.items.CancelButton;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.Bazaar;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.BazaarItemData;
import de.paradubsch.paradubschmanager.util.lang.BaseMessageType;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;

public class BazaarItemGui extends BaseGui {
    @Override
    public void init(Language lang) {
        BazaarItemData data = (BazaarItemData) this.args.get(0);
        BaseMessageType title = Message.Constant.BLANK;
        if (data != null) {
            title = Bazaar.translationForMaterial(data.getMaterial());
        }
        instantiate(lang, title, 3);
    }

    @Override
    public void build() {
        BazaarItemData data = (BazaarItemData) this.args.get(0);

        this.addItem(BazaarBuyOrderButton.class, 2, 4, data);

        this.addAbstractItem(BazaarInstantSellButton.class, 2, 8, data.getMaterial(), data);


        this.addItem(BackButton.class, 3, 8);
        this.addItem(CancelButton.class, 3, 9);
    }
}
