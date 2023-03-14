package de.paradubsch.paradubschmanager.commands;

import de.craftery.PlayerData;
import de.craftery.util.lang.Language;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.*;
import de.craftery.util.Expect;
import de.craftery.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.TimeCalculations;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class MuteCommand implements TabCompleter, CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.minArgs(1, args)) {
            MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_PROVIDED);
            return true;
        }

        switch (args[0]) {
            case "list": {
                break;
            }
            case "update":
            case "edit": {
                editMute(sender, args);
                break;
            }
            case "delete": {
                deleteMute(sender, args);
                break;
            }
            default: mutePlayer(sender, args);
        }

        return true;
    }

    private void mutePlayer(CommandSender sender, String[] args) {
        //mute player duration reason
        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            PlayerData target = PlayerData.getByName(args[0]);
            if (target == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[0]);
                return;
            }

            PunishmentHolder ph = PunishmentHolder.getByPlayerDataOrCreate(target);

            if (ph.isActiveMute()) {
               MessageAdapter.sendMessage(sender, Message.Error.CMD_MUTE_PLAYER_ALREADY_MUTED, args[0]);
               MessageAdapter.sendMessage(sender, Message.Info.CMD_MUTE_SUGGEST_UPDATE, target.getName());
               return;
            }

            if (!Expect.minArgs(2, args)) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_MUTE_DURATION_NOT_PROVIDED);
                return;
            }

            Timestamp muteExpiration = TimeCalculations.parseExpiration(args[1]);
            if (muteExpiration == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_MUTE_DURATION_INVALID, args[1]);
                return;
            }
            Language lang = Language.getLanguageByShortName(target.getLanguage());
            String expirationString = TimeCalculations.timeStampToExpiration(muteExpiration, lang);

            StringBuilder muteReasonBuilder = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                muteReasonBuilder.append(args[i]).append(" ");
            }
            String muteReason = muteReasonBuilder.toString().trim();

            if (muteReason.isEmpty()) {
                muteReason = "You have been Muted!";
            }
            MutePunishment mute = new MutePunishment();
            mute.setExpiration(muteExpiration);
            mute.setReason(muteReason);

            if (muteExpiration.getTime() > System.currentTimeMillis() + 852055200000L) {
                mute.setPermanent(true);
                ph.setPermaMuted(true);
            }

            if (sender instanceof Player) {
                mute.setGivenBy(((Player) sender).getUniqueId().toString());
            }
            mute.setHolderRef(target.getUuid());

            ph.saveOrUpdate();
            long id = (long) mute.save();
            ph.setActiveMuteId(id);
            ph.setActiveMute(true);
            ph.setActiveMuteExpiration(muteExpiration);
            ph.setActiveMuteReason(muteReason);
            ph.saveOrUpdate();

            //TODO: Send Message to player, that he has been muted
            //NOTE: expirationString
            Player targetPlayer = Bukkit.getPlayer(target.getName());
            if (targetPlayer != null) {
                MessageAdapter.sendMessage(targetPlayer, Message.Info.CMD_MUTE_GOT_MUTED_HEADER, expirationString);
                MessageAdapter.sendMessage(targetPlayer, Message.Info.CMD_MUTE_GOT_MUTED_BODY, muteReason);
            }

            MessageAdapter.sendMessage(sender, Message.Info.CMD_MUTE_PLAYER_MUTED, target.getName());
        });
    }

    private void deleteMute(CommandSender sender, String[] args) {
        //mute delete player reason
        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            if (!Expect.minArgs(2, args)) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_PROVIDED);
                return;
            }

            PlayerData target = PlayerData.getByName(args[1]);
            if (target == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[0]);
                return;
            }

            PunishmentHolder ph = PunishmentHolder.getByPlayerDataOrCreate(target);

            if (!ph.isActiveMute()) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_MUTE_PLAYER_NOT_MUTED, target.getName());
                return;
            }

            StringBuilder unmuteReasonBuilder = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                unmuteReasonBuilder.append(args[i]).append(" ");
            }
            String unmuteReason = unmuteReasonBuilder.toString().trim();

            if (unmuteReason.isEmpty()) {
                unmuteReason = "No reason given";
            }

            MutePunishment mute = MutePunishment.getByIdO(ph.getActiveMuteId());
            if (mute == null) return;

            ph.setActiveMuteId(0);
            ph.setActiveMuteReason(null);
            ph.setActiveMuteExpiration(Timestamp.valueOf(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())));
            ph.setActiveMute(false);
            ph.saveOrUpdate();

            PunishmentUpdate update = new PunishmentUpdate();
            update.setPunishmentRef(mute.getId());
            update.setReason(unmuteReason);
            update.setExpiration(Timestamp.valueOf(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())));
            if (sender instanceof Player) {
                update.setGivenBy(((Player) sender).getUniqueId().toString());
            }
            update.save();

            mute.setHasUpdate(true);
            mute.saveOrUpdate();

            MessageAdapter.sendMessage(sender, Message.Info.CMD_MUTE_PLAYER_UNMUTED, target.getName());
        });
    }

    private void editMute(CommandSender sender, String[] args) {
        //mute edit player expiration reason
        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            if (!Expect.minArgs(2, args)) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NOT_PROVIDED);
                return;
            }

            PlayerData target = PlayerData.getByName(args[1]);
            if (target == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[0]);
                return;
            }

            PunishmentHolder ph = PunishmentHolder.getByPlayerDataOrCreate(target);

            if (!ph.isActiveMute()) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_MUTE_PLAYER_NOT_MUTED, target.getName());
                return;
            }

            Timestamp muteExpiration = TimeCalculations.parseExpiration(args[2]);

            if (muteExpiration == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_MUTE_DURATION_INVALID, args[2]);
                return;
            }

            StringBuilder muteReasonBuilder = new StringBuilder();
            for (int i = 3; i < args.length; i++) {
                muteReasonBuilder.append(args[i]).append(" ");
            }
            String muteReason = muteReasonBuilder.toString().trim();

            if (muteReason.isEmpty()) {
                muteReason = "You have been Muted!";
            }
            MutePunishment mute = MutePunishment.getByIdO(ph.getActiveMuteId());
            if (mute == null) return;

            if (muteExpiration.getTime() > System.currentTimeMillis() + 852055200000L) {
                mute.setPermanent(true);
                ph.setPermaMuted(true);
            } else {
                mute.setPermanent(false);
                ph.setPermaMuted(false);
            }

            ph.setActiveMuteReason(muteReason);
            ph.setActiveMuteExpiration(muteExpiration);
            ph.saveOrUpdate();

            PunishmentUpdate update = new PunishmentUpdate();
            update.setPunishmentRef(mute.getId());
            update.setReason(muteReason);
            update.setExpiration(muteExpiration);
            if (sender instanceof Player) {
                update.setGivenBy(((Player) sender).getUniqueId().toString());
            }
            update.save();

            mute.setHasUpdate(true);
            mute.saveOrUpdate();

            MessageAdapter.sendMessage(sender, Message.Info.CMD_MUTE_EDITED, target.getName());
        });
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            Bukkit.getOnlinePlayers().forEach(x -> l.add(x.getName()));
            //l.add("list");
            l.add("edit");
            l.add("delete");
            return l;
        }
        if (args.length == 2) {
            if (/*args[0].equals("list") ||*/ args[0].equals("edit") || args[0].equals("delete")) {
                return null;
            }
        }
        return l;
    }
}
