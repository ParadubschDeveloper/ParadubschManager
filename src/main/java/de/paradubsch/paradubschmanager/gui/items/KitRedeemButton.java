package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.AbstractGuiItem;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.gui.window.KitCollectGui;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KitRedeemButton extends AbstractGuiItem {
    @Override
    public void onClick(Player p) {
        Integer kitId = (Integer) this.itemArgs.get(0);
        String neededPermission = "paradubsch.kit." + kitId;
        if (!p.hasPermission(neededPermission)) {
            p.closeInventory();
            MessageAdapter.sendMessage(p, Message.Error.KIT_GUI_NO_PERMISSION);
            return;
        }
        GuiManager.navigate(KitCollectGui.class, p, kitId);
    }

    @Override
    public void build() {
        Integer kitId = (Integer) this.itemArgs.get(0);
        switch (kitId) {
            case 1: {
                this.setItemMaterial(Material.BARREL);
                this.setDisplayName(Message.Gui.KIT_GUI_KIT_1_NAME);
                this.addLore(Message.Gui.KIT_GUI_KIT_1_LORE_1);
                this.addLore(Message.Gui.KIT_GUI_KIT_1_LORE_2);
                break;
            }
            case 2: {
                this.setItemMaterial(Material.CHEST);
                this.setDisplayName(Message.Gui.KIT_GUI_KIT_2_NAME);
                this.addLore(Message.Gui.KIT_GUI_KIT_2_LORE_1);
                this.addLore(Message.Gui.KIT_GUI_KIT_2_LORE_2);
                break;
            }
            case 3: {
                this.setItemMaterial(Material.ENDER_CHEST);
                this.setDisplayName(Message.Gui.KIT_GUI_KIT_3_NAME);
                this.addLore(Message.Gui.KIT_GUI_KIT_3_LORE_1);
                this.addLore(Message.Gui.KIT_GUI_KIT_3_LORE_2);
                break;
            }
            case 4: {
                this.setItemMaterial(Material.TRAPPED_CHEST);
                this.setDisplayName(Message.Gui.KIT_GUI_KIT_4_NAME);
                this.addLore(Message.Gui.KIT_GUI_KIT_4_LORE_1);
                this.addLore(Message.Gui.KIT_GUI_KIT_4_LORE_2);
                break;
            }
            case 5: {
                this.setItemMaterial(Material.TRAPPED_CHEST);
                this.setDisplayName(Message.Gui.KIT_GUI_KIT_5_NAME);
                this.addLore(Message.Gui.KIT_GUI_KIT_5_LORE_1);
                this.addLore(Message.Gui.KIT_GUI_KIT_5_LORE_2);
                break;
            }
        }
    }
}
