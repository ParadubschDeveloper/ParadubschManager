package de.paradubsch.paradubschmanager.commands;


import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MsgCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String arg, String[] args) {
        if(args.length >= 2) {
            String senderPlayer;
            Player recieverPlayer;
            StringBuilder message = new StringBuilder();
            if(s instanceof Player) {
                senderPlayer = s.getName();
            } else {
                senderPlayer = "§4§lServer Konsole";
            }
            recieverPlayer = Bukkit.getPlayer(args[0]);
            if(recieverPlayer == null) {
                s.sendMessage("§4§lFehler: Dieser Spieler ist nicht online.");
                return true;
            }
            for(int i = 1; i < args.length; i++) {
                message.append(args[i]).append(" ");
            }
            s.sendMessage(
                    ConfigurationManager.getString("msg.messageFormat")
                            .replaceAll("%sender%", ConfigurationManager.getString("msg.messageSendMe"))
                            .replaceAll("%reciever%", recieverPlayer.getName())
                            .replaceAll("%message%", message.toString())
            );

            recieverPlayer.sendMessage(
                    ConfigurationManager.getString("msg.messageFormat")
                            .replaceAll("%sender%", senderPlayer)
                            .replaceAll("%reciever%", ConfigurationManager.getString("msg.messageRecieveMe"))
                            .replaceAll("%message%", message.toString()));
        } else {
            s.sendMessage(ConfigurationManager.getString("msg.errorPrefix") + "§4§lFehler: Du musst einen Spielernamen und eine Nachricht eingeben.\n §4§l        Syntax: " + cmd.getUsage());
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
