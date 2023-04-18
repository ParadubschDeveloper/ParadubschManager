package de.paradubsch.paradubschmanager.commands;

import de.craftery.craftinglib.util.Expect;
import de.craftery.craftinglib.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InvseeCommand implements CommandExecutor, TabCompleter {
    public static final Component INVSEE_SECURED_TITLE = Component.text("Player Inventory").color(NamedTextColor.DARK_GRAY);
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;
        Player player = (Player) sender;

        if (!Expect.minArgs(1, args)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_PROVIDED);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_ONLINE, args[0]);
            return true;
        }

        if (player.hasPermission("paradubsch.invsee.modify")) {
            player.openInventory(target.getInventory());
        } else {
            Inventory inventory = Bukkit.createInventory(null, 36, INVSEE_SECURED_TITLE);
            for (int i = 0; i < 27; i++) {
                inventory.setItem(i, target.getInventory().getItem(i + 9));
            }
            for (int i = 0; i < 9; i++) {
                inventory.setItem(27 + i, target.getInventory().getItem(i));
            }
            player.openInventory(inventory);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
