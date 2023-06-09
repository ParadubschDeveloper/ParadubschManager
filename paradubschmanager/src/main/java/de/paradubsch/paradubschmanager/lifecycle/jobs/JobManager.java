package de.paradubsch.paradubschmanager.lifecycle.jobs;

import com.jeff_media.customblockdata.CustomBlockData;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.WorkerPlayer;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JobManager implements Listener {
    @Getter
    private final Map<UUID, Integer> progressPartPercentage = new HashMap<>();

    public JobManager() {
        ParadubschManager.getInstance().getServer().getPluginManager().registerEvents(this, ParadubschManager.getInstance());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        WorkerPlayer worker = WorkerPlayer.getById(event.getPlayer().getUniqueId().toString());
        if (worker == null) {
            return;
        }
        if (worker.getJob() == JobType.MINER) {
            MiningJob.onBlockBreak(worker, event);
        }
        if (worker.getJob() == JobType.LUMBERJACK) {
            LumberjackJob.onBlockBreak(worker, event);
        }
        if (worker.getJob() == JobType.COLLECTOR) {
            CollectorJob.onBlockBreak(worker, event);
        }
        if (worker.getJob() == JobType.FARMER) {
            FarmerJob.onBlockBreak(worker, event);
        }
    }

    public static final NamespacedKey NOT_ORIGINAL = new NamespacedKey(ParadubschManager.getInstance(), "notOriginal");

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        PersistentDataContainer customBlockData = new CustomBlockData(event.getBlockPlaced(), ParadubschManager.getInstance());
        customBlockData.set(NOT_ORIGINAL, PersistentDataType.BYTE, (byte) 1);

        switch (event.getBlock().getType()) {
            case SUNFLOWER:
            case ROSE_BUSH:
            case PEONY:
            case LILAC: {
                Location loc = event.getBlock().getLocation();
                loc.add(0, 1, 0);
                Block above = event.getBlock().getWorld().getBlockAt(loc);
                if (above.getType() == event.getBlock().getType()) {
                    PersistentDataContainer customBlockData2 = new CustomBlockData(above, ParadubschManager.getInstance());
                    customBlockData2.set(NOT_ORIGINAL, PersistentDataType.BYTE, (byte) 1);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if(entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) entity.getLastDamageCause();
            if(damageEvent.getDamager() instanceof Player) {
                WorkerPlayer worker = WorkerPlayer.getById(damageEvent.getDamager().getUniqueId().toString());
                if (worker == null) {
                    return;
                }
                if (worker.getJob() == JobType.HUNTER) {
                    HunterJob.onKill(worker, entity, (Player) damageEvent.getDamager());
                }
            }
        }
    }

    public static void sendActionBar(Player player, String message) {
        player.sendActionBar(Component.text(message));
    }

    public static String getPercentageBar(int percentage) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 1; i <= 10; i++) {
            if (i * 10 <= percentage) {
                sb.append("█");
            } else {
                sb.append("-");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
