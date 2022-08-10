package de.paradubsch.paradubschmanager.lifecycle.jobs;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.jeff_media.customblockdata.CustomBlockData;
import de.paradubsch.paradubschmanager.ParadubschManager;
import de.paradubsch.paradubschmanager.models.WorkerPlayer;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.InvocationTargetException;
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
    }

    public static final NamespacedKey NOT_ORIGINAL = new NamespacedKey(ParadubschManager.getInstance(), "notOriginal");

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        PersistentDataContainer customBlockData = new CustomBlockData(event.getBlockPlaced(), ParadubschManager.getInstance());
        customBlockData.set(NOT_ORIGINAL, PersistentDataType.BYTE, (byte) 1);
    }

    public static void sendActionBar(Player player, String message) {
        ProtocolManager pm = ParadubschManager.getProtocolManager();
        if (pm != null) {
            PacketContainer packet = pm.createPacket(PacketType.Play.Server.CHAT);
            packet.getChatComponents().write(0, WrappedChatComponent.fromText(message));
            packet.getChatTypes().write(0, EnumWrappers.ChatType.GAME_INFO);
            try {
                pm.sendServerPacket(player, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
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
