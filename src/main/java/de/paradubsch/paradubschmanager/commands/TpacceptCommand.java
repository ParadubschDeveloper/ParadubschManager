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

public class TpacceptCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;
        Player player = (Player) sender;

        if (Expect.minArgs(1, args)) {
            acceptSpecificTpa(player, args[0]);
        } else {
            acceptAllTpa(player);
        }

        return true;
    }

    private void acceptSpecificTpa(Player player, String target) {
        if (ParadubschManager.getInstance().getTpaRequests().stream().noneMatch(r ->
                r.getTarget().getName().equals(player.getName()) && r.getRequester().getName().equalsIgnoreCase(target))) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_TPA_NOT_GOT, target);
            return;
        }

        List<TpaRequest> copy = new ArrayList<>(ParadubschManager.getInstance().getTpaRequests());
        for (TpaRequest tpaRequest : copy) {
            if (!tpaRequest.getTarget().getName().equals(player.getName())) {
                continue;
            }
            if (!tpaRequest.getRequester().getName().equalsIgnoreCase(target)) {
                continue;
            }
            Player targetPlayer = Bukkit.getPlayer(target);
            if (targetPlayer == null) {
                MessageAdapter.sendMessage(player, Message.Error.CMD_PLAYER_NOT_ONLINE, target);
                return;
            }
            ParadubschManager.getInstance().getTpaRequests().remove(tpaRequest);
            targetPlayer.teleport(player);
            MessageAdapter.sendMessage(player, Message.Info.CMD_ACCEPTED_TPA, target);
            MessageAdapter.sendMessage(targetPlayer, Message.Info.CMD_ACCEPTED_TPA_RECEIVED, player.getName());
        }
    }

    private void acceptAllTpa(Player player) {
        if (ParadubschManager.getInstance().getTpaRequests().stream().noneMatch(r -> r.getTarget().getName().equals(player.getName()))) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_TPA_NO_TPA_GOT);
            return;
        }

        List<TpaRequest> copy = new ArrayList<>(ParadubschManager.getInstance().getTpaRequests());
        for (TpaRequest tpaRequest : copy) {
            if (!tpaRequest.getTarget().getName().equals(player.getName())) {
                continue;
            }

            Player targetPlayer = Bukkit.getPlayer(tpaRequest.getRequester().getName());
            if (targetPlayer == null) {
                MessageAdapter.sendMessage(player, Message.Error.CMD_PLAYER_NOT_ONLINE, tpaRequest.getRequester().getName());
                return;
            }
            ParadubschManager.getInstance().getTpaRequests().remove(tpaRequest);
            targetPlayer.teleport(player);
            MessageAdapter.sendMessage(player, Message.Info.CMD_ACCEPTED_TPA, tpaRequest.getRequester().getName());
            MessageAdapter.sendMessage(targetPlayer, Message.Info.CMD_ACCEPTED_TPA_RECEIVED, player.getName());
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
