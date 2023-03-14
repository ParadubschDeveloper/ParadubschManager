package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.AbstractGuiItem;
import de.craftery.util.gui.PromptType;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.BazaarItemData;
import de.paradubsch.paradubschmanager.util.StringValidator;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BazaarAmountSelectorButton extends AbstractGuiItem {
    @Override
    public void onClick(Player p) {
        BazaarItemData data = (BazaarItemData) this.itemArgs.get(0);
        String key = "blank";
        if (data != null) key = data.getMaterial().toString();
        this.prompt(PromptType.INTEGER, key + "amount");
    }

    @Override
    public void build() {
        this.setItemMaterial(Material.PAPER);
        this.setDisplayName(Message.Gui.AMOUNT);

        BazaarItemData data = (BazaarItemData) this.itemArgs.get(0);
        String key = data.getMaterial().toString();
        int amount = data.getAmount();
        String prompt = this.getPrompt(key + "amount");

        Integer integer = StringValidator.isInteger(prompt);
        if (integer == null && !StringValidator.isEmpty(prompt)) {
            this.addLore(Message.Gui.INVALID_AMOUNT_LORE);
            this.addLore(Message.Gui.INVALID_AMOUNT_LORE_MULTIPLE, data.getAmount() + "");
        } else if (!StringValidator.isEmpty(prompt)) {
            if (!StringValidator.isMultipleOf(integer, data.getAmount()) && integer != null) {
                this.addLore(Message.Gui.INVALID_AMOUNT_LORE_MULTIPLE, data.getAmount() + "");
                amount = integer + data.getAmount() - (integer % data.getAmount());
            } else if (integer != null) {
                amount = integer;
            }
        }

        this.addLore(Message.Gui.AMOUNT_LORE, amount + "");
        this.getKvStore().set(key + "totalAmount", amount);
        this.getKvStore().set(key + "batchAmount", amount / data.getAmount());
    }
}
