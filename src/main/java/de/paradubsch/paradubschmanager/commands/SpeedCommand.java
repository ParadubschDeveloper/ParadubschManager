package de.paradubsch.paradubschmanager.commands;

import de.craftery.util.Expect;
import de.craftery.util.MessageAdapter;
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

public class SpeedCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) {
            return true;
        }
        Player player = (Player) sender;

        if (!Expect.minArgs(1, args)) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_SPEED_NOT_PROVIDED);
            return true;
        }

        Float speed = Expect.floatRange(args[0], 1f, 10.0f);
        if (speed == null) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_SPEED_NOT_VALID, args[0]);
            return true;
        }

        float realSpeed = speed/10;

        if (realSpeed*2 > 1) {
            player.setWalkSpeed(1);
        } else {
            player.setWalkSpeed(realSpeed*2);
        }

        player.setFlySpeed(realSpeed);

        MessageAdapter.sendMessage(player, Message.Info.CMD_SPEED_SET, speed.toString());

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
