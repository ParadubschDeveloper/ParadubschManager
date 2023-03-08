package de.paradubsch.paradubschmanager.commands;

import de.craftery.util.Expect;
import de.craftery.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HatCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!Expect.playerSender(sender)) return true;
        Player player = (Player) sender;

        ItemStack headSlot = player.getInventory().getHelmet();
        PlayerInventory inv = player.getInventory();
        inv.setHelmet(player.getInventory().getItemInMainHand());
        inv.clear(player.getInventory().getHeldItemSlot()); // I don't know if this is needed. I assume clear is not right here.
        inv.setItemInMainHand(headSlot);
        MessageAdapter.sendMessage(sender, Message.Info.CMD_HAT_USE);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
