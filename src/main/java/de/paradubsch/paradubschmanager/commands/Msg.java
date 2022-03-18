package de.paradubsch.paradubschmanager.commands;


import de.paradubsch.paradubschmanager.ParadubschManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Msg implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String arg, String[] args) {
        if(args.length >= 2) {
            String senderPlayer = "";
            String recieverPlayer = "";
            Player recieverPlayerObject = null;
            String message = "";
            if(s instanceof Player) {
                senderPlayer = s.getName();
            } else {
                senderPlayer = "§4§lServer Konsole";
            }
            if(args[0] == "MinecraftServerKonsole") {
                if(ParadubschManager.allowMessageConsole) {
                    recieverPlayer = "§4§lServer Konsole";
                    recieverPlayerObject = null;
                } else {
                    s.sendMessage("§4§lFehler: Dieser Spieler ist nicht online.");
                    return true;
                }
            } else {
                recieverPlayer = args[0];
                recieverPlayerObject = Bukkit.getPlayer(recieverPlayer);
                if(recieverPlayerObject == null) {
                    s.sendMessage("§4§lFehler: Dieser Spieler ist nicht online.");
                    return true;
                }
            }
            for(int i = 1; i < args.length; i++) {
                message = message + args[i] + " ";
            }
            s.sendMessage(ParadubschManager.messageFormat.replaceAll("%sender%", ParadubschManager.messageSendMe).replaceAll("%reciever%", recieverPlayer).replaceAll("%message%", message));
            if(recieverPlayerObject == null) {
                Bukkit.getConsoleSender().sendMessage(ParadubschManager.messageFormat.replaceAll("%sender%", senderPlayer).replaceAll("%reciever%", ParadubschManager.messageRecieveMe).replaceAll("%message%", message));
            } else {
                recieverPlayerObject.sendMessage(ParadubschManager.messageFormat.replaceAll("%sender%", senderPlayer).replaceAll("%reciever%", ParadubschManager.messageRecieveMe).replaceAll("%message%", message));
            }
        } else {
            s.sendMessage(ParadubschManager.prefixColor + "§4§lFehler: Du musst einen Spielernamen und eine Nachricht eingeben.\n" + ParadubschManager.prefixSpaces + "§4§l        Syntax: " + cmd.getUsage());
        }
        return true;
    }

}
