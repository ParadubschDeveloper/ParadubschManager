package de.craftery;

import de.craftery.command.CraftingCommand;
import de.craftery.util.CachingManager;
import de.craftery.util.lang.LanguageManager;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CraftingPlugin extends JavaPlugin {
    private final List<String> registeredCommands = new ArrayList<>();

    @Override
    public void onDisable() {
        unregisterCommands();

        super.onDisable();
    }

    public CachingManager getCachingManager() {
        return CraftingLib.getInstance().getCachingManager();
    }

    public LanguageManager getLanguageManager() {
        return CraftingLib.getInstance().getLanguageManager();
    }

    @Deprecated
    protected <T extends CommandExecutor & TabCompleter> void registerLegacyCommand(String command, T obj) {
        registeredCommands.add(command);
        PluginCommand pc = Bukkit.getPluginCommand(command);
        if (pc == null) return;
        pc.setExecutor(obj);
        pc.setTabCompleter(obj);
    }

    protected void registerCommand(CraftingCommand command) {
        PluginCommand pc = Bukkit.getPluginCommand(command.getIdentifier());
        if (pc != null) {
            registeredCommands.add(command.getIdentifier());
            pc.setExecutor(command);
            pc.setTabCompleter(command);
            registerCommodore(pc, command);
        }

        command.getOtherIdentifiers().forEach((otherIdentifier) -> {
            PluginCommand pcAlt = Bukkit.getPluginCommand(otherIdentifier);

            if (pcAlt != null) {
                registeredCommands.add(otherIdentifier);
                pcAlt.setExecutor(command);
                pcAlt.setTabCompleter(command);
                registerCommodore(pcAlt, command);
            }
        });
    }

    private void registerCommodore(PluginCommand pc, CraftingCommand command) {
        if (CommodoreProvider.isSupported() && command.registerCommandHelper() != null) {
            Commodore commodore = CommodoreProvider.getCommodore(this);
            commodore.register(pc, command.registerCommandHelper());
        }
    }

    private void unregisterCommands() {
        for (String command : registeredCommands) {
            PluginCommand pc = Bukkit.getPluginCommand(command);
            if (pc == null) continue;
            pc.setExecutor(null);
            pc.setTabCompleter(null);
        }
    }

    // Constructors for testing
    public CraftingPlugin() {
        super();
    }
    protected CraftingPlugin(JavaPluginLoader loader, PluginDescriptionFile descriptionFile, File dataFolder, File file) {
        super(loader, descriptionFile, dataFolder, file);
    }
}
