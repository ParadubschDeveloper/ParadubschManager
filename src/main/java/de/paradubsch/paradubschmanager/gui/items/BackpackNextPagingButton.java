package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.GuiItem;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.entity.Player;

public class BackpackNextPagingButton extends GuiItem {

    @Override
    public void onClick(Player p) {
        Integer page = (Integer) this.getKvStore().get("backpackPage");
        if (page == null) {
            page = 1;
            this.getKvStore().set("backpackPage", page);
        } else {
            page++;
            this.getKvStore().set("backpackPage", page);
        }
        GuiManager.rebuild(p);
    }

    @Override
    public void build() {
        Integer page = (Integer) this.getKvStore().get("backpackPage");
        if (page == null) {
            page = 1;
        }
        this.setItemHead("7786");
        this.setDisplayName(Message.Gui.NEXT_PAGE);
        this.addLore(Message.Constant.PAGE, page+1 + "");
    }

}
