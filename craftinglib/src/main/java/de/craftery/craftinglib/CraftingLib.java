package de.craftery.craftinglib;

import de.craftery.craftinglib.util.CachingManager;
import de.craftery.craftinglib.util.TestDatabaseConnection;
import de.craftery.craftinglib.util.collectables.Collectable;
import de.craftery.craftinglib.util.collectables.CollectedCollectable;
import de.craftery.craftinglib.util.gui.GuiManager;
import de.craftery.craftinglib.util.ConfigurationManager;
import de.craftery.craftinglib.util.HibernateConfigurator;
import de.craftery.craftinglib.messaging.lang.LanguageManager;
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
import java.util.ArrayList;
import java.util.List;

public class CraftingLib extends JavaPlugin implements Listener {
    @Getter
    private static CraftingLib instance;

    @Getter
    private HeadDatabaseAPI headDatabase;

    @Getter
    private CachingManager cachingManager;

    @Getter
    private LanguageManager languageManager;

    @Getter
    private static final List<String> registeredCollectableTypes = new ArrayList<>();

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
        HibernateConfigurator.addEntity(PlayerData.class);
        HibernateConfigurator.addEntity(Collectable.class);
        HibernateConfigurator.addEntity(CollectedCollectable.class);
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

    public static void registerCollectableType(String type) {
        registeredCollectableTypes.add(type);
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
