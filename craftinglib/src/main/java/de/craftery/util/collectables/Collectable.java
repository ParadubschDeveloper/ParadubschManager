package de.craftery.util.collectables;

import de.craftery.util.BaseDatabaseEntity;
import de.craftery.util.ListCache;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "collectables")
@SequenceGenerator(name="collectablesSequence",sequenceName="collectables_sequence", allocationSize = 1)
public class Collectable extends BaseDatabaseEntity<Collectable, Long> {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="collectablesSequence")
    @Column(name = "collectable_id")
    private long id;

    @Column(name = "type", columnDefinition = "VARCHAR(255)", nullable = false)
    private String type;

    @Column(name = "world", columnDefinition = "VARCHAR(128)")
    private String world;

    @Column(name = "x", columnDefinition = "BIGINT")
    private long x;

    @Column(name = "y", columnDefinition = "BIGINT")
    private long y;

    @Column(name = "z", columnDefinition = "BIGINT")
    private long z;

    private static final ListCache<String, Collectable> cache = new ListCache<>();

    private static void prepareCache() {
        if (!cache.isValid("all")) {
            List<Collectable> collectables = BaseDatabaseEntity.getAll(Collectable.class);
            cache.cache("all", collectables);
        }
    }

    public static List<Collectable> getByType(String type) {
        prepareCache();
        return cache.get("all").stream().filter(collectable -> collectable.getType().equals(type)).toList();
    }

    @Override
    public Long getIdentifyingColumn() {
        return this.id;
    }
}
