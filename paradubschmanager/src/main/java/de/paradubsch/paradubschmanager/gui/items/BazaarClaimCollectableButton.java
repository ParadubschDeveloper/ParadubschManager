package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.craftinglib.util.gui.AbstractGuiItem;
import de.craftery.craftinglib.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.models.BazaarCollectable;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class BazaarClaimCollectableButton extends AbstractGuiItem {
    @Override
    public void onClick(Player p) {
        BazaarCollectable collectable = (BazaarCollectable) this.getKvStore().get(this.itemArgs.get(0).toString() + "newestCollectable");
        for (int i = 0; i < 36; i++) {
            ItemStack item = p.getInventory().getItem(i);
            if (item == null) {
                int maxStackSize = collectable.getMaterial().getMaxStackSize();
                if (collectable.getAmount() <= maxStackSize) {
                    p.getInventory().setItem(i, new ItemStack(collectable.getMaterial(), collectable.getAmount().intValue()));
                    collectable.delete();
                    GuiManager.rebuild(p);
                    return;
                } else {
                    p.getInventory().setItem(i, new ItemStack(collectable.getMaterial(), maxStackSize));
                    collectable.setAmount(collectable.getAmount() - maxStackSize);
                }
            }
        }
        collectable.saveOrUpdate();
        GuiManager.rebuild(p);
    }

    @Override
    public void build() {
        BazaarCollectable collectable = (BazaarCollectable) this.getKvStore().get(this.itemArgs.get(0).toString() + "newestCollectable");
        if (collectable != null) {
            this.setItemMaterial(collectable.getMaterial());
            this.setDisplayName(Component.translatable(new ItemStack(collectable.getMaterial()).translationKey()));
            this.addLore(Message.Gui.COLLECTABLE_AMOUNT, collectable.getAmount() + "");
            this.addLore(Message.Gui.CLICK_TO_COLLECT);
        } else {
            this.setItemMaterial(Material.AIR);
        }
    }
}
