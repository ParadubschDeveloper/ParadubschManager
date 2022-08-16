package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.AbstractGuiItem;
import de.craftery.util.gui.PromptType;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.BazaarItemData;
import de.paradubsch.paradubschmanager.util.StringValidator;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.entity.Player;

public class BazaarPriceSelectorButton extends AbstractGuiItem {
    @Override
    public void onClick(Player p) {
        BazaarItemData data = (BazaarItemData) this.itemArgs.get(0);
        String key = "blank";
        if (data != null) key = data.getMaterial().toString();
        this.prompt(PromptType.INTEGER, key + "price");
    }

    @Override
    public void build() {
        this.setItemHead("3005");

        BazaarItemData data = (BazaarItemData) this.itemArgs.get(0);
        this.setDisplayName(Message.Gui.PRICE, data.getAmount() + "");
        String key = data.getMaterial().toString();
        Integer price = ((data.getOffer() + data.getBuy()) / 2);
        String prompt = this.getPrompt(key + "price");

        Integer integer = StringValidator.isInteger(prompt);
        if (integer == null && !StringValidator.isEmpty(prompt)) {
            this.addLore(Message.Gui.INVALID_PRICE_LORE);
        } else if (!StringValidator.isEmpty(prompt) && integer != null) {
            if (integer >= data.getOffer()) {
                this.addLore(Message.Gui.INVALID_PRICE_LORE);
                this.addLore(Message.Gui.PRICE_TO_HIGH, data.getOffer() + "");
            } else if (integer < data.getBuy()) {
                this.addLore(Message.Gui.INVALID_PRICE_LORE);
                this.addLore(Message.Gui.PRICE_TO_LOW_1);
                this.addLore(Message.Gui.PRICE_TO_LOW_2, data.getBuy() + "");
            } else {
                price = integer;
            }
        }

        this.addLore(Message.Gui.PRICE_LORE, price + "");
        this.getKvStore().set(key + "price", price);
    }
}
