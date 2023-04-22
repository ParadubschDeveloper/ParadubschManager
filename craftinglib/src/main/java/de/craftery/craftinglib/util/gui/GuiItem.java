package de.craftery.craftinglib.util.gui;

import de.craftery.craftinglib.CraftingLib;
import de.craftery.craftinglib.InternalMessages;
import de.craftery.craftinglib.messaging.lang.Language;
import de.craftery.craftinglib.messaging.lang.BaseMessageType;
import de.craftery.craftinglib.util.MessageAdapter;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class GuiItem {

    @Setter
    @Getter
    private ItemStack itemStack;

    @Getter
    public Language lang;

    @Getter
    @Setter
    private String identifier;

    /**
     * The arguments passed when instantiating the Gui of this Item
     */
    public final List<Object> windowArgs = new ArrayList<>();

    /**
     * The arguments passed when instantiating the Item
     */
    public final List<Object> itemArgs = new ArrayList<>();

    @Getter
    private Player player;
    private Class<? extends BaseGui> parent;

    public void instantiate(Language lang, Player player, Class<? extends BaseGui> parent) {
        this.lang = lang;
        this.player = player;
        this.parent = parent;
    }

    public void setItemHead(String headId) {
        try {
            this.itemStack = CraftingLib.getInstance().getHeadDatabase().getItemHead(headId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setItemPlayerHead(String playerName) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
        skull.setItemMeta(meta);
        this.itemStack = skull;
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
        Component displayName = CraftingLib.getInstance().getLanguageManager().get(displayNameTemplate, lang, args);
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

    public void addLore(String lore) {
        ItemMeta meta = this.itemStack.getItemMeta();
        List<Component> loreList = meta.lore();
        if (loreList == null) {
            loreList = new ArrayList<>();
        }
        loreList.add(Component.text(lore));
        meta.lore(loreList);
        this.itemStack.setItemMeta(meta);
    }

    public void addLore(BaseMessageType loreTemplate, String... args) {
        Component displayName = CraftingLib.getInstance().getLanguageManager().get(loreTemplate, lang, args);
        this.addLore(displayName);
    }

    public void applyWindowArgs(Object... args) {
        this.windowArgs.clear();
        this.windowArgs.addAll(Arrays.asList(args));
    }

    public void applyItemArgs(Object... args) {
        this.itemArgs.clear();
        this.itemArgs.addAll(Arrays.asList(args));
    }

    public void prompt(Object identifier, String line1, String line2, String line3) {
        if (line1 == null) {
            line1 = "";
        }
        if (line2 == null) {
            line2 = "";
        }
        if (line3 == null) {
            line3 = "";
        }
        GuiManager.prompt(parent, player, identifier, line1, line2, line3);
    }

    public void prompt(PromptType type, Object identifier) {
        String prompt = "";
        switch (type) {
            case INTEGER:
                prompt = CraftingLib.getInstance().getLanguageManager().getString(InternalMessages.INSERT_NUMBER, MessageAdapter.getSenderLang(player));
                break;
        }
        GuiManager.prompt(parent, player, identifier, "^^^^^^^^^^", prompt, "");
    }

    /**
     * Returns the result of a fired prompt or null
     */
    public @Nullable String getPrompt(Object identifier) {
        return GuiManager.getPrompt(player, identifier);
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

    @Override
    public String toString() {
        return this.getIdentifier();
    }

    public void onClick(Player p, InventoryClickEvent event) {}
    public abstract void onClick(Player p);
    public abstract void build();
}
