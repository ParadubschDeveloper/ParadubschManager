package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.craftinglib.util.gui.AbstractGuiItem;
import de.craftery.craftinglib.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.BazaarItemData;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.OrderType;
import de.paradubsch.paradubschmanager.models.BazaarCollectable;
import de.paradubsch.paradubschmanager.models.BazaarOrder;
import de.craftery.craftinglib.PlayerData;
import de.craftery.craftinglib.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.StringValidator;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BazaarPlaceSellOrderButton extends AbstractGuiItem {
    @Override
    public void onClick(Player p) {
        BazaarItemData data = (BazaarItemData) this.itemArgs.get(0);
        String key = data.getMaterial().toString();

        Integer price = (Integer) this.getKvStore().get(key + "SellPrice");
        if (price == null) price = ((data.getOffer() + data.getBuy()) / 2);
        int taxes = (int) Math.floor(price * 0.1f);

        PlayerData pd = PlayerData.getByPlayer(p);
        if (pd.getMoney() < taxes) {
            MessageAdapter.sendMessage(p, Message.Error.NOT_ENOUGH_MONEY);
            return;
        }
        pd.setMoney(pd.getMoney() - taxes);
        pd.saveOrUpdate();

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
        if (!soldItem) {
            MessageAdapter.sendMessage(p, Message.Gui.NOT_ENOUGH_ITEMS_TRANSLATED, data.getMaterial().name());
            GuiManager.back(p);
            return;
        }


        BazaarPlaceSellOrderButton.sellItem(p, pd, data, price);
        GuiManager.rebuild(p);
    }

    public static void sellItem(Player p, PlayerData pd, BazaarItemData item, int price) {
        String blockName = StringValidator.toTitleCase(item.getMaterial().toString().replaceAll("_", " "));

        BazaarOrder order = BazaarOrder.getByHolderOrderTypeItemTypePrice(p.getUniqueId().toString(), OrderType.SELL, item.getMaterial(), price);
        order.setAmount(order.getAmount() + item.getAmount());
        order.saveOrUpdate();

        MessageAdapter.sendMessage(p, Message.Info.SELL_ORDER_PLACED, item.getAmount() + "", blockName, price + "");

        List<BazaarOrder> activeBuyOrders = BazaarOrder.getOrdersByMaterial(OrderType.BUY, item.getMaterial());
        activeBuyOrders.sort(Comparator.comparingInt(BazaarOrder::getPrice));
        Collections.reverse(activeBuyOrders);

        int leftToSell = item.getAmount();
        for (BazaarOrder buyOrder : activeBuyOrders) {
            if (buyOrder.getPrice() >= order.getPrice()) {
                BazaarCollectable collectable = BazaarCollectable.getByHolderItemType(buyOrder.getHolderUuid(), item.getMaterial());
                collectable.setAmount(collectable.getAmount() + item.getAmount());
                collectable.saveOrUpdate();

                pd.setMoney(pd.getMoney() + buyOrder.getPrice());
                pd.saveOrUpdate();

                order.delete();

                if (buyOrder.getAmount() > leftToSell) {
                    buyOrder.setAmount(buyOrder.getAmount() - item.getAmount());
                    buyOrder.saveOrUpdate();
                    break;
                } else {
                    buyOrder.delete();
                }
            }
        }
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

        PlayerData pd = PlayerData.getByPlayer(this.getPlayer());

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
            this.addLore(Message.Gui.NOT_ENOUGH_ITEMS_TRANSLATED, data.getMaterial().name());
        }
    }
}
