package de.paradubsch.paradubschmanager;

import de.paradubsch.paradubschmanager.commands.Msg;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ParadubschManager extends JavaPlugin {

    public static boolean debuggingEnabled = false;
    public static boolean allowMessageConsole =	false;
    public static String prefix	= "[DevMsgPlugin] ";
    public static String prefixColor = "§8§l[§c§lDev§9§lMsg§6§lPlugin§8§l] §r";
    public static String prefixSpaces =	"               ";
    public static String messageFormat = "§6§l%sender% §9§l» §6§l%reciever% §9§l» §r%message%";
    public static String messageSendMe = "Ich";
    public static String messageRecieveMe = "Mir";

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("==== DevelopmentMsgPlugin ====");
        Bukkit.getConsoleSender().sendMessage("Author: JeremyStar");
        Bukkit.getConsoleSender().sendMessage("Target Minecraft Version: 1.17-1.18");
        Bukkit.getConsoleSender().sendMessage("==== DevelopmentMsgPlugin ====");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("[DevMsgPlugin] !> Initializing");
        Bukkit.getConsoleSender().sendMessage("[DevMsgPlugin] !>> Loading variables");

        Bukkit.getConsoleSender().sendMessage(prefix + "!>> Registering commands");
        Bukkit.getPluginCommand("msg").setExecutor(new Msg());
        //Bukkit.getPluginCommand("msgoptions").setExecutor(new js.devmsg.commands.MsgOptions());
        Bukkit.getConsoleSender().sendMessage(prefix + "!> Initialization done");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(prefix + "!> Disabling");
        Bukkit.getConsoleSender().sendMessage(prefix + "!>> Unregistering commands");
        Bukkit.getPluginCommand("msg").setExecutor(null);
        //Bukkit.getPluginCommand("msgoptions").setExecutor(null);
        Bukkit.getConsoleSender().sendMessage(prefix + "!>> Removing variables");

        Bukkit.getConsoleSender().sendMessage("[DevMsgPlugin] !> Disabled");
    }
}
