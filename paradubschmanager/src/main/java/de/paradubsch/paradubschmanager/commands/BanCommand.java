package de.paradubsch.paradubschmanager.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.craftery.command.ArgType;
import de.craftery.command.CheckArg;
import de.craftery.command.CraftCommand;
import de.craftery.command.CraftPlayer;
import de.craftery.util.lang.Language;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.BanPunishment;
import de.craftery.PlayerData;
import de.paradubsch.paradubschmanager.models.PunishmentHolder;
import de.paradubsch.paradubschmanager.models.PunishmentUpdate;
import de.craftery.util.Expect;
import de.paradubsch.paradubschmanager.util.TimeCalculations;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class BanCommand extends CraftCommand {
    public BanCommand() {
        super("Ban Command");
        this.setIdentifier("cb");
    }

    @Override
    @CheckArg(index = 0, type = ArgType.PLAYER_NAME, checkType = false)
    public boolean execute(CraftPlayer player, String[] args) {
        switch (args[0]) {
            /* #TODO: Implement list command
            case "list": {
                break;
            }*/
            case "update":
            case "edit": {
                editBan(player, args);
                break;
            }
            case "delete": {
                deleteBan(player, args);
                break;
            }
            default: banPlayer(player, args);
        }

        return true;
    }

    private void banPlayer(CraftPlayer sender, String[] args) {
        //ban player duration reason
        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            PlayerData target = PlayerData.getByName(args[0]);
            if (target == null) {
                sender.sendMessage(Message.Error.CMD_PLAYER_NEVER_ONLINE, args[0]);
                return;
            }

            PunishmentHolder ph = PunishmentHolder.getByPlayerDataOrCreate(target);

            if (ph.isActiveBan()) {
               sender.sendMessage(Message.Error.CMD_BAN_PLAYER_ALREADY_BANNED, args[0]);
               sender.sendMessage(Message.Info.CMD_BAN_SUGGEST_UPDATE, target.getName());
               return;
            }

            if (!Expect.minArgs(2, args)) {
                sender.sendMessage(Message.Error.CMD_BAN_DURATION_NOT_PROVIDED);
                return;
            }

            Timestamp banExpiration = TimeCalculations.parseExpiration(args[1]);
            if (banExpiration == null) {
                sender.sendMessage(Message.Error.CMD_BAN_DURATION_INVALID, args[1]);
                return;
            }
            Language lang = Language.getLanguageByShortName(target.getLanguage());
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

            if (sender.isPlayer()) {
                ban.setGivenBy(sender.getPlayer().getUniqueId().toString());
            }
            ban.setHolderRef(target.getUuid());

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

            sender.sendMessage(Message.Info.CMD_BAN_PLAYER_BANNED, target.getName());
        });
    }

    private void deleteBan(CraftPlayer sender, String[] args) {
        //ban delete player reason
        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            if (!Expect.minArgs(2, args)) {
                sender.sendMessage(Message.Error.CMD_PLAYER_NOT_PROVIDED);
                return;
            }

            PlayerData target = PlayerData.getByName(args[1]);
            if (target == null) {
                sender.sendMessage(Message.Error.CMD_PLAYER_NEVER_ONLINE, args[0]);
                return;
            }

            PunishmentHolder ph = PunishmentHolder.getByPlayerDataOrCreate(target);

            if (!ph.isActiveBan()) {
                sender.sendMessage(Message.Error.CMD_BAN_PLAYER_NOT_BANNED, target.getName());
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
            update.setPunishmentRef(ban.getId());
            update.setReason(unbanReason);
            update.setExpiration(Timestamp.valueOf(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())));
            if (sender.isPlayer()) {
                update.setGivenBy(sender.getPlayer().getUniqueId().toString());
            }
            update.save();

            ban.setHasUpdate(true);
            ban.saveOrUpdate();

            sender.sendMessage(Message.Info.CMD_BAN_PLAYER_UNBANNED, target.getName());
        });
    }

    private void editBan(CraftPlayer sender, String[] args) {
        //ban edit player expiration reason
        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            if (!Expect.minArgs(2, args)) {
                sender.sendMessage(Message.Error.CMD_PLAYER_NOT_PROVIDED);
                return;
            }

            PlayerData target = PlayerData.getByName(args[1]);
            if (target == null) {
                sender.sendMessage(Message.Error.CMD_PLAYER_NEVER_ONLINE, args[0]);
                return;
            }

            PunishmentHolder ph = PunishmentHolder.getByPlayerDataOrCreate(target);

            if (!ph.isActiveBan()) {
                sender.sendMessage(Message.Error.CMD_BAN_PLAYER_NOT_BANNED, target.getName());
                return;
            }

            Timestamp banExpiration = TimeCalculations.parseExpiration(args[2]);

            if (banExpiration == null) {
                sender.sendMessage(Message.Error.CMD_BAN_DURATION_INVALID, args[2]);
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
            update.setPunishmentRef(ban.getId());
            update.setReason(banReason);
            update.setExpiration(banExpiration);
            if (sender.isPlayer()) {
                update.setGivenBy(sender.getPlayer().getUniqueId().toString());
            }
            update.save();

            ban.setHasUpdate(true);
            ban.saveOrUpdate();

            sender.sendMessage(Message.Info.CMD_BAN_EDITED, target.getName());
        });
    }

    @Override
    public List<String> tabComplete(CraftPlayer player, String[] args) {
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

    @Override
    public @Nullable LiteralCommandNode<?> registerCommandHelper() {
        return LiteralArgumentBuilder.literal("cb")
                .then(LiteralArgumentBuilder.literal("edit")
                        .then(RequiredArgumentBuilder.argument("player", StringArgumentType.word())
                                .then(RequiredArgumentBuilder.argument("duration", StringArgumentType.word())
                                        .then(RequiredArgumentBuilder.argument("reason", StringArgumentType.greedyString()))
                                )
                        )
                )
                .then(LiteralArgumentBuilder.literal("delete")
                        .then(RequiredArgumentBuilder.argument("player", StringArgumentType.word())
                                .then(RequiredArgumentBuilder.argument("reason", StringArgumentType.greedyString()))
                        )
                )
                .then(RequiredArgumentBuilder.argument("player", StringArgumentType.word())
                        .then(RequiredArgumentBuilder.argument("duration", StringArgumentType.word())
                                .then(RequiredArgumentBuilder.argument("reason", StringArgumentType.greedyString()))
                        )
                )
                .build();
    }
}
