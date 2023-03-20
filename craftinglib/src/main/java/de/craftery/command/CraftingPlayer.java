package de.craftery.command;

import de.craftery.CraftingLib;
import de.craftery.util.lang.BaseMessageType;
import de.craftery.util.MessageAdapter;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Data
public class CraftingPlayer {
    private final CommandSender commandSender;

    public CraftingPlayer(CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    public boolean isPlayer() {
        return commandSender instanceof Player;
    }

    public void sendMessage(BaseMessageType template, String... args) {
        MessageAdapter.sendMessage(commandSender, template, args);
    }

    @Nullable
    public InventoryView openAnvil(@Nullable Location location, boolean force) {
        return this.getPlayer().openAnvil(location, force);
    }

    @NotNull
    public Location getLocation() {
        return this.getPlayer().getLocation();
    }

    public boolean teleportNormalized(Location location) {
        double newX = location.getX() >= 0 ? location.getX() + 0.5 : location.getX() - 0.5;
        double newZ = location.getZ() >= 0 ? location.getZ() + 0.5 : location.getZ() - 0.5;
        return this.getPlayer().teleport(new Location(location.getWorld(), newX, location.getY(), newZ));
    }

    public boolean teleportNormalized(String world, double x, double y, double z) {
        World newWorld = CraftingLib.getInstance().getServer().getWorld(world);
        return this.teleportNormalized(new Location(newWorld, x, y, z));
    }

    public boolean hasPermission(@NotNull String name) {
        if (this.commandSender instanceof Player) {
            return this.getPlayer().hasPermission(name);
        }
        return true;
    }

    public UUID getUniqueId() {
        return this.getPlayer().getUniqueId();
    }

    public Player getPlayer() {
        return (Player) commandSender;
    }
}
