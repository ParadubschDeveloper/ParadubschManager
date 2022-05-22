package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.config.ConfigurationManager;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.Hibernate;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
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
import java.util.concurrent.CompletableFuture;

public class BuyhomeCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!Expect.playerSender(sender)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_ONLY_FOR_PLAYERS);
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            CompletableFuture.supplyAsync(() -> Hibernate.getPlayerData(player)).thenAccept(playerData -> {
                int price = ConfigurationManager.getInt("homePrice");
                MessageAdapter.sendMessage(player, Message.Info.CMD_BUYHOME, price + "");
            });

            return true;
        } else if (args[0].equals("confirm")) {
            CompletableFuture.supplyAsync(() -> Hibernate.getPlayerData(player)).thenApplyAsync(playerData -> {
                int price = ConfigurationManager.getInt("homePrice");

                if (playerData.getMoney() < price) {
                    MessageAdapter.sendMessage(player, Message.Error.CMD_BUYHOME_NOT_ENOUGH_MONEY);
                    return playerData;
                }
                playerData.setMoney(playerData.getMoney() - price);
                playerData.setMaxHomes(playerData.getMaxHomes() + 1);

                CompletableFuture.supplyAsync(() -> Hibernate.getHomes(player)).thenAccept(homes -> {
                    MessageAdapter.sendMessage(player, Message.Info.CMD_BUYHOME_SUCCESS, price + "", playerData.getMaxHomes() - homes.size() + "");
                });

                return playerData;
            }).thenAccept(Hibernate::save);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            l.add("confirm");
            return l;
        }
        return l;
    }
}
