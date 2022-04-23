package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlaytimeCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!Expect.minArgs(1, args)) {
            if (!Expect.playerSender(sender)) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_ONLY_FOR_PLAYERS);
                return true;
            }
            Player player = (Player) sender;

            long playtime = ParadubschManager.getInstance().getPlaytimeManager().getPlaytime(player);

            long day = 86400000;

            long days = TimeUnit.MILLISECONDS.toDays(playtime);

            playtime = playtime - day*days;

            long hour = 3600000;

            long hours = TimeUnit.MILLISECONDS.toHours(playtime);

            playtime = playtime - hours*hour;

            long minute = 60000;

            long minutes = TimeUnit.MILLISECONDS.toMinutes(playtime);

            playtime = playtime - minutes*minute;

            long seconds = TimeUnit.MILLISECONDS.toSeconds(playtime);

            MessageAdapter.sendMessage(player, Message.Info.CMD_YOUR_PLAYTIME, days + "", hours + "", minutes + "", seconds + "");

            return true;
        }


        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {


        return null;
    }
}
