package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.persistance.model.Warp;
import de.paradubsch.paradubschmanager.util.Hibernate;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Language;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WarpsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            List<Warp> warps = Hibernate.getWarps();

            if (warps.size() == 0) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_WARPS_NO_WARPS);
                return;
            }

            StringBuilder warpsString = new StringBuilder();
            Language lang = MessageAdapter.getSenderLang(sender);

            for (int i = 0; i < warps.size(); i++) {
                Warp warp = warps.get(i);
                if (i != 0) warpsString.append(", ");
                String warpN = ParadubschManager.getInstance().getLanguageManager().getString(Message.Constant.CMD_WARP_TEMPLATE, lang, warp.getName());
                warpsString.append(warpN);
            }
            MessageAdapter.sendMessage(sender, Message.Info.CMD_WARPS_WARPS, warpsString.toString());
        });
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
