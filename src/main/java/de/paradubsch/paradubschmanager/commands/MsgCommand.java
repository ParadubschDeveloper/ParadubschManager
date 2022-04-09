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
            recieverPlayer = args[0];
            recieverPlayerObject = Bukkit.getPlayer(recieverPlayer);
            if(recieverPlayerObject == null) {
                s.sendMessage("§4§lFehler: Dieser Spieler ist nicht online.");
                return true;
            }
            for(int i = 1; i < args.length; i++) {
                message += args[i] + " ";
            }
            s.sendMessage((ParadubschManager.getInstance().getConfig().getString("msg.messageFormat") + "").replaceAll("%sender%", ParadubschManager.getInstance().getConfig().getString("msg.messageSendMe") + "").replaceAll("%reciever%", recieverPlayer).replaceAll("%message%", message));
            recieverPlayerObject.sendMessage((ParadubschManager.getInstance().getConfig().getString("msg.messageFormat") + "").replaceAll("%sender%", senderPlayer).replaceAll("%reciever%", ParadubschManager.getInstance().getConfig().getString("msg.messageRecieveMe") + "").replaceAll("%message%", message));
        } else {
            s.sendMessage(ParadubschManager.getInstance().getConfig().getString("msg.errorPrefix") + "" + "§4§lFehler: Du musst einen Spielernamen und eine Nachricht eingeben.\n §4§l        Syntax: " + cmd.getUsage());
        }
        return true;
    }

}
