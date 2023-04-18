package de.paradubsch.paradubschmanager.models;

import de.craftery.craftinglib.util.BaseDatabaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "warps")
public class Warp extends BaseDatabaseEntity<Warp, String> {
    @Id
    @Column(name = "name", columnDefinition = "VARCHAR(96)", nullable = false)
    private String name;

    @Column(name = "createdAt")
    @CreationTimestamp
    private Timestamp creationTimestamp;

    @Column(name = "world", columnDefinition = "VARCHAR(64)", nullable = false)
    private String world;

    @Column(name = "x", nullable = false)
    private double x;

    @Column(name = "y", nullable = false)
    private double y;

    @Column(name = "z", nullable = false)
    private double z;

    @Column(name = "pitch", nullable = false)
    private float pitch;

    @Column(name = "yaw", nullable = false)
    private float yaw;

    public static Warp getById(String id) {
        return BaseDatabaseEntity.getById(Warp.class, id);
    }

    public static List<Warp> getAll() {
        return BaseDatabaseEntity.getAll(Warp.class);
    }

    @Override
    public String getIdentifyingColumn() {
        return this.name;
    }
}
