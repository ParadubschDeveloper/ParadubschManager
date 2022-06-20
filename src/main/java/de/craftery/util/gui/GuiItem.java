package de.craftery.util.gui;

import de.paradubsch.paradubschmanager.util.lang.BaseMessageType;
import de.paradubsch.paradubschmanager.util.lang.Language;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class GuiItem {
    @Getter
    private ItemStack itemStack;

    @Getter
    public Language lang;
    public final List<Object> args = new ArrayList<>();

    public void instantiate(Language lang) {
        this.lang = lang;
    }

    public void setItemHead(String headId) {
        try {
            this.itemStack = GuiManager.getHeadDatabaseAPI().getItemHead(headId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setItemMaterial(Material mat) {
        this.itemStack = new ItemStack(mat);
    }

    public void setDisplayName(Component displayName) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.displayName(displayName);
        this.itemStack.setItemMeta(meta);
    }

    public void setDisplayName(String displayName) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.displayName(Component.text(displayName));
        this.itemStack.setItemMeta(meta);
    }

    public void setDisplayName(BaseMessageType displayNameTemplate, String... args) {
        Component displayName = GuiManager.getLanguageManager().get(displayNameTemplate, lang, args);
        this.setDisplayName(displayName);
    }

    public void addLore(Component lore) {
        ItemMeta meta = this.itemStack.getItemMeta();
        List<Component> loreList = meta.lore();
        if (loreList == null) {
            loreList = new ArrayList<>();
        }
        loreList.add(lore);
        meta.lore(loreList);
        this.itemStack.setItemMeta(meta);
    }

    public void addLore(BaseMessageType loreTemplate, String... args) {
        Component displayName = GuiManager.getLanguageManager().get(loreTemplate, lang, args);
        this.addLore(displayName);
    }

    public void applyArgs(Object... args) {
        this.args.addAll(Arrays.asList(args));
    }

    public abstract void onClick(Player p);
    public abstract void build();
}
