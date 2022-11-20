package de.paradubsch.paradubschmanager.lifecycle.stairsit;

import de.paradubsch.paradubschmanager.ParadubschManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;

public class StairSitManager implements Listener {
    public StairSitManager() {
        ParadubschManager.getInstance().getServer().getPluginManager().registerEvents(this, ParadubschManager.getInstance());
    }

    private final Map<Player, Location> playerLocation = new HashMap<>();

    private final Map<Player, Entity> chairList = new HashMap<>();

    private final Map<Player, Location> chairLocation = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() != null) {
            Block block = event.getClickedBlock();
            if (
                    event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                    !player.isInsideVehicle() &&
                    player.getInventory().getItemInMainHand().getType() == Material.AIR
            ) {
                if (block.getState().getBlockData() instanceof Stairs) {
                    Stairs stairs = (Stairs) block.getState().getBlockData();
                    if (stairs.getHalf() == Stairs.Half.BOTTOM) {
                        World world = player.getWorld();
                        this.playerLocation.put(player, player.getLocation());
                        Entity chair = world.spawnEntity(player.getLocation(), EntityType.ARROW);
                        this.chairList.put(player, chair);
                        chair.teleport(block.getLocation().add(0.5D, 0.0D, 0.5D));
                        this.chairLocation.put(player, chair.getLocation());
                        chair.addPassenger(player);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (this.playerLocation.containsKey(event.getPlayer())) {
            event.setCancelled(true);
            if (event.getPlayer().isSneaking()) {
                Player player = event.getPlayer();
                final Player sit_player = player;
                final Location stand_location = this.playerLocation.get(player);
                Entity sit_chair = this.chairList.get(player);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ParadubschManager.getInstance(), () -> {
                    sit_player.teleport(stand_location);
                    sit_player.setSneaking(false);
                }, 1L);
                this.playerLocation.remove(player);
                sit_chair.remove();
                this.chairList.remove(player);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (this.playerLocation.containsKey(event.getPlayer())) {
            Player player = event.getPlayer();
            if (player.getLocation() != this.playerLocation.get(player) &&
                    !player.isInsideVehicle()) {
                World world = player.getWorld();
                if (this.chairLocation.containsKey(player)) {
                    Entity chair = world.spawnEntity(this.chairLocation.get(player), EntityType.ARROW);
                    chair.teleport(this.chairLocation.get(player).add(0.5D, 0.0D, 0.5D));
                    chair.addPassenger(player);
                }
            }
        }
    }
}
