package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.models.SaveRequest;
import de.craftery.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SaveRequestsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<SaveRequest> requests = SaveRequest.getAll();

        if (requests.isEmpty()) {
            MessageAdapter.sendMessage(sender, Message.Info.CMD_SAVE_REQUESTS_NO_REQUESTS);
            return true;
        }
        for (SaveRequest request : requests) {
            MessageAdapter.sendMessage(sender, Message.Info.CMD_SAVE_REQUESTS_REQUEST, request.getId() + "", request.getRefName(), request.getWorld());
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
