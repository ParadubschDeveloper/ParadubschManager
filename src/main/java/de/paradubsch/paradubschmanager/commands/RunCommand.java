package de.paradubsch.paradubschmanager.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionType;
import de.craftery.util.gui.ComponentConversion;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RunCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.minArgs(1, args)) return true;

        switch (args[0]) {
            case "migrate": {
                migrate(sender, args);
                break;
            }
            case "debug": {
                debug(sender, args);
                break;
            }
        }

        return true;
    }

    private void migrate(CommandSender sender, String[] args) {
        if (!Expect.minArgs(2, args)) return;

        switch (args[1]) {
            case "worldHeightFix": {
                worldHeightFix(sender);
                break;
            }
        }
    }

    private void debug(CommandSender sender, String[] args) {
        if (!Expect.minArgs(2, args)) return;

        switch (args[1]) {
            case "getDataInCachingManager": {
                getDataInCachingManager(sender);
                break;
            }
            case "guiManagerGetGuis": {
                guiManagerGetGuis(sender);
                break;
            }
            case "guiManagerGetSessions": {
                guiManagerGetSessions(sender);
                break;
            }
            case "guiManagerGetSessionData": {
                guiManagerGetSessionData(sender);
                break;
            }
            case "guiManagerGetPrompts": {
                guiManagerGetPrompts(sender);
                break;
            }
            case "getSignMenuFactoryInputs": {
                getSignMenuFactoryInputs(sender);
                break;
            }
            case "getGuiKvStore": {
                getGuiKvStore(sender);
                break;
            }
        }
    }

    private void getGuiKvStore(CommandSender sender) {
        MessageAdapter.sendMessage(sender, Message.Constant.OBJECT_DUMP, GuiManager.getKvStores().toString());
    }

    private void getSignMenuFactoryInputs(CommandSender sender) {
        MessageAdapter.sendMessage(sender, Message.Constant.OBJECT_DUMP, GuiManager.signFactory.getInputs().toString());
    }

    private void guiManagerGetPrompts(CommandSender sender) {
        MessageAdapter.sendMessage(sender, Message.Constant.OBJECT_DUMP, GuiManager.prompts.toString());
    }

    private void guiManagerGetSessionData(CommandSender sender) {
        MessageAdapter.sendMessage(sender, Message.Constant.OBJECT_DUMP, GuiManager.getInstance().getSessionData().toString());
    }

    private void guiManagerGetSessions(CommandSender sender) {
        MessageAdapter.sendMessage(sender, Message.Constant.OBJECT_DUMP, GuiManager.getInstance().getSessions().toString());
    }

    private void guiManagerGetGuis(CommandSender sender) {
        Map<String, String> guis = new HashMap<>();
        GuiManager.getInstance().getGuis().forEach((key, value) -> guis.put(ComponentConversion.fromComponent(key), value.toString()));
        MessageAdapter.sendMessage(sender, Message.Constant.OBJECT_DUMP, guis.toString());
    }

    private void getDataInCachingManager(CommandSender sender) {
        MessageAdapter.sendMessage(sender, Message.Constant.OBJECT_DUMP, ParadubschManager.getInstance().getCachingManager().getCache().toString());
    }

    private void worldHeightFix(CommandSender sender) {
        if (!Expect.playerSender(sender)) return;
        Player player = (Player) sender;

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        if (container == null) return;
        RegionManager manager = container.get(BukkitAdapter.adapt(player.getWorld()));
        if (manager == null) return;

        AtomicInteger effected = new AtomicInteger();
        manager.getRegions().values().forEach(region -> {
            if (region.getType() == RegionType.CUBOID && region.getId().startsWith("plot_")) {
                ProtectedCuboidRegion cuboid = (ProtectedCuboidRegion) region;
                BlockVector3 min = BlockVector3.at(cuboid.getMinimumPoint().getBlockX(), -64, cuboid.getMinimumPoint().getBlockZ());
                BlockVector3 max = BlockVector3.at(cuboid.getMaximumPoint().getBlockX(), 319, cuboid.getMaximumPoint().getBlockZ());
                ProtectedCuboidRegion newRegion = new ProtectedCuboidRegion(region.getId(), min, max);
                newRegion.copyFrom(region);
                manager.removeRegion(region.getId());
                manager.addRegion(newRegion);
                effected.getAndIncrement();
            }
        });
        MessageAdapter.sendMessage(sender, Message.Info.CMD_RUN_APPLIED_FIX_WORLD_HEIGHT, effected.get() + "");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            l.add("migrate");
            l.add("debug");
            return l;
        }

        if (args.length == 2 && args[0].equals("migrate")) {
            l.add("worldHeightFix");
            return l;
        }
        if (args.length == 2 && args[0].equals("debug")) {
            l.add("getDataInCachingManager");
            l.add("guiManagerGetGuis");
            l.add("guiManagerGetSessions");
            l.add("guiManagerGetSessionData");
            l.add("guiManagerGetPrompts");
            l.add("getSignMenuFactoryInputs");
            l.add("getGuiKvStore");
            return l;
        }

        return l;
    }
}
