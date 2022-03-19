package de.paradubsch.paradubschmanager;

import de.paradubsch.paradubschmanager.commands.Msg;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ParadubschManager extends JavaPlugin {

	// Variables
	// Development switches
    public static final boolean debuggingEnabled = false;
    // Language
    public static final String prefix	= "[Paradubsch] ";
    public static final String prefixColor = "§8§l[§6§lParadubsch§8§l] §r";
    public static final String prefixSpaces =	"               ";
    public static final String messageFormat = "§6§l%sender% §9§l» §6§l%reciever% §9§l» §r%message%";
    public static final String messageSendMe = "Ich";
    public static final String messageRecieveMe = "Mir";

    @Override
    public void onEnable() {
    	// Enable server plugin
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("==== Paradubsch ====");
        Bukkit.getConsoleSender().sendMessage("Author: Paradubsch-Team");
        Bukkit.getConsoleSender().sendMessage("Version: PREALPHA");
        Bukkit.getConsoleSender().sendMessage("==== Paradubsch ====");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !> Initializing");
        Bukkit.getConsoleSender().sendMessage(prefix + "!>> Registering commands");
        Bukkit.getPluginCommand("msg").setExecutor(new Msg());
        Bukkit.getConsoleSender().sendMessage(prefix + "!> Initialization done");
    }

    @Override
    public void onDisable() {
    	// Disable server plugin
        Bukkit.getConsoleSender().sendMessage(prefix + "!> Disabling");
        Bukkit.getConsoleSender().sendMessage(prefix + "!>> Unregistering commands");
        Bukkit.getPluginCommand("msg").setExecutor(null);
        Bukkit.getConsoleSender().sendMessage("[Paradubsch] !> Disabled");
    }
}
