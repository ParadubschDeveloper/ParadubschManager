package de.paradubsch.paradubschmanager.commands;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.command.util.AsyncCommandBuilder;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;
import com.sk89q.worldedit.util.io.file.FilenameException;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.craftery.util.features.HeadDatabaseFeature;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.craftery.util.ConfigurationManager;
import de.paradubsch.paradubschmanager.gui.window.ClaimGui;
import de.paradubsch.paradubschmanager.gui.window.GsDeleteGui;
import de.paradubsch.paradubschmanager.gui.window.GsTransferGui;
import de.paradubsch.paradubschmanager.models.GsBanMember;
import de.paradubsch.paradubschmanager.models.GsWhitelistEnabled;
import de.paradubsch.paradubschmanager.models.GsWhitelistMember;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.TimeCalculations;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.logging.Level;

public class GsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;
        Player p = (Player) sender;

        if (args.length == 0) {
            claimGs(p);
            return true;
        }

        switch(args[0]) {
            case "add": {
                gsAdd(p, args);
                break;
            }
            case "remove": {
                gsRemove(p, args);
                break;
            }
            case "ban": {
                gsBan(p, args);
                break;
            }
            case "kick": {
                gsKick(p, args);
                break;
            }
            case "pardon": {
                gsPardon(p, args);
                break;
            }
            case "whitelist": {
                gsWhitelist(p, args);
                break;
            }
            case "info": {
                gsInfo(p);
                break;
            }
            case "delete": {
                gsDelete(p);
                break;
            }
            case "transfer": {
                gsTransfer(p, args);
                break;
            }
            case "backup": {
                gsBackup(p);
                break;
            }
            /*case "flags": {
                break;
            }*/
            default: {
                MessageAdapter.sendMessage(p, Message.Error.CMD_MONEY_UNKNOWN_SUBCOMMAND, args[0]);
            }
        }

        return true;
    }

    private void gsBan(Player p, String[] args) {
        if (args.length == 1) {
            MessageAdapter.sendMessage(p, Message.Error.CMD_PLAYER_NOT_PROVIDED);
            return;
        }
        List<ProtectedRegion> regions = getRegionsPlayerIsIn(p, true);
        if (regions == null) return;
        if (regions.size() != 1) {
            Bukkit.getLogger().log(Level.WARNING, "No Support for overlapping regions yet");
            MessageAdapter.sendMessage(p, Message.Error.CMD_GS_WHITELIST_OVERLAPPING_REGIONS);
            return;
        }
        ProtectedRegion protectedRegion = regions.get(0);

        PlayerData target = PlayerData.getByName(args[1]);
        if (target == null) {
            MessageAdapter.sendMessage(p, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[1]);
            return;
        }
        GsBanMember.banPlayer(protectedRegion.getId(), target.getUuid());
        Player player = Bukkit.getPlayer(target.getName());
        if (player != null) {
            WarpCommand.warp(player, "spawn");
            MessageAdapter.sendMessage(player, Message.Info.CMD_GS_BAN_BANNED);
        }
        MessageAdapter.sendMessage(p, Message.Info.CMD_GS_BAN_SUCCESS, target.getName());
    }

    private void gsPardon(Player p, String[] args) {
        if (args.length == 1) {
            MessageAdapter.sendMessage(p, Message.Error.CMD_PLAYER_NOT_PROVIDED);
            return;
        }
        List<ProtectedRegion> regions = getRegionsPlayerIsIn(p, true);
        if (regions == null) return;
        if (regions.size() != 1) {
            Bukkit.getLogger().log(Level.WARNING, "No Support for overlapping regions yet");
            MessageAdapter.sendMessage(p, Message.Error.CMD_GS_WHITELIST_OVERLAPPING_REGIONS);
            return;
        }
        ProtectedRegion protectedRegion = regions.get(0);

        PlayerData target = PlayerData.getByName(args[1]);
        if (target == null) {
            MessageAdapter.sendMessage(p, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[1]);
            return;
        }
        GsBanMember.pardonPlayer(protectedRegion.getId(), target.getUuid());

        MessageAdapter.sendMessage(p, Message.Info.CMD_GS_PARDON_SUCCESS, target.getName());
    }

    private void gsWhitelist(Player p, String[] args) {
        // /gs whitelist
        if (args.length == 1) {
            MessageAdapter.sendMessage(p, Message.Error.CMD_GS_WHITELIST_MISSING_ARGUMENT);
            return;
        }

        switch (args[1]) {
            case "add": {
                gsWhitelistAdd(p, args);
                break;
            }
            case "remove": {
                gsWhitelistRemove(p, args);
                break;
            }
            case "list": {
                gsWhitelistList(p);
                break;
            }
            case "on": {
                gsWhitelistOn(p);
                break;
            }
            case "off": {
                gsWhitelistOff(p);
                break;
            }
            default: {
                MessageAdapter.sendMessage(p, Message.Error.CMD_GS_WHITELIST_UNKNOWN_SUBCOMMAND, args[1]);
                MessageAdapter.sendMessage(p, Message.Error.CMD_GS_WHITELIST_MISSING_ARGUMENT);
            }
        }
    }

    private void gsWhitelistList(Player player) {
        List<ProtectedRegion> regions = getRegionsPlayerIsIn(player, false);
        if (regions == null) return;
        if (regions.size() != 1) {
            Bukkit.getLogger().log(Level.WARNING, "No Support for overlapping regions yet");
            MessageAdapter.sendMessage(player, Message.Error.CMD_GS_WHITELIST_OVERLAPPING_REGIONS);
            return;
        }
        ProtectedRegion protectedRegion = regions.get(0);
        List<String> members = GsWhitelistMember.getPlayers(protectedRegion.getId());
        String players = String.join(", ", members);
        MessageAdapter.sendMessage(player, Message.Info.CMD_GS_WHITELIST_LIST, players);
    }

    private void gsWhitelistAdd(Player player, String[] args) {
        // /gs whitelist add <player>
        if (args.length == 2) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_PLAYER_NOT_PROVIDED);
            return;
        }

        List<ProtectedRegion> regions = getRegionsPlayerIsIn(player, true);
        if (regions == null) return;
        if (regions.size() != 1) {
            Bukkit.getLogger().log(Level.WARNING, "No Support for overlapping regions yet");
            MessageAdapter.sendMessage(player, Message.Error.CMD_GS_WHITELIST_OVERLAPPING_REGIONS);
            return;
        }
        ProtectedRegion protectedRegion = regions.get(0);

        String uuid;
        Player target = Bukkit.getPlayer(args[2]);
        if (target != null) {
            uuid = target.getUniqueId().toString();
        } else {
            PlayerData pd = PlayerData.getByName(args[2]);
            if (pd == null) {
                MessageAdapter.sendMessage(player, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[2]);
                return;
            }
            uuid = pd.getUuid();
        }

        GsWhitelistMember.addPlayer(protectedRegion.getId(), uuid);
        MessageAdapter.sendMessage(player, Message.Info.CMD_GS_WHITELIST_ADD, args[2]);
    }

    private void gsWhitelistRemove(Player player, String[] args) {
        // /gs whitelist add <player>
        if (args.length == 2) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_PLAYER_NOT_PROVIDED);
            return;
        }

        List<ProtectedRegion> regions = getRegionsPlayerIsIn(player, true);
        if (regions == null) return;
        if (regions.size() != 1) {
            Bukkit.getLogger().log(Level.WARNING, "No Support for overlapping regions yet");
            MessageAdapter.sendMessage(player, Message.Error.CMD_GS_WHITELIST_OVERLAPPING_REGIONS);
            return;
        }
        ProtectedRegion protectedRegion = regions.get(0);

        String uuid;
        Player target = Bukkit.getPlayer(args[2]);
        if (target != null) {
            uuid = target.getUniqueId().toString();
        } else {
            PlayerData pd = PlayerData.getByName(args[2]);
            if (pd == null) {
                MessageAdapter.sendMessage(player, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[2]);
                return;
            }
            uuid = pd.getUuid();
        }

        GsWhitelistMember.removePlayer(protectedRegion.getId(), uuid);
        MessageAdapter.sendMessage(player, Message.Info.CMD_GS_WHITELIST_REMOVE, args[2]);
    }

    private void gsWhitelistOff(Player p) {
        List<ProtectedRegion> regions = getRegionsPlayerIsIn(p, true);
        if (regions == null) return;
        if (regions.size() != 1) {
            Bukkit.getLogger().log(Level.WARNING, "No Support for overlapping regions yet");
            MessageAdapter.sendMessage(p, Message.Error.CMD_GS_WHITELIST_OVERLAPPING_REGIONS);
            return;
        }
        ProtectedRegion protectedRegion = regions.get(0);

        GsWhitelistEnabled.disable(protectedRegion.getId());
        MessageAdapter.sendMessage(p, Message.Info.CMD_GS_WHITELIST_DISABLED);
    }

    private void gsWhitelistOn(Player p) {
        List<ProtectedRegion> regions = getRegionsPlayerIsIn(p, true);
        if (regions == null) return;
        if (regions.size() != 1) {
            Bukkit.getLogger().log(Level.WARNING, "No Support for overlapping regions yet");
            MessageAdapter.sendMessage(p, Message.Error.CMD_GS_WHITELIST_OVERLAPPING_REGIONS);
            return;
        }
        ProtectedRegion protectedRegion = regions.get(0);

        GsWhitelistEnabled.enable(protectedRegion.getId());
        MessageAdapter.sendMessage(p, Message.Info.CMD_GS_WHITELIST_ENABLED);
    }

    private void gsBackup(Player p) {
        if (!p.hasPermission("paradubsch.gs.backup")) {
            MessageAdapter.sendMessage(p, Message.Error.NO_PERMISSION);
            return;
        }
        Long timeout = ParadubschManager.getInstance().getGsBackupTimeouts().get(p.getUniqueId());

        if (timeout != null && timeout > System.currentTimeMillis()) {
            MessageAdapter.sendMessage(p, Message.Info.COMMAND_TIMEOUT, TimeCalculations.timeMsToExpiration(timeout - System.currentTimeMillis(), MessageAdapter.getSenderLang(p)));
            return;
        }

        List<ProtectedRegion> regions = getRegionsPlayerIsIn(p, true);
        if (regions == null) return;
        if (regions.size() != 1) {
            Bukkit.getLogger().log(Level.WARNING, "No Support for overlapping regions yet");
            MessageAdapter.sendMessage(p, Message.Error.CMD_GS_BACKUP_FAILED);
            return;
        }
        ProtectedRegion protectedRegion = regions.get(0);
        LocalSession session = ParadubschManager.getInstance().getWorldEditPlugin().getSession(p);

        Region region;
        try {
            RegionSelector selector = new CuboidRegionSelector(BukkitAdapter.adapt(p.getWorld()));
            selector.selectPrimary(protectedRegion.getMinimumPoint(), null);
            selector.selectSecondary(protectedRegion.getMaximumPoint(), null);
            session.setRegionSelector(BukkitAdapter.adapt(p.getWorld()), selector);
            region = session.getSelection();
        } catch (Exception e) {
            MessageAdapter.sendMessage(p, Message.Error.CMD_GS_BACKUP_FAILED);
            Bukkit.getLogger().log(Level.WARNING, "Region selector probably not working somehow");
            return;
        }

        ParadubschManager.getInstance().getGsBackupTimeouts().put(p.getUniqueId(), System.currentTimeMillis() + 600000L);

        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

        EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                .world(BukkitAdapter.adapt(p.getWorld()))
                .build();
        ForwardExtentCopy copy = new ForwardExtentCopy(editSession, region, region.getMinimumPoint(), clipboard, region.getMinimumPoint());
        copy.setCopyingEntities(false);
        copy.setCopyingBiomes(false);
        try {
            Operations.completeLegacy(copy);
        } catch (MaxChangedBlocksException ex) {
            MessageAdapter.sendMessage(p, Message.Error.CMD_GS_BACKUP_FAILED);
            Bukkit.getLogger().log(Level.WARNING, "Max changed blocks reached");
            return;
        }

        session.setClipboard(new ClipboardHolder(clipboard));

        File dir = WorldEdit.getInstance().getWorkingDirectoryPath("uploadSchematics").toFile();
        ClipboardFormat format = ClipboardFormats.findByAlias("sponge");
        if (format == null) {
            MessageAdapter.sendMessage(p, Message.Error.CMD_GS_BACKUP_FAILED);
            Bukkit.getLogger().warning("No ClipboardFormat found");
            return;
        }
        Actor actor = BukkitAdapter.adapt(p);

        File f;
        try {
            f = WorldEdit.getInstance().getSafeSaveFile(actor, dir, protectedRegion.getId(), format.getPrimaryFileExtension());
        } catch (FilenameException ex) {
            MessageAdapter.sendMessage(p, Message.Error.CMD_GS_BACKUP_FAILED);
            Bukkit.getLogger().warning("FilenameException");
            return;
        }

        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                MessageAdapter.sendMessage(p, Message.Error.CMD_GS_BACKUP_FAILED);
                return;
            }
        }

        SchematicSaveTask task = new SchematicSaveTask(p, f, format, new ClipboardHolder(clipboard), protectedRegion.getId());
        AsyncCommandBuilder.wrap(task, actor).buildAndExec(WorldEdit.getInstance().getExecutorService());
    }

    private static class SchematicSaveTask implements Callable<Void> {
        private final Player player;
        private final File file;
        private final ClipboardFormat format;
        private final ClipboardHolder holder;
        private final String regionName;

        SchematicSaveTask(Player player, File file, ClipboardFormat format, ClipboardHolder holder, String regionName) {
            this.player = player;
            this.file = file;
            this.format = format;
            this.holder = holder;
            this.regionName = regionName;
        }

        @Override
        public Void call() {
            Clipboard target = holder.getClipboard();

            try (Closer closer = Closer.create()) {
                FileOutputStream fos = closer.register(new FileOutputStream(file));
                BufferedOutputStream bos = closer.register(new BufferedOutputStream(fos));
                ClipboardWriter writer = closer.register(format.getWriter(bos));
                writer.write(target);

                ParadubschManager.getInstance().getGsBackupTimeouts().put(player.getUniqueId(), System.currentTimeMillis() + 86400000L);
                MessageAdapter.sendMessage(player, Message.Info.CMD_GS_BACKUP_CREATED, ConfigurationManager.getString("http.server"), regionName);
            } catch (IOException e) {
                if (!file.delete()) {
                    Bukkit.getLogger().warning("Failed to delete " + file);
                }
                MessageAdapter.sendMessage(player, Message.Error.CMD_GS_BACKUP_FAILED);
            }
            return null;
        }
    }

    private static void claimGs(Player p) {
        if (!Expect.featuresEnabled(p, HeadDatabaseFeature.class)) return;
        GuiManager.entryGui(ClaimGui.class, p);
    }

    private static @Nullable List<ProtectedRegion> getRegionsPlayerIsIn(Player p, boolean checkOwnerPermission) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        if (container == null) return null;
        RegionManager manager = container.get(BukkitAdapter.adapt(p.getWorld()));
        if (manager == null) return null;
        Location loc = p.getLocation();
        BlockVector3 vec = BlockVector3.at(loc.getX(), loc.getY(), loc.getZ());
        ApplicableRegionSet regions = manager.getApplicableRegions(vec);
        if (regions.size() == 0) {
            MessageAdapter.sendMessage(p, Message.Error.GS_IN_NO_REGION);
            return null;
        }
        List<ProtectedRegion> regionList = new ArrayList<>();
        regions.forEach(regionList::add);
        if (checkOwnerPermission && regionList.stream().noneMatch(region -> region.getOwners().contains(p.getUniqueId()))) {
            MessageAdapter.sendMessage(p, Message.Error.GS_NO_PERMISSIONS_IN_REGION);
            return null;
        }
        return regionList;
    }

    private static void gsAdd(Player p, String[] args) {
        if (!Expect.minArgs(2, args)) {
            MessageAdapter.sendMessage(p, Message.Error.GS_ADD_NAME_NOT_PROVIDED);
            return;
        }

        PlayerData pd = PlayerData.getByName(args[1]);
        if (pd == null) {
            MessageAdapter.sendMessage(p, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[1]);
            return;
        }

        List<ProtectedRegion> regionList = getRegionsPlayerIsIn(p, true);
        if (regionList == null) return;

        for (ProtectedRegion region : regionList) {
            if (!region.getOwners().contains(p.getUniqueId())) {
                continue;
            }
            DefaultDomain owners = region.getOwners();
            DefaultDomain members = region.getMembers();
            if (members.contains(UUID.fromString(pd.getUuid()))) {
                MessageAdapter.sendMessage(p, Message.Error.GS_ADD_PLAYER_ALREADY_MEMBER, pd.getName());
                return;
            }
            if (owners.contains(UUID.fromString(pd.getUuid()))) {
                MessageAdapter.sendMessage(p, Message.Error.GS_ADD_PLAYER_IS_OWNER);
                return;
            }

            OfflinePlayer ofPl = Bukkit.getOfflinePlayer(UUID.fromString(pd.getUuid()));
            members.addPlayer(ParadubschManager.getInstance().getWorldGuardPlugin().wrapOfflinePlayer(ofPl));
            region.setMembers(members);


            MessageAdapter.sendMessage(p, Message.Info.GS_ADD_ADDED_PLAYER_SUCCESSFUL, pd.getName());
        }
    }

    private static void gsRemove(Player p, String[] args) {
        if (!Expect.minArgs(2, args)) {
            MessageAdapter.sendMessage(p, Message.Error.GS_REMOVE_NAME_NOT_PROVIDED);
            return;
        }

        PlayerData pd = PlayerData.getByName(args[1]);
        if (pd == null) {
            MessageAdapter.sendMessage(p, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[1]);
            return;
        }

        List<ProtectedRegion> regionList = getRegionsPlayerIsIn(p, true);
        if (regionList == null) return;

        for (ProtectedRegion region : regionList) {
            if (!region.getOwners().contains(p.getUniqueId())) {
                continue;
            }
            DefaultDomain members = region.getMembers();
            if (!members.contains(UUID.fromString(pd.getUuid()))) {
                MessageAdapter.sendMessage(p, Message.Error.GS_REMOVE_PLAYER_NOT_MEMBER, pd.getName());
                return;
            }

            members.removePlayer(UUID.fromString(pd.getUuid()));
            region.setMembers(members);

            MessageAdapter.sendMessage(p, Message.Info.GS_REMOVE_REMOVED_PLAYER_SUCCESSFUL, pd.getName());
        }
    }

    private static void gsTransfer(Player p, String[] args) {
        if (!Expect.minArgs(2, args)) {
            MessageAdapter.sendMessage(p, Message.Error.GS_TRANSFER_NAME_NOT_PROVIDED);
            return;
        }

        PlayerData pd = PlayerData.getByName(args[1]);
        if (pd == null) {
            MessageAdapter.sendMessage(p, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[1]);
            return;
        }

        List<ProtectedRegion> regionList = getRegionsPlayerIsIn(p, true);
        if (regionList == null) return;

        for (ProtectedRegion region : regionList) {
            if (!region.getOwners().contains(p.getUniqueId())) {
                continue;
            }
            DefaultDomain owners = region.getOwners();
            if (owners.contains(UUID.fromString(pd.getUuid()))) {
                MessageAdapter.sendMessage(p, Message.Error.GS_TRANSFER_PLAYER_IS_ALREADY_OWNER, pd.getName());
                return;
            }

            if (!Expect.featuresEnabled(p, HeadDatabaseFeature.class)) return;
            GuiManager.entryGui(GsTransferGui.class, p, pd.getName(), pd, region);
        }
    }

    private static void gsDelete(Player p) {
        List<ProtectedRegion> regionList = getRegionsPlayerIsIn(p, true);
        if (regionList == null) return;

        for (ProtectedRegion region : regionList) {
            if (!region.getOwners().contains(p.getUniqueId())) {
                continue;
            }
            if (!Expect.featuresEnabled(p, HeadDatabaseFeature.class)) return;
            GuiManager.entryGui(GsDeleteGui.class, p, region);
        }

    }

    public static void gsInfo(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            List<ProtectedRegion> regionList = getRegionsPlayerIsIn(p, false);
            if (regionList == null) return;

            for (ProtectedRegion region : regionList) {
                StringBuilder owners = new StringBuilder();
                List<PlayerData> ownerPlayerDataList = new ArrayList<>();
                for (UUID uuid : region.getOwners().getUniqueIds()) {
                    PlayerData pd = PlayerData.getByUuid(uuid);
                    ownerPlayerDataList.add(pd);
                }
                for (int i = 0; i < ownerPlayerDataList.size(); i++) {
                    owners.append(ownerPlayerDataList.get(i).getName());
                    if (i != ownerPlayerDataList.size() - 1) {
                        owners.append(", ");
                    }
                }
                StringBuilder members = new StringBuilder();
                List<PlayerData> memberPlayerDataList = new ArrayList<>();
                for (UUID uuid : region.getMembers().getUniqueIds()) {
                    PlayerData pd = PlayerData.getByUuid(uuid);
                    memberPlayerDataList.add(pd);
                }
                for (int i = 0; i < memberPlayerDataList.size(); i++) {
                    members.append(memberPlayerDataList.get(i).getName());
                    if (i != memberPlayerDataList.size() - 1) {
                        members.append(", ");
                    }
                }
                MessageAdapter.sendMessage(p, Message.Info.GS_INFO_REGION_NAME, region.getId());
                MessageAdapter.sendMessage(p, Message.Info.GS_INFO_REGION_OWNERS, owners.toString());
                MessageAdapter.sendMessage(p, Message.Info.GS_INFO_REGION_MEMBERS, members.toString());
            }
        });
    }

    private static void gsKick(Player player, String[] args) {
        if (!Expect.minArgs(2, args)) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_KICK_NAME_NOT_PROVIDED);
            return;
        }
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_PLAYER_NOT_ONLINE, args[1]);
            return;
        }

        if (!player.getWorld().equals(target.getWorld())) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_KICK_PLAYER_NOT_IN_REGION, target.getName());
            return;
        }

        List<ProtectedRegion> regionList = getRegionsPlayerIsIn(player, true);
        if (regionList == null) return;
        boolean kicked = false;
        for (ProtectedRegion region : regionList) {
            Location loc = target.getLocation();
            if (region.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
                WarpCommand.warp(target, "spawn");
                MessageAdapter.sendMessage(player, Message.Info.CMD_KICK_PLAYER_KICKED, target.getName());
                MessageAdapter.sendMessage(target, Message.Error.CMD_KICK_YOU_GOT_KICKED);
                kicked = true;
                break;
            }
        }
        if (!kicked) {
            MessageAdapter.sendMessage(player, Message.Error.CMD_KICK_PLAYER_NOT_IN_REGION, target.getName());
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();

        if (args.length == 1) {
            l.add("add");
            if (sender.hasPermission("paradubsch.gs.backup")) {
                l.add("backup");
            }
            l.add("remove");
            l.add("ban");
            l.add("kick");
            l.add("pardon");
            l.add("whitelist");
            l.add("info");
            l.add("delete");
            l.add("transfer");

            //l.add("flags");
            return l;
        }
        if (args.length == 2 && args[0].equals("whitelist")) {
            l.add("on");
            l.add("off");
            l.add("add");
            l.add("remove");
            l.add("list");
            return l;
        }

        if (args.length == 3 && args[1].equals("add") && args[0].equals("whitelist")) {
            return null;
        }

        if (args.length == 2 && (args[0].equals("add") || args[0].equals("kick") || args[0].equals("ban"))) {
            return null;
        }

        return new ArrayList<>();
    }
}
