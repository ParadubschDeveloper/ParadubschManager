package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.lifecycle.TpaRequest;
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

public class TpaCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;
        Player player = (Player) sender;

        if (!Expect.minArgs(1, args)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_PROVIDED);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_ONLINE, args[0]);
            return true;
        }

        if (target.getName().equals(player.getName())) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_TPA_SELF);
            return true;
        }

        TpaRequest tpaRequest = new TpaRequest(player, target);

        if (ParadubschManager.getInstance().getTpaRequests().contains(tpaRequest)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_TPA_ALREADY_SENT, target.getName());
            return true;
        }

        ParadubschManager.getInstance().getTpaRequests().add(tpaRequest);
        MessageAdapter.sendMessage(sender, Message.Info.CMD_TPA_SENT, target.getName());
        MessageAdapter.sendMessage(target, Message.Info.CMD_TPA_RECEIVED, player.getName());

        Bukkit.getScheduler().runTaskLater(ParadubschManager.getInstance(), () -> {
            if (ParadubschManager.getInstance().getTpaRequests().contains(tpaRequest)) {
                ParadubschManager.getInstance().getTpaRequests().remove(tpaRequest);
                MessageAdapter.sendMessage(player, Message.Error.CMD_TPA_TIMEOUT, target.getName());
            }
        }, 20 * 60 * 2);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
