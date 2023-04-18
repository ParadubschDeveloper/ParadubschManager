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

import java.util.Comparator;
import java.util.List;

public class BazaarPlaceBuyOrderButton extends AbstractGuiItem {
    @Override
    public void onClick(Player p) {
        BazaarItemData data = (BazaarItemData) this.itemArgs.get(0);
        String key = data.getMaterial().toString();

        Integer totalAmount = (Integer) this.getKvStore().get(key + "totalAmount");
        Integer batchAmount = (Integer) this.getKvStore().get(key + "batchAmount");
        Integer price = (Integer) this.getKvStore().get(key + "price");

        if (totalAmount == null) totalAmount = data.getAmount();
        if (batchAmount == null) batchAmount = 1;
        if (price == null) price = ((data.getOffer() + data.getBuy()) / 2);

        int taxes = (int) Math.floor(price * batchAmount * 0.1f);

        int finalPrice = price * batchAmount + taxes;

        PlayerData pd = PlayerData.getByPlayer(p);

        if (pd.getMoney() < finalPrice) {
            MessageAdapter.sendMessage(p, Message.Error.NOT_ENOUGH_MONEY);
            return;
        }
        pd.setMoney(pd.getMoney() - finalPrice);
        pd.saveOrUpdate();
        String blockName = StringValidator.toTitleCase(data.getMaterial().toString().replaceAll("_", " "));

        BazaarOrder order = BazaarOrder.getByHolderOrderTypeItemTypePrice(p.getUniqueId().toString(), OrderType.BUY, data.getMaterial(), price);
        order.setAmount(order.getAmount() + totalAmount);
        order.saveOrUpdate();

        MessageAdapter.sendMessage(p, Message.Info.BUY_ORDER_PLACED, totalAmount + "", blockName, (price * batchAmount) + "");

        List<BazaarOrder> activeSellOrders = BazaarOrder.getOrdersByMaterial(OrderType.SELL, data.getMaterial());
        activeSellOrders.sort(Comparator.comparingInt(BazaarOrder::getPrice));

        long leftToSell = order.getAmount();
        for (BazaarOrder sellOrder : activeSellOrders) {
            if (sellOrder.getPrice() <= order.getPrice()) {
                PlayerData targetPd = PlayerData.getById(sellOrder.getHolderUuid());

                if (sellOrder.getAmount() > leftToSell) {
                    targetPd.setMoney(targetPd.getMoney() + (leftToSell / data.getAmount()) * sellOrder.getPrice());
                    sellOrder.setAmount(sellOrder.getAmount() - leftToSell);
                    sellOrder.saveOrUpdate();
                    leftToSell = 0;
                    break;
                } else {
                    targetPd.setMoney(targetPd.getMoney() + (sellOrder.getAmount() / data.getAmount()) * sellOrder.getPrice());
                    leftToSell -= sellOrder.getAmount();
                    sellOrder.delete();
                }
            }
        }
        long directSold = order.getAmount() - leftToSell;
        if (leftToSell != order.getAmount()) {
            BazaarCollectable collectable = BazaarCollectable.getByHolderItemType(p.getUniqueId().toString(), data.getMaterial());
            collectable.setAmount(collectable.getAmount() + directSold);
            collectable.saveOrUpdate();
        }

        if (leftToSell == 0) {
            order.delete();
        } else {
            order.setAmount(leftToSell);
            order.saveOrUpdate();
        }

        GuiManager.back(p);
    }

    @Override
    public void build() {
        this.setItemMaterial(Material.LIME_STAINED_GLASS_PANE);
        this.setDisplayName(Message.Gui.BUY_ORDER_TITLE);

        BazaarItemData data = (BazaarItemData) this.itemArgs.get(0);
        String key = data.getMaterial().toString();

        Integer totalAmount = (Integer) this.getKvStore().get(key + "totalAmount");
        Integer batchAmount = (Integer) this.getKvStore().get(key + "batchAmount");
        Integer price = (Integer) this.getKvStore().get(key + "price");

        if (totalAmount == null) totalAmount = data.getAmount();
        if (batchAmount == null) batchAmount = 1;
        if (price == null) price = ((data.getOffer() + data.getBuy()) / 2);

        int taxes = (int) Math.floor(price * batchAmount * 0.1f);

        int finalPrice = price * batchAmount + taxes;

        this.addLore(Message.Constant.YOU_BUY);
        this.addLore(Message.Gui.AMOUNT_LORE, totalAmount + "");
        this.addLore(Message.Gui.PRICE_LORE_WITH_DESCRIPTION, price * batchAmount + "", batchAmount + "", price + "");
        this.addLore(Message.Gui.TAXES_LORE, taxes + "");
        this.addLore("");
        this.addLore(Message.Gui.FINAL_PRICE_LORE, finalPrice + "");

        PlayerData pd = PlayerData.getByPlayer(this.getPlayer());

        if (pd.getMoney() < finalPrice) {
            this.addLore(Message.Gui.NOT_ENOUGH_MONEY);
        }
    }
}
