package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.AbstractGuiItem;
import de.craftery.util.gui.GuiManager;
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

public class BazaarInstantBuyButton extends AbstractGuiItem {
    @Override
    public void onClick(Player p) {
        BazaarItemData data = (BazaarItemData) this.itemArgs.get(0);
        Map<Integer, Long> prices = getOrderedPriceMap(data.getMaterial());

        Integer firstPrice = null;
        try {
            firstPrice = prices.keySet().iterator().next();
        } catch (NoSuchElementException ignored) {}
        Integer displayedHighestPrice = (Integer) this.getKvStore().get(data.getMaterial().toString() + "lowestPrice");
        if (!Objects.equals(firstPrice, displayedHighestPrice)) {
            this.getKvStore().set(data.getMaterial().toString() + "alreadyTaken", 1);
            GuiManager.rebuild(p);
            return;
        }

        List<BazaarOrder> orders = BazaarOrder.getOrdersByMaterial(OrderType.SELL, data.getMaterial());
        orders.sort(Comparator.comparingInt(BazaarOrder::getPrice));

        BazaarOrder order = null;
        try {
            order = orders.get(0);
        } catch (IndexOutOfBoundsException ignored) {}

        int buyPrice = data.getOffer();
        PlayerData pd = PlayerData.getByPlayer(p);
        if (order != null) {
            buyPrice = order.getPrice();
        }
        if (pd.getMoney() < buyPrice) {
            this.getKvStore().set(data.getMaterial().toString() + "notEnoughMoney", 1);
            GuiManager.rebuild(p);
            return;
        }

        boolean received = false;
        for (int i = 0; i < 36; i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item == null) {
                p.getInventory().setItem(i, new ItemStack(data.getMaterial(), data.getAmount()));
                received = true;
                break;
            }
            if (item.getType().equals(Material.AIR)) {
                p.getInventory().setItem(i, new ItemStack(data.getMaterial(), data.getAmount()));
                received = true;
                break;
            }
            if (item.isSimilar(new ItemStack(data.getMaterial())) && item.getAmount() + data.getAmount() <= item.getMaxStackSize()) {
                item.setAmount(item.getAmount() + data.getAmount());
                received = true;
                break;
            }

        }
        if (!received) {
            MessageAdapter.sendMessage(p, Message.Gui.NOT_ENOUGH_INVENTORY_SPACE);
            this.getKvStore().set(data.getMaterial().toString() + "notEnoughInventorySpace", 1);
            GuiManager.rebuild(p);
            return;
        }

        if (order != null) {
            if (order.getAmount() == data.getAmount()) {
                order.delete();
            } else {
                order.setAmount(order.getAmount() - data.getAmount());
                order.saveOrUpdate();
            }
            BazaarCollectable collectable = BazaarCollectable.getByHolderItemType(order.getHolderUuid(), data.getMaterial());
            collectable.setAmount(collectable.getAmount() + data.getAmount());
            collectable.saveOrUpdate();
            pd.setMoney(pd.getMoney() + buyPrice);
            pd.saveOrUpdate();
        }

        pd.setMoney(pd.getMoney() - buyPrice);
        pd.saveOrUpdate();
        MessageAdapter.sendMessage(p, Message.Info.BOUGHT_ITEM_TRANSLATED, data.getAmount() + "", data.getMaterial().name(), buyPrice + "");

        GuiManager.rebuild(p);
    }

    @Override
    public void build() {
        this.setItemMaterial(Material.BUCKET);
        this.setDisplayName(Message.Gui.BUY_INSTANT_TITLE);
        BazaarItemData data = (BazaarItemData) this.itemArgs.get(0);
        this.addLore(Message.Gui.INCOME_PER_UNIT_LORE_TRANSLATED, data.getAmount() + "", data.getMaterial().name());

        Map<Integer, Long> prices = getOrderedPriceMap(data.getMaterial());

        try {
            Integer firstPrice = prices.keySet().iterator().next();
            this.getKvStore().set(data.getMaterial().toString() + "lowestPrice", firstPrice);
        } catch (NoSuchElementException ignored) {
            this.getKvStore().set(data.getMaterial().toString() + "lowestPrice", null);
        }

        for (Map.Entry<Integer, Long> entry : prices.entrySet()) {
            this.addLore(Message.Gui.SELL_INSTANT_LORE,entry.getValue() + "", entry.getKey() + "");
        }
        this.addLore("");
        this.addLore(Message.Gui.SERVER_PRICE_LORE, data.getOffer() + "");
        Integer soldOut = (Integer) this.getKvStore().get(data.getMaterial().toString() + "alreadyTaken");
        if (Objects.equals(soldOut, 1)) {
            this.addLore(Message.Gui.AUCTION_JUST_TAKEN);
            this.getKvStore().set(data.getMaterial().toString() + "alreadyTaken", 0);
        }
        Integer notEnoughInvSpace = (Integer) this.getKvStore().get(data.getMaterial().toString() + "notEnoughInventorySpace");
        if (Objects.equals(notEnoughInvSpace, 1)) {
            this.addLore(Message.Gui.NOT_ENOUGH_INVENTORY_SPACE);
            this.getKvStore().set(data.getMaterial().toString() + "notEnoughInventorySpace", 0);
        }
        Integer notEnoughMoney = (Integer) this.getKvStore().get(data.getMaterial().toString() + "notEnoughMoney");
        if (Objects.equals(notEnoughMoney, 1)) {
            this.addLore(Message.Gui.NOT_ENOUGH_MONEY);
            this.getKvStore().set(data.getMaterial().toString() + "notEnoughMoney", 0);
        }

    }

    private static Map<Integer, Long> getOrderedPriceMap(Material mat) {
        List<BazaarOrder> orders = BazaarOrder.getOrdersByMaterial(OrderType.SELL, mat);

        orders.sort(Comparator.comparingInt(BazaarOrder::getPrice));

        Map<Integer, Long> prices = new TreeMap<>();
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
