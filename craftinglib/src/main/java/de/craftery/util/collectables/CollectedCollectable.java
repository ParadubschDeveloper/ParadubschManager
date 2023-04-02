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

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "collected_collectables")
public class CollectedCollectable extends BaseDatabaseEntity<CollectedCollectable, String> {
    @Id
    @Column(name = "id_uuid_pair", columnDefinition = "VARCHAR(196)")
    private String pair;

    @Column(name = "collectable_id", nullable = false)
    private long id;

    @Column(name = "collector_uuid", columnDefinition = "VARCHAR(36)", nullable = false)
    private String uuid;

    @Override
    public String getIdentifyingColumn() {
        return this.pair;
    }

    public static boolean hasCollected(Player player, Collectable collectable) {
        long id = collectable.getId();
        return BaseDatabaseEntity.getById(CollectedCollectable.class, player.getUniqueId() + "_" + id) != null;
    }

    public static void collect(Player player, Collectable collectable) {
        if (hasCollected(player, collectable)) {
            MessageAdapter.sendConsoleError(new Exception("CollectedCollectable.collect() called for already collected collectable!"));
            return;
        }

        long id = collectable.getId();
        CollectedCollectable collectedCollectable = new CollectedCollectable();
        collectedCollectable.setPair(player.getUniqueId() + "_" + id);
        collectedCollectable.setId(id);
        collectedCollectable.setUuid(player.getUniqueId().toString());
        collectedCollectable.save();
    }
}
