package de.paradubsch.paradubschmanager.gui.window;

import de.craftery.util.gui.BaseGui;
import de.craftery.util.gui.GuiItem;
import de.craftery.util.lang.Language;
import de.paradubsch.paradubschmanager.models.SavedKit;
import de.craftery.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitEditGui extends BaseGui {
    @Override
    public void init(Language lang) {
        instantiate(lang, Component.text("§7Edit Kit"), 3);
    }

    @Override
    public void build() {
        Integer kitId = (Integer) this.args.get(0);
        SavedKit kit = SavedKit.getByKitId(kitId);
        this.getKvStore().set("kit", kit);
        for (ItemStack item : kit.getKitItems()) {
            this.inv.addItem(item);
        }
    }

    @Override
    public void onClose(Player player, InventoryCloseEvent event) {
        SavedKit kit = (SavedKit) this.getKvStore().get("kit");
        List<ItemStack> contents = new ArrayList<>();
        for (ItemStack item : event.getInventory().getContents()) {
            if (item != null) {
                contents.add(item);
            }
        }
        kit.setKitItems(contents);
        SavedKit.saveKit(kit);
        MessageAdapter.sendMessage(player, Message.Info.KIT_EDITED, kit.getKitId() + "");
    }

    @Override
    public void onClick(Player whoClicked, InventoryClickEvent event, GuiItem handledItem) {
        event.setCancelled(false);
    }
}
