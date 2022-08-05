package de.paradubsch.paradubschmanager.commands;


import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.persistance.model.PlayerData;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.Hibernate;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Language;
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

        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            Language language;
            if (s instanceof Player) {
                Player player = (Player) s;
                PlayerData playerData = Hibernate.getPlayerData(player);
                language = Language.getLanguageByShortName(playerData.getLanguage());
            } else {
                language = Language.getDefaultLanguage();
            }

            MessageAdapter.sendUnprefixedMessage(
                    s,
                    Message.Constant.MSG_TEMPLATE,
                    ParadubschManager.getInstance().getLanguageManager().getString(Message.Constant.FROM_YOU, language),
                    receiver.getName(),
                    message.toString()
            );

            Language playerLang = Language.getLanguageByShortName(Hibernate.getPlayerData(receiver).getLanguage());
            String sender;
            if (s instanceof Player) {
                sender = s.getName();
            } else {
                sender = ParadubschManager.getInstance().getLanguageManager().getString(Message.Constant.SERVER_CONSOLE, playerLang);
            }

            MessageAdapter.sendUnprefixedMessage(
                    receiver,
                    Message.Constant.MSG_TEMPLATE,
                    sender,
                    ParadubschManager.getInstance().getLanguageManager().getString(Message.Constant.TO_YOU, playerLang),
                    message.toString()
            );
        });
        return true;
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
