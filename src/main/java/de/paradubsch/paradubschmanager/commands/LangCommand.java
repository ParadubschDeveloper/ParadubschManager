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
import java.util.Arrays;
import java.util.List;

public class LangCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) {
            return true;
        }
        Player player = (Player) sender;

        if (Expect.argLen(0, args)) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_LANGUAGE_NOT_PROVIDED);
            return true;
        }

        if (!Language.isLanguage(args[0])) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_LANGUAGE_NOT_FOUND, args[0]);
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            PlayerData pd = Hibernate.getPlayerData(player);
            pd.setLanguage(Language.getLanguageByName(args[0]).getShortName());
            Hibernate.save(pd);
            MessageAdapter.sendMessage(player, Message.Info.CMD_LANGUAGE_SET, args[0]);
        });
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(Arrays.asList(Language.getLanguages()));
        }
        return new ArrayList<>();
    }

}
