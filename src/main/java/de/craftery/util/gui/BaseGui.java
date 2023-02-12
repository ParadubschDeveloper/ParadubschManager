package de.craftery.util.gui;

import de.craftery.CraftPlugin;
import de.craftery.util.lang.Language;
import de.craftery.util.lang.BaseMessageType;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseGui {
    public Language lang;
    public Component title;
    public Inventory inv;
    public Player player;

    /**
     * The arguments passed when instantiating this Gui
     */
    public final List<Object> args = new ArrayList<>();

    public void instantiate(Language lang, BaseMessageType title, int rows) {
        this.title = CraftPlugin.getInstance().getLanguageManager().get(title, lang);
        this.lang = lang;
        this.inv = GuiManager.createInventory(this.title, rows);
    }

    public void instantiate(Language lang, Component title, int rows) {
        this.title = title;
        this.lang = lang;
        this.inv = GuiManager.createInventory(this.title, rows);
    }

    public <T extends GuiItem> void addItem(Class<T> guiItem, int row, int column, Object... args) {
        GuiManager.addGuiItem(this, guiItem, player, row, column, args);
    }

    public <T extends AbstractGuiItem> void addAbstractItem(Class<T> guiItem, int row, int column, Serializable identifier, Object... args) {
        GuiManager.addAbstractGuiItem(this, guiItem, player, row, column, identifier, args);
    }

    public void applyArgs(Player p, Object... args) {
        this.player = p;
        this.args.clear();
        this.args.addAll(Arrays.asList(args));
    }

    /**
     * A Key-Value Store that is persisted while the player is in the GUI.
     */
    public KVStore getKvStore(Player player) {
        KVStore store = GuiManager.getKvStores().get(player);
        if (store == null)
            GuiManager.getKvStores().put(player, store = new KVStore(player));
        return store;
    }

    public KVStore getKvStore() {
        return getKvStore(this.player);
    }

    public abstract void init(Language lang);

    public abstract void build();

    public void onClick(Player whoClicked, InventoryClickEvent event, GuiItem handledItem) {}

    public void onClose(Player player, InventoryCloseEvent event) {}

}
