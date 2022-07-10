package de.paradubsch.paradubschmanager.commands;

import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.BanPunishment;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.models.PunishmentHolder;
import de.paradubsch.paradubschmanager.models.PunishmentUpdate;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.Hibernate;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
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
            case "edit": {
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
            PlayerData target = Hibernate.getPlayerData(args[0]);
            if (target == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[0]);
                return;
            }

            PunishmentHolder ph = Hibernate.getPunishmentHolder(target);

            if (ph == null || ph.isActiveBan()) {
               MessageAdapter.sendMessage(sender, Message.Error.CMD_BAN_PLAYER_ALREADY_BANNED, args[0]);
               MessageAdapter.sendMessage(sender, Message.Info.CMD_BAN_SUGGEST_UPDATE, target.getName());
               return;
            }

            if (!Expect.minArgs(2, args)) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_BAN_DURATION_NOT_PROVIDED);
                return;
            }

            Timestamp banExpiration = parseExpiration(args[1]);

            if (banExpiration == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_BAN_DURATION_INVALID, args[1]);
                return;
            }

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
                PlayerData giver = Hibernate.getPlayerData((Player) sender);
                ban.setGivenBy(giver);
            }
            ban.setHolderRef(ph);

            Hibernate.save(ph);
            long id = Hibernate.saveAndReturnPunishment(ban);
            ph.setActiveBanId(id);
            ph.setActiveBan(true);
            ph.setActiveBanExpiration(banExpiration);
            ph.setActiveBanReason(banReason);
            Hibernate.save(ph);

            Player targetPlayer = Bukkit.getPlayer(target.getName());
            if (targetPlayer != null) {
                Language lang = Language.getLanguageByName(target.getLanguage());
                Component msg = ParadubschManager.getInstance().getLanguageManager().get(Message.Info.CMD_BAN_KICK_MESSAGE, lang, banReason, args[1], "#b-" + id);
                Bukkit.getScheduler().runTask(ParadubschManager.getInstance(), () -> targetPlayer.kick(msg));
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

            PlayerData target = Hibernate.getPlayerData(args[1]);
            if (target == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[0]);
                return;
            }

            PunishmentHolder ph = Hibernate.getPunishmentHolder(target);

            if (ph == null || !ph.isActiveBan()) {
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

            BanPunishment ban = Hibernate.get(BanPunishment.class, ph.getActiveBanId());
            if (ban == null) return;

            ph.setActiveBanId(0);
            ph.setActiveBanReason(null);
            ph.setActiveBanExpiration(Timestamp.valueOf(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())));
            ph.setActiveBan(false);
            Hibernate.save(ph);

            PunishmentUpdate update = new PunishmentUpdate();
            update.setPunishmentRef(ban);
            update.setReason(unbanReason);
            update.setExpiration(Timestamp.valueOf(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())));
            if (sender instanceof Player) {
                PlayerData giver = Hibernate.getPlayerData((Player) sender);
                update.setGivenBy(giver);
            }
            Hibernate.save(update);

            ban.setHasUpdate(true);
            Hibernate.save(ban);

            MessageAdapter.sendMessage(sender, Message.Info.CMD_BAN_PLAYER_UNBANNED, target.getName());
        });
    }

    private Timestamp parseExpiration(String exp) {
        if (exp.matches("^[Pp]erma(nent)?$")) {
            return Timestamp.from(Instant.ofEpochMilli(System.currentTimeMillis() + 946728000000L));
        } else if (exp.matches("^\\d+(.|,)?\\d*[ydhm]$")) {
            String[] split = exp.split("(?=[ydhm])");
            float amount = Float.parseFloat(split[0]);
            long msTimestamp;
            switch (split[1]) {
                case "y":
                    msTimestamp = (long) (amount * 365 * 24 * 60 * 60 * 1000L) + System.currentTimeMillis();
                    break;
                case "d":
                    msTimestamp = (long) (amount * 24 * 60 * 60 * 1000L) + System.currentTimeMillis();
                    break;
                case "h":
                    msTimestamp = (long) (amount * 60 * 60 * 1000L) + System.currentTimeMillis();
                    break;
                case "m":
                    msTimestamp = (long) (amount * 60 * 1000L) + System.currentTimeMillis();
                    break;
                default:
                    return null;
            }
            return Timestamp.from(Instant.ofEpochMilli(msTimestamp));
        } else {
            return null;
        }
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
