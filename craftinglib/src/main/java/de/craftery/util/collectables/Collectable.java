package de.craftery.util.collectables;

import de.craftery.util.BaseDatabaseEntity;
import de.craftery.util.ListCache;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.annotation.Nullable;
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

    public static @Nullable Collectable getById(long id) {
        prepareCache();
        return cache.get("all").stream().filter(collectable -> collectable.getId() == id).findFirst().orElse(null);
    }

    public static void removeCollectable(Collectable collectable) {
        cache.get("all").remove(collectable);
        collectable.delete();
    }

    public static boolean exists(Location loc) {
        prepareCache();
        return cache.get("all").stream().anyMatch(collectable -> collectable.getLocation().equals(loc));
    }

    public static void addCollectable(Location loc, String type) {
        Collectable collectable = new Collectable();
        collectable.setType(type);
        collectable.setX(loc.getBlockX());
        collectable.setY(loc.getBlockY());
        collectable.setZ(loc.getBlockZ());
        collectable.setWorld(loc.getWorld().getName());
        Long id = (Long) collectable.save();
        collectable.setId(id);
        cache.get("all").add(collectable);
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z);
    }

    @Override
    public Long getIdentifyingColumn() {
        return this.id;
    }
}
