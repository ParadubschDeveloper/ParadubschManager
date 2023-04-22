package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.craftinglib.util.gui.GuiItem;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.entity.Player;

public class CancelButton extends GuiItem {
    @Override
    public void onClick(Player p) {
        p.closeInventory();
    }

    @Override
    public void build() {
        this.setItemHead("844");
        this.setDisplayName(Message.Gui.CANCEL);
    }
}
