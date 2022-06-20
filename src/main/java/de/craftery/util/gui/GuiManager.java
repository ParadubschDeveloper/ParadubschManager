package de.craftery.util.gui;

import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.Hibernate;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.LanguageManager;
import lombok.Getter;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GuiManager implements Listener {
    private final Map<Component, List<GuiItem>> guis = new HashMap<>();
    private final Map<Player, Stack<Class<? extends BaseGui>>> sessions = new HashMap<>();

    @Getter
    private static JavaPlugin plugin;

    @Getter
    private static GuiManager instance;

    @Getter
    private static HeadDatabaseAPI headDatabaseAPI;

    @Getter
    private static LanguageManager languageManager;

    public GuiManager(JavaPlugin plugin, LanguageManager languageManager) {
        GuiManager.plugin = plugin;
        GuiManager.instance = this;
        GuiManager.languageManager = languageManager;
        GuiManager.plugin.getServer().getPluginManager().registerEvents(this, GuiManager.getPlugin());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (guis.containsKey(event.getView().title())) {
            event.setCancelled(true);
            Bukkit.getScheduler().runTaskLaterAsynchronously(GuiManager.plugin, () -> {
                guis.get(event.getView().title()).forEach(guiItem -> {
                    if (event.getCurrentItem() == null) return;
                    if (event.getCurrentItem().isSimilar(guiItem.getItemStack())) {
                        Bukkit.getScheduler().runTask(GuiManager.plugin, () -> {
                            guiItem.onClick((Player) event.getWhoClicked());
                        });
                    }
                });
            }, 1);
        }
    }

    @EventHandler
    public void onDatabaseLoad(DatabaseLoadEvent e) {
        headDatabaseAPI = new HeadDatabaseAPI();
    }

    public static Inventory createInventory(Component title, int rows) {
        registerGui(title);
        return Bukkit.createInventory(null, rows * 9, title);
    }

    public static <T extends GuiItem> void addGuiItem(BaseGui src, Class<T> guiItem, int row, int column) {
        try {
            T item = guiItem.getConstructor().newInstance();
            item.applyArgs(src.args.toArray());
            item.instantiate(src.lang);
            item.build();
            try {
                src.inv.setItem((column - 1) + (row - 1) * 9, item.getItemStack());
            } catch (IndexOutOfBoundsException ex) {
                src.inv.addItem(item.getItemStack());
            }

            registerGuiItem(src.title, item);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static void registerGui(Component component) {
        if (!GuiManager.instance.guis.containsKey(component)) {
            GuiManager.instance.guis.put(component, new ArrayList<>());
        }
    }

    private static void registerGuiItem(Component title, GuiItem item) {
        registerGui(title);
        Map<Component, List<GuiItem>> gui = GuiManager.instance.guis;
        List<GuiItem> items = gui.get(title);
        if (items.stream().noneMatch(i -> i.getClass().equals(item.getClass()) && i.getLang().equals(item.getLang()))) {
            items.add(item);
        }
        GuiManager.instance.guis.put(title, items);
    }

    private static <T extends BaseGui> Inventory getGui (Class<T> gui, Player p, Object... args) {
        try {
            PlayerData playerData = Hibernate.getPlayerData(p);
            Language playerLang = Language.getLanguageByShortName(playerData.getLanguage());
            BaseGui window = gui.getConstructor().newInstance();
            window.applyArgs(args);
            window.init(playerLang);
            window.build();
            return window.inv;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T extends BaseGui> void entryGui (Class<T> gui, Player p, Object... args) {
        Inventory inv = getGui(gui, p, args);
        if (inv == null) return;
        Bukkit.getScheduler().runTask(GuiManager.plugin, () -> {
            Stack<Class<? extends BaseGui>> stack = new Stack<>();
            stack.push(gui);
            GuiManager.instance.sessions.put(p, stack);
            p.openInventory(inv);
        });
    }

    public static <T extends BaseGui> void navigate (Class<T> gui, Player p, Object... args) {
        Stack<Class<? extends BaseGui>> stack = GuiManager.instance.sessions.get(p);
        if (stack == null) return;
        stack.push(gui);
        Inventory inv = getGui(gui, p, args);
        if (inv == null) return;
        p.openInventory(inv);
    }

    public static void back(Player p) {
        Stack<Class<? extends BaseGui>> stack = GuiManager.instance.sessions.get(p);
        if (stack == null) return;

        if (stack.size() > 1) {
            stack.pop();
            Class<? extends BaseGui> gui = stack.peek();
            Inventory inv = getGui(gui, p);
            if (inv == null) return;
            p.openInventory(inv);
            GuiManager.instance.sessions.put(p, stack);
        }
    }
}
