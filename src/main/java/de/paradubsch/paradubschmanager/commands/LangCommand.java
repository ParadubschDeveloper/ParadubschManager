package de.paradubsch.paradubschmanager.commands;

import de.craftery.util.lang.Language;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.craftery.util.Expect;
import de.craftery.util.MessageAdapter;
import de.craftery.util.lang.LanguageManager;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LangCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;
        Player player = (Player) sender;

        if (Expect.argLen(0, args)) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_LANGUAGE_NOT_PROVIDED);
            return true;
        }

        if (LanguageManager.getLanguages().stream().noneMatch(lang -> lang.getName().equals(args[0]))) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_LANGUAGE_NOT_FOUND, args[0]);
            return true;
        }

        PlayerData pd = PlayerData.getByPlayer(player);
        LanguageManager.getLanguages().stream()
            .filter(lang -> lang.getName().equals(args[0])).findFirst()
            .ifPresent(lang -> pd.setLanguage(lang.getShortName()));

        pd.saveOrUpdate();
        MessageAdapter.sendMessage(player, Message.Info.CMD_LANGUAGE_SET, args[0]);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return LanguageManager.getLanguages().stream().map(Language::getName).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}
