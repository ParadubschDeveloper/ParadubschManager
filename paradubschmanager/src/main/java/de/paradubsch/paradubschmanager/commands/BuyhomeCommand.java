package de.paradubsch.paradubschmanager.commands;

import de.craftery.craftinglib.util.ConfigurationManager;
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

public class BuyhomeCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!Expect.playerSender(sender)) return true;

        Player player = (Player) sender;
        PlayerData playerData = PlayerData.getByPlayer(player);

        int price = ConfigurationManager.getInt("homePrice") * playerData.getMaxHomes();

        if (args.length == 0) {
            MessageAdapter.sendMessage(player, Message.Info.CMD_BUYHOME, price + "");
        } else if (args[0].equals("confirm")) {
            if (playerData.getMoney() < price) {
                MessageAdapter.sendMessage(player, Message.Error.CMD_BUYHOME_NOT_ENOUGH_MONEY);
                return true;
            }
            
            playerData.setMoney(playerData.getMoney() - price);
            playerData.setMaxHomes(playerData.getMaxHomes() + 1);

            List<Home> homes = Home.getByPlayer(player);
            MessageAdapter.sendMessage(player, Message.Info.CMD_BUYHOME_SUCCESS, price + "", playerData.getMaxHomes() - homes.size() + "");

            playerData.saveOrUpdate();
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            l.add("confirm");
        }
        return l;
    }
}
