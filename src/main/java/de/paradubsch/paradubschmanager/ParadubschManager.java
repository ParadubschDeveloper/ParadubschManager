package de.paradubsch.paradubschmanager;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.commands.*;
import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import de.paradubsch.paradubschmanager.config.HibernateConfigurator;
import de.paradubsch.paradubschmanager.lifecycle.*;
import de.paradubsch.paradubschmanager.lifecycle.playtime.PlaytimeManager;
import de.paradubsch.paradubschmanager.util.lang.LanguageManager;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class ParadubschManager extends JavaPlugin {
    private static ParadubschManager instance;

    @Getter
    private LanguageManager languageManager;

    @Getter
    private PlaytimeManager playtimeManager;

    @Getter
    private GuiManager guiManager;

    @Getter
    private WorldGuardPlugin worldGuardPlugin;

    @Getter
    private WorldEditPlugin worldEditPlugin;

    private ProtocolManager protocolManager;

    private LuckPerms luckPermsApi;

    @Override
    public void onEnable() {
        instance = this;
        ConfigurationManager.copyDefaultConfiguration();
        registerEvents();

        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("==== Paradubsch ====");
        Bukkit.getConsoleSender().sendMessage("Author: Paradubsch-Team");
        Bukkit.getConsoleSender().sendMessage("Version: PREALPHA");
        Bukkit.getConsoleSender().sendMessage("==== Paradubsch ====");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !> Initializing");
        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !>> Registering commands");
        this.registerCommands();
        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !>> Testing Database Connection");
        new TestDatabaseConnection();

        worldGuardPlugin = initializeWorldGuardPlugin();
        worldEditPlugin = initializeWorldEditPlugin();
        protocolManager = ProtocolLibrary.getProtocolManager();

        languageManager = new LanguageManager();
        playtimeManager = new PlaytimeManager();
        this.guiManager = new GuiManager(this, languageManager);
        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !> Initialization done");

    }

    public static ProtocolManager getProtocolManager() {
        ProtocolManager pm = ParadubschManager.getInstance().protocolManager;
        if (pm == null) {
            ParadubschManager.getInstance().protocolManager = ProtocolLibrary.getProtocolManager();
        }
        return ParadubschManager.getInstance().protocolManager;
    }

    public static LuckPerms getLuckPermsApi() {
        LuckPerms lp = ParadubschManager.getInstance().luckPermsApi;
        if (lp == null) {
            try {
                ParadubschManager.getInstance().luckPermsApi = LuckPermsProvider.get();
            } catch (IllegalStateException ex) {
                return null;
            }
        }
        return ParadubschManager.getInstance().luckPermsApi;
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        worldGuardPlugin = null;
        worldEditPlugin = null;
        playtimeManager = null;
        protocolManager = null;
        luckPermsApi = null;
        languageManager = null;
        guiManager = null;
        unregisterCommands();
        HibernateConfigurator.shutdown();
        System.gc();
        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !> Disabled");
    }

    private void registerEvents() {
        new ChatMessageListener();
        new PlayerCacher();
        new QuitListener();
        new TabDecorationManager(this);
    }

    private void registerCommands() {
        register("msg", new MsgCommand());
        register("prefix", new PrefixCommand());
        register("lang", new LangCommand());
        register("namecolor", new NameColorCommand());
        register("defaultchatcolor", new DefaultChatColorCommand());
        register("playtime", new PlaytimeCommand());
        register("money", new MoneyCommand());
        register("sethome", new SethomeCommand());
        register("home", new HomeCommand());
        register("buyhome", new BuyhomeCommand());
        register("homes", new HomesCommand());
        register("viewhome", new ViewhomeCommand());
        register("delhome", new DelhomeCommand());
        register("gs", new GsCommand());
        register("save", new SaveCommand());
        register("rank", new RankCommand());
        register("b", new BanCommand());
    }

    List<String> registeredCommands = new ArrayList<>();
    private <T extends CommandExecutor & TabCompleter> void register(String command, T obj) {
        registeredCommands.add(command);
        PluginCommand pc = Bukkit.getPluginCommand(command);
        if (pc == null) return;
        pc.setExecutor(obj);
        pc.setTabCompleter(obj);
    }

    private void unregisterCommands() {
        for (String command : registeredCommands) {
            PluginCommand pc = Bukkit.getPluginCommand(command);
            if (pc == null) continue;
            pc.setExecutor(null);
            pc.setTabCompleter(null);
        }
    }

    private WorldGuardPlugin initializeWorldGuardPlugin () {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");
        if (!(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }

    private WorldEditPlugin initializeWorldEditPlugin () {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldEdit");
        if (!(plugin instanceof WorldEditPlugin)) {
            return null;
        }
        return (WorldEditPlugin) plugin;
    }

    public static ParadubschManager getInstance() {
        return instance;
    }
}
