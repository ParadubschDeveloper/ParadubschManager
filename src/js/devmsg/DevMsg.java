package js.devmsg;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DevMsg extends JavaPlugin {
	public		static	final	int		version				=	1;
	public		static			boolean	debuggingEnabled	=	false;
	public		static			boolean	allowMessageConsole	=	false;
	public		static			String	prefix				=	"";
	public		static			String	prefixColor			=	"";
	public		static			String	prefixSpaces		=	"";
	public		static			String	messageFormat		=	"";
	public		static			String	messageSendMe		=	"";
	public		static			String	messageRecieveMe	=	"";
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("==== DevelopmentMsgPlugin ====");
		Bukkit.getConsoleSender().sendMessage("Author: JeremyStar");
		Bukkit.getConsoleSender().sendMessage("Target Minecraft Version: 1.17-1.18");
		Bukkit.getConsoleSender().sendMessage("==== DevelopmentMsgPlugin ====");
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("[DevMsgPlugin] !> Initializing");
		Bukkit.getConsoleSender().sendMessage("[DevMsgPlugin] !>> Loading variables");
		debuggingEnabled	=	false;
		allowMessageConsole	=	false;
		prefix				=	"[DevMsgPlugin] ";
		prefixColor			=	"§8§l[§c§lDev§9§lMsg§6§lPlugin§8§l] §r";
		prefixSpaces		=	"               ";
		messageFormat		=	"§6§l%sender% §9§l» §6§l%reciever% §9§l» §r%message%";
		messageSendMe		=	"Ich";
		messageRecieveMe	=	"Mir";
		Bukkit.getConsoleSender().sendMessage(prefix + "!>> Registering commands");
		Bukkit.getPluginCommand("msg").setExecutor(new js.devmsg.commands.Msg());
		//Bukkit.getPluginCommand("msgoptions").setExecutor(new js.devmsg.commands.MsgOptions());
		Bukkit.getConsoleSender().sendMessage(prefix + "!> Initialization done");
	}
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(prefix + "!> Disabling");
		Bukkit.getConsoleSender().sendMessage(prefix + "!>> Unregistering commands");
		Bukkit.getPluginCommand("msg").setExecutor(null);
		//Bukkit.getPluginCommand("msgoptions").setExecutor(null);
		Bukkit.getConsoleSender().sendMessage(prefix + "!>> Removing variables");
		debuggingEnabled	=	false;
		allowMessageConsole	=	false;
		prefix				=	"";
		prefixColor			=	"";
		prefixSpaces		=	"";
		messageFormat		=	"";
		messageSendMe		=	"";
		messageRecieveMe	=	"";
		Bukkit.getConsoleSender().sendMessage("[DevMsgPlugin] !> Disabled");
	}
}
