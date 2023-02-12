package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.util.gui.BaseGui;
import de.paradubsch.paradubschmanager.gui.items.*;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.BazaarItemData;
import de.craftery.util.lang.Language;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

public class BazaarItemGui extends BaseGui {
    @Override
    public void init(Language lang) {
        BazaarItemData data = (BazaarItemData) this.args.get(0);
        Component title = Component.translatable(new ItemStack(data.getMaterial()).translationKey());
        instantiate(lang, title, 3);
    }

    @Override
    public void build() {
        BazaarItemData data = (BazaarItemData) this.args.get(0);

        this.addAbstractItem(BazaarInstantBuyButton.class, 2, 2, data.getMaterial(), data);

        this.addItem(BazaarBuyOrderButton.class, 2, 4, data);

        this.addItem(BazaarSellOrderButton.class, 2, 6, data);

        this.addAbstractItem(BazaarInstantSellButton.class, 2, 8, data.getMaterial(), data);


        this.addItem(BackButton.class, 3, 8);
        this.addItem(CancelButton.class, 3, 9);
    }
}
