package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.GuiItem;
import de.paradubsch.paradubschmanager.models.SaveRequest;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class SaverequestPlayerInfoButton extends GuiItem {

    @Override
    public void onClick(Player p) {

    }

    @Override
    public void build() {
        SaveRequest saveRequest = (SaveRequest) this.windowArgs.get(1);
        this.setItemPlayerHead(saveRequest.getPlayerRef().getName());
        this.setDisplayName(Message.Gui.REQUEST_BY);
        this.addLore(Component.text("§a"+saveRequest.getPlayerRef().getName()));
    }
}
