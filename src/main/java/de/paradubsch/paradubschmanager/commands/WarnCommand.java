package de.paradubsch.paradubschmanager.commands;

import de.craftery.util.lang.Language;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.models.PunishmentHolder;
import de.paradubsch.paradubschmanager.models.WarnPunishment;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.kyori.adventure.text.Component;
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

public class WarnCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.minArgs(1, args)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_PROVIDED);
            return true;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);

        if (targetPlayer == null) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_ONLINE, args[0]);
            return true;
        }

        PlayerData target = PlayerData.getByPlayer(targetPlayer);
        PunishmentHolder ph = PunishmentHolder.getByPlayerDataOrCreate(target);

        StringBuilder warnReasonBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            warnReasonBuilder.append(args[i]).append(" ");
        }
        String warnReason = warnReasonBuilder.toString().trim();

        if (warnReason.isEmpty()) {
            warnReason = "&cDies ist eine Warnung! Nimm sie ernst!";
        }

        WarnPunishment warn = new WarnPunishment();
        warn.setHolderRef(ph);
        warn.setReason(warnReason);
        if (sender instanceof Player) {
            PlayerData giver = PlayerData.getByPlayer((Player) sender);
            warn.setGivenBy(giver);

        }
        long id = (long) warn.save();

        Language lang = Language.getLanguageByShortName(target.getLanguage());
        Component msg = ParadubschManager.getInstance().getLanguageManager().get(Message.Info.CMD_WARN_KICK_MESSAGE, lang, warnReason, "#w-" + id);
        Bukkit.getScheduler().runTask(ParadubschManager.getInstance(), () -> {
            // kicking is currently not supported by the testing environment
            try {
                targetPlayer.kick(msg);
            } catch (Exception ignored) {}
        });
        MessageAdapter.sendMessage(sender, Message.Info.CMD_BAN_PLAYER_WARNED, target.getName());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            return null;
        }
        return l;
    }
}
