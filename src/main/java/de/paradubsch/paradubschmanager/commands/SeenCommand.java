package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.TimeCalculations;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SeenCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.minArgs(1, args)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_PROVIDED);
            return true;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

        long lastSeen = player.getLastSeen();
        if (lastSeen == 0) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[0]);
            return true;
        }

        if (player.isOnline()) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_ONLINE, player.getName());
            return true;
        }

        String lastSeenString = TimeCalculations.timeMsToExpiration(System.currentTimeMillis() - lastSeen, MessageAdapter.getSenderLang(sender));
        MessageAdapter.sendMessage(sender, Message.Info.CMD_PLAYER_LAST_SEEN, player.getName(), lastSeenString);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return null;
        }
        return new ArrayList<>();
    }
}
