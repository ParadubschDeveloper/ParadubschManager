package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.craftinglib.util.gui.BaseGui;
import de.craftery.craftinglib.messaging.lang.Language;
import de.paradubsch.paradubschmanager.gui.items.KitRedeemButton;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class KitGui extends BaseGui {
    @Override
    public void init(Language lang) {
        instantiate(lang, Message.Gui.KIT_GUI_TITLE, 3);
    }

    @Override
    public void build() {
        for (int i = 0; i < 27; i++) {
            this.inv.setItem(i, new ItemStack(Material.GLASS_PANE));
        }

        this.addAbstractItem(KitRedeemButton.class, 2, 1, 1, 1);
        this.addAbstractItem(KitRedeemButton.class, 2, 3, 2, 2);
        this.addAbstractItem(KitRedeemButton.class, 2, 5, 3, 3);
        this.addAbstractItem(KitRedeemButton.class, 2, 7, 4, 4);
        this.addAbstractItem(KitRedeemButton.class, 2, 9, 5, 5);
    }
}
