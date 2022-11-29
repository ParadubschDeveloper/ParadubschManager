package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
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
import java.util.UUID;

public class ReplyCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;

        Player player = (Player) sender;

        UUID receiver = ParadubschManager.getInstance().getReplyCandidates().get(player.getUniqueId());

        if (receiver == null) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_REPLY_NO_REPLY_CANDIDATE);
            return true;
        }

        Player target = Bukkit.getPlayer(receiver);

        if (target == null) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_REPLY_CANDIDATE_OFFLINE);
            return true;
        }

        if (!Expect.minArgs(2, args)) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_MESSAGE_NOT_PROVIDED);
            return true;
        }

        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
        }
        MsgCommand.sendMsg(player, target, message.toString());

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
