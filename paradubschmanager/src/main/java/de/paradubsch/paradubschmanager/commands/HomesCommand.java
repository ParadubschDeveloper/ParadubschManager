package de.paradubsch.paradubschmanager.commands;

import de.craftery.craftinglib.messaging.lang.Language;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.Home;
import de.craftery.craftinglib.PlayerData;
import de.craftery.craftinglib.util.Expect;
import de.craftery.craftinglib.util.MessageAdapter;
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

public class HomesCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;
        Player player = (Player) sender;

        List<Home> homes =  Home.getByPlayer(player);
        PlayerData playerData = PlayerData.getByPlayer(player);

        if (homes.size() == 0) {
            MessageAdapter.sendMessage(player, Message.Info.CMD_HOMES_NO_HOMES);
            return true;
        }

        StringBuilder homesString = new StringBuilder();
        Language playerLang = Language.getLanguageByShortName(playerData.getLanguage());

        for (int i = 0; i < homes.size(); i++) {
            Home home = homes.get(i);
            if (i != 0) homesString.append(", ");
            String homeN = ParadubschManager.getInstance().getLanguageManager().getString(Message.Constant.CMD_HOMES_TEMPLATE, playerLang, home.getName());
            homesString.append(homeN);
        }

        MessageAdapter.sendMessage(player, Message.Info.CMD_HOMES_HOMES, homesString.toString());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
