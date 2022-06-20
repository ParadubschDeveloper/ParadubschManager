package de.paradubsch.paradubschmanager;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.commands.*;
import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import de.paradubsch.paradubschmanager.lifecycle.playtime.PlaytimeManager;
import de.paradubsch.paradubschmanager.lifecycle.TestDatabaseConnection;
import de.paradubsch.paradubschmanager.lifecycle.PlayerCacher;
import de.paradubsch.paradubschmanager.lifecycle.QuitListener;
import de.paradubsch.paradubschmanager.util.lang.LanguageManager;
import de.paradubsch.paradubschmanager.lifecycle.ChatMessageListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

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

        languageManager = new LanguageManager();
        playtimeManager = new PlaytimeManager();
        this.guiManager = new GuiManager(this, languageManager);
        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !> Initialization done");

    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !> Disabled");
    }

    private void registerEvents() {
        new ChatMessageListener();
        new PlayerCacher();
        new QuitListener();
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
    }

    private void register(String command, CommandExecutor obj) {
        PluginCommand pc = Bukkit.getPluginCommand(command);
        if (pc == null) return;
        pc.setExecutor(obj);

        if (obj instanceof TabExecutor) {
            pc.setTabCompleter((TabCompleter) obj);
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
