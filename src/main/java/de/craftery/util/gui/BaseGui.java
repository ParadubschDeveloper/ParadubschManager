package de.craftery.util.gui;

import de.paradubsch.paradubschmanager.util.lang.BaseMessageType;
import de.paradubsch.paradubschmanager.util.lang.Language;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseGui {
    public Language lang;
    public Component title;
    public Inventory inv;
    public final List<Object> args = new ArrayList<>();

    public void instantiate(Language lang, BaseMessageType title, int rows) {
        this.title = GuiManager.getLanguageManager().get(title, lang);
        this.lang = lang;
        this.inv = GuiManager.createInventory(this.title, rows);
    }

    public <T extends GuiItem> void addItem(Class<T> guiItem, int row, int column) {
        GuiManager.addGuiItem(this, guiItem, row, column);
    }

    public void applyArgs(Object... args) {
        this.args.addAll(Arrays.asList(args));
    }

    public abstract void init(Language lang);

    public abstract void build();
}
