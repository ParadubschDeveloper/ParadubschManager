package de.craftery.command;

import de.craftery.util.lang.BaseMessageType;
import de.craftery.util.MessageAdapter;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
public class CraftPlayer {
    private final CommandSender commandSender;

    public CraftPlayer(CommandSender commandSender) {
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

    public Player getPlayer() {
        return (Player) commandSender;
    }
}
