package de.craftery;

import de.craftery.util.CachingManager;
import de.craftery.util.ConfigurationManager;
import de.craftery.util.TestDatabaseConnection;
import de.craftery.util.gui.GuiManager;
import de.craftery.util.lang.LanguageManager;
import lombok.Getter;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CraftPlugin extends JavaPlugin implements Listener {
    private static CraftPlugin instance;
    private final List<String> registeredCommands = new ArrayList<>();

    @Getter
    private CachingManager cachingManager;

    @Getter
    private LanguageManager languageManager;

    @Getter
    private HeadDatabaseAPI headDatabase;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(this, this);
        ConfigurationManager.copyDefaultConfiguration();
        cachingManager = new CachingManager();
        languageManager = new LanguageManager();
        new GuiManager();

        languageManager.registerMessageEnum(InternalMessages.class);

        new TestDatabaseConnection();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(Player::closeInventory);
        Bukkit.getScheduler().cancelTasks(this);

        cachingManager = null;
        languageManager = null;
        unregisterCommands();

        System.gc();
        super.onDisable();
    }

    @EventHandler
    public void onDatabaseLoad(DatabaseLoadEvent e) {
        headDatabase = new HeadDatabaseAPI();
    }

    protected <T extends CommandExecutor & TabCompleter> void register(String command, T obj) {
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

    public static CraftPlugin getInstance() {
        return instance;
    }

    // Constructors for testing
    public CraftPlugin() {
        super();
    }
    protected CraftPlugin(JavaPluginLoader loader, PluginDescriptionFile descriptionFile, File dataFolder, File file) {
        super(loader, descriptionFile, dataFolder, file);
    }
}
