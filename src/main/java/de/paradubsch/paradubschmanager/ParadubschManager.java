package de.paradubsch.paradubschmanager;

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
import org.bukkit.plugin.java.JavaPlugin;

public final class ParadubschManager extends JavaPlugin {
    private static ParadubschManager instance;

    @Getter
    private LanguageManager languageManager;

    @Getter
    private PlaytimeManager playtimeManager;

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
        languageManager = new LanguageManager();
        playtimeManager = new PlaytimeManager();
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
    }

    private void register(String command, CommandExecutor obj) {
        PluginCommand pc = Bukkit.getPluginCommand(command);
        if (pc == null) return;
        pc.setExecutor(obj);

        if (obj instanceof TabExecutor) {
            pc.setTabCompleter((TabCompleter) obj);
        }
    }

    public static ParadubschManager getInstance() {
        return instance;
    }
}
