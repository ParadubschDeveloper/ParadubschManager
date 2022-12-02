package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.BanPunishment;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.models.PunishmentHolder;
import de.paradubsch.paradubschmanager.models.PunishmentUpdate;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.TimeCalculations;
import de.paradubsch.paradubschmanager.util.lang.Language;
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

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class BanCommand implements TabCompleter, CommandExecutor {
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
                editBan(sender, args);
                break;
            }
            case "delete": {
                deleteBan(sender, args);
                break;
            }
            default: banPlayer(sender, args);
        }

        return true;
    }

    private void banPlayer(CommandSender sender, String[] args) {
        //ban player duration reason
        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            PlayerData target = PlayerData.getByName(args[0]);
            if (target == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[0]);
                return;
            }

            PunishmentHolder ph = PunishmentHolder.getByPlayerDataOrCreate(target);

            if (ph.isActiveBan()) {
               MessageAdapter.sendMessage(sender, Message.Error.CMD_BAN_PLAYER_ALREADY_BANNED, args[0]);
               MessageAdapter.sendMessage(sender, Message.Info.CMD_BAN_SUGGEST_UPDATE, target.getName());
               return;
            }

            if (!Expect.minArgs(2, args)) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_BAN_DURATION_NOT_PROVIDED);
                return;
            }

            Timestamp banExpiration = TimeCalculations.parseExpiration(args[1]);
            if (banExpiration == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_BAN_DURATION_INVALID, args[1]);
                return;
            }
            Language lang = Language.getLanguageByName(target.getLanguage());
            String expirationString = TimeCalculations.timeStampToExpiration(banExpiration, lang);

            StringBuilder banReasonBuilder = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                banReasonBuilder.append(args[i]).append(" ");
            }
            String banReason = banReasonBuilder.toString().trim();

            if (banReason.isEmpty()) {
                banReason = "\"The Ban Hammer has Spoken!\"";
            }
            BanPunishment ban = new BanPunishment();
            ban.setExpiration(banExpiration);
            ban.setReason(banReason);

            if (banExpiration.getTime() > System.currentTimeMillis() + 915170400000L) {
                ban.setPermanent(true);
                ph.setPermaBanned(true);
            }

            if (sender instanceof Player) {
                PlayerData giver = PlayerData.getByPlayer((Player) sender);
                ban.setGivenBy(giver);
            }
            ban.setHolderRef(ph);

            ph.saveOrUpdate();
            long id = (long) ban.save();
            ph.setActiveBanId(id);
            ph.setActiveBan(true);
            ph.setActiveBanExpiration(banExpiration);
            ph.setActiveBanReason(banReason);
            ph.saveOrUpdate();

            Player targetPlayer = Bukkit.getPlayer(target.getName());
            if (targetPlayer != null) {
                Component msg = ParadubschManager.getInstance().getLanguageManager().get(Message.Info.CMD_BAN_KICK_MESSAGE, lang, banReason, expirationString, "#b-" + id);
                Bukkit.getScheduler().runTask(ParadubschManager.getInstance(), () -> {
                    // kicking is currently not supported by the testing environment
                    try {
                        targetPlayer.kick(msg);
                    } catch (Exception ignored) {}
                });
            }

            MessageAdapter.sendMessage(sender, Message.Info.CMD_BAN_PLAYER_BANNED, target.getName());
        });
    }

    private void deleteBan(CommandSender sender, String[] args) {
        //ban delete player reason
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

            if (!ph.isActiveBan()) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_BAN_PLAYER_NOT_BANNED, target.getName());
                return;
            }

            StringBuilder unbanReasonBuilder = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                unbanReasonBuilder.append(args[i]).append(" ");
            }
            String unbanReason = unbanReasonBuilder.toString().trim();

            if (unbanReason.isEmpty()) {
                unbanReason = "No reason given";
            }

            BanPunishment ban = BanPunishment.getByIdO(ph.getActiveBanId());
            if (ban == null) return;

            ph.setActiveBanId(0);
            ph.setActiveBanReason(null);
            ph.setActiveBanExpiration(Timestamp.valueOf(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())));
            ph.setActiveBan(false);
            ph.saveOrUpdate();

            PunishmentUpdate update = new PunishmentUpdate();
            update.setPunishmentRef(ban);
            update.setReason(unbanReason);
            update.setExpiration(Timestamp.valueOf(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())));
            if (sender instanceof Player) {
                PlayerData giver = PlayerData.getByPlayer((Player) sender);
                update.setGivenBy(giver);
            }
            update.save();

            ban.setHasUpdate(true);
            ban.saveOrUpdate();

            MessageAdapter.sendMessage(sender, Message.Info.CMD_BAN_PLAYER_UNBANNED, target.getName());
        });
    }

    private void editBan(CommandSender sender, String[] args) {
        //ban edit player expiration reason
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

            if (!ph.isActiveBan()) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_BAN_PLAYER_NOT_BANNED, target.getName());
                return;
            }

            Timestamp banExpiration = TimeCalculations.parseExpiration(args[2]);

            if (banExpiration == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_BAN_DURATION_INVALID, args[2]);
                return;
            }

            StringBuilder banReasonBuilder = new StringBuilder();
            for (int i = 3; i < args.length; i++) {
                banReasonBuilder.append(args[i]).append(" ");
            }
            String banReason = banReasonBuilder.toString().trim();

            if (banReason.isEmpty()) {
                banReason = "\"The Ban Hammer has Spoken!\"";
            }
            BanPunishment ban = BanPunishment.getByIdO(ph.getActiveBanId());
            if (ban == null) return;

            if (banExpiration.getTime() > System.currentTimeMillis() + 915170400000L) {
                ban.setPermanent(true);
                ph.setPermaBanned(true);
            } else {
                ban.setPermanent(false);
                ph.setPermaBanned(false);
            }

            ph.setActiveBanReason(banReason);
            ph.setActiveBanExpiration(banExpiration);
            ph.saveOrUpdate();

            PunishmentUpdate update = new PunishmentUpdate();
            update.setPunishmentRef(ban);
            update.setReason(banReason);
            update.setExpiration(banExpiration);
            if (sender instanceof Player) {
                PlayerData giver = PlayerData.getByPlayer((Player) sender);
                update.setGivenBy(giver);
            }
            update.save();

            ban.setHasUpdate(true);
            ban.saveOrUpdate();

            MessageAdapter.sendMessage(sender, Message.Info.CMD_BAN_EDITED, target.getName());
        });
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            Bukkit.getOnlinePlayers().forEach(x -> l.add(x.getName()));
            l.add("list");
            l.add("edit");
            l.add("delete");
            return l;
        }
        if (args.length == 2) {
            if (args[0].equals("list") || args[0].equals("edit") ||args[0].equals("delete")) {
                return null;
            }
        }
        return l;
    }
}
