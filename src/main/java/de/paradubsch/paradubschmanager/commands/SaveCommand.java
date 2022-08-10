package de.paradubsch.paradubschmanager.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.craftery.util.gui.GuiManager;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.gui.window.SaveConfirmGui;
import de.paradubsch.paradubschmanager.models.SaveRequest;
import de.paradubsch.paradubschmanager.util.Expect;
import de.paradubsch.paradubschmanager.util.Hibernate;
import de.paradubsch.paradubschmanager.util.MessageAdapter;
import de.paradubsch.paradubschmanager.util.lang.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SaveCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!Expect.playerSender(sender)) return true;

        Player p = (Player) sender;

        if (args.length == 0) {
            saveRegion(p);
            return true;
        }
        if (args.length == 2 && args[0].equals("tp")) {
            Integer id = Expect.parseInt(args[1]);
            if (id == null) {
                MessageAdapter.sendMessage(sender, Message.Error.CMD_SAVE_TP_INVALID_ID);
                return true;
            }
            saveTp(p, id);
        }


        return true;
    }

    private static final NamespacedKey saveRequestIdKey = new NamespacedKey(ParadubschManager.getInstance(), "saveRequestId");

    private static void saveRegion(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            if (p.getInventory().getItemInMainHand().getItemMeta() == null) {
                MessageAdapter.sendMessage(p, Message.Error.SAVE_AXE_NOT_VALID);
                return;
            }
            PersistentDataContainer itemData = p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer();
            if (!itemData.has(saveRequestIdKey)) {
                MessageAdapter.sendMessage(p, Message.Error.SAVE_AXE_NOT_VALID);
                return;
            }
            Integer saveRequestId = itemData.get(saveRequestIdKey, PersistentDataType.INTEGER);
            if (saveRequestId == null) {
                MessageAdapter.sendMessage(p, Message.Error.SAVE_AXE_NOT_VALID);
                return;
            }
            SaveRequest saveRequest = Hibernate.getSaveRequest(saveRequestId);

            if (saveRequest == null) {
                MessageAdapter.sendMessage(p, Message.Error.SAVE_AXE_NOT_VALID);
                return;
            }

            Region region;
            try {
                region = ParadubschManager.getInstance().getWorldEditPlugin().getSession(p).getSelection();
            } catch (Exception e) {
                MessageAdapter.sendMessage(p, Message.Error.SAVE_REGION_NO_SELECTION);
                return;
            }

            int minx = region.getMinimumPoint().getX();
            int minz = region.getMinimumPoint().getZ();
            int maxx = region.getMaximumPoint().getX();
            int maxz = region.getMaximumPoint().getZ();

            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            if (container == null) return;
            RegionManager manager = container.get(BukkitAdapter.adapt(p.getWorld()));
            if (manager == null) return;

            int xDiff;
            if (minx > maxx) {
                xDiff = minx - maxx;
            } else {
                xDiff = maxx - minx;
            }

            int zDiff;
            if (minz > maxz) {
                zDiff = minz - maxz;
            } else {
                zDiff = maxz - minz;
            }
            xDiff++;
            zDiff++;

            String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
            String plotName = "plot_" + saveRequest.getPlayerRef().getName() + "_" + p.getName() + "_" + xDiff + "x" + zDiff + "_" + timestamp;
            ProtectedCuboidRegion region1 = new ProtectedCuboidRegion(plotName, BlockVector3.at(minx, -64, minz), BlockVector3.at(maxx, 319, maxz));
            List<ProtectedRegion> collidingRegions = region1.getIntersectingRegions(manager.getRegions().values());

            if (collidingRegions.size() > 0) {
                MessageAdapter.sendMessage(p, Message.Error.SAVE_REGION_COLLISION);
                return;
            }

            DefaultDomain owners = new DefaultDomain();
            owners.addPlayer(UUID.fromString(saveRequest.getPlayerRef().getUuid()));
            region1.setOwners(owners);

            GuiManager.entryGui(SaveConfirmGui.class, p, region1, saveRequest);
        });
    }

    private static void saveTp(Player p, Integer id) {
        Bukkit.getScheduler().runTaskAsynchronously(ParadubschManager.getInstance(), () -> {
            SaveRequest request = Hibernate.getSaveRequest(id);
            if (request == null) {
                MessageAdapter.sendMessage(p, Message.Error.CMD_SAVE_TP_INVALID_ID);
                return;
            }

            double x = request.getX() >= 0 ? request.getX() + 0.5 : request.getX() - 0.5;
            double z = request.getZ() >= 0 ? request.getZ() + 0.5 : request.getZ() - 0.5;

            World world = ParadubschManager.getInstance().getServer().getWorld(request.getWorld());
            Location loc = new Location(world, x, request.getY(), z);
            Bukkit.getScheduler().runTask(ParadubschManager.getInstance(), () -> {
                p.teleport(loc);
                p.getInventory().addItem(generateSaveAxe(p, request));
            });
        });
    }

    private static ItemStack generateSaveAxe(Player p, SaveRequest saveRequest) {
        ItemStack item = new ItemStack(Material.WOODEN_AXE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("§aSave-Axe"));

        meta.getPersistentDataContainer().set(saveRequestIdKey, PersistentDataType.INTEGER, saveRequest.getId());
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            l.add("tp");
            return l;
        }
        return l;
    }
}
