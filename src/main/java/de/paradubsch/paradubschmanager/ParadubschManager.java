package de.paradubsch.paradubschmanager;

import de.paradubsch.paradubschmanager.commands.LangCommand;
import de.paradubsch.paradubschmanager.commands.MsgCommand;
import de.paradubsch.paradubschmanager.commands.PrefixCommand;
import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import de.paradubsch.paradubschmanager.lifecycle.PlayerCacher;
import de.paradubsch.paradubschmanager.util.lang.LanguageManager;
import de.paradubsch.paradubschmanager.util.prefix.ChatMessageListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ParadubschManager extends JavaPlugin {
    private static ParadubschManager instance;
    private LanguageManager languageManager;

    @Override
    public void onEnable() {
        instance = this;
        ConfigurationManager.copyDefaultConfiguration();
        registerEvents();
        languageManager = new LanguageManager();

        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("==== Paradubsch ====");
        Bukkit.getConsoleSender().sendMessage("Author: Paradubsch-Team");
        Bukkit.getConsoleSender().sendMessage("Version: PREALPHA");
        Bukkit.getConsoleSender().sendMessage("==== Paradubsch ====");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !> Initializing");
        Bukkit.getConsoleSender().sendMessage(getConfig().getString("consolePrefix") + "!>> Registering commands");
        Bukkit.getPluginCommand("msg").setExecutor(new MsgCommand());
        Bukkit.getPluginCommand("msg").setTabCompleter(new MsgCommand());
        Bukkit.getPluginCommand("prefix").setExecutor(new PrefixCommand());
        Bukkit.getPluginCommand("prefix").setTabCompleter(new PrefixCommand());
        Bukkit.getPluginCommand("lang").setExecutor(new LangCommand());
        Bukkit.getPluginCommand("lang").setTabCompleter(new LangCommand());
        Bukkit.getConsoleSender().sendMessage(getConfig().getString("consolePrefix") + "!> Initialization done");
    }

    @Override
    public void onDisable() {
    	// Disable server plugin
        Bukkit.getConsoleSender().sendMessage(getConfig().getString("consolePrefix") + "!> Disabling");
        Bukkit.getConsoleSender().sendMessage(getConfig().getString("consolePrefix") + "!>> Unregistering commands");

        Bukkit.getPluginCommand("msg").setExecutor(null);
        Bukkit.getPluginCommand("msg").setTabCompleter(null);
        Bukkit.getPluginCommand("prefix").setExecutor(null);
        Bukkit.getPluginCommand("prefix").setTabCompleter(null);

        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !> Disabled");
    }

    private static void registerEvents() {
        new ChatMessageListener();
        new PlayerCacher();
    }

    public static ParadubschManager getInstance() {
        return instance;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }
}
