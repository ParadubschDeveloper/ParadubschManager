package de.craftery.util.gui;

import lombok.ToString;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@ToString
public class KVStore {
    private final Map<Object, Object> store;
    private final Player player;
    public KVStore(Player player) {
        this.player = player;
        KVStore parent = GuiManager.getKvStores().get(player);
        if (parent == null) {
            this.store = new HashMap<>();
        } else {
            this.store = parent.store;
        }
    }

    public Object get(Object key) {
        return this.store.get(key);
    }
    public void set(Object key, Object value) {
        this.store.put(key, value);
        GuiManager.getKvStores().put(player, this);
    }
}
