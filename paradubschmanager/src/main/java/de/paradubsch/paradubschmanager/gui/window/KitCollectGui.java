package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.util.gui.BaseGui;
import de.craftery.util.gui.GuiItem;
import de.craftery.util.lang.Language;
import de.paradubsch.paradubschmanager.models.KitCollectable;
import de.paradubsch.paradubschmanager.models.KitRedeemEntry;
import de.paradubsch.paradubschmanager.models.logging.KitRedeemLog;
import de.craftery.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class KitCollectGui extends BaseGui {
    @Override
    public void init(Language lang) {
        instantiate(lang, Message.Gui.KIT_COLLECT_GUI_TITLE, 3);
    }

    @Override
    public void build() {
        Integer kitId = (Integer) this.args.get(0);
        long lastRedeemed = KitRedeemEntry.checkLastRedeemed(this.player, kitId);
        long diff = System.currentTimeMillis() - lastRedeemed;

        long minDiff = 0;
        switch (kitId) {
            case 1: {
                // 3 days
                minDiff = 1000L * 60 * 60 * 24 * 3;
                break;
            }
            case 2: {
                // 7 days
                minDiff = 1000L * 60 * 60 * 24 * 7;
                break;
            }
            case 3: {
                // 14 days
                minDiff = 1000L * 60 * 60 * 24 * 14;
                break;
            }
            case 4: {
                // 3 days
                minDiff = 1000 * 60 * 60 * 24 * 3;
                break;
            }
            case 5: {
                // 7 days
                minDiff = 1000 * 60 * 60 * 24 * 7;
                break;
            }
        }

        if (diff > minDiff) {
            KitRedeemEntry.redeem(this.player, kitId);
            KitCollectable.renewKit(this.player, kitId);
            KitRedeemLog.logKitRedeem(this.player, kitId);
        }
        KitCollectable kit = KitCollectable.getContents(this.player, kitId);
        this.getKvStore().set("kit", kit);

        for (ItemStack item : kit.getItems()) {
            this.inv.addItem(item);
        }
    }

    @Override
    public void onClick(Player whoClicked, InventoryClickEvent event, GuiItem handledItem) {
        KitCollectable kit = (KitCollectable) this.getKvStore().get("kit");
        if (kit == null) {
            whoClicked.closeInventory();
            return;
        }
        if (event.getCurrentItem() != null &&
                event.getClickedInventory() != null &&
                event.getClickedInventory().equals(event.getView().getTopInventory())
        ) {
            if (whoClicked.getInventory().firstEmpty() == -1) {
                whoClicked.closeInventory();
                MessageAdapter.sendMessage(whoClicked, Message.Error.KIT_COLLECT_GUI_INVENTORY_FULL);
                return;
            }
            whoClicked.getInventory().addItem(event.getCurrentItem());
            kit.redeemItem(event.getCurrentItem());
            event.getClickedInventory().remove(event.getCurrentItem());
        }
    }
}
