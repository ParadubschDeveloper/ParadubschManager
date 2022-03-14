package js.devmsg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import js.devmsg.DevMsg;

public class MsgOptions implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String arg, String[] args) {
		if(args.length >= 1) {
			if(args[0] == "list") {
				s.sendMessage("§8§lVerfügbare experimentelle Einstellungen:\n"
							+ "§5§ldebuggingOptions      §Erlaubt Debuglogging in der Serverkonsole\n"
							+ "§5§lallowMessageConsole   §bErlaubt das schreiben an die Serverkonsole");
			} else if(args[0] == "enable") {
				if(args.length == 2) {
					if(args[1] == "debuggingOptions") {
						DevMsg.debuggingEnabled = true;
						s.sendMessage(DevMsg.prefixColor + "§bEinstellung §9§ldebuggingOptions §btemporär §a§laktiviert§b.");
					} else if(args[1] == "allowMessageConsole") {
						DevMsg.allowMessageConsole = true;
						s.sendMessage(DevMsg.prefixColor + "§bEinstellung §9§lallowMessageConsole §btemporär §a§laktiviert§b.");
					} else {
						s.sendMessage(DevMsg.prefixColor + "§4§lFehler: Du musst eine existierende Einstellung eingeben.\n" + DevMsg.prefixSpaces + "§4§l        Syntax: " + cmd.getUsage());
					}
				} else {
					s.sendMessage(DevMsg.prefixColor + "§4§lFehler: Du musst eine Einstellung eingeben.\n" + DevMsg.prefixSpaces + "§4§l        Syntax: " + cmd.getUsage());
				}
			} else if(args[0] == "disable") {
				if(args.length == 2) {
					if(args[1] == "debuggingOptions") {
						DevMsg.debuggingEnabled = false;
						s.sendMessage(DevMsg.prefixColor + "§bEinstellung §9§ldebuggingOptions §c§ldeaktiviert§b.");
					} else if(args[1] == "allowMessageConsole") {
						DevMsg.allowMessageConsole = false;
						s.sendMessage(DevMsg.prefixColor + "§bEinstellung §9§lallowMessageConsole §c§ldeaktiviert§b.");
					} else {
						s.sendMessage(DevMsg.prefixColor + "§4§lFehler: Du musst eine existierende Einstellung eingeben.\n" + DevMsg.prefixSpaces + "§4§l        Syntax: " + cmd.getUsage());
					}
				} else {
					s.sendMessage(DevMsg.prefixColor + "§4§lFehler: Du musst eine Einstellung eingeben.\n" + DevMsg.prefixSpaces + "§4§l        Syntax: " + cmd.getUsage());
				}
			} else {
				s.sendMessage(DevMsg.prefixColor + "§4§lFehler: Du musst einen existierenden Subbefehl eingeben.\n" + DevMsg.prefixSpaces + "§4§l        Syntax: " + cmd.getUsage());
			}
		} else {
			s.sendMessage(DevMsg.prefixColor + "§4§lFehler: Du musst mindestens ein Subbefehl eingeben.\n" + DevMsg.prefixSpaces + "§4§l        Syntax: " + cmd.getUsage());
		}
		return true;
	}

}
