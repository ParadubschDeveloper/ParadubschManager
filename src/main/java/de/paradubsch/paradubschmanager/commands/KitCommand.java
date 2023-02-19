package de.paradubsch.paradubschmanager.commands;

import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.gui.window.KitEditGui;
import de.paradubsch.paradubschmanager.gui.window.KitGui;
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

import java.util.ArrayList;
import java.util.List;

public class KitCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;
        Player player = (Player) sender;

        if (args.length == 0) {
            GuiManager.entryGui(KitGui.class, player);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("edit") && sender.hasPermission("paradubsch.kit.edit")) {
            Integer kitId = Expect.parseInt(args[1]);
            if (kitId == null || kitId < 1 || kitId > 5) {
                MessageAdapter.sendMessage(player, Message.Error.INVALID_KIT_ID, args[1]);
                return true;
            }
            GuiManager.entryGui(KitEditGui.class, player, kitId);
        } else {
            MessageAdapter.sendMessage(player, Message.Error.INVALID_KIT_SYNTAX);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1 && sender.hasPermission("paradubsch.kit.edit")) {
            l.add("edit");
        }
        if (args.length == 2 && sender.hasPermission("paradubsch.kit.edit") && args[0].equalsIgnoreCase("edit")) {
            l.add("1");
            l.add("2");
            l.add("3");
            l.add("4");
            l.add("5");
        }
        return l;
    }
}
