package de.craftery;

import de.craftery.util.CachingManager;
import de.craftery.util.ConfigurationManager;
import de.craftery.util.HibernateConfigurator;
import de.craftery.util.TestDatabaseConnection;
import de.craftery.util.gui.GuiManager;
import de.craftery.util.lang.LanguageManager;
import lombok.Getter;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public class CraftingLib extends JavaPlugin implements Listener {
    @Getter
    private static CraftingLib instance;

    @Getter
    private HeadDatabaseAPI headDatabase;

    @Getter
    private CachingManager cachingManager;

    @Getter
    private LanguageManager languageManager;

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

        HibernateConfigurator.shutdown();

        System.gc();
        super.onDisable();
    }

    @EventHandler
    public void onDatabaseLoad(DatabaseLoadEvent e) {
        headDatabase = new HeadDatabaseAPI();
    }

    // Constructors for testing
    public CraftingLib() {
        super();
    }
    protected CraftingLib(JavaPluginLoader loader, PluginDescriptionFile descriptionFile, File dataFolder, File file) {
        super(loader, descriptionFile, dataFolder, file);
    }
}
