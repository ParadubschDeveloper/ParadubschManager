package de.paradubsch.paradubschmanager;

import de.paradubsch.paradubschmanager.commands.*;
import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import de.paradubsch.paradubschmanager.lifecycle.playtime.PlaytimeManager;
import de.paradubsch.paradubschmanager.lifecycle.TestDatabaseConnection;
import de.paradubsch.paradubschmanager.lifecycle.PlayerCacher;
import de.paradubsch.paradubschmanager.lifecycle.QuitListener;
import de.paradubsch.paradubschmanager.util.lang.LanguageManager;
import de.paradubsch.paradubschmanager.util.prefix.ChatMessageListener;
import lombok.Getter;
import org.bukkit.Bukkit;
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
        languageManager = new LanguageManager();
        playtimeManager = new PlaytimeManager();

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
        Bukkit.getPluginCommand("msg").setExecutor(new MsgCommand());
        Bukkit.getPluginCommand("msg").setTabCompleter(new MsgCommand());
        Bukkit.getPluginCommand("prefix").setExecutor(new PrefixCommand());
        Bukkit.getPluginCommand("prefix").setTabCompleter(new PrefixCommand());
        Bukkit.getPluginCommand("lang").setExecutor(new LangCommand());
        Bukkit.getPluginCommand("lang").setTabCompleter(new LangCommand());
        Bukkit.getPluginCommand("namecolor").setExecutor(new NameColorCommand());
        Bukkit.getPluginCommand("namecolor").setTabCompleter(new NameColorCommand());
        Bukkit.getPluginCommand("defaultchatcolor").setExecutor(new DefaultChatColorCommand());
        Bukkit.getPluginCommand("defaultchatcolor").setTabCompleter(new DefaultChatColorCommand());
        Bukkit.getPluginCommand("playtime").setExecutor(new PlaytimeCommand());
    }

    public static ParadubschManager getInstance() {
        return instance;
    }
}
