package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.GuiItem;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class WorldInfoButton extends GuiItem {
    @Override
    public void onClick(Player p) {

    }

    @Override
    public void build() {
        this.setItemHead("32442");
        this.setDisplayName(Message.Gui.YOU_ARE_IN_WORLD);
        this.addLore(Component.text("§a" + this.getPlayer().getWorld().getName()));
    }
}
