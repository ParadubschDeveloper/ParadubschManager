package de.paradubsch.paradubschmanager.commands;

import de.craftery.craftinglib.util.features.HeadDatabaseFeature;
import de.craftery.craftinglib.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.gui.window.JobGui;
import de.craftery.craftinglib.util.Expect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class JobCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;
        if (!Expect.featuresEnabled(sender, HeadDatabaseFeature.class)) return true;
        GuiManager.entryGui(JobGui.class, (Player) sender);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
