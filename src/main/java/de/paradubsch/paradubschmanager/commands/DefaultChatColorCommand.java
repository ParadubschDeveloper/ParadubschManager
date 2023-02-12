package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DefaultChatColorCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.minArgs(1, args)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_PROVIDED);
            return true;
        }

        if (!Expect.playerString(args[0])) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_RECEIVER_NOT_PLAYER, args[0]);
            return true;
        }

        if (!Expect.argLen(2, args)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_COLOR_NOT_PROVIDED);
            return true;
        }

        if (!Expect.colorCode(args[1])) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_COLOR_NOT_PROVIDED);
            return true;
        }

        PlayerData pd = PlayerData.getByName(args[0]);
        if (pd == null) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[0]);
            return true;
        }
        pd.setDefaultChatColor(args[1]);
        pd.saveOrUpdate();
        MessageAdapter.sendMessage(sender, Message.Info.CMD_DEFAULT_CHAT_COLOR_SET, pd.getName(), args[1], pd.getName());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return null;
        }
        return new ArrayList<>();
    }
}
