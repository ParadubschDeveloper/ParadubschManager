package de.paradubsch.paradubschmanager.commands;

import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.gui.window.ClaimGui;
import de.paradubsch.paradubschmanager.util.Expect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;
        Player p = (Player) sender;

        if (args.length == 0) {
            claimGs(p);
            return true;
        }

        return true;
    }

    private static void claimGs(Player p) {
        GuiManager.entryGui(ClaimGui.class, p);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();

        if (args.length == 1) {
            l.add("add");
            l.add("remove");
            l.add("ban");
            l.add("unban");
            l.add("whitelist");
            l.add("info");
            l.add("delete");
            l.add("transfer");

            l.add("flags");
            return l;
        }
        if (args.length == 2 && args[0].equals("whitelist")) {
            l.add("on");
            l.add("off");
            return l;
        }

        if (args.length == 2 && args[0].equals("add")) {
            return null;
        }

        return new ArrayList<>();
    }
}
