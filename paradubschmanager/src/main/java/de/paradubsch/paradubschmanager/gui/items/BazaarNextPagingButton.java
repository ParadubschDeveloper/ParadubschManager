package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.util.gui.GuiItem;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BazaarNextPagingButton extends GuiItem {
    @Override
    public void onClick(Player p) {
        Bukkit.getLogger().info("BazaarNextPagingButton.onClick");
        Integer page = (Integer) this.getKvStore().get("bazaarPage");
        if (page == null) {
            page = 1;
            this.getKvStore().set("bazaarPage", page);
        } else {
            page++;
            this.getKvStore().set("bazaarPage", page);
        }
        GuiManager.rebuild(p);
    }

    @Override
    public void build() {
        Integer page = (Integer) this.getKvStore().get("bazaarPage");
        if (page == null) {
            page = 1;
        }
        this.setItemHead("7786");
        this.setDisplayName(Message.Gui.NEXT_PAGE);
        this.addLore(Message.Constant.PAGE, page+1 + "");
    }
}
