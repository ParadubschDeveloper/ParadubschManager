package de.paradubsch.paradubschmanager.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.gui.window.ClaimGui;
import de.paradubsch.paradubschmanager.models.PlayerData;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.Hibernate;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                break;
            }
            case "ban": {
                break;
            }
            case "unban": {
                break;
            }
            case "whitelist": {
                break;
            }
            case "info": {
                break;
            }
            case "delete": {
                break;
            }
            case "transfer": {
                break;
            }
            case "flags": {
                break;
            }
        }

        return true;
    }

    private static void claimGs(Player p) {
        GuiManager.entryGui(ClaimGui.class, p);
    }

    private static void gsAdd(Player p, String[] args) {
        if (!Expect.minArgs(2, args)) {
            MessageAdapter.sendMessage(p, Message.Error.GS_ADD_NAME_NOT_PROVIDED);
            return;
        }

        PlayerData pd = Hibernate.getPlayerData(args[1]);
        if (pd == null) {
            MessageAdapter.sendMessage(p, Message.Error.CMD_PLAYER_NEVER_ONLINE, args[1]);
            return;
        }

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        if (container == null) return;
        RegionManager manager = container.get(BukkitAdapter.adapt(p.getWorld()));
        if (manager == null) return;
        Location loc = p.getLocation();
        BlockVector3 vec = BlockVector3.at(loc.getX(), loc.getY(), loc.getZ());
        ApplicableRegionSet regions = manager.getApplicableRegions(vec);
        for (ProtectedRegion region : regions) {
            if (!region.getOwners().contains(p.getUniqueId())) {
                return;
            }
            DefaultDomain members = region.getMembers();
            if (members.contains(UUID.fromString(pd.getUuid()))) {
                MessageAdapter.sendMessage(p, Message.Error.GS_ADD_PLAYER_ALREADY_MEMBER, pd.getName());
                return;
            }

            OfflinePlayer ofPl = Bukkit.getOfflinePlayer(UUID.fromString(pd.getUuid()));
            members.addPlayer(ParadubschManager.getInstance().getWorldGuardPlugin().wrapOfflinePlayer(ofPl));
            region.setMembers(members);

            MessageAdapter.sendMessage(p, Message.Info.GS_ADD_ADDED_PLAYER_SUCCESSFUL, pd.getName());
        }
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
