package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.AbstractGuiItem;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.Bazaar;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.BazaarItemData;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.OrderType;
import de.paradubsch.paradubschmanager.models.BazaarOrder;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.StringValidator;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BazaarPlaceSellOrderButton extends AbstractGuiItem {
    @Override
    public void onClick(Player p) {
        BazaarItemData data = (BazaarItemData) this.itemArgs.get(0);
        String key = data.getMaterial().toString();

        Integer price = (Integer) this.getKvStore().get(key + "SellPrice");

        if (price == null) price = ((data.getOffer() + data.getBuy()) / 2);

        int taxes = (int) Math.floor(price * 0.1f);

        PlayerData pd = PlayerData.getById(p.getUniqueId().toString());

        if (pd.getMoney() < taxes) {
            MessageAdapter.sendMessage(p, Message.Error.NOT_ENOUGH_MONEY);
            return;
        }

        boolean soldItem = false;
        for (int i = 0; i < 36; i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item != null &&
                    item.getType().equals(data.getMaterial()) &&
                    item.getAmount() >= data.getAmount()) {
                if (item.getAmount() == data.getAmount()) {
                    p.getInventory().setItem(i, null);
                } else {
                    item.setAmount(item.getAmount() - data.getAmount());
                    p.getInventory().setItem(i, item);
                }
                soldItem = true;
                break;
            }
        }
        String translatedItemName = ParadubschManager.getInstance().getLanguageManager().getString(Bazaar.translationForMaterial(data.getMaterial()), MessageAdapter.getSenderLang(p));
        if (!soldItem) {
            MessageAdapter.sendMessage(p, Message.Gui.NOT_ENOUGH_ITEMS, translatedItemName);
            GuiManager.back(p);
            return;
        }

        pd.setMoney(pd.getMoney() - taxes);
        pd.saveOrUpdate();
        String blockName = StringValidator.toTitleCase(data.getMaterial().toString().replaceAll("_", " "));

        BazaarOrder order = BazaarOrder.getByHolderOrderTypeItemTypePrice(p.getUniqueId().toString(), OrderType.SELL, data.getMaterial(), price);
        order.setAmount(order.getAmount() + data.getAmount());
        order.saveOrUpdate();

        MessageAdapter.sendMessage(p, Message.Info.SELL_ORDER_PLACED, data.getAmount() + "", blockName, price + "");

        //TODO: check if sell orders can be resolved already

        GuiManager.rebuild(p);
    }

    @Override
    public void build() {
        this.setItemMaterial(Material.LIME_STAINED_GLASS_PANE);
        this.setDisplayName(Message.Gui.SELL_ORDER_TITLE);

        BazaarItemData data = (BazaarItemData) this.itemArgs.get(0);
        String key = data.getMaterial().toString();

        Integer price = (Integer) this.getKvStore().get(key + "SellPrice");

        if (price == null) price = ((data.getOffer() + data.getBuy()) / 2);

        int taxes = (int) Math.floor(price * 0.1f);

        this.addLore(Message.Constant.YOU_SELL);
        this.addLore(Message.Gui.SELL_INSTANT_LORE, data.getAmount() + "", price + "");
        this.addLore("");
        this.addLore(Message.Gui.SELL_ORDER_TAXES, taxes + "");

        PlayerData pd = PlayerData.getById(this.getPlayer().getUniqueId().toString());

        if (pd.getMoney() < taxes) {
            this.addLore(Message.Gui.NOT_ENOUGH_MONEY);
        }

        boolean hasEnoughItems = false;
        for (int i = 0; i < 36; i++) {
            ItemStack item = this.getPlayer().getInventory().getItem(i);
            if (item != null && item.getType() == data.getMaterial() && item.getAmount() >= data.getAmount()) {
                hasEnoughItems = true;
                break;
            }
        }
        if (!hasEnoughItems) {
            String translatedItemName = ParadubschManager.getInstance().getLanguageManager().getString(Bazaar.translationForMaterial(data.getMaterial()), MessageAdapter.getSenderLang(this.getPlayer()));
            this.addLore(Message.Gui.NOT_ENOUGH_ITEMS, translatedItemName);
        }
    }
}
