package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.craftinglib.util.gui.GuiItem;
import de.craftery.craftinglib.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.entity.Player;

public class BackButton extends GuiItem {
    @Override
    public void onClick(Player p) {
        GuiManager.back(p);
    }

    @Override
    public void build() {
        this.setItemHead("8785");
        this.setDisplayName(Message.Gui.BACK);
    }
}
