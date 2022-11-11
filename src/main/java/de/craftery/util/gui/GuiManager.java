package de.craftery.util.gui;

import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.LanguageManager;
import lombok.Getter;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;

public class GuiManager implements Listener {
    public static NamespacedKey itemIdentifier;
    public static SignMenuFactory signFactory;

    @Getter
    private static final Map<Class<? extends BaseGui>, BaseGui> handlerMap = new HashMap<>();

    @Getter
    private final Map<Component, List<GuiItem>> guis = new HashMap<>();

    @Getter
    private final Map<Player, Stack<Class<? extends BaseGui>>> sessions = new HashMap<>();

    @Getter
    private final Map<Player, List<Object>> sessionData = new HashMap<>();

    @Getter
    private static final Map<Player, KVStore> kvStores = new HashMap<>();

    public static final Map<Player, Map<Object, String>> prompts = new HashMap<>();

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
        GuiManager.itemIdentifier = new NamespacedKey(plugin, "itemIdentifier");
        GuiManager.instance = this;
        GuiManager.languageManager = languageManager;
        GuiManager.signFactory = new SignMenuFactory(plugin);
        GuiManager.plugin.getServer().getPluginManager().registerEvents(this, GuiManager.getPlugin());
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (guis.containsKey(event.getView().title())) {
            Class<? extends BaseGui> gui = sessions.get((Player) event.getPlayer()).peek();
            BaseGui handler = getWindowHandler(gui);
            if (handler != null) {
                handler.onClose((Player) event.getPlayer(), event);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (guis.containsKey(event.getView().title())) {
            event.setCancelled(true);
            GuiItem handledItem = null;
            Class<? extends BaseGui> gui = sessions.get((Player) event.getWhoClicked()).peek();
            for (GuiItem guiItem : guis.get(event.getView().title())) {
                if (event.getCurrentItem() == null) continue;
                if (event.getCurrentItem().getType() == Material.AIR) continue;
                guiItem.instantiate(MessageAdapter.getSenderLang(event.getWhoClicked()), (Player) event.getWhoClicked(), gui);
                guiItem.applyWindowArgs(GuiManager.instance.sessionData.get((Player) event.getWhoClicked()).toArray());
                guiItem.build();
                if (event.getCurrentItem().isSimilar(guiItem.getItemStack())) {
                    if (guiItem instanceof AbstractGuiItem) {
                        ItemMeta meta = event.getCurrentItem().getItemMeta();
                        if (meta == null) continue;
                        PersistentDataContainer container = meta.getPersistentDataContainer();
                        if (!container.has(GuiManager.itemIdentifier, new PersistentSerializableType())) {
                            Bukkit.getLogger().log(Level.WARNING, "Abstract Item has no identifier!");
                            continue;
                        }
                        Serializable identifier = container.get(GuiManager.itemIdentifier, new PersistentSerializableType());
                        if (!guiItem.getIdentifier().equals(identifier)) {
                            Bukkit.getLogger().log(Level.INFO, "Got: " + guiItem.getIdentifier() + " Expected: " + identifier);
                            continue;
                        }
                    }
                    handledItem = guiItem;
                    Bukkit.getScheduler().runTask(GuiManager.plugin, () -> {
                        guiItem.onClick((Player) event.getWhoClicked());
                        guiItem.onClick((Player) event.getWhoClicked(), event);
                    });
                    break;
                }
            }
            BaseGui handler = getWindowHandler(gui);
            if (handler != null) {
                handler.onClick((Player) event.getWhoClicked(), event, handledItem);
            }
        }
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent e) {
        this.sessions.remove(e.getPlayer());
        this.sessionData.remove(e.getPlayer());
        prompts.remove(e.getPlayer());
        kvStores.remove(e.getPlayer());
    }

    @EventHandler
    public void onDatabaseLoad(DatabaseLoadEvent e) {
        headDatabaseAPI = new HeadDatabaseAPI();
    }

    public static Inventory createInventory(Component title, int rows) {
        registerGui(title);
        return Bukkit.createInventory(null, rows * 9, title);
    }

    public static <T extends GuiItem> void addGuiItem(BaseGui src, Class<T> guiItem, Player p, int row, int column, Object... itemArgs) {
        try {
            T item = guiItem.getConstructor().newInstance();
            item.applyWindowArgs(GuiManager.instance.sessionData.get(p).toArray());
            item.applyItemArgs(itemArgs);
            item.instantiate(src.lang, p, src.getClass());
            item.setIdentifier(src.lang.toString() + guiItem.getName());
            item.build();
            try {
                src.inv.setItem((column - 1) + (row - 1) * 9, item.getItemStack());
            } catch (IndexOutOfBoundsException ex) {
                src.inv.addItem(item.getItemStack());
            }

            registerGuiItem(src.title, item);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static <T extends AbstractGuiItem> void addAbstractGuiItem(BaseGui src, Class<T> guiItem, Player p, int row, int column, Serializable identifier, Object... itemArgs) {
        try {
            T item = guiItem.getConstructor().newInstance();
            item.applyWindowArgs(GuiManager.instance.sessionData.get(p).toArray());
            item.applyItemArgs(itemArgs);
            item.instantiate(src.lang, p, src.getClass());
            item.setIdentifier(src.lang.toString() + guiItem.getName() + identifier.toString());
            item.build();
            try {
                src.inv.setItem((column - 1) + (row - 1) * 9, item.getItemStack());
            } catch (IndexOutOfBoundsException ex) {
                src.inv.addItem(item.getItemStack());
            }

            registerGuiItem(src.title, item);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static @Nullable BaseGui getWindowHandler(@NotNull Class<? extends BaseGui> gui) {
        return handlerMap.get(gui);
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
        if (items.stream().noneMatch(i -> i.getIdentifier().equals(item.getIdentifier()))) {
            items.add(item);
        }
        GuiManager.instance.guis.put(title, items);
    }

    private static <T extends BaseGui> Inventory getGui(Class<T> gui, Player p, Object... args) {
        try {
            PlayerData playerData = PlayerData.getByPlayer(p);
            Language playerLang = Language.getLanguageByShortName(playerData.getLanguage());
            BaseGui window = gui.getConstructor().newInstance();
            if (!handlerMap.containsKey(gui)) {
                handlerMap.put(gui, window);
            }
            window.applyArgs(p, args);
            List<Object> argList = new ArrayList<>(Arrays.asList(args));
            GuiManager.instance.sessionData.put(p, argList);
            window.init(playerLang);
            window.build();
            return window.inv;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T extends BaseGui> void entryGui(Class<T> gui, Player p, Object... args) {
        GuiManager.prompts.remove(p);
        GuiManager.kvStores.remove(p);

        Inventory inv = getGui(gui, p, args);
        if (inv == null) return;
        Bukkit.getScheduler().runTask(GuiManager.plugin, () -> {
            Stack<Class<? extends BaseGui>> stack = new Stack<>();
            stack.push(gui);
            GuiManager.instance.sessions.put(p, stack);
            p.openInventory(inv);
        });
    }

    public static void prompt(Class<? extends BaseGui> origin, Player p, Object identifier, String line1, String line2, String line3) {
        Bukkit.getLogger().log(Level.INFO, "prompt: Origin: " + origin.getSimpleName() + " Player: " + p + " Identifier: " + identifier + " Line1: " + line1 + " Line2: " + line2 + " Line3: " + line3);
        SignMenuFactory.Menu menu = signFactory.newMenu(Arrays.asList("", line1, line2, line3))
                .reopenIfFail(false)
                .response((player, strings) -> {
                    Map<Object, String> prompts = GuiManager.prompts.get(p);
                    if (prompts == null) {
                        prompts = new HashMap<>();
                    }
                    System.out.println("Closed: " + strings[0]);
                    prompts.put(identifier, strings[0]);
                    GuiManager.prompts.put(p, prompts);
                    List<Object> args = GuiManager.instance.sessionData.get(p);

                    Inventory inv;
                    if (args == null || args.isEmpty()) {
                        inv = getGui(origin, p);
                    } else {
                        inv = getGui(origin, p, args.toArray());
                    }
                    if (inv == null) return true;
                    Bukkit.getScheduler().runTaskLater(GuiManager.plugin, () -> p.openInventory(inv), 1);
                    return true;
                });
        Bukkit.getScheduler().runTaskLater(plugin, () -> p.closeInventory(), 1);
        Bukkit.getScheduler().runTaskLater(plugin, () -> menu.open(p), 2);
    }

    public static String getPrompt(Player player, Object identifier) {
        Map<Object, String> prompts = GuiManager.prompts.get(player);
        if (prompts == null) return null;
        return prompts.get(identifier);
    }

    public static <T extends BaseGui> void navigate(Class<T> gui, Player p, Object... args) {
        Stack<Class<? extends BaseGui>> stack = GuiManager.instance.sessions.get(p);
        if (stack == null) {
            stack = new Stack<>();
        }
        stack.push(gui);
        GuiManager.instance.sessions.put(p, stack);
        Inventory inv = getGui(gui, p, args);
        if (inv == null) return;
        p.openInventory(inv);
    }

    public static void rebuild(Player p) {
        Stack<Class<? extends BaseGui>> stack = GuiManager.instance.sessions.get(p);
        if (stack == null) return;
        if (stack.size() == 0) return;
        Class<? extends BaseGui> gui = stack.peek();
        List<Object> args = GuiManager.instance.sessionData.get(p);

        Inventory inv;
        if (args == null || args.isEmpty()) {
            inv = getGui(gui, p);
        } else {
            inv = getGui(gui, p, args.toArray());
        }
        if (inv == null) return;
        p.openInventory(inv);
    }

    public static void back(Player p) {
        Stack<Class<? extends BaseGui>> stack = GuiManager.instance.sessions.get(p);
        if (stack == null) return;

        if (stack.size() > 1) {
            stack.pop();
            Class<? extends BaseGui> gui = stack.peek();
            List<Object> args = GuiManager.instance.sessionData.get(p);

            Inventory inv;
            if (args == null || args.isEmpty()) {
                inv = getGui(gui, p);
            } else {
                inv = getGui(gui, p, args.toArray());
            }
            if (inv == null) return;
            p.openInventory(inv);
            GuiManager.instance.sessions.put(p, stack);
        }
    }
}
