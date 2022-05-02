package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.Hibernate;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
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
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class MoneyCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            displayMoney(sender);
        } else {
            switch (args[0]) {
                case "pay": moneyPay(sender, args);
                case "from": moneyFrom(sender, args);
                case "set": moneySet(sender, args);
                case "add": moneyAdd(sender, args);
                case "top": moneyTop(sender);
                break;
                default: unknownSubcommand(sender, args[0]);
            }
        }
        return true;
    }

    private static void displayMoney (CommandSender sender) {
        if (!Expect.playerSender(sender)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_ONLY_FOR_PLAYERS);
            return;
        }
        Player player = (Player) sender;
        CompletableFuture.supplyAsync(() -> Hibernate.getPlayerData(player))
                .thenAccept(playerData -> {
                    String money = String.format(Locale.GERMAN, "%,d", playerData.getMoney());
                    MessageAdapter.sendMessage(player, Message.Info.CMD_MONEY_DISPLAY_SELF, money);
                });
    }

    private static void moneyPay (CommandSender sender, String[] args) {
        if (!Expect.playerSender(sender)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_ONLY_FOR_PLAYERS);
            return;
        }
        Player player = (Player) sender;

        if (!Expect.minArgs(2, args)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_PROVIDED);
            return;
        }

        if (player.getName().equalsIgnoreCase(args[1])) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_MONEY_PAY_SELF);
            return;
        }

        CompletableFuture.supplyAsync(() -> Expect.cachedPlayer(args[1])).thenAccept(isCachedPlayer -> {
            if (!isCachedPlayer) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[1]);
                return;
            }

            if (!Expect.argLen(3, args)) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_MONEY_AMOUNT_NOT_PROVIDED);
                return;
            }

            Long transferAmount = Expect.parseLong(args[2]);

            if (transferAmount == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_MONEY_AMOUNT_NOT_VALID, args[2]);
                return;
            }

            if (transferAmount <= 0) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_MONEY_AMOUNT_NOT_VALID, args[2]);
                return;
            }
            String transferAmountString = String.format(Locale.GERMAN, "%,d", transferAmount);

            CompletableFuture.supplyAsync(() -> Hibernate.getPlayerData(player)).thenAccept(playerData -> {
                if (playerData.getMoney() < transferAmount) {
                    MessageAdapter.sendMessage(sender, Message.Error.CMD_MONEY_PAY_NOT_ENOUGH_MONEY, transferAmountString);
                    return;
                }

                playerData.setMoney(playerData.getMoney() - transferAmount);


                CompletableFuture.runAsync(() -> Hibernate.save(playerData))
                        .thenApplyAsync(Void -> Hibernate.getPlayerData(args[1]))
                        .thenApply(targetPlayerData -> {
                            targetPlayerData.setMoney(targetPlayerData.getMoney() + transferAmount);
                            MessageAdapter.sendMessage(player, Message.Info.CMD_MONEY_PAYED, transferAmountString, targetPlayerData.getName());
                            Player targetPlayer = Bukkit.getPlayer(targetPlayerData.getName());
                            if (targetPlayer != null) {
                                MessageAdapter.sendMessage(targetPlayer, Message.Info.CMD_MONEY_RECEIVED, transferAmountString, player.getName());
                            }

                            return targetPlayerData;
                        })
                        .thenAccept(Hibernate::save);
            });
        });
    }

    private static void moneyFrom (CommandSender sender, String[] args) {
        if (!sender.hasPermission("paradubsch.money.from")) {
            MessageAdapter.sendMessage(sender, Message.Error.NO_PERMISSION);
            return;
        }

        if (!Expect.minArgs(2, args)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_PROVIDED);
            return;
        }

        CompletableFuture.supplyAsync(() -> Expect.cachedPlayer(args[1])).thenAccept(isCachedPlayer -> {
            if (!isCachedPlayer) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[1]);
                return;
            }

            CompletableFuture.supplyAsync(() -> Hibernate.getPlayerData(args[1])).thenAccept(targetPlayerData -> {
                String money = String.format(Locale.GERMAN, "%,d", targetPlayerData.getMoney());
                MessageAdapter.sendMessage(sender, Message.Info.CMD_MONEY_DISPLAY_OTHER, targetPlayerData.getName(), money);
            });

        });
    }

    private static void moneySet (CommandSender sender, String[] args) {
        if (!sender.hasPermission("paradubsch.money.set")) {
            MessageAdapter.sendMessage(sender, Message.Error.NO_PERMISSION);
            return;
        }

        if (!Expect.minArgs(2, args)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_PROVIDED);
            return;
        }

        CompletableFuture.supplyAsync(() -> Expect.cachedPlayer(args[1])).thenAccept(isCachedPlayer -> {
            if (!isCachedPlayer) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[1]);
                return;
            }

            if (!Expect.argLen(3, args)) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_MONEY_AMOUNT_NOT_PROVIDED);
                return;
            }

            Long transferAmount = Expect.parseLong(args[2]);

            if (transferAmount == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_MONEY_AMOUNT_NOT_VALID, args[2]);
                return;
            }

            if (transferAmount < 0) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_MONEY_AMOUNT_NOT_VALID, args[2]);
                return;
            }
            String transferAmountString = String.format(Locale.GERMAN, "%,d", transferAmount);

            CompletableFuture.supplyAsync(() -> Hibernate.getPlayerData(args[1]))
                    .thenApply(targetPlayerData -> {
                        targetPlayerData.setMoney(transferAmount);
                        MessageAdapter.sendMessage(sender, Message.Info.CMD_MONEY_SET, targetPlayerData.getName(), transferAmountString);

                        return targetPlayerData;
                    })
                    .thenAccept(Hibernate::save);
        });
    }

    private static void moneyAdd (CommandSender sender, String[] args) {
        if (!sender.hasPermission("paradubsch.money.add")) {
            MessageAdapter.sendMessage(sender, Message.Error.NO_PERMISSION);
            return;
        }

        if (!Expect.minArgs(2, args)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_PROVIDED);
            return;
        }

        CompletableFuture.supplyAsync(() -> Expect.cachedPlayer(args[1])).thenAccept(isCachedPlayer -> {
            if (!isCachedPlayer) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[1]);
                return;
            }

            if (!Expect.argLen(3, args)) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_MONEY_AMOUNT_NOT_PROVIDED);
                return;
            }

            Long transferAmount = Expect.parseLong(args[2]);

            if (transferAmount == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_MONEY_AMOUNT_NOT_VALID, args[2]);
                return;
            }

            String transferAmountString = String.format(Locale.GERMAN, "%,d", transferAmount);

            CompletableFuture.supplyAsync(() -> Hibernate.getPlayerData(args[1]))
                    .thenApply(targetPlayerData -> {
                        targetPlayerData.setMoney(targetPlayerData.getMoney() + transferAmount);
                        MessageAdapter.sendMessage(sender, Message.Info.CMD_MONEY_SET, targetPlayerData.getName(), transferAmountString);

                        return targetPlayerData;
                    })
                    .thenAccept(Hibernate::save);
        });
    }

    private static void moneyTop (CommandSender sender) {
        if (!sender.hasPermission("paradubsch.money.top")) {
            MessageAdapter.sendMessage(sender, Message.Error.NO_PERMISSION);
            return;
        }

        CompletableFuture.supplyAsync(Hibernate::getMoneyTop).thenAccept(playerDataList -> {
            if (playerDataList.isEmpty()) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_MONEY_TOP_EMPTY);
                return;
            }

            MessageAdapter.sendMessage(sender, Message.Info.CMD_MONEY_TOP_HEADER);
            Bukkit.getScheduler().runTaskLater(ParadubschManager.getInstance(), () -> {
                for (int i = 0; i < playerDataList.size(); i++) {
                    PlayerData playerData = playerDataList.get(i);
                    MessageAdapter.sendMessage(sender, Message.Info.CMD_MONEY_TOP_PLAYER, (i+1) + "", playerData.getName(), String.format(Locale.GERMAN, "%,d", playerData.getMoney()));
                }
            }, 1L);

        });
    }

    private static void unknownSubcommand (CommandSender sender, String subCommand) {
        MessageAdapter.sendMessage(sender, Message.Error.CMD_MONEY_UNKNOWN_SUBCOMMAND, subCommand);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> arglist = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("paradubsch.money.pay")) {
                arglist.add("pay");
            }
            if (sender.hasPermission("paradubsch.money.top")) {
                arglist.add("top");
            }
            if (sender.hasPermission("paradubsch.money.set")) {
                arglist.add("set");
            }
            if (sender.hasPermission("paradubsch.money.from")) {
                arglist.add("from");
            }
            if (sender.hasPermission("paradubsch.money.add")) {
                arglist.add("add");
            }
            return arglist;

        } else if (args.length >= 3) {
            return arglist;
        }
        return null;
    }
}
