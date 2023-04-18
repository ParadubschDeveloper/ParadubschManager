package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.craftery.craftinglib.util.Expect;
import de.craftery.craftinglib.util.MessageAdapter;
import de.craftery.craftinglib.messaging.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MsgCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String arg, String[] args) {
        if (!Expect.minArgs(1, args)) {
            MessageAdapter.sendMessage(s, Message.Error.CMD_RECEIVER_NOT_PROVIDED);
            return true;
        }

        if (!Expect.playerString(args[0])) {
            MessageAdapter.sendMessage(s, Message.Error.CMD_RECEIVER_NOT_PLAYER, args[0]);
            return true;
        }

        if (!Expect.minArgs(2, args)) {
            MessageAdapter.sendMessage(s, Message.Error.CMD_MESSAGE_NOT_PROVIDED);
            return true;
        }

        Player receiver = Bukkit.getPlayer(args[0]);

        if (receiver == null) {
            MessageAdapter.sendMessage(s, Message.Error.CMD_RECEIVER_NOT_ONLINE, args[0]);
            return true;
        }

        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }
        sendMsg(s, receiver, message.toString());
        return true;
    }

    public static void sendMsg(CommandSender s, Player receiver, String message) {
        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            Language language = MessageAdapter.getSenderLang(s);

            MessageAdapter.sendUnprefixedMessage(
                    s,
                    Message.Constant.MSG_TEMPLATE,
                    ParadubschManager.getInstance().getLanguageManager().getString(Message.Constant.FROM_YOU, language),
                    receiver.getName(),
                    message
            );

            Language playerLang = MessageAdapter.getSenderLang(receiver);
            String sender;
            if (s instanceof Player) {
                Player player = (Player) s;
                sender = player.getName();
                ParadubschManager.getInstance().getReplyCandidates().put(receiver.getUniqueId(), player.getUniqueId());
                ParadubschManager.getInstance().getReplyCandidates().put(player.getUniqueId(), receiver.getUniqueId());
            } else {
                sender = ParadubschManager.getInstance().getLanguageManager().getString(Message.Constant.SERVER_CONSOLE, playerLang);
            }

            MessageAdapter.sendUnprefixedMessage(
                    receiver,
                    Message.Constant.MSG_TEMPLATE,
                    sender,
                    ParadubschManager.getInstance().getLanguageManager().getString(Message.Constant.TO_YOU, playerLang),
                    message
            );
        });
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return null;
        } else {
            return new ArrayList<>();
        }
    }
}
