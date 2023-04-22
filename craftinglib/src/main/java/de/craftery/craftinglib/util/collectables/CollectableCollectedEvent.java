package de.craftery.craftinglib.util.collectables;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class CollectableCollectedEvent extends PlayerEvent {
    private static final HandlerList handlerList = new HandlerList();
    private final Collectable collectable;
    private boolean isHandled = false;

    public CollectableCollectedEvent(Player player, Collectable collectable) {
        super(player);
        this.collectable = collectable;
    }

    public Collectable getCollectable() {
        return this.collectable;
    }

    public boolean isHandled() {
        return this.isHandled;
    }

    public void handle() {
        this.isHandled = true;
    }

    public static HandlerList getHandlerList()
    {
        return handlerList;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
