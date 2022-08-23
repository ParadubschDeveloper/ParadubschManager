package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.AbstractGuiItem;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.Bazaar;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.BazaarItemData;
import de.paradubsch.paradubschmanager.lifecycle.bazaar.OrderType;
import de.paradubsch.paradubschmanager.models.BazaarCollectable;
import de.paradubsch.paradubschmanager.models.BazaarOrder;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BazaarInstantSellButton extends AbstractGuiItem {
    @Override
    public void onClick(Player p) {
        BazaarItemData data = (BazaarItemData) this.itemArgs.get(0);
        Map<Integer, Long> prices = getOrderedPriceMap(data.getMaterial());

        Integer firstPrice = null;
        try {
            firstPrice = prices.keySet().iterator().next();
        } catch (NoSuchElementException ignored) {}
        Integer displayedHighestPrice = (Integer) this.getKvStore().get(data.getMaterial().toString() + "highestPrice");
        if (!Objects.equals(firstPrice, displayedHighestPrice)) {
            this.getKvStore().set(data.getMaterial().toString() + "soldOut", 1);
            GuiManager.rebuild(p);
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
            this.getKvStore().set(data.getMaterial().toString() + "notEnoughItems", 1);
            GuiManager.rebuild(p);
            return;
        }
        List<BazaarOrder> orders = BazaarOrder.getOrdersByMaterial(OrderType.BUY, data.getMaterial());
        orders.sort(Comparator.comparingInt(BazaarOrder::getPrice));
        Collections.reverse(orders);
        BazaarOrder order = null;
        try {
            order = orders.get(0);
        } catch (IndexOutOfBoundsException ignored) {}

        int sellPrice = data.getBuy();
        if (order != null) {
            sellPrice = order.getPrice();
            if (order.getAmount() == data.getAmount()) {
                order.delete();
            } else {
                order.setAmount(order.getAmount() - data.getAmount());
                order.saveOrUpdate();
            }
            BazaarCollectable collectable = BazaarCollectable.getByHolderItemType(order.getHolderUuid(), data.getMaterial());
            collectable.setAmount(collectable.getAmount() + data.getAmount());
            collectable.saveOrUpdate();
        }
        PlayerData pd = PlayerData.getById(p.getUniqueId().toString());
        pd.setMoney(pd.getMoney() + sellPrice);
        pd.saveOrUpdate();
        MessageAdapter.sendMessage(p, Message.Info.SOLD_ITEM, data.getAmount() + "", translatedItemName, sellPrice + "");

        GuiManager.rebuild(p);
    }

    @Override
    public void build() {
        this.setItemMaterial(Material.PAPER);
        this.setDisplayName(Message.Gui.SELL_INSTANT_TITLE);
        BazaarItemData data = (BazaarItemData) this.itemArgs.get(0);

        String translatedItemName = ParadubschManager.getInstance().getLanguageManager().getString(Bazaar.translationForMaterial(data.getMaterial()), MessageAdapter.getSenderLang(this.getPlayer()));

        this.addLore(Message.Gui.PRICE_PER_UNIT_LORE, data.getAmount() + "", translatedItemName);

        Map<Integer, Long> prices = getOrderedPriceMap(data.getMaterial());

        try {
            Integer firstPrice = prices.keySet().iterator().next();
            this.getKvStore().set(data.getMaterial().toString() + "highestPrice", firstPrice);
        } catch (NoSuchElementException ignored) {
            this.getKvStore().set(data.getMaterial().toString() + "highestPrice", null);
        }

        for (Map.Entry<Integer, Long> entry : prices.entrySet()) {
            this.addLore(Message.Gui.SELL_INSTANT_LORE,entry.getValue() + "", entry.getKey() + "");
        }
        this.addLore("");
        this.addLore(Message.Gui.SERVER_PRICE_LORE, data.getBuy() + "");
        Integer soldOut = (Integer) this.getKvStore().get(data.getMaterial().toString() + "soldOut");
        if (Objects.equals(soldOut, 1)) {
            this.addLore(Message.Gui.AUCTION_NO_LONGER_AVAILABLE);
            this.getKvStore().set(data.getMaterial().toString() + "soldOut", 0);
        }
        Integer notEnoughItems = (Integer) this.getKvStore().get(data.getMaterial().toString() + "notEnoughItems");
        if (Objects.equals(notEnoughItems, 1)) {
            this.addLore(Message.Gui.NOT_ENOUGH_ITEMS, translatedItemName);
            this.getKvStore().set(data.getMaterial().toString() + "notEnoughItems", 0);
        }

    }

    private static Map<Integer, Long> getOrderedPriceMap(Material mat) {
        List<BazaarOrder> orders = BazaarOrder.getOrdersByMaterial(OrderType.BUY, mat);

        orders.sort(Comparator.comparingInt(BazaarOrder::getPrice));

        Map<Integer, Long> prices = new TreeMap<>(Collections.reverseOrder());
        orders.forEach(order -> {
            if (prices.get(order.getPrice()) == null) {
                prices.put(order.getPrice(), order.getAmount());
            } else {
                Long amount = prices.get(order.getPrice());
                amount += order.getAmount();
                prices.put(order.getPrice(), amount);
            }
        });
        return prices;
    }
}
