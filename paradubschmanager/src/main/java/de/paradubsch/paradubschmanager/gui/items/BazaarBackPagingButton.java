package de.paradubsch.paradubschmanager.gui.items;

import de.craftery.craftinglib.util.gui.GuiItem;
import de.craftery.craftinglib.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BazaarBackPagingButton extends GuiItem {

    @Override
    public void onClick(Player p) {
        Bukkit.getLogger().info("BazaarBackPagingButton.onClick");
        Integer page = (Integer) this.getKvStore().get("bazaarPage");
        if (page == null) {
            page = 1;
            this.getKvStore().set("bazaarPage", page);
        } else {
            page--;
            this.getKvStore().set("bazaarPage", page);
        }
        GuiManager.rebuild(p);
    }

    @Override
    public void build() {
        Bukkit.getLogger().info("build BazaarBackPagingButton");
        Integer page = (Integer) this.getKvStore().get("bazaarPage");
        Bukkit.getLogger().info("Page is " + page);
        if (page == null) {
            page = 1;
        }
        this.setItemHead("7789");
        this.setDisplayName(Message.Gui.BACK_PAGE);
        this.addLore(Message.Constant.PAGE, page-1 + "");
    }
}
