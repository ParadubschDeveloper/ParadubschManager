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

    public boolean teleportNormalized(String world, double x, double y, double z) {
        double newX = x >= 0 ? x + 0.5 : x - 0.5;
        double newZ = z >= 0 ? z + 0.5 : z - 0.5;
        World newWorld = CraftingLib.getInstance().getServer().getWorld(world);
        return this.getPlayer().teleport(new Location(newWorld, newX, y, newZ));
    }

    public boolean hasPermission(@NotNull String name) {
        if (this.commandSender instanceof Player) {
            return this.getPlayer().hasPermission(name);
        }
        return true;
    }

    public Player getPlayer() {
        return (Player) commandSender;
    }
}
