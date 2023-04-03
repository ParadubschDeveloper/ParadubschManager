package de.craftery.util.collectables;

import de.craftery.util.BaseDatabaseEntity;
import de.craftery.util.MessageAdapter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "collected_collectables")
public class CollectedCollectable extends BaseDatabaseEntity<CollectedCollectable, String> {
    @Id
    @Column(name = "id_uuid_pair", columnDefinition = "VARCHAR(196)")
    private String pair;

    @Column(name = "collectable_id", nullable = false)
    private long collectableId;

    @Column(name = "collector_uuid", columnDefinition = "VARCHAR(36)", nullable = false)
    private String uuid;

    @Override
    public String getIdentifyingColumn() {
        return this.pair;
    }

    private static final List<String> notCollected = new ArrayList<>();

    public static boolean hasCollected(Player player, Collectable collectable) {
        String key = player.getUniqueId() + "_" + collectable.getId();
        if (notCollected.contains(key)) return false;

        boolean collected = BaseDatabaseEntity.getById(CollectedCollectable.class, key) != null;

        if (!collected) notCollected.add(key);
        return collected;
    }

    public static void collect(Player player, Collectable collectable) {
        long id = collectable.getId();
        String key = player.getUniqueId() + "_" + id;

        if (hasCollected(player, collectable)) {
            MessageAdapter.sendConsoleError(new Exception("CollectedCollectable.collect() called for already collected collectable!"));
            return;
        }

        notCollected.remove(key);

        CollectedCollectable collectedCollectable = new CollectedCollectable();
        collectedCollectable.setPair(key);
        collectedCollectable.setCollectableId(id);
        collectedCollectable.setUuid(player.getUniqueId().toString());
        collectedCollectable.save();
    }
}
