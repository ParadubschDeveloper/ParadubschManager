package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.Home;
import de.paradubsch.paradubschmanager.models.PlayerData;
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

public class HomesCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_ONLY_FOR_PLAYERS);
            return true;
        }
        Player player = (Player) sender;

        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            List<Home> homes =  Hibernate.getHomes(player);
            PlayerData playerData = PlayerData.getByPlayer(player);

            if (homes.size() == 0) {
                MessageAdapter.sendMessage(player, Message.Info.CMD_HOMES_NO_HOMES);
                return;
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
        });
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
